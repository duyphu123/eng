// Generate auto-dict.json from English content in src/app/data/*.json.
// Uses any OpenAI-compatible LLM API (default: Google Gemini, free tier).
// Stop-word particles (the, a, is, ...) get null so we don't waste tooltips
// on them. Run incrementally — only new words trigger API calls.
//
// Provider env vars (pick ONE):
//   GEMINI_API_KEY=...     → Google Gemini (recommended, free)
//   GROQ_API_KEY=...       → Groq Llama 3.3 70B (free, very fast)
//   OPENROUTER_API_KEY=... → OpenRouter (aggregator, some free models)
//
// Override default model: TRANSLATE_MODEL=<model-id>
//
// Run: npm run gen-dict
import {
  readFileSync,
  writeFileSync,
  readdirSync,
  existsSync,
} from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const ROOT = resolve(__dirname, '..');
const DATA_DIR = resolve(ROOT, 'src/app/data');
const OUTPUT_PATH = resolve(ROOT, 'src/app/shared/dict/auto-dict.json');

const BATCH_SIZE = 60;

/** Provider configs — OpenAI-compatible chat completions endpoints. */
const PROVIDERS = {
  gemini: {
    baseUrl: 'https://generativelanguage.googleapis.com/v1beta/openai',
    apiKeyEnv: 'GEMINI_API_KEY',
    // gemini-2.5-flash-lite has the most generous free tier; 2.0-flash often 429s
    defaultModel: 'gemini-2.5-flash-lite',
    consoleUrl: 'https://aistudio.google.com/app/apikey',
  },
  groq: {
    baseUrl: 'https://api.groq.com/openai/v1',
    apiKeyEnv: 'GROQ_API_KEY',
    defaultModel: 'llama-3.3-70b-versatile',
    consoleUrl: 'https://console.groq.com/keys',
  },
  openrouter: {
    baseUrl: 'https://openrouter.ai/api/v1',
    apiKeyEnv: 'OPENROUTER_API_KEY',
    defaultModel: 'google/gemini-2.0-flash-exp:free',
    consoleUrl: 'https://openrouter.ai/keys',
  },
};

function pickProvider() {
  for (const [name, cfg] of Object.entries(PROVIDERS)) {
    if (process.env[cfg.apiKeyEnv]) {
      return {
        name,
        ...cfg,
        apiKey: process.env[cfg.apiKeyEnv],
        model: process.env['TRANSLATE_MODEL'] || cfg.defaultModel,
      };
    }
  }
  return null;
}

const SYSTEM_PROMPT = `You translate English words to Vietnamese for TOEIC business English learners.

Rules:
1. For each input word, output 1-3 concise Vietnamese meanings, comma-separated. Example: "đạt được, với tới"
2. Pick meanings most relevant to business / legal / office / contract (TOEIC) context.
3. For basic English particles every beginner already knows — articles, pronouns, common auxiliaries (the, a, an, is, are, am, was, were, be, been, being, to, of, in, on, at, by, for, with, it, he, she, we, they, you, i, me, him, her, us, them, this, that, these, those, my, your, his, its, our, their, will, would, can, could, should, may, might, must, do, does, did, has, have, had, very, more, most, also, not, no, yes, and, or, but, if, so) — output null instead of a translation.
4. Return ONLY a JSON object mapping each input word (lowercase) to its translation string or null. No prose, no markdown fences, no commentary outside the JSON.

Example output:
{"contract": "hợp đồng", "the": null, "decided": "đã quyết định", "negotiate": "đàm phán"}`;

function stripMarkup(text) {
  return text.replace(/\[([^\]]+)\]\{[^}]+\}/g, '$1');
}

function extractWords(text) {
  if (!text) return [];
  const plain = stripMarkup(text);
  const matches = plain.match(/\b[a-zA-Z]+\b/g) || [];
  return matches.map((w) => w.toLowerCase());
}

function scanContent() {
  const files = readdirSync(DATA_DIR).filter((f) => f.endsWith('.json'));
  const words = new Set();

  for (const file of files) {
    const data = JSON.parse(readFileSync(resolve(DATA_DIR, file), 'utf8'));

    if (Array.isArray(data)) {
      for (const entry of data) {
        if (entry.word) extractWords(entry.word).forEach((w) => words.add(w));
        if (entry.example)
          extractWords(entry.example).forEach((w) => words.add(w));
        if (Array.isArray(entry.family)) {
          for (const f of entry.family) {
            if (f.word) extractWords(f.word).forEach((w) => words.add(w));
          }
        }
      }
    } else if (Array.isArray(data.sentences)) {
      for (const s of data.sentences) {
        if (s.en) extractWords(s.en).forEach((w) => words.add(w));
      }
    }
  }

  return [...words].sort();
}

