function DashboardStatCard({ label, value, description }) {
  return (
    <article className="border-border/80 bg-surface/75 rounded-2xl border p-5 shadow-[0_18px_45px_color-mix(in_srgb,var(--theme-shadow)_8%,transparent)] backdrop-blur-xl">
      <p className="text-ink-500 text-xs font-semibold uppercase tracking-[0.16em]">
        {label}
      </p>

      <p className="text-ink-950 mt-3 font-mono text-3xl font-bold tracking-tight">
        {value}
      </p>

      <p className="text-ink-500 mt-2 text-sm">{description}</p>
    </article>
  );
}

export default DashboardStatCard;
