import { useEffect, useState } from "react";
import { Link, Navigate, useParams } from "react-router-dom";
import { getExerciseById, submitExercise } from "../api/exercises";
import "../style/AppLayout.css";
import "../style/ExercisePage.css";
import JavaCodeEditor from "../components/JavaCodeEditor";
import ExerciseReferences from "../components/ExerciseReferences";
import VerdictLoader from "../components/VerdictLoader";
import {
  readEditorPreferences,
  storeEditorPreferences,
} from "../editor/editorPreferences";


function ExercisePage({ authSession, onLogout }) {
  const { exerciseId } = useParams();
  const [exercise, setExercise] = useState(null);
  const [sourceCode, setSourceCode] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [result, setResult] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
const [autocompleteEnabled, setAutocompleteEnabled] = useState(
  () => readEditorPreferences().autocompleteEnabled,
);

  useEffect(() => {
    if (!authSession) {
      return undefined;
    }

    let isActive = true;

    async function loadExercise() {
      try {
        setError("");
        setIsLoading(true);

        const response = await getExerciseById(exerciseId);

        if (isActive) {
          setExercise(response);
          setSourceCode(response.starterCode ?? "");
        }
      } catch (requestError) {
        if (isActive) {
          setError(requestError.message);
        }
      } finally {
        if (isActive) {
          setIsLoading(false);
        }
      }
    }

    loadExercise();

    return () => {
      isActive = false;
    };
  }, [authSession, exerciseId]);

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setResult(null);
    setIsSubmitting(true);

    try {
      const response = await submitExercise(
        exerciseId,
        sourceCode,
        authSession.token,
      );

      setResult(response);
    } catch (requestError) {
      if (requestError.status === 401) {
        onLogout();
        return;
      }

      setError(requestError.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  if (!authSession) {
    return <Navigate to="/login" replace />;
  }

  return (
    <main className="page-shell exercise-page">
      <header className="page-header">
        <Link to="/dashboard">← Dashboard</Link>
        <h1>{exercise?.title ?? "Exercise"}</h1>
      </header>

      {isLoading && <p>Loading exercise…</p>}

      {error && (
        <p className="form-error" role="alert">
          {error}
        </p>
      )}

      {exercise && (
        <>
          <section className="exercise-instructions">
            <p>{exercise.description}</p>
            <p>
              <strong>Difficulty:</strong> {exercise.difficulty}
            </p>
          </section>

          <div className="exercise-workspace">
            <form className="editor-section" onSubmit={handleSubmit}>
              <div className="flex flex-wrap items-baseline justify-between gap-2">
                <label id="source-code-label">Java source code</label>

                <div className="flex flex-wrap items-center justify-end gap-x-4 gap-y-2">
                  <label className="inline-flex cursor-pointer items-center gap-2 text-sm text-ink-500">
                    <input
                      type="checkbox"
                      checked={autocompleteEnabled}
                      onChange={(event) => {
  const nextAutocompleteEnabled = event.target.checked;

  setAutocompleteEnabled(nextAutocompleteEnabled);
  storeEditorPreferences({
    autocompleteEnabled: nextAutocompleteEnabled,
  });
}}
                      className="h-4 w-4 accent-brand-500"
                    />
                    Code suggestions
                  </label>

                  <p
                    id="editor-keyboard-help"
                    className="m-0 text-sm text-ink-500">
                    Tab indents. Press Escape, then Tab to leave the editor.
                  </p>
                </div>
              </div>

              <JavaCodeEditor
                ariaDescribedBy="editor-keyboard-help"
                key={exercise.id}
                value={sourceCode}
                onChange={(nextSourceCode) => {
                  setSourceCode(nextSourceCode);
                  setResult(null);
                }}
                maxLength={20000}
                ariaLabelledBy="source-code-label"
                autocompleteEnabled={autocompleteEnabled}
              />

              <p className="character-count">
                {sourceCode.length} / 20000 characters
              </p>

              <div className="flex items-center justify-end gap-4">
                <div className="min-w-0 flex-1">
                  {result ? (
                    <section
                      className={`grid min-h-14 gap-1 border border-l-[0.375rem] px-3 py-2 sm:grid-cols-[auto_minmax(0,1fr)_auto] sm:items-center sm:gap-3 ${
                        result.correct ? "result-correct" : "result-incorrect"
                      }`}
                      aria-live="polite">
                      <h2 className="m-0 text-base">
                        {result.correct ? "Correct!" : "Not quite yet"}
                      </h2>

                      <p className="m-0 text-sm">
                        {result.correct
                          ? "Your attempt was accepted and your mastery has been updated."
                          : "Your attempt was not accepted. Review your code and try again."}
                      </p>

                      {result.correct && (
                        <Link
                          className="next-recommendation-link whitespace-nowrap text-sm"
                          to="/dashboard">
                          View next recommendation
                        </Link>
                      )}
                    </section>
                  ) : (
                    <VerdictLoader isVisible={isSubmitting} />
                  )}
                </div>

                <button
                  className="primary-button w-44 shrink-0"
                  type="submit"
                  disabled={isSubmitting || sourceCode.trim() === ""}>
                  {isSubmitting ? "Checking solution…" : "Submit solution"}
                </button>
              </div>
            </form>

            <ExerciseReferences references={exercise.references} />
          </div>
        </>
      )}
    </main>
  );
}

export default ExercisePage;