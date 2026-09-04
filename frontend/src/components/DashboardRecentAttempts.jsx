import { Link } from "react-router-dom";

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
          Exercise history
        </p>

        <h2
          id="dashboard-attempts-heading"
          className="mt-2 text-2xl font-bold text-ink-950">
          Attempted exercises
        </h2>

        <p className="mt-2 text-sm text-ink-500">
          Your latest result for every exercise you have attempted.
        </p>
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
              className="py-1">
              <Link
                to={`/exercises/${attempt.exerciseId}`}
                className="group -mx-3 flex flex-wrap items-center justify-between gap-4 rounded-xl px-3 py-3 transition-colors hover:bg-surface-muted focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-500"
                aria-label={`Revisit ${attempt.exerciseTitle}`}>
                <div>
                  <h3 className="font-semibold text-ink-950 transition-colors group-hover:text-brand-600">
                    {attempt.exerciseTitle}
                  </h3>

                  <time
                    className="mt-1 block text-sm text-ink-500"
                    dateTime={attempt.attemptedAt}>
                    {formatAttemptDate(attempt.attemptedAt)}
                  </time>
                </div>

                <div className="flex items-center gap-3">
                  <span
                    className={
                      attempt.correct
                        ? "rounded-full border border-(--theme-success-border) bg-(--theme-success-surface) px-3 py-1 text-xs font-semibold text-(--theme-success-foreground)"
                        : "rounded-full border border-(--theme-warning-border) bg-(--theme-warning-surface) px-3 py-1 text-xs font-semibold text-(--theme-warning-foreground)"
                    }>
                    {attempt.correct ? "Accepted" : "Try again"}
                  </span>

                  <span className="text-sm font-semibold text-brand-600">
                    Revisit <span aria-hidden="true">→</span>
                  </span>
                </div>
              </Link>
            </li>
          ))}
        </ol>
      )}
    </section>
  );
}

export default DashboardRecentAttempts;
