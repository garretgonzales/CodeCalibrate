import { useEffect, useMemo, useState } from "react";

import {
  applyThemePreferences,
  isValidMode,
  isValidPalette,
  readAppliedThemePreferences,
  readThemePreferences,
  storeThemePreferences,
} from "./themePreferences";
import ThemeContext from "./ThemeContext";

function getInitialThemePreferences() {
  return readAppliedThemePreferences() ?? readThemePreferences();
}

export function ThemeProvider({ children }) {
  const [preferences, setPreferences] = useState(getInitialThemePreferences);

  useEffect(() => {
    const appliedPreferences = applyThemePreferences(preferences);
    storeThemePreferences(appliedPreferences);
  }, [preferences]);

  const contextValue = useMemo(
    () => ({
      palette: preferences.palette,
      mode: preferences.mode,
      setPalette(nextPalette) {
        if (isValidPalette(nextPalette)) {
          setPreferences((current) => ({
            ...current,
            palette: nextPalette,
          }));
        }
      },
      setMode(nextMode) {
        if (isValidMode(nextMode)) {
          setPreferences((current) => ({
            ...current,
            mode: nextMode,
          }));
        }
      },
    }),
    [preferences],
  );

  return (
    <ThemeContext.Provider value={contextValue}>
      {children}
    </ThemeContext.Provider>
  );
}
