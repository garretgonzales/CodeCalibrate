import { useState } from "react";

import { login } from "../api/auth";
import "../style/LoginPage.css";
import "../style/AppLayout.css";
import { Link, useLocation, useNavigate } from "react-router-dom";

function LoginPage({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setIsSubmitting(true);

    try {
      const authSession = await login({ email, password });
      onLogin(authSession);
      setPassword("");
      navigate("/dashboard");
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <main className="page-shell">
      <header className="page-header">
        <h1>Code Calibrate</h1>
        <p>Practice what you need. Build toward mastery.</p>
      </header>

      <section className="login-card" aria-labelledby="login-heading">
        <h2 id="login-heading">Log in</h2>
        {location.state?.message && (
          <p className="form-success" role="status">
            {location.state.message}
          </p>
        )}
        <form onSubmit={handleSubmit}>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            autoComplete="email"
            required
          />

          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            autoComplete="current-password"
            required
          />

          {error && (
            <p className="form-error" role="alert">
              {error}
            </p>
          )}

          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Logging in…" : "Log in"}
          </button>
        </form>
        <p>
          New to Code Calibrate? <Link to="/register">Create an account</Link>
        </p>
      </section>
    </main>
  );
}

export default LoginPage;
