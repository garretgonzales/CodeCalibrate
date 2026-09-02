function ExerciseReferences({ references = [] }) {
  if (references.length === 0) {
    return null;
  }

  return (
    <aside
      className="exercise-references"
      aria-labelledby="exercise-references-heading">
      <details open>
        <summary id="exercise-references-heading">Java references</summary>

        <ul className="exercise-reference-list">
          {references.map((reference) => (
            <li key={reference.url}>
              <a href={reference.url} target="_blank" rel="noopener noreferrer">
                {reference.label}
              </a>

              <p>{reference.description}</p>
            </li>
          ))}
        </ul>
      </details>
    </aside>
  );
}

export default ExerciseReferences;
