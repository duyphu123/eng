/** Shared interfaces for vocab data files (read by both Angular + Node scripts). */

export interface WordFamily {
  pos: string;
  word: string;
  vi: string;
}

export interface VocabEntry {
  word: string;
  pos: string;
  ipa: string;
  vi: string;
  example: string;
  exampleVi: string;
  highlight: string;
  emoji?: string;
  image?: string;
  family?: WordFamily[];
}
