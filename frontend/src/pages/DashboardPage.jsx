import { useEffect, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { getRecommendedExercise } from "../api/exercises";
import "../style/AppLayout.css";
import "../style/DashboardPage.css";
import LogoutButton from "../components/LogoutButton";

function DashboardPage({ authSession, onLogout }) {
  const [exercise, setExercise] = useState(null);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

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
        if (isActive && requestError.status === 401) {
          onLogout();
          return;
        }

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
  }, [authSession, onLogout]);

  if (!authSession) {
    return <Navigate to="/" replace />;
  }

  return (
    <main className="mx-auto min-h-screen w-full max-w-3xl px-6 py-12 md:py-16">
      <header className="mb-12 border-b border-brand-100 pb-6">
        <h1 className="text-3xl font-bold tracking-tight text-ink-950 md:text-4xl">
          Code Calibrate
        </h1>
        <p className="mt-2 text-base text-ink-500">
          Practice what you need. Build toward mastery.
        </p>
        <LogoutButton onLogout={onLogout} />
      </header>

      <section className="mb-4 rounded-2xl border border-brand-100 bg-linear-to-br from-white to-brand-50 p-6 shadow-xs">
        <h2 className="text-2xl font-bold text-ink-950">
          Welcome, {authSession.username}
        </h2>
        <p className="mt-2 text-ink-500">
          Here is the next exercise selected from your current mastery.
        </p>
      </section>

      {isLoading && <p>Loading your recommended exercise…</p>}

      {error && (
        <p className="form-error" role="alert">
          {error}
        </p>
      )}

      {exercise && (
        <section className="relative grid gap-4 overflow-hidden rounded-2xl border border-brand-100 bg-white p-6 pl-7 shadow-sm before:absolute before:inset-y-0 before:left-0 before:w-1 before:bg-brand-500">
          <h2 className="text-sm font-semibold uppercase tracking-[0.16em] text-brand-600">
            Recommended next exercise
          </h2>

          <h3 className="text-2xl font-bold text-ink-950">{exercise.title}</h3>

          <p className="leading-7 text-ink-700">{exercise.description}</p>

          <p className="text-sm text-ink-700">
            <strong>Difficulty:</strong> {exercise.difficulty}
          </p>

          <p className="text-sm text-ink-500">
            <strong>Skills:</strong>{" "}
            {exercise.skills.map((skill) => skill.name).join(", ")}
          </p>
          <button
            className="primary-button justify-self-start"
            type="button"
            onClick={() => navigate(`/exercises/${exercise.id}`)}>
            Start exercise
          </button>
        </section>
      )}
    </main>
  );
}

export default DashboardPage;
