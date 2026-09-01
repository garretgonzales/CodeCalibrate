import { Link } from "react-router-dom";

const learningSteps = [
  {
    number: "01",
    title: "Practice in the browser",
    description:
      "Open a focused Java exercise and work from starter code in the embedded editor.",
  },
  {
    number: "02",
    title: "Submit to a trusted runner",
    description:
      "Spring Boot coordinates the authenticated request while Judge0 evaluates the Java code in isolation.",
  },
  {
    number: "03",
    title: "Continue with direction",
    description:
      "A safe verdict updates mastery and helps select the next exercise that deserves your attention.",
  },
];

const javaSkills = [
  {
    label: "Variables",
    description: "Build confidence with values, types, and program output.",
  },
  {
    label: "Control Flow",
    description: "Practice decisions and repeated behavior in readable Java.",
  },
  {
    label: "Methods",
    description: "Break behavior into reusable, testable units.",
  },
];

function LandingPage() {
  return (
    <main>
      <section className="landing-grid relative overflow-hidden border-b border-brand-100 bg-canvas">
        <div className="mx-auto grid w-full max-w-7xl gap-14 px-6 py-18 md:py-24 lg:grid-cols-[minmax(0,1.02fr)_minmax(30rem,0.98fr)] lg:items-center lg:px-8 lg:py-28">
          <div className="landing-reveal max-w-2xl">
            <p className="font-mono text-xs font-bold tracking-[0.22em] text-brand-600">
              JAVA PRACTICE, CALIBRATED
            </p>
            <h1 className="mt-5 max-w-3xl text-4xl font-bold tracking-[-0.04em] text-ink-950 sm:text-5xl lg:text-6xl lg:leading-[1.06]">
              Practice should respond to what you understand.
            </h1>
            <p className="mt-6 max-w-xl text-xl font-semibold leading-8 text-ink-700">
              Practice what you need. Build toward mastery.
            </p>
            <p className="mt-4 max-w-xl text-base leading-7 text-ink-500 sm:text-lg">
              Code Calibrate turns each trusted Java verdict into a clear next
              step, so you can spend less time searching and more time learning.
            </p>

            <div className="mt-9 flex flex-col items-start gap-4 sm:flex-row sm:items-center">
              <Link className="primary-button m-0 min-w-40" to="/register">
                Start learning
              </Link>
              <Link
                className="rounded-sm font-semibold text-ink-700 underline decoration-brand-300 decoration-2 underline-offset-4 transition hover:text-brand-700 focus-visible:outline-3 focus-visible:outline-offset-4 focus-visible:outline-accent-400"
                to="/login">
                I already have an account
              </Link>
            </div>
          </div>

          <aside
            className="landing-reveal calibration-panel relative border border-white/12 bg-ink-950 p-5 text-white shadow-[0_24px_70px_rgb(8_6_13_/_22%)] sm:p-7"
            aria-label="Code Calibrate learning loop">
            <div className="flex items-center justify-between border-b border-white/12 pb-4">
              <div>
                <p className="font-mono text-[0.68rem] font-bold tracking-[0.2em] text-accent-400">
                  CALIBRATION LOOP
                </p>
                <p className="mt-1 text-sm text-white/55">Java / Variables</p>
              </div>
              <span className="flex items-center gap-2 font-mono text-xs text-white/65">
                <span className="h-2 w-2 bg-accent-400 shadow-[0_0_12px_var(--color-accent-400)]" />
                READY
              </span>
            </div>

            <div className="mt-5 border-l-2 border-brand-500 bg-white/5 p-4 font-mono text-sm leading-7 text-white/88">
              <p>
                <span className="text-brand-300">int</span> age = 25;
              </p>
              <p>
                System.out.println(<span className="text-accent-400">age</span>);
              </p>
            </div>

            <ol className="mt-6 grid gap-0" aria-label="Calibration sequence">
              <li className="calibration-row">
                <span>01</span>
                <strong>Java code</strong>
                <small>submitted</small>
              </li>
              <li className="calibration-row">
                <span>02</span>
                <strong>Trusted verdict</strong>
                <small className="text-emerald-300!">accepted</small>
              </li>
              <li className="calibration-row">
                <span>03</span>
                <strong>Mastery update</strong>
                <small>recorded safely</small>
              </li>
              <li className="calibration-row">
                <span>04</span>
                <strong>Next exercise</strong>
                <small>selected</small>
              </li>
            </ol>

            <div className="mt-6">
              <div className="flex items-center justify-between font-mono text-[0.68rem] tracking-[0.14em] text-white/55">
                <span>SKILL SIGNAL</span>
                <span>CALIBRATING</span>
              </div>
              <div className="mt-3 grid grid-cols-8 gap-1" aria-hidden="true">
                {[0, 1, 2, 3, 4, 5, 6, 7].map((segment) => (
                  <span
                    className={`h-1.5 ${
                      segment < 5 ? "bg-brand-500" : "bg-white/14"
                    }`}
                    key={segment}
                  />
                ))}
              </div>
            </div>
          </aside>
        </div>
      </section>

      <section
        className="border-b border-brand-100 bg-white"
        id="how-it-works"
        aria-labelledby="how-it-works-heading">
        <div className="mx-auto w-full max-w-7xl px-6 py-18 lg:px-8 lg:py-24">
          <div className="max-w-2xl">
            <p className="font-mono text-xs font-bold tracking-[0.2em] text-brand-600">
              ONE TRUSTED LOOP
            </p>
            <h2
              className="mt-3 text-3xl font-bold tracking-tight text-ink-950 sm:text-4xl"
              id="how-it-works-heading">
              From practice to a useful next step
            </h2>
            <p className="mt-4 leading-7 text-ink-500">
              Your source code is evaluated for the exercise, not stored as
              learning history. Only safe attempt metadata and mastery updates
              are persisted.
            </p>
          </div>

          <ol className="mt-12 grid border-y border-brand-100 md:grid-cols-3">
            {learningSteps.map((step) => (
              <li
                className="relative border-brand-100 px-0 py-7 md:border-r md:px-7 md:last:border-r-0"
                key={step.number}>
                <span className="font-mono text-sm font-bold text-brand-500">
                  {step.number}
                </span>
                <h3 className="mt-5 text-xl text-ink-950">{step.title}</h3>
                <p className="mt-3 max-w-sm leading-7 text-ink-500">
                  {step.description}
                </p>
              </li>
            ))}
          </ol>
        </div>
      </section>

      <section
        className="bg-brand-50/55"
        id="java-path"
        aria-labelledby="java-path-heading">
        <div className="mx-auto grid w-full max-w-7xl gap-12 px-6 py-18 lg:grid-cols-[0.8fr_1.2fr] lg:items-start lg:px-8 lg:py-24">
          <div className="max-w-xl lg:sticky lg:top-24">
            <p className="font-mono text-xs font-bold tracking-[0.2em] text-brand-600">
              JAVA PATH
            </p>
            <h2
              className="mt-3 text-3xl font-bold tracking-tight text-ink-950 sm:text-4xl"
              id="java-path-heading">
              A progression with a reason
            </h2>
            <p className="mt-5 leading-7 text-ink-700">
              Code Calibrate uses your mastery and attempt history to choose an
              exercise that supports your current learning needs—not simply the
              next item in a fixed catalog.
            </p>
          </div>

          <ol className="border-l border-brand-300">
            {javaSkills.map((skill, index) => (
              <li
                className="relative border-b border-brand-100 py-7 pl-9 first:pt-0 last:border-b-0 last:pb-0"
                key={skill.label}>
                <span
                  className={`absolute -left-3 grid h-6 w-6 place-items-center border border-brand-500 bg-brand-50 font-mono text-[0.65rem] font-bold text-brand-700 ${
                    index === 0 ? "top-0" : "top-7"
                  }`}>
                  {index + 1}
                </span>
                <p className="font-mono text-[0.68rem] font-bold tracking-[0.16em] text-ink-500">
                  SKILL {String(index + 1).padStart(2, "0")}
                </p>
                <h3 className="mt-2 text-2xl text-ink-950">{skill.label}</h3>
                <p className="mt-2 leading-7 text-ink-500">
                  {skill.description}
                </p>
              </li>
            ))}
          </ol>
        </div>
      </section>

      <section className="border-t border-brand-100 bg-ink-950 text-white">
        <div className="mx-auto flex w-full max-w-7xl flex-col items-start gap-7 px-6 py-14 sm:flex-row sm:items-center sm:justify-between lg:px-8">
          <div className="max-w-2xl">
            <p className="font-mono text-xs font-bold tracking-[0.2em] text-accent-400">
              READY FOR THE NEXT SIGNAL?
            </p>
            <h2 className="mt-3 text-3xl font-bold tracking-tight">
              Write Java. Get a trusted verdict. Keep moving.
            </h2>
          </div>
          <Link className="primary-button m-0 shrink-0" to="/register">
            Create account
          </Link>
        </div>
      </section>
    </main>
  );
}

export default LandingPage;
