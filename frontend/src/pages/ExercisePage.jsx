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
              <div className="editor-toolbar">
                <label className="editor-label" id="source-code-label">
                  Java source code
                </label>

                <div className="editor-options">
                  <label className="editor-suggestions-control">
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
                    className="editor-keyboard-help">
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

              <div className="exercise-submit-row">
                <div className="exercise-verdict-region">
                  {result ? (
                    <section
                      className={`result-card ${
                        result.correct ? "result-correct" : "result-incorrect"
                      }`}
                      aria-live="polite">
                      <h2 className="m-0 text-base">
                        {result.correct ? "Solution accepted" : "Try again"}
                      </h2>

                      <p className="m-0 text-sm">
                        {result.correct
                          ? "Your progress has been updated."
                          : "Review your code, make a change, and submit it again."}
                      </p>

                      {result.correct && (
                        <Link
                          className="next-recommendation-link whitespace-nowrap text-sm"
                          to="/dashboard">
                          Back to dashboard
                        </Link>
                      )}
                    </section>
                  ) : (
                    <VerdictLoader isVisible={isSubmitting} />
                  )}
                </div>

                <button
                  className="primary-button exercise-submit-button"
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
