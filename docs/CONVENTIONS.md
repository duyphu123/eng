# EngToeic — Conventions & Rules

Quy ước kỹ thuật cho dự án. Đọc trước khi code mới. Ưu tiên hơn instinct mặc định.

---

## 0. Content source — Google Drive PDFs là xương sống

Toàn bộ **nội dung vocab/lesson** của project được lấy từ chuỗi PDF Mun English trên Google Drive (series "600 từ vựng TOEIC"). Mỗi lesson = 1 file PDF.

### Cấu trúc PDF chuẩn (mọi lesson tuân theo)

| Section | Nội dung | Map vào code |
|---|---|---|
| **I. Words to Learn** | ~12 từ vựng: pos, từ, nghĩa VN | `vocab[]` chính của page |
| **II. Word Families** | Họ từ (vd: agree/agreement/agreeable) | Field `family?: WordFamily[]` trên mỗi VocabEntry |
| **III. Script** | 9 câu hỏi listening TOEIC + đáp án + transcript | Page listening riêng (`/chu-de/<topic>/luyen-nghe`) — chưa làm |

### Workflow khi user paste Drive link

1. Extract `fileId` từ URL `https://drive.google.com/file/d/<id>/view`
2. Đọc nội dung qua `mcp__claude_ai_Google_Drive__read_file_content { fileId }`
3. Parse 3 section, map theo bảng trên
4. **Author thêm:** IPA (tra dictionary), `example` + `exampleVi` (viết theo TOEIC business context với markup `[phrase]{vi}`), `highlight` (base form của từ đích)
5. **Không bịa entry không có trong PDF** — user chọn lesson có chủ đích

### Files đã identified

- Lesson 1 — Contracts: `1mjQxYzOfWpmcysmF_EZFfy-rrC7Npmx7` → [`src/app/pages/contract/vocab/`](../src/app/pages/contract/vocab/)
- *(các lesson sau sẽ thêm vào đây khi user paste link)*

---

## 1. Design system — DESIGN.md là source of truth

- **Source of truth:** [`DESIGN.md`](../DESIGN.md) ở root project (format Google Labs `@google/design.md`).
- **Workflow khi đổi style:**
  1. Sửa `DESIGN.md` (YAML frontmatter cho token, markdown body cho rationale).
  2. `npm run design:lint` — validate cấu trúc, contrast WCAG.
  3. `npm run design:tokens` — sinh lại `src/styles/_tokens.scss`.
- **Không sửa tay** `src/styles/_tokens.scss` (file auto-generated).

### Quy tắc tuyệt đối: không hardcode style

Trong **mọi** SCSS component, luôn dùng token, **không** literal:

| ❌ Sai | ✅ Đúng |
|---|---|
| `color: #4F46E5` | `color: var(--color-primary)` |
| `padding: 16px` | `padding: var(--space-4)` |
| `border-radius: 8px` | `border-radius: var(--rounded-md)` |
| `font-size: 20px; font-weight: 600` | `@include type-heading-md` |

**Ngoại lệ cho phép literal:** layout primitives không thuộc design system — `1px` (hairline border), `0`, `auto`, `100%`, `transparent`, transition duration (`0.15s`), icon size hardcoded (`16px`, `width: 14px`).

**Khi thiếu token:** thêm vào `DESIGN.md` rồi regenerate, đừng bypass.

---

## 2. Angular 17 patterns

- **Standalone components** mặc định (`standalone: true`, không NgModule).
- **`inject()`** thay constructor DI:
  ```ts
  private readonly tts = inject(TtsService);
  ```
- **Signals** cho state component, không dùng `BehaviorSubject` cho UI state thuần:
  ```ts
  private readonly translated = signal(new Set<string>());
  ```
- **Control flow mới:** `@if` / `@for` / `@switch` thay `*ngIf` / `*ngFor`.
- **Lazy routes:** `loadComponent: () => import(...).then(m => m.X)`.

