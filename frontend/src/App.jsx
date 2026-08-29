import { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import DashboardPage from "./pages/DashboardPage";
import LoginPage from "./pages/LoginPage";

const AUTH_SESSION_STORAGE_KEY = "codeCalibrate.authSession";

function readStoredAuthSession() {
  try {
    const storedSession = window.sessionStorage.getItem(AUTH_SESSION_STORAGE_KEY);

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

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage onLogin={handleLogin} />} />
        <Route
          path="/dashboard"
          element={<DashboardPage authSession={authSession} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
