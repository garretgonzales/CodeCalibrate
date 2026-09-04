import AnimatedNumber from "./NumberAnimation";

function DashboardMastery({ skills }) {
  return (
    <section
      aria-labelledby="dashboard-mastery-heading"
      className="rounded-2xl border border-border/80 bg-surface/75 p-6 shadow-[0_18px_45px_color-mix(in_srgb,var(--theme-shadow)_8%,transparent)] backdrop-blur-xl">
      <div>
        <p className="text-xs font-semibold uppercase tracking-[0.18em] text-brand-600">
          Skill mastery
        </p>

        <h2
          id="dashboard-mastery-heading"
          className="mt-2 text-2xl font-bold text-ink-950">
          Where you are improving
        </h2>
      </div>

      {skills.length === 0 ? (
        <p className="mt-6 text-sm text-ink-500">
          Complete an exercise to begin tracking skill mastery.
        </p>
      ) : (
        <div className="mt-6 grid gap-5">
          {skills.map((skill) => {
            const masteryScore = Math.min(
              100,
              Math.max(0, Number(skill.masteryScore)),
            );

            return (
              <article key={skill.skillId}>
                <div className="flex items-end justify-between gap-4">
                  <div>
                    <h3 className="font-semibold text-ink-950">{skill.name}</h3>

                    <p className="mt-1 text-sm text-ink-500">
                      <AnimatedNumber value={skill.questionsCorrect} /> correct
                      of <AnimatedNumber value={skill.questionsAttempted} />{" "}
                      attempted
                    </p>
                  </div>

                  <p className="font-mono text-lg font-bold text-brand-600">
                    <AnimatedNumber
                      value={masteryScore}
                      decimals={2}
                      suffix="%"
                    />
                  </p>
                </div>

                <div
                  className="mt-3 h-2 overflow-hidden rounded-full bg-brand-100"
                  role="progressbar"
                  aria-label={`${skill.name} mastery`}
                  aria-valuemin="0"
                  aria-valuemax="100"
                  aria-valuenow={masteryScore}>
                  <div
                    className="h-full rounded-full bg-brand-500 transition-[width] duration-500"
                    style={{ width: `${masteryScore}%` }}
                  />
                </div>
              </article>
            );
          })}
        </div>
      )}
    </section>
  );
}

export default DashboardMastery;
