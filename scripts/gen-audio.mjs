// Generate pre-recorded TTS MP3s for vocab data using msedge-tts.
// Voice: Microsoft Neural (Aria en-US, HoaiMy vi-VN).
// Output: src/assets/audio/{en-US|vi-VN}/<slug>.mp3 + manifest.json
//
// Run: npm run gen-audio
import {
  readFileSync,
  writeFileSync,
  mkdirSync,
  existsSync,
  renameSync,
  unlinkSync,
} from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { MsEdgeTTS, OUTPUT_FORMAT } from 'msedge-tts';

const __dirname = dirname(fileURLToPath(import.meta.url));
const ROOT = resolve(__dirname, '..');
const OUT_DIR = resolve(ROOT, 'src/assets/audio');

const LESSONS = ['src/app/data/contract-lesson-1.json'];

// Long-form passage files (1 MP3 per passage, English narration)
const PASSAGES = ['src/app/data/contract-lesson-1-passage.json'];

const VOICES = {
  'en-US': 'en-US-AriaNeural',
  'vi-VN': 'vi-VN-HoaiMyNeural',
};

function stripMarkup(text) {
  return text.replace(/\[([^\]]+)\]\{[^}]+\}/g, '$1');
}

function slugify(text) {
  return text
    .toLowerCase()
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '') // strip combining diacritics
    .replace(/đ/g, 'd') // Vietnamese đ → d (not in NFD decomposition)
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '')
    .slice(0, 80);
}

const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function generateOne(voiceName, text, outPath, attempt = 1) {
  // Fresh instance per call — setMetadata is finicky if called multiple
  // times on the same instance (the WebSocket gets into a bad state).
  const tts = new MsEdgeTTS();
  try {
    await tts.setMetadata(
      voiceName,
      OUTPUT_FORMAT.AUDIO_24KHZ_48KBITRATE_MONO_MP3
    );
    const { audioFilePath } = await tts.toFile(dirname(outPath), text);
    if (existsSync(outPath)) unlinkSync(outPath);
    renameSync(audioFilePath, outPath);
  } catch (err) {
    if (attempt < 4) {
      await sleep(500 * attempt); // exponential-ish backoff
      try {
        tts.close();
      } catch {}
      return generateOne(voiceName, text, outPath, attempt + 1);
    }
    throw err;
  } finally {
    try {
      tts.close();
    } catch {
      /* ignore */
    }
  }
}

function collectTexts(lessons) {
  const collected = [];
  for (const lessonPath of lessons) {
    const data = JSON.parse(readFileSync(resolve(ROOT, lessonPath), 'utf8'));
    for (const entry of data) {
      collected.push({
        lang: 'en-US',
        text: entry.word,
        slug: slugify(entry.word),
      });
      collected.push({
        lang: 'vi-VN',
        text: entry.vi,
        slug: slugify(entry.vi),
      });
    }
  }
  return collected;
}

async function main() {
  console.log('Reading lesson data...');
  const items = collectTexts(LESSONS);

  // Dedupe by (lang, slug)
  const seen = new Set();
  const unique = items.filter((it) => {
    const k = `${it.lang}/${it.slug}`;
    if (seen.has(k)) return false;
    seen.add(k);
    return true;
  });
  console.log(`Total unique texts: ${unique.length}`);

  const lookup = { 'en-US': {}, 'vi-VN': {} };

  for (const [i, item] of unique.entries()) {
    const langDir = resolve(OUT_DIR, item.lang);
    mkdirSync(langDir, { recursive: true });
    const outPath = resolve(langDir, `${item.slug}.mp3`);

    if (existsSync(outPath)) {
      console.log(
        `[${i + 1}/${unique.length}] CACHED ${item.lang}/${item.slug}`
      );
      lookup[item.lang][item.text] = `${item.slug}.mp3`;
      continue;
    }

    process.stdout.write(
      `[${i + 1}/${unique.length}] ${item.lang} "${item.text}"... `
    );
    try {
      await generateOne(VOICES[item.lang], item.text, outPath);
      console.log('OK');
      lookup[item.lang][item.text] = `${item.slug}.mp3`;
    } catch (err) {
      console.log(`FAIL: ${err?.message ?? err ?? '(unknown)'}`);
    }
    // Small breather between calls to give the previous WS time to close
    await sleep(150);
  }

  // ─── Passages (long-form narration) ──────────────────
  for (const passagePath of PASSAGES) {
    const data = JSON.parse(readFileSync(resolve(ROOT, passagePath), 'utf8'));
    // Schema: { title, titleVi, sentences: [{ en, vi }, ...] }
    const enSentences = (data.sentences || []).map((s) => stripMarkup(s.en));
    const plain = enSentences.join(' ');
    const slug = `passage-${slugify(data.title)}`;
    const outPath = resolve(OUT_DIR, 'en-US', `${slug}.mp3`);

    mkdirSync(dirname(outPath), { recursive: true });

    if (existsSync(outPath)) {
      console.log(`[passage] CACHED ${slug}`);
      lookup['en-US'][plain] = `${slug}.mp3`;
      continue;
    }

    process.stdout.write(`[passage] GEN "${data.title}"... `);
    try {
      await generateOne(VOICES['en-US'], plain, outPath);
      console.log('OK');
      lookup['en-US'][plain] = `${slug}.mp3`;
    } catch (err) {
      console.log(`FAIL: ${err?.message ?? err ?? '(unknown)'}`);
    }
    await sleep(150);
  }

  const manifestPath = resolve(OUT_DIR, 'manifest.json');
  writeFileSync(manifestPath, JSON.stringify(lookup, null, 2), 'utf8');
  console.log(`Wrote manifest: ${manifestPath}`);
  console.log('Done.');
}

main().catch((err) => {
  console.error(err);
  process.exit(1);
});
