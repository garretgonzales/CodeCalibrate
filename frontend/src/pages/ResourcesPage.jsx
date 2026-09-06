import { useState } from "react";

import ResourceCard from "../components/ResourceCard";
import { RESOURCE_TECHNOLOGIES, resources } from "../data/resources";

const ALL_RESOURCES_FILTER = "All";

function ResourcesPage() {
  const [selectedTechnology, setSelectedTechnology] =
    useState(ALL_RESOURCES_FILTER);

  const filteredResources =
    selectedTechnology === ALL_RESOURCES_FILTER
      ? resources
      : resources.filter((resource) =>
          resource.technologies.includes(selectedTechnology),
        );

  return (
    <main className="min-h-screen bg-canvas">
      <section className="landing-grid border-b border-border">
        <div className="mx-auto w-full max-w-7xl px-6 py-16 md:py-20 lg:px-8">
          <div className="landing-reveal max-w-3xl">
            <p className="font-mono text-xs font-bold uppercase tracking-[0.22em] text-primary">
              Project reference library
            </p>

            <h1 className="mt-5 text-4xl font-bold tracking-[-0.04em] text-ink-950 sm:text-5xl">
              Built with documented decisions.
            </h1>

            <p className="mt-6 max-w-2xl text-lg leading-8 text-ink-500">
              Explore the official documentation, technical guides, and design
              references used while building Code Calibrate.
            </p>
          </div>
        </div>
      </section>

      <section
        className="mx-auto w-full max-w-7xl px-6 py-12 lg:px-8"
        aria-labelledby="resource-filter-heading">
        <div className="flex flex-col gap-4">
          <div>
            <p className="font-mono text-xs font-bold uppercase tracking-[0.18em] text-primary">
              Filter the library
            </p>

            <h2
              className="mt-2 text-2xl font-bold text-ink-950"
              id="resource-filter-heading">
              Technologies and tools
            </h2>
          </div>

          <div
            className="flex flex-wrap gap-2"
            role="group"
            aria-label="Filter resources by technology">
            {[ALL_RESOURCES_FILTER, ...RESOURCE_TECHNOLOGIES].map(
              (technology) => {
                const isSelected = technology === selectedTechnology;

                return (
                  <button
                    className={
                      isSelected
                        ? "rounded-full border border-primary bg-primary px-4 py-2 text-sm font-semibold text-primary-contrast shadow-sm transition-colors focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400"
                        : "rounded-full border border-border bg-surface px-4 py-2 text-sm font-semibold text-ink-700 transition-colors hover:border-primary hover:text-primary focus-visible:outline-3 focus-visible:outline-offset-2 focus-visible:outline-accent-400"
                    }
                    type="button"
                    aria-pressed={isSelected}
                    key={technology}
                    onClick={() => setSelectedTechnology(technology)}>
                    {technology}
                  </button>
                );
              },
            )}
          </div>
        </div>

        <div className="mt-10 flex flex-wrap items-end justify-between gap-4 border-b border-border pb-4">
          <div>
            <p className="text-sm text-ink-500">Showing resources for</p>

            <p className="mt-1 text-lg font-bold text-ink-950">
              {selectedTechnology}
            </p>
          </div>

          <p
            className="font-mono text-sm text-ink-500"
            role="status"
            aria-live="polite"
            aria-atomic="true">
            {filteredResources.length}{" "}
            {filteredResources.length === 1 ? "resource" : "resources"}
          </p>
        </div>

        <ul className="mt-8 grid list-none gap-6 p-0 sm:grid-cols-2 xl:grid-cols-3">
          {filteredResources.map((resource) => (
            <ResourceCard resource={resource} key={resource.id} />
          ))}
        </ul>
      </section>
    </main>
  );
}

export default ResourcesPage;
