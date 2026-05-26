---
version: alpha
name: EngToeic
description: Visual identity for an English listening & vocabulary learning app focused on TOEIC preparation.
colors:
  primary: "#4F46E5"
  primary-soft: "#EEF2FF"
  primary-strong: "#3730A3"
  secondary: "#0D9488"
  secondary-soft: "#CCFBF1"
  accent: "#F59E0B"
  accent-soft: "#FEF3C7"
  success: "#10B981"
  warning: "#F59E0B"
  danger: "#EF4444"
  neutral-0: "#FFFFFF"
  neutral-50: "#F8FAFC"
  neutral-100: "#F1F5F9"
  neutral-200: "#E2E8F0"
  neutral-400: "#94A3B8"
  neutral-600: "#475569"
  neutral-800: "#1E293B"
  neutral-900: "#0F172A"
typography:
  display-lg:
    fontFamily: Lexend
    fontSize: 40px
    fontWeight: 600
    lineHeight: 1.15
    letterSpacing: -0.02em
  display-md:
    fontFamily: Lexend
    fontSize: 32px
    fontWeight: 600
    lineHeight: 1.2
    letterSpacing: -0.01em
  heading-lg:
    fontFamily: Lexend
    fontSize: 24px
    fontWeight: 600
    lineHeight: 1.3
  heading-md:
    fontFamily: Lexend
    fontSize: 20px
    fontWeight: 600
    lineHeight: 1.35
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: 400
    lineHeight: 1.6
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: 400
    lineHeight: 1.6
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: 400
    lineHeight: 1.5
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: 500
    lineHeight: 1.4
    letterSpacing: 0.01em
  caption:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: 500
    lineHeight: 1.4
    letterSpacing: 0.04em
  mono-vocab:
    fontFamily: JetBrains Mono
    fontSize: 15px
    fontWeight: 400
    lineHeight: 1.5
rounded:
  none: 0px
  sm: 4px
  md: 8px
  lg: 12px
  xl: 16px
  pill: 9999px
spacing:
  0: 0px
  1: 4px
  2: 8px
  3: 12px
  4: 16px
  5: 24px
  6: 32px
  7: 48px
  8: 64px
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.neutral-0}"
    rounded: "{rounded.md}"
    padding: "{spacing.3}"
    typography: "{typography.label-md}"
  button-ghost:
    backgroundColor: "{colors.neutral-0}"
    textColor: "{colors.primary}"
    rounded: "{rounded.md}"
    padding: "{spacing.3}"
    typography: "{typography.label-md}"
  card:
    backgroundColor: "{colors.neutral-0}"
    rounded: "{rounded.lg}"
    padding: "{spacing.5}"
  listening-player:
    backgroundColor: "{colors.primary-soft}"
    textColor: "{colors.neutral-900}"
    rounded: "{rounded.lg}"
    padding: "{spacing.5}"
  vocab-card:
    backgroundColor: "{colors.accent-soft}"
    textColor: "{colors.neutral-900}"
    rounded: "{rounded.lg}"
    padding: "{spacing.5}"
    typography: "{typography.heading-md}"
  quiz-option:
    backgroundColor: "{colors.neutral-0}"
    rounded: "{rounded.md}"
    padding: "{spacing.4}"
    typography: "{typography.body-md}"
  quiz-option-correct:
    backgroundColor: "{colors.success}"
    textColor: "{colors.neutral-0}"
    rounded: "{rounded.md}"
    padding: "{spacing.4}"
    typography: "{typography.body-md}"
  quiz-option-wrong:
    backgroundColor: "{colors.danger}"
    textColor: "{colors.neutral-0}"
    rounded: "{rounded.md}"
    padding: "{spacing.4}"
    typography: "{typography.body-md}"
---

# EngToeic Design System

## Overview

EngToeic is a focused, encouraging learning environment for Vietnamese learners
preparing for the TOEIC Listening test and building English vocabulary. The
visual identity must feel **calm, modern, and trustworthy** — never noisy or
gamified to the point of distraction. Long study sessions are the norm, so the
UI prioritises low eye strain, generous whitespace, and clear visual hierarchy
between *audio*, *transcript*, and *vocabulary* content.

Tone: friendly but not childish. The user is an adult studying to pass a real
test. Decorations are minimal; emphasis is reserved for the words and sounds
being learned.

## Colors

The palette pairs a calm indigo for trust and focus with a warm amber accent
reserved for new vocabulary and key highlights.

