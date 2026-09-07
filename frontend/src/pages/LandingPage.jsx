import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import javaLogo from "../assets/technology/java.svg";
import codeMirrorLogo from "../assets/technology/code-mirror.svg";
import springLogo from "../assets/technology/spring.svg";
import judge0Logo from "../assets/technology/judge0.svg";
import mysqlLogo from "../assets/technology/mysql.svg";

const learningSteps = [
  {
    number: "01",
    title: "Write your solution",
    description: "Work from starter code in the built-in Java editor.",
    result: "Write and refine your answer in the browser.",
    accent: "#e76f00",
    technologies: [
      { name: "Java", image: javaLogo },
      { name: "CodeMirror", image: codeMirrorLogo },
    ],
  },
  {
    number: "02",
    title: "Run the checks",
    description: "Spring Boot sends your Java solution to Judge0 for testing.",
    result: "See whether the solution passed and what to try next.",
    accent: "#6cb52d",
    technologies: [
      { name: "Spring Boot", image: springLogo },
      { name: "Judge0", image: judge0Logo },
    ],
  },
  {
    number: "03",
    title: "Keep moving",
    description: "Your result updates your progress and shapes the next exercise.",
    result: "Practice continues where it can help most.",
    accent: "#00546b",
    technologies: [{ name: "MySQL", image: mysqlLogo }],
  },
];

const javaSkills = [
  {
    label: "Variables",
    description: "Build confidence with values, types, and program output.",
    state: "Practiced",
    className: "adaptive-skill-foundation",
  },
  {
    label: "Control Flow",
    description: "Practice decisions and repeated behavior in readable Java.",
    state: "Practice next",
    className: "adaptive-skill-recommended",
  },
  {
    label: "Methods",
    description: "Break behavior into reusable, testable units.",
    state: "Later",
    className: "adaptive-skill-queued",
  },
];

