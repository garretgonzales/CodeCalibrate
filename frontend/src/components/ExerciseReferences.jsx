function ExerciseReferences({ references = [] }) {
  if (references.length === 0) {
    return null;
  }

  return (
    <aside
      className="overflow-hidden rounded-xl border border-border bg-surface text-ink-950 shadow-[0_10px_30px_color-mix(in_srgb,var(--theme-shadow)_8%,transparent)] min-[70rem]:sticky min-[70rem]:top-6"
      aria-labelledby="exercise-references-heading">
      <details className="[&[open]>summary]:border-b [&[open]>summary]:border-border" open>
        <summary
          className="cursor-pointer px-6 py-5 font-bold"
          id="exercise-references-heading">
          Java references
        </summary>

        <ul className="m-0 grid list-none gap-4 px-6 pt-5 pb-6">
          {references.map((reference) => (
            <li
              className="grid gap-1.5 border-b border-border pb-4 last:border-b-0 last:pb-0"
              key={reference.url}>
              <a
                className="font-bold text-primary hover:text-primary-hover"
                href={reference.url}
                target="_blank"
                rel="noopener noreferrer">
                {reference.label}
              </a>

              <p className="m-0 leading-6 text-ink-500">
                {reference.description}
              </p>
            </li>
          ))}
        </ul>
      </details>
    </aside>
  );
}

export default ExerciseReferences;
