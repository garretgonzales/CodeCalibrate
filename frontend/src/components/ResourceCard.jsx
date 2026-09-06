const TECHNOLOGY_COLORS = {
  Spring: "#6db33f",
  Testing: "#14b8a6",
  Judge0: "#f97316",
  Railway: "#8b5cf6",
  Docker: "#2496ed",
  MySQL: "#e48e00",
  "Tailwind CSS": "#38bdf8",
  React: "#61dafb",
  "React Router": "#f44250",
  Vite: "#a855f7",
  CodeMirror: "#d97706",
  JJWT: "#fb7185",
  "GitHub API": "#2f81f7",
  Caddy: "#22d3ee",
  Java: "#ed8b00",
  Design: "#ec4899",
};

function ResourceCard({ resource }) {
  const cardAccent =
    TECHNOLOGY_COLORS[resource.technologies[0]] ?? "var(--color-primary)";

  return (
    <li className="min-w-0">
      <a
        className="resource-card group relative grid h-full min-h-72 overflow-hidden rounded-2xl border border-border bg-surface p-6 text-ink-950 shadow-sm transition-[translate,border-color,box-shadow] duration-300 ease-out hover:-translate-y-1 hover:border-primary hover:shadow-[0_22px_55px_color-mix(in_srgb,var(--theme-shadow)_16%,transparent)] focus-visible:-translate-y-1 focus-visible:border-primary focus-visible:outline-3 focus-visible:outline-offset-4 focus-visible:outline-accent-400 motion-reduce:transform-none motion-reduce:transition-none"
        href={resource.url}
        target="_blank"
        rel="noopener noreferrer"
        aria-label={`Open ${resource.title} in a new tab`}
        style={{ "--resource-card-accent": cardAccent }}>
        <span
          className="resource-card-animation-layer"
          aria-hidden="true"
        />

        <div className="relative z-10 flex h-full flex-col">
          <p className="font-mono text-xs font-bold uppercase tracking-[0.16em] text-ink-500">
            {resource.type}
          </p>

          <h2 className="mt-5 text-2xl font-bold tracking-[-0.025em] text-ink-950 transition-colors duration-300 group-hover:text-primary group-focus-visible:text-primary">
            {resource.title}
          </h2>

          <p className="mt-4 leading-7 text-ink-500">
            {resource.description}
          </p>

          <ul
            className="mt-6 flex list-none flex-wrap gap-2 p-0"
            aria-label="Technologies">
            {resource.technologies.map((technology) => (
              <li key={technology}>
                <span
                  className="resource-technology-label inline-flex items-center border px-2.5 py-1 font-mono text-xs font-bold"
                  style={{
                    "--resource-technology-color":
                      TECHNOLOGY_COLORS[technology] ??
                      "var(--color-primary)",
                  }}>
                  {technology}
                </span>
              </li>
            ))}
          </ul>

          <span className="resource-card-action mt-auto pt-8 font-semibold text-primary">
            Open resource <span aria-hidden="true">↗</span>
          </span>
        </div>
      </a>
    </li>
  );
}

export default ResourceCard;