---

## 3. TTS audio — pre-recorded MP3 + Web Speech fallback

Project dùng **2 tầng audio** để đảm bảo chất lượng đồng nhất trên mọi platform:

### Tầng 1 (ưu tiên) — Pre-recorded MP3

- Generate trước bằng [`scripts/gen-audio.mjs`](../scripts/gen-audio.mjs) sử dụng [`msedge-tts`](https://www.npmjs.com/package/msedge-tts) (free, không API key).
- Voice: **Microsoft Neural** — `en-US-AriaNeural` cho EN, `vi-VN-HoaiMyNeural` cho VI.
- Output: `src/assets/audio/{en-US|vi-VN}/<slug>.mp3` + `src/assets/audio/manifest.json` mapping `text → filename`.
- Runtime: TtsService load manifest, khi cần phát text+lang nào → tra manifest, play local MP3 qua `<audio>` element. Không CORS, không cần internet, chất lượng cao nhất.

### Tầng 2 (fallback) — Web Speech API

- Nếu manifest KHÔNG có entry cho text+lang → dùng `SpeechSynthesisUtterance` với voice native của OS.
- Áp dụng cho text động (vd: từng từ trong câu khi user hover, không nằm trong vocab list).

### Workflow khi thêm/sửa vocab

1. Sửa file JSON trong `src/app/data/<lesson>.json`
2. Thêm path vào `LESSONS` array trong `scripts/gen-audio.mjs` (nếu là file mới)
3. Chạy `npm run gen-audio` — script:
   - Đọc tất cả JSON
   - Tạo MP3 cho mọi `word` (EN) và `vi` (VI)
   - Skip những file đã tồn tại (cached)
   - Update `manifest.json`
4. Commit MP3 files + manifest

### Vì sao chọn approach này

- ❌ Google Translate unofficial endpoint: bị block CORS từ origin khác
- ❌ Web Speech native: máy Windows thường không có vi-VN voice
- ❌ Cloud TTS API: cần API key + có cost
- ✅ Pre-recorded với msedge-tts: **free, quality cao, hoạt động mọi browser/mobile, offline**

### Quy tắc lưu

- Slug = `text.normalize('NFD').stripDiacritics().replace(đ→d).kebab-case().slice(0,80)`
- Manifest **commit vào git** — không phải build artifact (cần serve runtime)
- MP3 files **cũng commit** — ~5-10KB mỗi file, lesson 12 từ ≈ 120KB tổng

## 4. Web Speech API (TTS) — internal fallback rules

Tham khảo [`src/app/services/tts.service.ts`](../src/app/services/tts.service.ts).

### Vấn đề & cách giải

| Vấn đề | Giải pháp |
|---|---|
| Lần đầu click bị delay 500ms–2s (cold-start) | **Pre-warm** engine bằng utterance câm (`volume = 0`) trên gesture đầu tiên của user (`pointerdown` + `keydown`, `{ once: true, capture: true }`). |
| `getVoices()` trả mảng rỗng lúc đầu | Gọi `getVoices()` ngay khi service khởi tạo + listen event `voiceschanged` → cache `cachedVoice`. |
| `cancel()` gọi khi engine idle gây delay | Chỉ gọi `cancel()` nếu `ss.speaking \|\| ss.pending`. |
| Chrome đôi khi vào paused state sau `speak()` | Gọi `ss.resume()` ngay sau `ss.speak()`. |
| Race condition khi đổi nhanh giữa các từ → `onend` cũ clear state sai | **Generation counter** — mỗi `speak()` tăng counter, trong `onend` kiểm tra `myGen === this.generation` mới clear. |
| `SpeechSynthesisUtterance` events ngoài Angular zone | Bọc signal update trong `NgZone.run()` (defensive — signals tự trigger CD nhưng safer). |

### Voice selection

Ưu tiên theo thứ tự:
1. `lang === 'en-US' && /Google|Natural|Online/.test(name)` — chất lượng cao
2. `lang === 'en-US'` — bất kỳ voice en-US
3. `lang.startsWith('en')` — fallback en bất kỳ

### Default settings

- `lang = 'en-US'` (TOEIC chuẩn, voice phổ biến nhất)
- `rate = 0.9` (chậm hơn natural một chút cho người học)
- `pitch = 1`

### SSR safety

Mọi entry point đều check `typeof window !== 'undefined' && 'speechSynthesis' in window` — kể cả project hiện tại không bật SSR (đỡ phải rewrite sau).

---

## 5. UI conventions

### Icon button pattern (loa, dịch, hành động phụ)

- Kích thước: **28×28** hoặc **32×32**
- `border-radius: var(--rounded-md)`
- Default: `background: rgba(255, 255, 255, 0.5)`, border transparent, color `neutral-600`
- Hover: bg `neutral-0`, color `primary`, border `primary-soft`
- Active state (đang xử lý): bg `primary` solid, color `neutral-0`
- Click feedback: `transform: scale(0.92)` ở `:active`
- Loading state: `@keyframes` pulse với box-shadow gradient
- Transition chuẩn: `0.15s ease`

### Vocab data shape

```ts
interface VocabEntry {
  word: string;        // từ tiếng Anh (lower-case)
  pos: string;         // 'n.' | 'v.' | 'adj.' | 'n./v.' ...
  ipa: string;         // '/ˈkɒntrækt/' — luôn có / /
  vi: string;          // nghĩa tiếng Việt
  example: string;     // câu ví dụ EN với markup tooltip: `[phrase]{vi meaning}` — xem mục 8
  exampleVi: string;   // bản dịch VN
  highlight: string;   // từ/cụm cần highlight trong example (có thể khác form: 'parties' cho 'party')
  emoji: string;       // emoji visual cue (fallback nếu image lỗi/thiếu)
  image?: string;      // optional: ABSOLUTE path '/assets/vocab/<word>.svg'
}
```

**Quan trọng — image path phải bắt đầu bằng `/`:**

Angular routing → `[src]="v.image"` resolve tương đối theo URL route hiện tại. Ở route `/chu-de/contract/tu-vung`, path `assets/vocab/x.svg` (không có `/` đầu) bị resolve thành `/chu-de/contract/assets/vocab/x.svg` → 404 → fallback emoji.

`<base href="/">` trong `index.html` **chỉ áp** cho `<a href>`, không áp cho `<img [src]>` binding. Phải dùng absolute path.

### Highlight pattern

Dùng `<mark>` tag, styled trong card:
```scss
mark {
  background: var(--color-accent);
  color: var(--color-neutral-900);
  padding: 1px 4px;
  border-radius: var(--rounded-sm);
}
```

Khi cần style mark từ template binding qua `[innerHTML]`, dùng `::ng-deep mark { ... }` trong component SCSS.

### Toggle state với Set + signal

Pattern cho "card nào đang ở trạng thái X":
```ts
private readonly translated = signal(new Set<string>());

toggle(key: string): void {
  const next = new Set(this.translated());
  next.has(key) ? next.delete(key) : next.add(key);
  this.translated.set(next);    // tạo Set mới, signal detect thay đổi
}

isOn(key: string): boolean {
  return this.translated().has(key);
}
```

**Quan trọng:** phải tạo `new Set(...)` mới, không `.add()` trực tiếp vào Set cũ (signal so sánh reference).

---

## 6. Sidebar / Layout

- **Desktop (≥1024px):** Sidebar **fixed bên trái**, `width: 280px`, `height: 100vh`. Main content thêm `padding-left: calc(280px + var(--space-5))`.
- **Mobile/tablet (<1024px):** Sidebar thành **drawer** — slide-in từ trái, ẩn mặc định. Hamburger button cố định top-left (44×44). Click → mở drawer, có backdrop overlay đen 45% opacity. Click ngoài / chọn route mới → tự đóng (`NavigationEnd` listener trong AppComponent).
- Menu items: dùng cấu trúc data-driven + `ng-template` đệ quy (xem [`sidebar.component.html`](../src/app/components/sidebar/sidebar.component.html)) để dễ thêm chủ đề mới.
- State drawer (`sidebarOpen` signal) đặt trong `AppComponent` — đơn giản, không cần service riêng vì chỉ có 1 nơi điều khiển.

## 7. Responsive — mobile-first cho mọi màn hình

### Breakpoints

Định nghĩa trong [`src/styles/_breakpoints.scss`](../src/styles/_breakpoints.scss):

| Mixin | Min width | Khi nào dùng |
|---|---|---|
| (default — không mixin) | 0px | Mobile-first base — tất cả style mặc định |
| `@include media-md` | 640px | Tablet portrait / phone landscape — grid 2 cột, padding lớn hơn |
| `@include media-lg` | 1024px | Desktop — sidebar persistent, hamburger ẩn |

```scss
@use '../../../styles/breakpoints' as *;

.card {
  padding: var(--space-4);

  @include media-md {
    padding: var(--space-5);
  }
}
```

**Không tạo media query rời rạc** — luôn dùng mixin để giữ breakpoint đồng nhất.

### Touch targets

- Mọi button/link **clickable trên mobile** phải có ô bấm tối thiểu **40×40px** (đề xuất 44×44 cho action quan trọng).
- Áp dụng: hamburger 44×44, speaker-btn 40×40, translate-btn 40×40, menu items `min-height: 44px`.

### Typography fluid

Heading lớn (display-lg, display-md) **bị tràn 2 dòng xấu trên màn nhỏ**. Dùng `clamp(min, fluid, max)` để scale theo viewport:

```scss
.hero__title {
  @include type-display-lg;             // family + weight + line-height từ token
  font-size: clamp(28px, 7vw, 40px);    // 40px là giá trị token, scale xuống 28px ở 320px viewport
}
```

**Exception cho no-hardcode rule:** giá trị `clamp` là **layout primitive cho fluid scaling**, không phải "design decision". Vẫn phải reference token value làm `max` để biết desktop dùng size nào.

### Adaptive padding

- Mobile (default): dùng `var(--space-3)` hoặc `var(--space-4)`.
- Desktop (`@include media-md` hoặc `media-lg`): tăng lên `var(--space-5)` hoặc cao hơn.

### Grid layout

Vocab grid và list view: mặc định 1 cột, từ `@include media-md` chuyển sang `auto-fill minmax`.

```scss
.list {
  grid-template-columns: 1fr;
  gap: var(--space-3);

  @include media-md {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: var(--space-4);
  }
}
```

### Khi tạo page/component mới

1. Viết style **mobile-first** trước (giả định viewport ~360px)
2. Thêm `@include media-md { ... }` cho điều chỉnh tablet
3. Thêm `@include media-lg { ... }` nếu cần khác biệt desktop
4. **Test ở 3 viewport** trước khi báo xong: 375px (iPhone), 768px (tablet portrait), 1280px (desktop)
5. Touch targets ≥40px ở mọi nơi user có thể bấm

---

## 8. Angular build budget

Đã raise `anyComponentStyle` budget lên `8kb` warning / `16kb` error (mặc định 2/4 quá chặt cho component có nhiều variant style). Xem [`angular.json`](../angular.json).

---

## 9. Tooltip dịch từ trong câu English

Mọi câu English trong project hiển thị qua [`<app-tooltip-text>`](../src/app/shared/tooltip/tooltip-text.component.ts) để có **tooltip nghĩa tiếng Việt** khi hover (PC) / tap (mobile).

### Markup syntax — hybrid (manual + auto-dict)

**Hai lớp tooltip song song:**

1. **Manual markup** `[phrase]{vi}` — luôn ưu tiên cao nhất, dùng cho cụm (phrasal verbs, idioms, collocations) và những từ cần dịch khác với nghĩa mặc định trong dict.
2. **Auto-match** từ [`COMMON_DICT`](../src/app/shared/dict/common-toeic.ts) — tự động tooltip cho từ đơn TOEIC phổ thông (verbs, nouns, prepositions). Có **lemma support** (parties → party, reached → reach, signing → sign).

```
'Both [parties]{các bên} reached a [mutual agreement]{thỏa thuận chung} after [long negotiations]{cuộc đàm phán dài}.'
```

→ Render:
- `parties` solid dotted indigo (manual)
- `mutual agreement` solid dotted indigo (manual)
- `long negotiations` solid dotted indigo (manual)
- **`reached`** dashed gray (auto-matched từ dict: "đạt được, với tới")
- `Both`, `a`, `after` không match dict → không có tooltip

**Quy tắc:**
- `[phrase]` — đoạn text hiển thị (1 từ hoặc cụm)
- `{vi meaning}` — nghĩa tiếng Việt
- Đặt sát nhau, không space giữa `]` và `{`

### Khi nào dùng manual markup vs để auto

| Trường hợp | Cách |
|---|---|
| Cụm > 1 từ (`last Friday`, `breach of contract`, `look forward to`) | **Manual** — auto chỉ match từ đơn |
| Từ trong dict, nghĩa khác do ngữ cảnh (vd `party` = bữa tiệc thay vì "bên") | **Manual** override |
| Từ chưa có trong dict | Manual hoặc **thêm vào dict** nếu sẽ tái dùng nhiều lesson |
| Từ TOEIC phổ thông trong dict | **Không cần** — auto tự xử |

### 2 tầng dictionary

| Tầng | File | Cách sinh | Khi nào dùng |
|---|---|---|---|
| **COMMON_DICT** (manual) | [`src/app/shared/dict/common-toeic.ts`](../src/app/shared/dict/common-toeic.ts) | Hand-curated | Override khi muốn ép nghĩa khác mặc định AI (vd: domain-specific term, idiom) |
| **AUTO_DICT** (AI-generated) | [`src/app/shared/dict/auto-dict.json`](../src/app/shared/dict/auto-dict.json) | `npm run gen-dict` (Claude Haiku 4.5) | Tự động dịch mọi từ trong content data |

Runtime: COMMON_DICT **lookup trước** → nếu không có, fallback AUTO_DICT. Manual markup `[phrase]{vi}` trong câu **luôn ưu tiên cao nhất**.

### Workflow build-time generation

1. **Setup 1 lần** — set API key của 1 trong các provider (script tự detect):

   | Provider | Env var | Get key | Free tier |
   |---|---|---|---|
   | **Google Gemini** (recommended) | `GEMINI_API_KEY` | [aistudio.google.com/app/apikey](https://aistudio.google.com/app/apikey) | 1500 req/ngày |
   | **Groq** (Llama 3.3 70B) | `GROQ_API_KEY` | [console.groq.com/keys](https://console.groq.com/keys) | 14400 req/ngày |
   | **OpenRouter** | `OPENROUTER_API_KEY` | [openrouter.ai/keys](https://openrouter.ai/keys) | Hạn chế free model |

   ```powershell
   $env:GEMINI_API_KEY = "..."
   ```
2. **Khi thêm content** (vocab/passage JSON mới): `npm run gen-dict`
3. Script `scripts/gen-dict.mjs`:
   - Scan tất cả `src/app/data/*.json`
   - Extract unique English words (strip markup, lowercase)
   - **Skip những từ đã có** trong `auto-dict.json` (incremental)
   - Gọi LLM theo batch 60 từ qua OpenAI-compatible endpoint
   - Map mỗi từ → nghĩa Việt (1-3 nghĩa, business context); stop-words → `null`
   - Save `auto-dict.json` sorted alphabetically
4. Commit `auto-dict.json` vào git → runtime ko cần API

### Cost

- **Gemini 2.0 Flash**: **Free** trong giới hạn 1500 req/ngày (4 batch × ~200 từ = đủ cho cả project)
- Groq: Free
- Run incrementally → ngay cả khi free tier hết quota cũng chỉ cần đợi reset

### Khi nào dùng manual COMMON_DICT vs AUTO_DICT

| Trường hợp | Tầng |
|---|---|
| Từ phổ thông TOEIC, nghĩa mặc định OK | **AUTO_DICT** — tự sinh, không phải maintain |
| Từ trong vocab list chính của lesson | Manual markup trong `example` (`[contract]{hợp đồng}`) ưu tiên |
| Domain-specific muốn override (vd `party` = "bữa tiệc" trong context khác) | **COMMON_DICT** — manual override |
| Nghĩa Claude trả về sai/lệch context | Thêm vào **COMMON_DICT** để override |

### Sử dụng trong template

```html
<app-tooltip-text [text]="v.example" [highlight]="v.highlight" />
```

- `text` — chuỗi có markup
- `highlight` (optional) — từ/cụm nào trong markup khớp với chuỗi này sẽ được **strong amber background** thay vì dotted underline. Dùng cho "từ vựng đích" của card.

### Visual states

| State | Style |
|---|---|
| Marked phrase mặc định | Dotted underline indigo, cursor `help` |
| Marked phrase + khớp `highlight` | Solid amber background (`<mark>`-style) |
| Hover/focus | Soft indigo bg + indigo-strong text |

### Behavior

| Device | Trigger | Dismiss |
|---|---|---|
| PC (hover capable) | `mouseenter` show, `mouseleave` hide. Cũng support `focus`/`blur` cho keyboard | Click ngoài / Esc |
| Mobile/touch | Tap để toggle. Tap ngoài để đóng | Tap ngoài / Esc |

Detect mobile: `window.matchMedia('(hover: none) and (pointer: coarse)')`.

### Cấu trúc

- [`tooltip.service.ts`](../src/app/shared/tooltip/tooltip.service.ts) — singleton, **một** popover dùng chung cả app. Dùng `@floating-ui/dom` cho position (auto-flip, shift trong viewport, arrow).
- [`tooltip-text.component.ts`](../src/app/shared/tooltip/tooltip-text.component.ts) — parse markup, render inline spans, gắn handler hover/click.
- Global style `.word-tooltip` đặt trong [`src/styles.scss`](../src/styles.scss) (vì popover render trực tiếp vào `<body>`).

### Khi nào KHÔNG dùng markup

- Câu thuần tiếng Việt → render bằng `<span>` thường, không cần `<app-tooltip-text>`
- Câu English không cần tra nghĩa (như tên riêng, code) → render plain text

### Mở rộng

Nếu sau này muốn auto-match từ đơn (không phải mark tay), tham khảo phương án Hybrid trong session discussion: combine markup (cho cụm) + dict-based auto-match (cho từ đơn).

---

## 10. Không lặp SCSS — 3-layer reuse strategy

Project tránh duplicate CSS qua **3 tầng** (tham khảo trước khi viết style mới):

### Layer 1 — SCSS Mixins ([`src/styles/_mixins.scss`](../src/styles/_mixins.scss))

Pattern lặp trong style → tạo mixin. **Không copy-paste**.

| Mixin | Mục đích |
|---|---|
| `@mixin icon-button($size)` | Button vuông icon (default 40px, dùng 36px cho in-card) |
| `@mixin button-active` | State đang chọn (primary fill) |
| `@mixin button-pulse` | Halo animation (đang phát/loading) |
| `@mixin eyebrow` | Caption label phía trên title |
| `@mixin transition-default` | Transition chuẩn 0.15s ease |
| `@mixin clickable-feedback` | `:active { transform: scale(0.92) }` |
| `@mixin card-base` | Card container (rounded-lg, padding adaptive) |
| `@mixin card-tinted($bg)` | Card với background tùy chỉnh |
| `@mixin fluid-font($min, $vw, $max)` | clamp() typography |

**Quy tắc:** Pattern xuất hiện **≥2 lần** → promote thành mixin ngay.

### Layer 2 — Shared Angular Components (`src/app/shared/`)

Pattern xuất hiện **≥3 page** hoặc cần encapsulate logic → tạo component.

| Component | Selector | Dùng cho |
|---|---|---|
| [`IconButtonComponent`](../src/app/shared/icon-button/icon-button.component.ts) | `<app-icon-button>` | Mọi icon button (speaker, translate, close, more) |
| [`PageHeadComponent`](../src/app/shared/page-head/page-head.component.ts) | `<app-page-head>` | Header của mọi content page (eyebrow + h1 + sub) |
| [`TooltipTextComponent`](../src/app/shared/tooltip/tooltip-text.component.ts) | `<app-tooltip-text>` | Câu English có markup `[phrase]{vi}` |

**Lợi ích:** Component encapsulation → SCSS chỉ load 1 lần (Angular scoped style). Bundle nhỏ hơn mixin.

**Khi nào KHÔNG promote thành component:** chỉ 1-2 chỗ dùng → mixin đủ, đừng over-engineer.

### Layer 3 — Global utility classes ([`src/styles.scss`](../src/styles.scss))

Pattern **layout-only**, không có logic, dùng khắp project → utility class prefix `u-`.

| Class | Tác dụng |
|---|---|
| `.u-sr-only` | Visually hidden cho screen reader |
| `.u-stack` | Vertical flex + gap (column layout nhanh) |
| `.u-cluster` | Horizontal flex wrap + gap (chip/tag group) |
| `.u-no-wrap` | `white-space: nowrap` |

**Khi nào dùng:** quick layout helper trong template. **Không dùng** để thay thế design system token (vẫn dùng `var(--space-*)` cho spacing).

### Quy trình khi thấy CSS lặp

1. Pattern lặp **2 lần** → tạo mixin ở Layer 1
2. Pattern lặp **3+ page** với cấu trúc HTML phức tạp → promote lên Layer 2 (Angular component)
3. Layout helper ngắn (≤3 dòng CSS) dùng nhiều → utility class ở Layer 3

### Anti-patterns

- ❌ Copy-paste style giống nhau giữa các component SCSS
- ❌ Tạo mixin cho pattern chỉ dùng 1 chỗ
- ❌ Promote thành component khi chỉ dùng 1 page (premature abstraction)
- ❌ Dùng utility class thay token (vd: `.u-p-4` thay `var(--space-4)`)
- ❌ `@extend` xuyên Angular component (không xuyên được scope)

---

## 11. npm scripts có sẵn

| Lệnh | Mục đích |
|---|---|
| `npm start` | Dev server (http://localhost:4200) |
| `npm run build` | Build production |
| `npm run design:lint` | Validate DESIGN.md |
| `npm run design:tokens` | Regen `_tokens.scss` từ DESIGN.md |
| `npm run design:export:tailwind` | Xuất tokens dạng Tailwind theme JSON |
| `npm run design:export:dtcg` | Xuất tokens chuẩn W3C DTCG |
| `npm run gen-audio` | Generate MP3 pre-recorded TTS từ vocab JSON (msedge-tts) |
| `npm run gen-dict` | Generate auto-dict.json từ content JSON (Claude Haiku 4.5) — cần `ANTHROPIC_API_KEY` |

---

## Khi sửa tài liệu này

- Giữ format bảng cho quy tắc — dễ scan.
- Khi giải quyết bug khó, viết lại dưới dạng **Vấn đề → Giải pháp** (như mục TTS).
- Đừng viết lý thuyết — chỉ rule + ví dụ code thật.
