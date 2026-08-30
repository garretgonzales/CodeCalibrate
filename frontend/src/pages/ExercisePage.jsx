import { useEffect, useState } from "react";
import { Link, Navigate, useParams } from "react-router-dom";
import { getExerciseById, submitExercise } from "../api/exercises";
import "../style/AppLayout.css";
import "../style/ExercisePage.css";

function ExercisePage({ authSession }) {
  const { exerciseId } = useParams();
  const [exercise, setExercise] = useState(null);
  const [sourceCode, setSourceCode] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [result, setResult] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

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
      setError(requestError.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  if (!authSession) {
    return <Navigate to="/" replace />;
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

          <form className="editor-section" onSubmit={handleSubmit}>
            <label htmlFor="source-code">Java source code</label>

            <textarea
              id="source-code"
              className="code-editor"
              value={sourceCode}
              onChange={(event) => {
                setSourceCode(event.target.value);
                setResult(null);
              }}
              maxLength={20000}
              spellCheck="false"
            />

            <p className="character-count">
              {sourceCode.length} / 20000 characters
            </p>

            <button
              className="submit-exercise-button"
              type="submit"
              disabled={isSubmitting || sourceCode.trim() === ""}>
              {isSubmitting ? "Checking solution…" : "Submit solution"}
            </button>
          </form>
          {result && (
            <section
              className={`result-card ${
                result.correct ? "result-correct" : "result-incorrect"
              }`}
              aria-live="polite">
              <h2>{result.correct ? "Correct!" : "Not quite yet"}</h2>

              <p>
                {result.correct
                  ? "Your attempt was accepted and your mastery has been updated."
                  : "Your attempt was not accepted. Review your code and try again."}
              </p>

              {result.correct && (
                <Link className="next-recommendation-link" to="/dashboard">
                  View next recommendation
                </Link>
              )}
            </section>
          )}
        </>
      )}
    </main>
  );
}

export default ExercisePage;
