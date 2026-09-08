export const THEME_STORAGE_KEY = "codeCalibrate.themePreferences";

export const THEME_PALETTES = Object.freeze([
  Object.freeze({ value: "calibration", label: "Calibration" }),
  Object.freeze({ value: "aquamarine-bronze", label: "Aquamarine Bronze" }),
  Object.freeze({ value: "midnight-mint", label: "Midnight Mint" }),
  Object.freeze({ value: "tech-stack", label: "Tech Stack" }),
]);

export const THEME_MODES = Object.freeze([
  Object.freeze({ value: "light", label: "Light" }),
  Object.freeze({ value: "dark", label: "Dark" }),
]);

export const DEFAULT_THEME_PREFERENCES = Object.freeze({
  palette: "calibration",
  mode: "light",
});

const THEME_TRANSITION_CLASS = "theme-transitioning";
const THEME_TRANSITION_DURATION_MS = 240;
let themeTransitionTimer;

const paletteValues = new Set(THEME_PALETTES.map(({ value }) => value));
const modeValues = new Set(THEME_MODES.map(({ value }) => value));

export function isValidPalette(value) {
  return typeof value === "string" && paletteValues.has(value);
}

export function isValidMode(value) {
  return typeof value === "string" && modeValues.has(value);
}

export function validateThemePreferences(value) {
  if (
    value === null ||
    typeof value !== "object" ||
    Array.isArray(value) ||
    !isValidPalette(value.palette) ||
    !isValidMode(value.mode)
  ) {
    return null;
  }

  return {
    palette: value.palette,
    mode: value.mode,
  };
}

export function readThemePreferences() {
  try {
    const storedPreferences = window.localStorage.getItem(THEME_STORAGE_KEY);

    if (!storedPreferences) {
      return DEFAULT_THEME_PREFERENCES;
    }

    const preferences = validateThemePreferences(
      JSON.parse(storedPreferences),
    );

    if (!preferences) {
      window.localStorage.removeItem(THEME_STORAGE_KEY);
      return DEFAULT_THEME_PREFERENCES;
    }

    return preferences;
  } catch {
    return DEFAULT_THEME_PREFERENCES;
  }
}

export function readAppliedThemePreferences() {
  if (typeof document === "undefined") {
    return null;
  }

  return validateThemePreferences({
    palette: document.documentElement.dataset.palette,
    mode: document.documentElement.dataset.mode,
  });
}

export function applyThemePreferences(value) {
  const preferences =
    validateThemePreferences(value) ?? DEFAULT_THEME_PREFERENCES;

  if (typeof document !== "undefined") {
    const root = document.documentElement;
    const themeChanged =
      root.dataset.palette !== preferences.palette ||
      root.dataset.mode !== preferences.mode;
    const reduceMotion = window.matchMedia?.(
      "(prefers-reduced-motion: reduce)",
    ).matches;

    if (themeChanged && !reduceMotion) {
      window.clearTimeout(themeTransitionTimer);
      root.classList.add(THEME_TRANSITION_CLASS);
      void root.offsetWidth;
    }

    root.dataset.palette = preferences.palette;
    root.dataset.mode = preferences.mode;
    root.style.colorScheme = preferences.mode;

    if (themeChanged && !reduceMotion) {
      themeTransitionTimer = window.setTimeout(() => {
        root.classList.remove(THEME_TRANSITION_CLASS);
      }, THEME_TRANSITION_DURATION_MS);
    } else {
      root.classList.remove(THEME_TRANSITION_CLASS);
    }
  }

  return preferences;
}

export function storeThemePreferences(value) {
  const preferences = validateThemePreferences(value);

  if (!preferences) {
    return false;
  }

  try {
    window.localStorage.setItem(
      THEME_STORAGE_KEY,
      JSON.stringify({
        palette: preferences.palette,
        mode: preferences.mode,
      }),
    );
    return true;
  } catch {
    return false;
  }
}
