import { useEffect, useState } from "react";
import { Link, Navigate, useParams } from "react-router-dom";
import { getExerciseById } from "../api/exercises";
import "../style/AppLayout.css";
import "../style/ExercisePage.css";

function ExercisePage({ authSession }) {
  const { exerciseId } = useParams();
  const [exercise, setExercise] = useState(null);
  const [sourceCode, setSourceCode] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);

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

          <section className="editor-section">
            <label htmlFor="source-code">Java source code</label>
            <textarea
              id="source-code"
              className="code-editor"
              value={sourceCode}
              onChange={(event) => setSourceCode(event.target.value)}
              maxLength={20000}
              spellCheck="false"
            />
            <p className="character-count">
              {sourceCode.length} / 20000 characters
            </p>
          </section>
        </>
      )}
    </main>
  );
}

export default ExercisePage;
