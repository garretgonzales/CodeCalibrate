import { useEffect, useRef } from "react";

import { THEME_MODES, THEME_PALETTES } from "../theme/themePreferences";
import { useTheme } from "../theme/useTheme";

function ThemeControls() {
  const { palette, mode, setPalette, setMode } = useTheme();
  const detailsRef = useRef(null);

  useEffect(() => {
    function handlePointerDown(event) {
      if (
        detailsRef.current?.open &&
        !detailsRef.current.contains(event.target)
      ) {
        detailsRef.current.removeAttribute("open");
      }
    }

    function handleKeyDown(event) {
      if (event.key === "Escape" && detailsRef.current?.open) {
        detailsRef.current.removeAttribute("open");
        detailsRef.current.querySelector("summary")?.focus();
      }
    }

    document.addEventListener("pointerdown", handlePointerDown);
    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("pointerdown", handlePointerDown);
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, []);

  return (
    <details className="theme-controls relative shrink-0" ref={detailsRef}>
      <summary className="theme-trigger inline-flex min-h-10 cursor-pointer list-none items-center justify-center gap-2 border border-inverse-border bg-transparent px-3 py-2 text-sm font-semibold text-inverse-foreground transition hover:bg-inverse-overlay focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 [&::-webkit-details-marker]:hidden">
        <span aria-hidden="true">◐</span>
        <span>Theme</span>
      </summary>

      <div className="theme-panel absolute right-0 z-50 mt-3 w-64 border border-strong bg-surface p-4 text-ink-950 shadow-xl">
        <p className="m-0 font-mono text-[0.68rem] font-bold tracking-[0.16em] text-ink-500">
          DISPLAY CALIBRATION
        </p>

        <label
          className="mt-4 block text-sm font-semibold text-ink-700"
          htmlFor="theme-palette">
          Color palette
        </label>
        <select
          className="mt-2 min-h-10 w-full border border-strong bg-surface px-3 py-2 text-sm text-ink-950 focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400"
          id="theme-palette"
          value={palette}
          onChange={(event) => setPalette(event.target.value)}>
          {THEME_PALETTES.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>

        <fieldset className="mt-4 border-0 p-0">
          <legend className="text-sm font-semibold text-ink-700">
            Appearance
          </legend>
          <div className="mt-2 grid grid-cols-2 border border-strong bg-surface-muted p-1">
            {THEME_MODES.map((option) => (
              <label
                className={`relative cursor-pointer px-3 py-2 text-center text-sm font-semibold focus-within:outline-3 focus-within:outline-offset-2 focus-within:outline-accent-400 ${
                  mode === option.value
                    ? "bg-primary text-primary-contrast shadow-sm"
                    : "text-ink-700 hover:bg-surface-strong"
                }`}
                key={option.value}>
                <input
                  className="sr-only"
                  type="radio"
                  name="theme-mode"
                  value={option.value}
                  checked={mode === option.value}
                  onChange={(event) => setMode(event.target.value)}
                />
                {option.label}
              </label>
            ))}
          </div>
        </fieldset>

        <p className="mb-0 mt-3 text-xs leading-5 text-ink-500" aria-live="polite">
          {THEME_PALETTES.find((option) => option.value === palette)?.label},{" "}
          {mode} mode
        </p>
      </div>
    </details>
  );
}

export default ThemeControls;
