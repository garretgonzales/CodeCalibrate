export const EDITOR_PREFERENCES_STORAGE_KEY =
  "codeCalibrate.editorPreferences";

export const DEFAULT_EDITOR_PREFERENCES = Object.freeze({
  autocompleteEnabled: true,
});

export function readEditorPreferences() {
  try {
    const storedPreferences = window.localStorage.getItem(
      EDITOR_PREFERENCES_STORAGE_KEY,
    );

    if (!storedPreferences) {
      return DEFAULT_EDITOR_PREFERENCES;
    }

    const preferences = JSON.parse(storedPreferences);

    if (typeof preferences.autocompleteEnabled !== "boolean") {
      window.localStorage.removeItem(EDITOR_PREFERENCES_STORAGE_KEY);
      return DEFAULT_EDITOR_PREFERENCES;
    }

    return {
      autocompleteEnabled: preferences.autocompleteEnabled,
    };
  } catch {
    return DEFAULT_EDITOR_PREFERENCES;
  }
}

export function storeEditorPreferences(preferences) {
  if (typeof preferences.autocompleteEnabled !== "boolean") {
    return false;
  }

  try {
    window.localStorage.setItem(
      EDITOR_PREFERENCES_STORAGE_KEY,
      JSON.stringify({
        autocompleteEnabled: preferences.autocompleteEnabled,
      }),
    );

    return true;
  } catch {
    return false;
  }
}