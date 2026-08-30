import { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import DashboardPage from "./pages/DashboardPage";
import LoginPage from "./pages/LoginPage";
import ExercisePage from "./pages/ExercisePage";
import RegisterPage from "./pages/RegisterPage";

const AUTH_SESSION_STORAGE_KEY = "codeCalibrate.authSession";

function readStoredAuthSession() {
  try {
    const storedSession = window.sessionStorage.getItem(
      AUTH_SESSION_STORAGE_KEY,
    );

    if (!storedSession) {
      return null;
    }

    const authSession = JSON.parse(storedSession);

    if (!authSession?.token) {
      window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
      return null;
    }

    return authSession;
  } catch {
    window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    return null;
  }
}

function App() {
  const [authSession, setAuthSession] = useState(readStoredAuthSession);

  function handleLogin(authSession) {
    window.sessionStorage.setItem(
      AUTH_SESSION_STORAGE_KEY,
      JSON.stringify(authSession),
    );
    setAuthSession(authSession);
  }

  function handleLogout() {
    window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    setAuthSession(null);
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage onLogin={handleLogin} />} />
        <Route
          path="/dashboard"
          element={
            <DashboardPage authSession={authSession} onLogout={handleLogout} />
          }
        />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/exercises/:exerciseId"
          element={
            <ExercisePage authSession={authSession} onLogout={handleLogout} />
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
