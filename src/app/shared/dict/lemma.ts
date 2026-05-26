/**
 * Lightweight English lemmatizer — generates candidate base forms for a
 * surface word so the dictionary can match inflected forms.
 *
 * Handles: regular -s/-es/-ies plurals, -ed/-d past, -ing participle.
 * Does NOT handle irregular verbs (went/gone/took/taken). For those, add
 * the surface form directly as a separate key in one of the dictionaries.
 */
export function lemmaCandidates(word: string): string[] {
  const w = word.toLowerCase();
  const cands = new Set<string>([w]);

  if (w.endsWith('ies') && w.length > 4) {
    cands.add(w.slice(0, -3) + 'y');
  }

  if (w.endsWith('es') && w.length > 3) {
    cands.add(w.slice(0, -2));
    cands.add(w.slice(0, -1));
  }

  if (
    w.endsWith('s') &&
    !w.endsWith('ss') &&
    !w.endsWith('us') &&
    !w.endsWith('is') &&
    w.length > 3
  ) {
    cands.add(w.slice(0, -1));
  }

  if (w.endsWith('ied') && w.length > 4) {
    cands.add(w.slice(0, -3) + 'y');
  }

  if (w.endsWith('ed') && w.length > 3) {
    cands.add(w.slice(0, -2));
    cands.add(w.slice(0, -1));
  }

  if (w.endsWith('ing') && w.length > 4) {
    cands.add(w.slice(0, -3));
    cands.add(w.slice(0, -3) + 'e');
  }

  return Array.from(cands);
}

/**
 * Look up a word against one or more dictionaries. First dict wins on conflict
 * (so manual COMMON_DICT can override auto-generated AUTO_DICT). A `null`
 * value in any dict means "explicitly known stop-word, no tooltip" — return
 * null up so the caller skips this token.
 */
export function lookup(
  word: string,
  ...dicts: Readonly<Record<string, string | null>>[]
): string | null {
  for (const cand of lemmaCandidates(word)) {
    for (const dict of dicts) {
      if (cand in dict) {
        return dict[cand]; // may be null (intentional stop-word)
      }
    }
  }
  return null;
}
