// Generate src/styles/_tokens.scss from DESIGN.md (single source of truth).
// Run: node scripts/gen-tokens.mjs
import { readFileSync, writeFileSync, mkdirSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { parse as parseYaml } from 'yaml';

const __dirname = dirname(fileURLToPath(import.meta.url));
const ROOT = resolve(__dirname, '..');
const SRC = resolve(ROOT, 'DESIGN.md');
const OUT = resolve(ROOT, 'src/styles/_tokens.scss');

const raw = readFileSync(SRC, 'utf8');
const fmMatch = raw.match(/^---\r?\n([\s\S]*?)\r?\n---/);
if (!fmMatch) {
  console.error('No YAML frontmatter found in DESIGN.md');
  process.exit(1);
}
const tokens = parseYaml(fmMatch[1]);

const lines = [
  '// AUTO-GENERATED from DESIGN.md by scripts/gen-tokens.mjs',
  '// Run `npm run design:tokens` to regenerate. Do not edit by hand.',
  '',
  ':root {',
];

const push = (group, key, val) => lines.push(`  --${group}-${key}: ${val};`);

if (tokens.colors) {
  lines.push('  // Colors');
  for (const [k, v] of Object.entries(tokens.colors)) push('color', k, v);
}

if (tokens.rounded) {
  lines.push('', '  // Rounded corners');
  for (const [k, v] of Object.entries(tokens.rounded)) push('rounded', k, v);
}

if (tokens.spacing) {
  lines.push('', '  // Spacing scale');
  for (const [k, v] of Object.entries(tokens.spacing)) {
    const val = typeof v === 'number' ? `${v}px` : v;
    push('space', k, val);
  }
}

if (tokens.typography) {
  lines.push('', '  // Typography (one variable per property; use via mixins below)');
  for (const [name, t] of Object.entries(tokens.typography)) {
    if (t.fontFamily) push(`font-${name}`, 'family', `'${t.fontFamily}', system-ui, sans-serif`);
    if (t.fontSize) push(`font-${name}`, 'size', t.fontSize);
    if (t.fontWeight != null) push(`font-${name}`, 'weight', String(t.fontWeight));
    if (t.lineHeight != null) push(`font-${name}`, 'line', String(t.lineHeight));
    if (t.letterSpacing) push(`font-${name}`, 'tracking', t.letterSpacing);
  }
}

lines.push('}', '');

// Typography mixins
if (tokens.typography) {
  lines.push('// Typography mixins — apply a full type style in one line');
  for (const name of Object.keys(tokens.typography)) {
    lines.push(`@mixin type-${name} {`);
    lines.push(`  font-family: var(--font-${name}-family);`);
    lines.push(`  font-size: var(--font-${name}-size);`);
    lines.push(`  font-weight: var(--font-${name}-weight);`);
    lines.push(`  line-height: var(--font-${name}-line);`);
    const t = tokens.typography[name];
    if (t.letterSpacing) lines.push(`  letter-spacing: var(--font-${name}-tracking);`);
    lines.push('}');
  }
  lines.push('');
}

mkdirSync(dirname(OUT), { recursive: true });
writeFileSync(OUT, lines.join('\n'), 'utf8');
console.log(`Wrote ${OUT}`);
