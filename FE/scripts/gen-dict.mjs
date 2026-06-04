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
const PHRASES_PATH = resolve(ROOT, 'src/app/shared/dict/auto-phrases.json');

// Load .env file (if exists) into process.env. Existing env vars take precedence.
function loadDotEnv(filePath) {
  if (!existsSync(filePath)) return;
  const content = readFileSync(filePath, 'utf8');
  for (const rawLine of content.split(/\r?\n/)) {
    const line = rawLine.trim();
    if (!line || line.startsWith('#')) continue;
    const eqIdx = line.indexOf('=');
    if (eqIdx === -1) continue;
    const key = line.slice(0, eqIdx).trim();
    let value = line.slice(eqIdx + 1).trim();
    if (
      (value.startsWith('"') && value.endsWith('"')) ||
      (value.startsWith("'") && value.endsWith("'"))
    ) {
      value = value.slice(1, -1);
    }
    if (key && !(key in process.env)) {
      process.env[key] = value;
    }
  }
}

loadDotEnv(resolve(ROOT, '.env'));

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

const PHRASE_SYSTEM_PROMPT = `You translate English multi-word phrases to Vietnamese for TOEIC business English learners.

Rules:
1. For each input phrase, decide if it is a MEANINGFUL compound that learners would benefit from seeing translated as a unit:
   - Compositional noun phrases (adj+noun, noun+noun): "new store" → "cửa hàng mới", "young customers" → "khách hàng trẻ", "ad campaign" → "chiến dịch quảng cáo"
   - Verb phrases / collocations: "attract customers" → "thu hút khách hàng", "compare prices" → "so sánh giá cả"
   - Idioms / phrasal verbs: "look forward to" → "mong đợi", "in addition" → "thêm vào đó"
   - Time / quantity phrases: "first time" → "lần đầu tiên", "30 days" → "30 ngày"
2. Return null for phrases that are random word combos with no meaningful unit translation:
   - "in our", "to come", "of the", "and the", "have to" (too generic), "is a"
3. Translation: 1-2 concise Vietnamese options. Comma-separated if multiple.
4. Return ONLY a JSON object mapping each input phrase (exactly as given, lowercase) to its translation string or null. No prose, no markdown fences.

Example output:
{"new store": "cửa hàng mới", "in our": null, "free samples": "hàng mẫu miễn phí", "to come": null, "satisfied customers": "khách hàng hài lòng"}`;

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

// Stop words that cannot start or end a phrase — they're never meaningful boundaries.
// Function words mid-phrase are OK (e.g., "look forward to" ends with "to" — but we
// also accept that and let the LLM decide; "in" between content words is rarer).
const PHRASE_BOUNDARY_STOPS = new Set([
  'a', 'an', 'the',
  'and', 'or', 'but', 'so', 'if', 'as',
  'to', 'of', 'in', 'on', 'at', 'by', 'for', 'with', 'from', 'into', 'about',
  'is', 'are', 'am', 'was', 'were', 'be', 'been', 'being',
  'has', 'have', 'had', 'do', 'does', 'did',
  'will', 'would', 'can', 'could', 'should', 'may', 'might', 'must',
  'this', 'that', 'these', 'those',
  'it', 'he', 'she', 'we', 'they', 'i', 'you',
  'my', 'your', 'his', 'her', 'its', 'our', 'their',
  'not', 'no', 'yes',
  'than', 'then', 'when', 'where', 'while', 'because',
]);

