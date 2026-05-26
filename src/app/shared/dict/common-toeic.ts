/**
 * Manual override registry — empty by default.
 *
 * The auto-generated `AUTO_DICT` (`auto-dict.json`, produced by
 * `npm run gen-dict`) covers virtually every word in the content data.
 * This file exists ONLY for cases where Gemini's translation is wrong,
 * awkward, or context-inappropriate.
 *
 * Lookup order in tooltip-text.component.ts: COMMON_DICT first, then
 * AUTO_DICT — so any entry here overrides the auto-generated translation.
 *
 * ─────────────────────────────────────────────────────────────────────────
 * When to add an entry:
 * ─────────────────────────────────────────────────────────────────────────
 *  1. Spot a tooltip with bad/awkward Vietnamese
 *  2. Determine the correct contextual meaning for this project
 *  3. Add: `<word>: '<correct vi>'`
 *  4. Reload — manual entry wins over AUTO_DICT
 *
 * Use the **base form, lowercase**. The lemma helper handles inflections
 * automatically (e.g., `party` matches `parties`).
 *
 * Examples (uncomment / add as needed):
 *
 *   party: 'bên (hợp đồng), nhóm người',   // override if Gemini gives "bữa tiệc"
 *   draft: 'bản dự thảo, soạn thảo',        // override if Gemini gives "gió lùa"
 *   issue: 'vấn đề, phát hành',             // override if context-ambiguous
 */
export const COMMON_DICT: Readonly<Record<string, string>> = {
  // Add overrides here when needed. Currently empty —
  // auto-dict.json handles all translations.
};
