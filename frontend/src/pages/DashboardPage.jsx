import { Navigate } from "react-router-dom";

function DashboardPage({ authSession }) {
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
        <p>Recommended exercise will appear here next.</p>
      </section>
    </main>
  );
}

export default DashboardPage;
