import { useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import DashboardPage from "./pages/DashboardPage";
import LoginPage from "./pages/LoginPage";

function App() {
  const [authSession, setAuthSession] = useState(null);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage onLogin={setAuthSession} />} />
        <Route
          path="/dashboard"
          element={<DashboardPage authSession={authSession} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