- **Primary (#4F46E5):** Focused indigo. Used for primary actions (Play, Submit,
  Next), active navigation, and the listening player frame.
- **Secondary (#0D9488):** Fresh teal. Used for progress indicators, completed
  lessons, and growth signals (streaks, mastery).
- **Accent (#F59E0B):** Warm amber. Used **only** to highlight target
  vocabulary, the current word being read aloud, or a "tip" callout. Overuse
  destroys its meaning.
- **Success / Warning / Danger:** Standard semantic colors for quiz feedback.
  Quiz answers turn green or red only after the user submits.
- **Neutrals (50 – 900):** A cool slate scale. Body text uses `neutral-800`,
  secondary text uses `neutral-600`, borders use `neutral-200`. Pure black
  (`#000`) is never used.

## Typography

Two typefaces. **Lexend** for headings — it is engineered for reading
proficiency and lowers cognitive load when scanning. **Inter** for body text
and UI labels. **JetBrains Mono** is reserved for IPA phonetic transcription
and word lists where character alignment matters.

- **Display & Heading:** Lexend Semi-Bold. Reserved for screen titles and
  section headers. Tight tracking on the largest sizes.
- **Body:** Inter Regular at 16px (`body-md`) is the default. 18px (`body-lg`)
  is used inside the transcript reader because users stare at it for minutes
  at a time.
- **Label & Caption:** Inter Medium with slight positive tracking, used for
  buttons, tabs, and metadata (timestamps, lesson part numbers).
- **Mono (vocab):** JetBrains Mono Regular for phonetics like `/əˈpɔɪntmənt/`
  and vocabulary index lists.

## Layout

Layout follows a **4px spacing scale** (`spacing.1` = 4px through `spacing.8` =
64px). Content is centered in a single column up to 720px wide on mobile/tablet
and 960px on desktop — readability over filling the viewport.

- Vertical rhythm: section gap = `spacing.6` (32px); card gap = `spacing.4`
  (16px).
- Horizontal page padding: `spacing.4` on mobile, `spacing.6` on desktop.
- The listening player is sticky at the bottom on mobile so playback controls
  are always reachable while scrolling the transcript.

## Elevation & Depth

Elevation is conveyed with **borders and soft tints**, not heavy shadows.

- Default cards: 1px border in `neutral-200`, no shadow.
- Active / focused card: 1px border in `primary`, plus a 4px `primary-soft`
  glow on the focus ring only.
- The listening player uses the tinted `primary-soft` background to lift it
  off the page without a drop shadow.

## Shapes

Rounded scale: `sm` (4px) for inputs and small chips, `md` (8px) for buttons
and quiz options, `lg` (12px) for cards and the player, `pill` (9999px) for
status chips ("New", "Mastered", "Part 1").

Avoid mixing radii within the same component cluster — pick one level per
group.

## Components

- **button-primary:** Solid indigo, white text, `md` radius. The single
  highest-emphasis action per screen (Play, Submit, Next Lesson).
- **button-ghost:** White background, indigo text and border. Secondary
  actions (Replay, Skip, Show Transcript).
- **card:** Neutral surface with a thin border. The base container for lesson
  list items, vocabulary entries, and score summaries.
- **listening-player:** Tinted indigo container holding the audio waveform,
  playback controls, speed selector, and transcript toggle.
- **vocab-card:** Tinted amber container. Headword in `heading-md`, IPA in
  `mono-vocab`, Vietnamese gloss in `body-md`, example sentence in `body-sm`.
- **quiz-option:** Neutral card. On submit, becomes `quiz-option-correct`
  (green) or `quiz-option-wrong` (red). Never reveal correctness before
  submission.

## Do's and Don'ts

- ✅ **Do** use `accent` (amber) sparingly — only for the active vocabulary
  word or a single tip per screen.
- ✅ **Do** keep transcript text in `body-lg` with `neutral-800` for maximum
  readability during long sessions.
- ✅ **Do** show audio progress in `secondary` (teal) to signal forward
  motion / growth.
- ❌ **Don't** use red (`danger`) for anything except wrong-answer feedback.
  Never use it as a brand color or warning chip.
- ❌ **Don't** use drop shadows to indicate elevation. Use borders + soft
  tints.
- ❌ **Don't** mix more than two typefaces on screen. Lexend + Inter is the
  default; JetBrains Mono only appears inside vocabulary blocks.
- ❌ **Don't** auto-play audio without user interaction — respect focus and
  bandwidth.
