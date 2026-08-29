import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { getRecommendedExercise } from "../api/exercises";
import "../style/AppLayout.css";
import "../style/DashboardPage.css";

function DashboardPage({ authSession }) {
  const [exercise, setExercise] = useState(null);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (!authSession) {
      return undefined;
    }

    let isActive = true;

    async function loadRecommendation() {
      try {
        setError("");
        setIsLoading(true);

        const response = await getRecommendedExercise(authSession.token);

        if (isActive) {
          setExercise(response);
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

    loadRecommendation();

    return () => {
      isActive = false;
    };
  }, [authSession]);

  if (!authSession) {
    return <Navigate to="/" replace />;
  }

  return (
    <main className="page-shell">
      <header className="page-header">
        <h1>Code Calibrate</h1>
        <p>Practice what you need. Build toward mastery.</p>
      </header>

      <section className="status-card">
        <h2>Welcome, {authSession.username}</h2>
        <p>Here is the next exercise selected from your current mastery.</p>
      </section>

      {isLoading && <p>Loading your recommended exercise…</p>}

      {error && (
        <p className="form-error" role="alert">
          {error}
        </p>
      )}

      {exercise && (
        <section className="recommendation-card">
          <h2>Recommended next exercise</h2>
          <h3>{exercise.title}</h3>
          <p>{exercise.description}</p>
          <p>
            <strong>Difficulty:</strong> {exercise.difficulty}
          </p>
          <p className="skill-list">
            <strong>Skills:</strong>{" "}
            {exercise.skills.map((skill) => skill.name).join(", ")}
          </p>
        </section>
      )}
    </main>
  );
}

export default DashboardPage;
