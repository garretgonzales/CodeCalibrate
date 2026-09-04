import { useEffect, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { getDashboard } from "../api/dashboard";
import DashboardMastery from "../components/DashboardMastery";
import DashboardOverview from "../components/DashboardOverview";
import DashboardPathProgress from "../components/DashboardPathProgress";
import DashboardRecentAttempts from "../components/DashboardRecentAttempts";
import "../style/AppLayout.css";
import "../style/DashboardPage.css";

function DashboardPage({ authSession, onLogout }) {
  const [dashboard, setDashboard] = useState(null);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    if (!authSession) {
      return undefined;
    }

    let isActive = true;

    async function loadDashboard() {
      try {
        setError("");
        setIsLoading(true);

        const response = await getDashboard(authSession.token);

        if (isActive) {
          setDashboard(response);
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

    loadDashboard();

    return () => {
      isActive = false;
    };
  }, [authSession, onLogout]);

  if (!authSession) {
    return <Navigate to="/login" replace />;
  }

  const exercise = dashboard?.recommendedExercise;
  const username = dashboard?.user.username ?? authSession.username;

  return (
    <main className="mx-auto min-h-screen w-full max-w-7xl px-6 py-12 md:py-16">
      {isLoading && !dashboard && (
        <p role="status" aria-live="polite">
          Loading your dashboard…
        </p>
      )}

      {error && (
        <p className="form-error" role="alert">
          {error}
        </p>
      )}

      {dashboard && (
        <div className="landing-reveal">
          <section className="mb-4 rounded-2xl border border-brand-100 bg-linear-to-br from-surface to-brand-50 p-6 shadow-xs">
            <h1 className="text-2xl font-bold text-ink-950">
              Welcome, {username}
            </h1>
          </section>

          <div className="mb-8">
            <DashboardOverview overview={dashboard.overview} />
          </div>

          <div className="grid gap-6 lg:grid-cols-[minmax(0,1.3fr)_minmax(18rem,0.7fr)]">
            <DashboardMastery skills={dashboard.skillMastery} />

            {exercise && (
              <section className="relative grid content-start gap-4 overflow-hidden rounded-2xl border border-brand-100 bg-surface p-6 pl-7 shadow-sm before:absolute before:inset-y-0 before:left-0 before:w-1 before:bg-brand-500">
                <h2 className="text-sm font-semibold uppercase tracking-[0.16em] text-brand-600">
                  Recommended next exercise
                </h2>

                <h3 className="text-2xl font-bold text-ink-950">
                  {exercise.title}
                </h3>

                <p className="leading-7 text-ink-700">{exercise.description}</p>

                <p className="text-sm text-ink-700">
                  <strong>Difficulty:</strong> {exercise.difficulty}
                </p>

                <p className="text-sm text-ink-500">
                  <strong>Skills:</strong> {exercise.skills.join(", ")}
                </p>

                <button
                  className="primary-button justify-self-start"
                  type="button"
                  onClick={() => navigate(`/exercises/${exercise.id}`)}>
                  Start exercise
                </button>
              </section>
            )}
          </div>

          <div className="mt-6 grid gap-6 lg:grid-cols-[minmax(18rem,0.75fr)_minmax(0,1.25fr)]">
            <DashboardRecentAttempts attempts={dashboard.recentAttempts} />

            <DashboardPathProgress paths={dashboard.pathProgress} />
          </div>
        </div>
      )}
    </main>
  );
}

export default DashboardPage;