function chunk(arr, size) {
  const out = [];
  for (let i = 0; i < arr.length; i += size) {
    out.push(arr.slice(i, i + size));
  }
  return out;
}

async function translateBatch(provider, words) {
  const resp = await fetch(`${provider.baseUrl}/chat/completions`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${provider.apiKey}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      model: provider.model,
      messages: [
        { role: 'system', content: SYSTEM_PROMPT },
        {
          role: 'user',
          content: `Translate these ${words.length} words:\n${words.join(', ')}`,
        },
      ],
      max_tokens: 4096,
      temperature: 0.2,
    }),
  });

  if (!resp.ok) {
    const body = await resp.text();
    throw new Error(`HTTP ${resp.status}: ${body.slice(0, 300)}`);
  }

  const data = await resp.json();
  const text = data.choices?.[0]?.message?.content ?? '';

  // Strip markdown fences if present (Gemini sometimes wraps in ```json ... ```)
  const cleaned = text
    .replace(/```json\s*/g, '')
    .replace(/```\s*/g, '')
    .trim();

  const jsonMatch = cleaned.match(/\{[\s\S]*\}/);
  if (!jsonMatch) {
    throw new Error(`No JSON object in response:\n${text}`);
  }

  return JSON.parse(jsonMatch[0]);
}

async function main() {
  const provider = pickProvider();
  if (!provider) {
    console.error('Error: No API key found. Set ONE of:');
    for (const [name, cfg] of Object.entries(PROVIDERS)) {
      console.error(`  ${cfg.apiKeyEnv}  (provider: ${name})`);
      console.error(`    Get key: ${cfg.consoleUrl}`);
    }
    console.error('\nPowerShell example:');
    console.error('  $env:GEMINI_API_KEY = "..."');
    console.error('  npm run gen-dict');
    process.exit(1);
  }

  console.log(`Provider: ${provider.name} · model: ${provider.model}\n`);

  console.log(`Scanning ${DATA_DIR}...`);
  const allWords = scanContent();
  console.log(`Found ${allWords.length} unique English words.`);

  let existing = {};
  if (existsSync(OUTPUT_PATH)) {
    existing = JSON.parse(readFileSync(OUTPUT_PATH, 'utf8'));
  }
  console.log(`Existing auto-dict.json: ${Object.keys(existing).length} entries cached.`);

  const newWords = allWords.filter((w) => !(w in existing));
  if (!newWords.length) {
    console.log('Nothing to do — dict is up to date.');
    return;
  }
  console.log(`${newWords.length} new words to translate.\n`);

  const batches = chunk(newWords, BATCH_SIZE);
  const merged = { ...existing };

  for (const [i, batch] of batches.entries()) {
    process.stdout.write(
      `[Batch ${i + 1}/${batches.length}] ${batch.length} words... `
    );
    try {
      const translations = await translateBatch(provider, batch);
      let added = 0;
      let skipped = 0;
      for (const [word, vi] of Object.entries(translations)) {
        const key = word.toLowerCase();
        if (vi === null || vi === undefined) {
          merged[key] = null;
          skipped++;
        } else if (typeof vi === 'string' && vi.trim()) {
          merged[key] = vi.trim();
          added++;
        }
      }
      console.log(`+${added} translations, ${skipped} stop-words`);
    } catch (err) {
      console.log(`FAIL: ${err?.message ?? err}`);
    }
    // Brief breather between calls for rate limits
    await new Promise((r) => setTimeout(r, 300));
  }

  const sorted = Object.fromEntries(
    Object.entries(merged).sort(([a], [b]) => a.localeCompare(b))
  );
  writeFileSync(OUTPUT_PATH, JSON.stringify(sorted, null, 2) + '\n', 'utf8');

  const translated = Object.values(sorted).filter((v) => v !== null).length;
  const stopWords = Object.values(sorted).filter((v) => v === null).length;

  console.log(`\nWrote ${OUTPUT_PATH}`);
  console.log(`  Total entries: ${Object.keys(sorted).length}`);
  console.log(`  Translated:    ${translated}`);
  console.log(`  Stop-words:    ${stopWords}`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