function LandingPage({ authSession }) {
  const isAuthenticated = Boolean(authSession?.token);
  const learningDestination = isAuthenticated ? "/dashboard" : "/register";
  const javaPathRef = useRef(null);
  const [hasEnteredJavaPath, setHasEnteredJavaPath] = useState(false);

  useEffect(() => {
    const section = javaPathRef.current;

    if (!section || hasEnteredJavaPath) {
      return undefined;
    }

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setHasEnteredJavaPath(true);
          observer.disconnect();
        }
      },
      { rootMargin: "0px 0px -12%", threshold: 0.3 },
    );

    observer.observe(section);

    return () => observer.disconnect();
  }, [hasEnteredJavaPath]);

  return (
    <main>
      <section className="landing-hero relative isolate overflow-hidden bg-canvas">
        <div className="relative z-10 mx-auto grid w-full max-w-7xl gap-14 px-6 py-20 md:py-28 lg:grid-cols-[minmax(0,1.02fr)_minmax(30rem,0.98fr)] lg:items-center lg:px-8 lg:py-32">
          <div className="landing-reveal max-w-2xl">
            <p className="text-xs font-bold uppercase tracking-[0.16em] text-brand-600">
              Adaptive Java practice
            </p>
            <h1 className="mt-5 max-w-3xl text-4xl font-bold tracking-[-0.04em] text-ink-950 sm:text-5xl lg:text-6xl lg:leading-[1.06]">
              Java practice that meets you where you are.
            </h1>
            <p className="mt-6 max-w-xl text-xl font-semibold leading-8 text-ink-700">
              Build confidence one focused exercise at a time.
            </p>
            <p className="mt-4 max-w-xl text-base leading-7 text-ink-500 sm:text-lg">
              Submit a solution, see the result, and get another exercise based
              on what you need to practice.
            </p>

            <div className="mt-9 flex flex-col items-start gap-4 sm:flex-row sm:items-center">
              <Link
                className="primary-button m-0 min-w-40"
                to={learningDestination}>
                {isAuthenticated ? "Continue learning" : "Start learning"}
              </Link>

              {!isAuthenticated && (
                <Link
                  className="rounded-lg font-semibold text-ink-700 underline decoration-brand-300 decoration-2 underline-offset-4 transition hover:text-brand-700 focus-visible:outline-3 focus-visible:outline-offset-4 focus-visible:outline-accent-400"
                  to="/login">
                  I already have an account
                </Link>
              )}
            </div>
          </div>

          <aside
            className="landing-reveal calibration-panel relative rounded-3xl border border-inverse-border bg-inverse-surface p-5 text-inverse-foreground shadow-[0_24px_70px_color-mix(in_srgb,var(--theme-shadow)_22%,transparent)] sm:p-7"
            aria-label="Code Calibrate learning loop">
            <div className="flex items-center justify-between border-b border-inverse-border pb-4">
              <div>
                <p className="text-[0.68rem] font-bold uppercase tracking-[0.16em] text-accent-400">
                  Your practice loop
                </p>
                <p className="mt-1 text-sm text-inverse-muted">
                  Java / Variables
                </p>
              </div>
              <span className="flex items-center gap-2 font-mono text-xs text-inverse-muted">
                <span className="h-2 w-2 bg-accent-400 shadow-[0_0_12px_var(--color-accent-400)]" />
                READY
              </span>
            </div>

            <div className="flow-source mt-5 border-l-2 border-brand-500 bg-inverse-overlay p-4 text-inverse-foreground">
              <div className="flex items-center justify-between gap-4 font-mono text-[0.65rem] tracking-[0.12em] text-inverse-muted">
                <span>SOURCE / React + CodeMirror</span>
                <span className="flow-source-state">ready</span>
              </div>
              <div className="mt-3 font-mono text-sm leading-7">
                <p>
                  <span className="text-brand-300">int</span> age = 25;
                </p>
                <p>
                  System.out.println(<span className="text-accent-400">age</span>
                  );
                </p>
              </div>
            </div>

            <div className="flow-sequence mt-5">
              <span className="flow-signal" aria-hidden="true" />
              <ol className="grid gap-0" aria-label="Practice sequence">
                <li className="calibration-row flow-row-api">
                  <span className="flow-step">01</span>
                  <span className="flow-service">
                  <strong>Send solution</strong>
                    <small>Spring Boot + JWT</small>
                  </span>
                  <small className="flow-state" aria-label="processed">
                    <span className="flow-state-idle" aria-hidden="true">
                      idle
                    </span>
                    <span className="flow-state-active" aria-hidden="true">
                      processing
                    </span>
                    <span className="flow-state-final" aria-hidden="true">
                      received
                    </span>
                  </small>
                </li>
                <li className="calibration-row flow-row-runner">
                  <span className="flow-step">02</span>
                  <span className="flow-service">
                    <strong>Run checks</strong>
                    <small>Judge0</small>
                  </span>
                  <small className="flow-state" aria-label="verified">
                    <span className="flow-state-idle" aria-hidden="true">
                      idle
                    </span>
                    <span className="flow-state-active" aria-hidden="true">
                      evaluating
                    </span>
                    <span className="flow-state-final" aria-hidden="true">
                      checked
                    </span>
                  </small>
                </li>
                <li className="calibration-row flow-row-storage">
                  <span className="flow-step">03</span>
                  <span className="flow-service">
                    <strong>Save progress</strong>
                    <small>MySQL</small>
                  </span>
                  <small className="flow-state" aria-label="persisted">
                    <span className="flow-state-idle" aria-hidden="true">
                      idle
                    </span>
                    <span className="flow-state-active" aria-hidden="true">
                      writing
                    </span>
                    <span className="flow-state-final" aria-hidden="true">
                      saved
                    </span>
                  </small>
                </li>
                <li className="calibration-row flow-row-recommendation">
                  <span className="flow-step">04</span>
                  <span className="flow-service">
                    <strong>Choose next</strong>
                    <small>recommendation service</small>
                  </span>
                  <small className="flow-state" aria-label="recommended">
                    <span className="flow-state-idle" aria-hidden="true">
                      idle
                    </span>
                    <span className="flow-state-active" aria-hidden="true">
                      selecting
                    </span>
                    <span className="flow-state-final" aria-hidden="true">
                      ready
                    </span>
                  </small>
                </li>
              </ol>
            </div>

            <div className="mt-6">
              <div className="flex items-center justify-between font-mono text-[0.68rem] tracking-[0.14em] text-inverse-muted">
                <span>PROGRESS</span>
                <span>NEXT EXERCISE READY</span>
              </div>
              <div className="mt-3 grid grid-cols-8 gap-1" aria-hidden="true">
                {[0, 1, 2, 3, 4, 5, 6, 7].map((segment) => (
                  <span
                    className="calibration-signal-segment h-1.5"
                    key={segment}
                  />
                ))}
              </div>
              <p className="mt-3 font-mono text-[0.62rem] leading-5 text-inverse-muted">
                Your source is checked for this exercise. Only results and
                progress are saved.
              </p>
            </div>
          </aside>
        </div>
      </section>

      <section
        className="bg-surface"
        id="how-it-works"
        aria-labelledby="how-it-works-heading">
        <div className="mx-auto w-full max-w-7xl px-6 py-20 md:py-28 lg:px-8 lg:py-32">
          <div className="max-w-2xl">
            <p className="text-xs font-bold uppercase tracking-[0.16em] text-brand-600">
              How it works
            </p>
            <h2
              className="mt-3 text-3xl font-bold tracking-tight text-ink-950 sm:text-4xl"
              id="how-it-works-heading">
              Practice, submit, keep moving.
            </h2>
            <p className="mt-4 leading-7 text-ink-500">
              Work through a focused Java exercise, submit your solution, and
              use the result to decide what to practice next.
            </p>
          </div>

          <ol className="how-it-works-cards mt-12 grid gap-5 md:grid-cols-3">
            {learningSteps.map((step) => (
              <li
                className="process-card group relative min-w-0 overflow-hidden rounded-2xl border border-border bg-surface p-6 shadow-sm transition-[translate,border-color,box-shadow] duration-300 ease-out hover:-translate-y-1 hover:shadow-[0_22px_55px_color-mix(in_srgb,var(--theme-shadow)_16%,transparent)] focus-visible:-translate-y-1 focus-visible:outline-3 focus-visible:outline-offset-4 focus-visible:outline-accent-400 motion-reduce:transform-none motion-reduce:transition-none"
                key={step.number}
                style={{ "--process-card-accent": step.accent }}
                tabIndex={0}>
                <span className="process-card-animation-layer" aria-hidden="true" />

                <div className="relative z-10 flex h-full flex-col">
                  <div className="flex items-start justify-between gap-4">
                    <span className="font-mono text-sm font-bold text-brand-500">
                      STEP {step.number}
                    </span>
                    <div
                      className="flex items-center gap-1.5"
                      role="group"
                      aria-label="Technologies used">
                      {step.technologies.map((technology) => (
                        <span
                          className="process-technology-mark grid h-10 w-10 place-items-center rounded-xl border border-border bg-canvas p-2 md:h-11 md:w-11 md:p-2.5"
                          key={technology.name}
                          title={technology.name}>
                          <img
                            alt={`${technology.name} logo`}
                            className={`h-full w-full object-contain ${technology.name === "CodeMirror" ? "codemirror-technology-logo" : ""}`}
                            src={technology.image}
                          />
                        </span>
                      ))}
                    </div>
                  </div>

                  <h3 className="mt-7 text-xl font-bold tracking-[-0.02em] text-ink-950 transition-colors duration-300 group-hover:text-primary group-focus-visible:text-primary">
                    {step.title}
                  </h3>
                  <p className="mt-3 leading-7 text-ink-500">{step.description}</p>

                  <p className="process-card-result mt-7 border-l-2 pl-4 text-sm leading-6 text-ink-700">
                    <span className="block font-semibold text-ink-950">Result</span>
                    <span className="mt-1 block">{step.result}</span>
                  </p>
                </div>
              </li>
            ))}
          </ol>
        </div>
      </section>

      <section
        className={`java-path-section bg-brand-50/55 ${
          hasEnteredJavaPath ? "java-path-active" : ""
        }`}
        id="java-path"
        ref={javaPathRef}
        aria-labelledby="java-path-heading">
        <div className="mx-auto grid w-full max-w-7xl gap-12 px-6 py-20 md:py-28 lg:grid-cols-[0.82fr_1.18fr] lg:items-center lg:gap-18 lg:px-8 lg:py-32">
          <div className="max-w-xl">
            <p className="text-xs font-bold uppercase tracking-[0.16em] text-brand-600">
              Example Java path
            </p>
            <h2
              className="mt-3 text-3xl font-bold tracking-tight text-ink-950 sm:text-4xl"
              id="java-path-heading">
              Your progress shapes what comes next.
            </h2>
            <p className="mt-5 leading-7 text-ink-700">
              Recent attempts help choose an exercise that gives you useful
              practice instead of simply sending you down a fixed list.
            </p>
          </div>

          <div className="adaptive-path-readout rounded-3xl border border-brand-200 bg-surface shadow-[0_20px_55px_color-mix(in_srgb,var(--theme-shadow)_12%,transparent)]">
            <div className="flex flex-col gap-3 border-b border-brand-100 px-5 py-5 sm:flex-row sm:items-end sm:justify-between sm:px-7">
              <div>
                <p className="text-[0.68rem] font-bold uppercase tracking-[0.14em] text-brand-600">
                  Example progress
                </p>
                <h3 className="mt-2 text-xl font-bold tracking-[-0.02em] text-ink-950">
                  Where should practice focus next?
                </h3>
              </div>
              <span className="adaptive-path-status font-mono text-[0.65rem] font-bold uppercase tracking-[0.14em] text-ink-500">
                Example ready
              </span>
            </div>

            <div className="relative px-4 py-4 sm:px-6 sm:py-5">
              <span className="adaptive-signal-rail" aria-hidden="true">
                <span className="adaptive-signal-marker" />
              </span>

              <ol className="relative grid gap-2" aria-label="Example skill signals">
                {javaSkills.map((skill) => (
                  <li
                    className={`adaptive-skill-row ${skill.className}`}
                    key={skill.label}>
                    <span className="adaptive-skill-indicator" aria-hidden="true" />
                    <div className="min-w-0">
                      <h4 className="text-base font-bold text-ink-950 sm:text-lg">
                        {skill.label}
                      </h4>
                      <p className="mt-1 text-sm leading-6 text-ink-500">
                        {skill.description}
                      </p>
                    </div>
                    <span className="adaptive-skill-state font-mono text-[0.62rem] font-bold uppercase leading-5 tracking-[0.11em] text-ink-500">
                      {skill.state}
                    </span>
                  </li>
                ))}
              </ol>
            </div>

            <p className="adaptive-path-result border-t border-brand-100 px-5 py-4 text-sm leading-6 text-ink-700 sm:px-7">
              <strong className="text-ink-950">Control Flow is next</strong>{" "}
              because it needs more practice.
            </p>
          </div>
        </div>
      </section>

      <section className="bg-inverse-surface text-inverse-foreground">
        <div className="mx-auto flex w-full max-w-7xl flex-col items-start gap-7 px-6 py-20 sm:flex-row sm:items-center sm:justify-between md:py-24 lg:px-8">
          <div className="max-w-2xl">
            <p className="text-xs font-bold uppercase tracking-[0.16em] text-accent-400">
              Keep practicing
            </p>
            <h2 className="mt-3 text-3xl font-bold tracking-tight">
              Solve one exercise. Let your progress guide the next.
            </h2>
          </div>
          <Link
            className="primary-button m-0 shrink-0"
            to={learningDestination}>
            {isAuthenticated ? "Continue learning" : "Create account"}
          </Link>
        </div>
      </section>
    </main>
  );
}

export default LandingPage;
