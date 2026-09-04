import AnimatedNumber from "./NumberAnimation";

function DashboardStatCard({
  label,
  value,
  description,
  decimals = 0,
  suffix = "",
}) {
  return (
    <article className="rounded-2xl border border-border/80 bg-surface/75 p-5 shadow-[0_18px_45px_color-mix(in_srgb,var(--theme-shadow)_8%,transparent)] backdrop-blur-xl">
      <p className="text-xs font-semibold uppercase tracking-[0.16em] text-ink-500">
        {label}
      </p>

      <p className="mt-3 font-mono text-3xl font-bold tracking-tight text-ink-950">
        <AnimatedNumber value={value} decimals={decimals} suffix={suffix} />
      </p>

      <p className="mt-2 text-sm text-ink-500">{description}</p>
    </article>
  );
}

export default DashboardStatCard;
