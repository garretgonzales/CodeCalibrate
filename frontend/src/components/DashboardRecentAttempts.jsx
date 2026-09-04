const dateFormatter = new Intl.DateTimeFormat(undefined, {
  month: "short",
  day: "numeric",
  hour: "numeric",
  minute: "2-digit",
});

function formatAttemptDate(value) {
  if (!value) {
    return "Unknown time";
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return "Unknown time";
  }

  return dateFormatter.format(date);
}

function DashboardRecentAttempts({ attempts }) {
  return (
    <section
      aria-labelledby="dashboard-attempts-heading"
      className="rounded-2xl border border-border/80 bg-surface/75 p-6 shadow-[0_18px_45px_color-mix(in_srgb,var(--theme-shadow)_8%,transparent)] backdrop-blur-xl">
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-brand-600">
          Recent activity
        </p>

        <h2
          id="dashboard-attempts-heading"
          className="mt-2 text-2xl font-bold text-ink-950">
          Latest submissions
        </h2>
      </div>

      {attempts.length === 0 ? (
        <p className="mt-6 text-sm text-ink-500">
          Your recent exercise attempts will appear here.
        </p>
      ) : (
        <ol className="mt-6 divide-y divide-border">
          {attempts.map((attempt, index) => (
            <li
              key={`${attempt.exerciseId}-${attempt.attemptedAt}-${index}`}
              className="flex flex-wrap items-center justify-between gap-4 py-4 first:pt-0 last:pb-0">
              <div>
                <h3 className="font-semibold text-ink-950">
                  {attempt.exerciseTitle}
                </h3>

                <time
                  className="mt-1 block text-sm text-ink-500"
                  dateTime={attempt.attemptedAt}>
                  {formatAttemptDate(attempt.attemptedAt)}
                </time>
              </div>

              <span
                className={
                  attempt.correct
                    ? "rounded-full border border-[var(--theme-success-border)] bg-[var(--theme-success-surface)] px-3 py-1 text-xs font-semibold text-[var(--theme-success-foreground)]"
                    : "rounded-full border border-[var(--theme-warning-border)] bg-[var(--theme-warning-surface)] px-3 py-1 text-xs font-semibold text-[var(--theme-warning-foreground)]"
                }>
                {attempt.correct ? "Accepted" : "Try again"}
              </span>
            </li>
          ))}
        </ol>
      )}
    </section>
  );
}

export default DashboardRecentAttempts;