// Extract n-grams from plain text (after stripping markup). Lowercased.
// Splits on sentence-ending punctuation so phrases don't cross sentence boundaries.
function extractNgrams(text, n) {
  if (!text) return [];
  const plain = stripMarkup(text);
  // Break at punctuation so "store, the" doesn't become "store the"
  const segments = plain.split(/[.,;:!?()\[\]{}"–—\n]+/);
  const out = new Set();
  for (const seg of segments) {
    const words = (seg.toLowerCase().match(/\b[a-zA-Z]+\b/g) || []);
    for (let i = 0; i <= words.length - n; i++) {
      const phrase = words.slice(i, i + n).join(' ');
      // Reject phrases starting/ending with boundary stop words
      const first = words[i];
      const last = words[i + n - 1];
      if (PHRASE_BOUNDARY_STOPS.has(first) || PHRASE_BOUNDARY_STOPS.has(last)) continue;
      // Reject phrases where every token is a stop word
      const allStop = words.slice(i, i + n).every((w) => PHRASE_BOUNDARY_STOPS.has(w));
      if (allStop) continue;
      out.add(phrase);
    }
  }
  return [...out];
}

function scanPhrases() {
  const files = readdirSync(DATA_DIR).filter((f) => f.endsWith('.json'));
  const phrases = new Set();

  for (const file of files) {
    const data = JSON.parse(readFileSync(resolve(DATA_DIR, file), 'utf8'));

    const addFromText = (text) => {
      for (const n of [2, 3]) {
        for (const ng of extractNgrams(text, n)) phrases.add(ng);
      }
    };

    if (Array.isArray(data)) {
      for (const entry of data) {
        if (entry.example) addFromText(entry.example);
      }
    } else if (Array.isArray(data.sentences)) {
      for (const s of data.sentences) {
        if (s.en) addFromText(s.en);
      }
    }
  }

  return [...phrases].sort();
}

function chunk(arr, size) {
  const out = [];
  for (let i = 0; i < arr.length; i += size) {
    out.push(arr.slice(i, i + size));
  }
  return out;
}

async function translateBatch(provider, items, kind = 'word') {
  const systemPrompt = kind === 'phrase' ? PHRASE_SYSTEM_PROMPT : SYSTEM_PROMPT;
  const noun = kind === 'phrase' ? 'phrases' : 'words';
  // Use one-per-line for phrases to avoid confusion with comma-in-phrase
  const userContent =
    kind === 'phrase'
      ? `Translate these ${items.length} phrases:\n${items.map((p) => `- ${p}`).join('\n')}`
      : `Translate these ${items.length} words:\n${items.join(', ')}`;

  const resp = await fetch(`${provider.baseUrl}/chat/completions`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${provider.apiKey}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      model: provider.model,
      messages: [
        { role: 'system', content: systemPrompt },
        { role: 'user', content: userContent },
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

async function processItems({ provider, items, existing, kind, label }) {
  console.log(`\n=== ${label} ===`);
  console.log(`Found ${items.length} unique ${kind === 'phrase' ? 'phrases' : 'words'}.`);
  console.log(
    `Existing cache: ${Object.keys(existing).length} entries.`
  );

  const newItems = items.filter((it) => !(it in existing));
  if (!newItems.length) {
    console.log('Nothing to do — cache is up to date.');
    return existing;
  }
  console.log(`${newItems.length} new to translate.\n`);

  const batches = chunk(newItems, BATCH_SIZE);
  const merged = { ...existing };

  for (const [i, batch] of batches.entries()) {
    process.stdout.write(
      `[Batch ${i + 1}/${batches.length}] ${batch.length} ${kind === 'phrase' ? 'phrases' : 'words'}... `
    );
    try {
      const translations = await translateBatch(provider, batch, kind);
      let added = 0;
      let skipped = 0;
      for (const [key, vi] of Object.entries(translations)) {
        const normKey = key.toLowerCase();
        if (vi === null || vi === undefined) {
          merged[normKey] = null;
          skipped++;
        } else if (typeof vi === 'string' && vi.trim()) {
          merged[normKey] = vi.trim();
          added++;
        }
      }
      console.log(`+${added} translations, ${skipped} skipped`);
    } catch (err) {
      console.log(`FAIL: ${err?.message ?? err}`);
    }
    await new Promise((r) => setTimeout(r, 300));
  }

  return merged;
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

  console.log(`Provider: ${provider.name} · model: ${provider.model}`);
  console.log(`Scanning ${DATA_DIR}...`);

  // ─── Pass 1: single words ──────────────────────────────
  const allWords = scanContent();
  let existingDict = {};
  if (existsSync(OUTPUT_PATH)) {
    existingDict = JSON.parse(readFileSync(OUTPUT_PATH, 'utf8'));
  }
  const mergedDict = await processItems({
    provider,
    items: allWords,
    existing: existingDict,
    kind: 'word',
    label: 'Single words',
  });
  const sortedDict = Object.fromEntries(
    Object.entries(mergedDict).sort(([a], [b]) => a.localeCompare(b))
  );
  writeFileSync(OUTPUT_PATH, JSON.stringify(sortedDict, null, 2) + '\n', 'utf8');

  // ─── Pass 2: multi-word phrases ────────────────────────
  const allPhrases = scanPhrases();
  let existingPhrases = {};
  if (existsSync(PHRASES_PATH)) {
    existingPhrases = JSON.parse(readFileSync(PHRASES_PATH, 'utf8'));
  }
  const mergedPhrases = await processItems({
    provider,
    items: allPhrases,
    existing: existingPhrases,
    kind: 'phrase',
    label: 'Multi-word phrases',
  });
  const sortedPhrases = Object.fromEntries(
    Object.entries(mergedPhrases).sort(([a], [b]) => a.localeCompare(b))
  );
  writeFileSync(
    PHRASES_PATH,
    JSON.stringify(sortedPhrases, null, 2) + '\n',
    'utf8'
  );

  // ─── Summary ───────────────────────────────────────────
  const wordsTranslated = Object.values(sortedDict).filter((v) => v !== null).length;
  const wordsSkipped = Object.values(sortedDict).filter((v) => v === null).length;
  const phrasesTranslated = Object.values(sortedPhrases).filter((v) => v !== null).length;
  const phrasesSkipped = Object.values(sortedPhrases).filter((v) => v === null).length;

  console.log(`\n=== Summary ===`);
  console.log(`Words   — total: ${Object.keys(sortedDict).length}, translated: ${wordsTranslated}, stop: ${wordsSkipped}`);
  console.log(`Phrases — total: ${Object.keys(sortedPhrases).length}, translated: ${phrasesTranslated}, rejected: ${phrasesSkipped}`);
  console.log(`Wrote: ${OUTPUT_PATH}`);
  console.log(`Wrote: ${PHRASES_PATH}`);
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
