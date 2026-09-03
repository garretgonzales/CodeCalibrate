import { lazy, Suspense, useEffect, useState } from "react";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";

import { getCurrentUser } from "./api/auth";
import SiteHeader from "./components/SiteHeader";
import DashboardPage from "./pages/DashboardPage";
import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
const ExercisePage = lazy(() => import("./pages/ExercisePage"));

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
  const [isSessionChecking, setIsSessionChecking] = useState(
    () => authSession !== null,
  );

  useEffect(() => {
    const token = authSession?.token;

    if (!token) {
      return undefined;
    }

    let isActive = true;

    async function verifySession() {
      try {
        const currentUser = await getCurrentUser(token);

        if (isActive) {
          const verifiedSession = {
            ...currentUser,
            token,
          };

          window.sessionStorage.setItem(
            AUTH_SESSION_STORAGE_KEY,
            JSON.stringify(verifiedSession),
          );
          setAuthSession(verifiedSession);
        }
      } catch (requestError) {
        if (isActive && requestError.status === 401) {
          window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
          setAuthSession(null);
        }
      } finally {
        if (isActive) {
          setIsSessionChecking(false);
        }
      }
    }

    verifySession();

    return () => {
      isActive = false;
    };
  }, [authSession?.token]);

  function handleLogin(authSession) {
    window.sessionStorage.setItem(
      AUTH_SESSION_STORAGE_KEY,
      JSON.stringify(authSession),
    );
    setIsSessionChecking(true);
    setAuthSession(authSession);
  }

  function handleLogout() {
    window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    setIsSessionChecking(false);
    setAuthSession(null);
  }

  return (
    <BrowserRouter>
      <SiteHeader authSession={authSession} onLogout={handleLogout} />

      {isSessionChecking ? (
        <main className="mx-auto min-h-[calc(100vh-4.5rem)] w-full max-w-7xl px-6 py-12 lg:px-8">
          <p className="font-mono text-sm text-ink-500">Verifying session…</p>
        </main>
      ) : (
        <Routes>
          <Route path="/" element={<LandingPage authSession={authSession} />} />
          <Route
            path="/login"
            element={
              authSession ? (
                <Navigate to="/dashboard" replace />
              ) : (
                <LoginPage onLogin={handleLogin} />
              )
            }
          />

          <Route
            path="/register"
            element={
              authSession ? (
                <Navigate to="/dashboard" replace />
              ) : (
                <RegisterPage />
              )
            }
          />

          <Route
            path="/dashboard"
            element={
              <DashboardPage
                authSession={authSession}
                onLogout={handleLogout}
              />
            }
          />
          <Route
            path="/exercises/:exerciseId"
            element={
              <Suspense
                fallback={
                  <main className="mx-auto min-h-screen w-full max-w-5xl px-6 py-12 md:py-16">
                    <p className="text-ink-500">Loading code editor…</p>
                  </main>
                }>
                <ExercisePage
                  authSession={authSession}
                  onLogout={handleLogout}
                />
              </Suspense>
            }
          />
        </Routes>
      )}
    </BrowserRouter>
  );
}

export default App;
