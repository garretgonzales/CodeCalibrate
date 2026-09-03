function VerdictLoader({ isVisible }) {
  return (
    <div
      className="min-h-14 min-w-0"
      role="status"
      aria-live="polite"
      aria-atomic="true">
      <span className="sr-only">
        {isVisible
          ? "Calibrating solution. Waiting for the Judge0 verdict."
          : ""}
      </span>

      <div
        className={`px-1 py-2 transition-[opacity,transform] duration-300 ease-out motion-reduce:transition-none ${
          isVisible ? "translate-y-0 opacity-100" : "translate-y-1 opacity-0"
        }`}
        aria-hidden="true">
        <div className="flex items-center justify-between gap-3 font-mono text-xs">
          <span className="font-bold text-ink-700">Calibrating solution</span>

          <span className="text-ink-500">Judge0 evaluation</span>
        </div>

        <div className="mt-2 h-2 overflow-hidden bg-inverse-overlay">
          <span
            className={`block h-full w-2/5 bg-primary shadow-[0_0_14px_var(--color-primary)] ${
              isVisible ? "verdict-loader-progress" : ""
            }`}
          />
        </div>
      </div>
    </div>
  );
}

export default VerdictLoader;
