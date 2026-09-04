import DashboardStatCard from "./DashboardStatCard";

function DashboardOverview({ overview }) {
  const metrics = [
    {
      label: "Attempts",
      value: overview.totalAttempts,
      description: "Submitted solutions",
    },
    {
      label: "Correct",
      value: overview.correctAttempts,
      description: "Accepted verdicts",
    },
    {
      label: "Completed",
      value: overview.completedExercises,
      description: "Distinct exercises solved",
    },
    {
      label: "Accuracy",
      value: `${overview.accuracy}%`,
      description: "Correct across all attempts",
    },
    {
      label: "Mastery",
      value: `${overview.averageMastery}%`,
      description: "Across practiced skills",
    },
  ];

  return (
    <section
      aria-labelledby="dashboard-overview-heading"
      className="grid gap-4">
      <div>
        <p className="text-brand-600 text-xs font-semibold uppercase tracking-[0.18em]">
          Learning snapshot
        </p>
        <h2
          id="dashboard-overview-heading"
          className="text-ink-950 mt-2 text-2xl font-bold">
          Your progress at a glance
        </h2>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-5">
        {metrics.map((metric) => (
          <DashboardStatCard
            key={metric.label}
            label={metric.label}
            value={metric.value}
            description={metric.description}
          />
        ))}
      </div>
    </section>
  );
}

export default DashboardOverview;
