import { Link } from "react-router-dom";

import wordmark from "../assets/code-calibrate-wordmark.png";

function SiteHeader({ authSession, onLogout }) {
  return (
    <header className="border-b border-white/10 bg-ink-950 text-white">
      <div className="mx-auto flex min-h-18 w-full max-w-7xl items-center gap-4 px-4 py-3 sm:px-6 lg:px-8">
        <Link
          className="shrink-0 rounded-sm focus-visible:outline-3 focus-visible:outline-offset-4 focus-visible:outline-accent-400"
          to="/"
          aria-label="Code Calibrate home">
          <img
            className="h-auto w-28 sm:w-52"
            src={wordmark}
            alt="Code Calibrate"
          />
        </Link>

        <nav
          className="ml-auto flex items-center gap-2 sm:gap-4"
          aria-label="Primary navigation">
          {authSession ? (
            <>
              <Link
                className="inline-flex rounded-sm px-1 py-2 text-sm font-semibold text-white/80 transition hover:text-white focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 sm:px-2"
                to="/dashboard">
                Dashboard
              </Link>
              <button
                className="inline-flex min-h-10 items-center justify-center border border-white/25 bg-transparent px-3 py-2 text-sm font-semibold text-white transition hover:border-white/60 hover:bg-white/8 focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 sm:px-4"
                type="button"
                onClick={onLogout}>
                Log out
              </button>
            </>
          ) : (
            <>
              <a
                className="hidden rounded-sm px-2 py-2 text-sm font-semibold text-white/70 transition hover:text-white focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 lg:inline-flex"
                href="/#how-it-works">
                How it works
              </a>
              <a
                className="hidden rounded-sm px-2 py-2 text-sm font-semibold text-white/70 transition hover:text-white focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 lg:inline-flex"
                href="/#java-path">
                Java path
              </a>
              <Link
                className="hidden rounded-sm px-2 py-2 text-sm font-semibold text-white/80 transition hover:text-white focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 sm:inline-flex"
                to="/login">
                Log in
              </Link>
              <Link
                className="inline-flex min-h-10 items-center justify-center bg-brand-500 px-3 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-600 focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400 sm:px-4"
                to="/register">
                Get started
              </Link>
            </>
          )}
        </nav>

        <div className="hidden h-8 w-8 shrink-0 md:block" aria-hidden="true" />
      </div>
    </header>
  );
}

export default SiteHeader;
