function calculateProgress(completed, total) {
  if (total === 0) {
    return 0;
  }

  return Math.min(100, Math.max(0, Math.round((completed / total) * 100)));
}

function DashboardPathProgress({ paths }) {
  return (
    <section
      aria-labelledby="dashboard-path-heading"
      className="rounded-2xl border border-border/80 bg-surface/75 p-6 shadow-[0_18px_45px_color-mix(in_srgb,var(--theme-shadow)_8%,transparent)] backdrop-blur-xl">
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-brand-600">
          Learning paths
        </p>

        <h2
          id="dashboard-path-heading"
          className="mt-2 text-2xl font-bold text-ink-950">
          Curriculum progress
        </h2>
      </div>

      {paths.length === 0 ? (
        <p className="mt-6 text-sm text-ink-500">
          No learning paths are currently available.
        </p>
      ) : (
        <div className="mt-6 grid gap-5">
          {paths.map((path) => {
            const progress = calculateProgress(
              path.completedExercises,
              path.totalExercises,
            );

            return (
              <article
                key={path.pathId}
                className="rounded-xl border border-border bg-surface-muted/70 p-5">
                <div className="flex flex-wrap items-end justify-between gap-3">
                  <div>
                    <h3 className="font-semibold text-ink-950">{path.name}</h3>

                    <p className="mt-1 text-sm text-ink-500">
                      {path.language} · {path.completedExercises} of{" "}
                      {path.totalExercises} exercises completed
                    </p>
                  </div>

                  <p className="font-mono font-bold text-brand-600">
                    {progress}%
                  </p>
                </div>

                <div
                  className="mt-4 h-2 overflow-hidden rounded-full bg-brand-100"
                  role="progressbar"
                  aria-label={`${path.name} completion`}
                  aria-valuemin="0"
                  aria-valuemax="100"
                  aria-valuenow={progress}>
                  <div
                    className="h-full rounded-full bg-brand-500"
                    style={{ width: `${progress}%` }}
                  />
                </div>

                <ul className="mt-5 grid gap-3">
                  {path.skills.map((skill) => (
                    <li
                      key={skill.skillId}
                      className="flex items-center justify-between gap-4 text-sm">
                      <div>
                        <p className="font-semibold text-ink-950">
                          {skill.name}
                        </p>

                        <p className="mt-1 text-ink-500">
                          {skill.completedExercises} of {skill.totalExercises}{" "}
                          exercises
                        </p>
                      </div>

                      <p className="font-mono text-ink-700">
                        {Number(skill.masteryScore)}%
                      </p>
                    </li>
                  ))}
                </ul>
              </article>
            );
          })}
        </div>
      )}
    </section>
  );
}

export default DashboardPathProgress;
