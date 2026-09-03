function VerdictLoader() {
  return (
    <div
      className="border border-inverse-border bg-inverse-surface p-4 text-inverse-foreground shadow-[0_12px_30px_color-mix(in_srgb,var(--theme-shadow)_18%,transparent)]"
      role="status"
      aria-live="polite"
      aria-atomic="true">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <p className="m-0 font-mono text-sm font-bold tracking-wide">
          Calibrating solution
        </p>

        <span className="font-mono text-xs text-inverse-muted">
          Judge0 evaluation
        </span>
      </div>

      <div
        className="mt-3 h-2 overflow-hidden bg-inverse-overlay"
        aria-hidden="true">
        <span className="verdict-loader-progress block h-full w-2/5 bg-primary shadow-[0_0_14px_var(--color-primary)]" />
      </div>

      <p className="mb-0 mt-3 text-sm text-inverse-muted">
        Running trusted tests and waiting for a verdict…
      </p>
    </div>
  );
}

export default VerdictLoader;
