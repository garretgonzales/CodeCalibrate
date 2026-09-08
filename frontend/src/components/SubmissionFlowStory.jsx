import { useEffect, useMemo, useRef, useState } from "react";
import codeMirrorLogo from "../assets/technology/code-mirror.svg";
import javaLogo from "../assets/technology/java.svg";
import judge0Logo from "../assets/technology/judge0.svg";
import mysqlLogo from "../assets/technology/mysql.svg";
import springLogo from "../assets/technology/spring.svg";

const submissionStages = [
  {
    number: "01",
    action: "Send the authenticated solution",
    shortAction: "Send solution",
    mobileLabel: "Editor → API",
    mobileDetail: "JWT-authenticated solution",
    nodeIds: ["editor", "api"],
  },
  {
    number: "02",
    action: "Load the pinned exercise content",
    shortAction: "Load trusted content",
    mobileLabel: "API → Exercise content",
    mobileDetail: "Pinned private GitHub revision",
    nodeIds: ["api", "content"],
  },
  {
    number: "03",
    action: "Run the trusted checks",
    shortAction: "Run trusted checks",
    mobileLabel: "API → Judge0",
    mobileDetail: "Learner source + trusted tests",
    nodeIds: ["api", "judge0"],
  },
  {
    number: "04",
    action: "Return the trusted verdict",
    shortAction: "Return verdict",
    mobileLabel: "Judge0 → API",
    mobileDetail: "Trusted verdict returned",
    nodeIds: ["judge0", "api"],
  },
  {
    number: "05",
    action: "Save safe progress data",
    shortAction: "Update progress",
    mobileLabel: "API → Progress",
    mobileDetail: "Safe attempt + mastery data in MySQL",
    nodeIds: ["api", "progress"],
  },
  {
    number: "06",
    action: "Return feedback and the next exercise",
    shortAction: "Choose what comes next",
    mobileLabel: "API → Editor",
    mobileDetail: "Feedback + next exercise",
    nodeIds: ["api", "next"],
  },
];

const submissionNodes = [
  {
    id: "editor",
    eyebrow: "Learner",
    label: "Editor",
    detail: "React + CodeMirror",
    image: codeMirrorLogo,
  },
  {
    id: "api",
    eyebrow: "Authority",
    label: "API",
    detail: "Spring Boot + JWT",
    image: springLogo,
  },
  {
    id: "content",
    eyebrow: "Pinned revision",
    label: "Exercise content",
    detail: "Private GitHub source",
    image: javaLogo,
  },
  {
    id: "judge0",
    eyebrow: "Isolated runner",
    label: "Judge0",
    detail: "Source + trusted tests",
    image: judge0Logo,
  },
  {
    id: "progress",
    eyebrow: "Safe persistence",
    label: "Progress",
    detail: "Private MySQL",
    image: mysqlLogo,
  },
  {
    id: "next",
    eyebrow: "Learner outcome",
    label: "Next exercise",
    detail: "Feedback + recommendation",
    image: javaLogo,
  },
];

const STAGE_DURATION_MS = 900;
const FOCUS_RESET_DELAY_MS = 240;

function SubmissionFlowStory() {
  const sectionRef = useRef(null);
  const sequenceTimerRef = useRef(null);
  const resetTimerRef = useRef(null);
  const isFocusedRef = useRef(false);
  const [activeStageIndex, setActiveStageIndex] = useState(-1);
  const [animationComplete, setAnimationComplete] = useState(false);
  const [reduceMotion, setReduceMotion] = useState(() =>
    typeof window === "undefined"
      ? false
      : window.matchMedia("(prefers-reduced-motion: reduce)").matches,
  );

  useEffect(() => {
    const motionQuery = window.matchMedia("(prefers-reduced-motion: reduce)");
    const handleMotionChange = (event) => setReduceMotion(event.matches);

    motionQuery.addEventListener("change", handleMotionChange);

    return () => motionQuery.removeEventListener("change", handleMotionChange);
  }, []);

  useEffect(() => {
    const section = sectionRef.current;

    if (!section) {
      return undefined;
    }

    if (reduceMotion) {
      const frameId = window.requestAnimationFrame(() => {
        setActiveStageIndex(submissionStages.length - 1);
        setAnimationComplete(true);
      });

      return () => window.cancelAnimationFrame(frameId);
    }

    const stopSequence = () => {
      if (sequenceTimerRef.current !== null) {
        window.clearInterval(sequenceTimerRef.current);
        sequenceTimerRef.current = null;
      }
    };

    const startSequence = () => {
      stopSequence();
      setActiveStageIndex(0);
      setAnimationComplete(false);

      let nextStageIndex = 0;
      sequenceTimerRef.current = window.setInterval(() => {
        nextStageIndex += 1;

        if (nextStageIndex < submissionStages.length) {
          setActiveStageIndex(nextStageIndex);
          return;
        }

        stopSequence();
        setAnimationComplete(true);
      }, STAGE_DURATION_MS);
    };

    const resetSequence = () => {
      stopSequence();
      setActiveStageIndex(-1);
      setAnimationComplete(false);
    };

    const useDesktopFocus = window.matchMedia(
      "(min-width: 48rem) and (min-height: 46rem)",
    ).matches;
    const focusThreshold = useDesktopFocus ? 0.62 : 0.36;
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (
          entry.intersectionRatio > 0.14 &&
          resetTimerRef.current !== null
        ) {
          window.clearTimeout(resetTimerRef.current);
          resetTimerRef.current = null;
        }

        if (entry.intersectionRatio >= focusThreshold) {
          if (!isFocusedRef.current) {
            isFocusedRef.current = true;
            startSequence();
          }
          return;
        }

        if (entry.intersectionRatio <= 0.14 && isFocusedRef.current) {
          if (resetTimerRef.current === null) {
            resetTimerRef.current = window.setTimeout(() => {
              isFocusedRef.current = false;
              resetTimerRef.current = null;
              resetSequence();
            }, FOCUS_RESET_DELAY_MS);
          }
        }
      },
      {
        threshold: [0, 0.14, 0.36, 0.62, 0.8, 1],
      },
    );

    observer.observe(section);

    return () => {
      observer.disconnect();
      stopSequence();
      isFocusedRef.current = false;

      if (resetTimerRef.current !== null) {
        window.clearTimeout(resetTimerRef.current);
        resetTimerRef.current = null;
      }
    };
  }, [reduceMotion]);

  const displayStageIndex = Math.max(activeStageIndex, 0);
  const activeStage = submissionStages[displayStageIndex];
  const progress = animationComplete
    ? 1
    : activeStageIndex < 0
      ? 0
      : (activeStageIndex + 1) / submissionStages.length;
  const completedNodeIds = useMemo(() => {
    const nodeIds = new Set();

    submissionStages
      .slice(
        0,
        animationComplete
          ? submissionStages.length
          : Math.max(activeStageIndex, 0),
      )
      .forEach((stage) => {
        stage.nodeIds.forEach((nodeId) => nodeIds.add(nodeId));
      });

    return nodeIds;
  }, [activeStageIndex, animationComplete]);

  return (
    <section
      className="submission-flow-story landing-snap-section"
      ref={sectionRef}
      aria-labelledby="submission-flow-heading">
      <div className="submission-flow-sticky">
        <div className="submission-flow-shell">
          <header className="submission-flow-intro">
            <div>
              <p className="submission-flow-eyebrow">Behind each submission</p>
              <h2 className="submission-flow-heading" id="submission-flow-heading">
                From your editor to a useful next step.
              </h2>
            </div>
            <p className="submission-flow-copy">
              Your solution moves through a trusted evaluation loop. The source
              is checked, but only safe progress data is stored.
            </p>
          </header>

          <div className="submission-flow-stage-bar" aria-live="polite">
            <span className="submission-flow-stage-count">
              {activeStageIndex < 0 ? "00" : activeStage.number} /{" "}
              {String(submissionStages.length).padStart(2, "0")}
            </span>
            <span className="submission-flow-stage-action">
              {animationComplete
                ? "Trusted loop complete"
                : activeStageIndex < 0
                  ? "Ready to follow the submission"
                  : activeStage.action}
            </span>
            <span className="submission-flow-stage-progress" aria-hidden="true">
              <span style={{ transform: `scaleX(${progress})` }} />
            </span>
          </div>

          <div className="submission-flow-diagram">
            <svg
              className="submission-flow-routes"
              viewBox="0 0 1000 430"
              preserveAspectRatio="none"
              aria-hidden="true">
              <path className="submission-flow-route-base" d="M135 145 C220 145 295 145 405 145" />
              <path className="submission-flow-route-base" d="M405 178 C405 225 405 266 405 318" />
              <path className="submission-flow-route-base" d="M455 145 C555 145 650 145 775 145" />
              <path className="submission-flow-route-base" d="M775 178 C675 225 555 225 455 178" />
              <path className="submission-flow-route-base" d="M440 178 C525 250 635 290 775 318" />
              <path className="submission-flow-route-base" d="M370 178 C320 244 245 292 135 318" />
              <path className="submission-flow-route-base submission-flow-route-loop" d="M98 286 C48 245 48 205 98 178" />

              {[
                "M135 145 C220 145 295 145 405 145",
                "M405 178 C405 225 405 266 405 318",
                "M455 145 C555 145 650 145 775 145",
                "M775 178 C675 225 555 225 455 178",
                "M440 178 C525 250 635 290 775 318",
                "M370 178 C320 244 245 292 135 318",
              ].map((path, index) => {
                const isActive = index === activeStageIndex && !animationComplete;
                const isComplete = index < activeStageIndex || animationComplete;

                return (
                  <path
                    className={`submission-flow-route-active ${
                      isActive ? "submission-flow-route-current" : ""
                    } ${isComplete ? "submission-flow-route-complete" : ""}`}
                    d={path}
                    key={path}
                    pathLength="1"
                  />
                );
              })}

              <path
                className={`submission-flow-route-active submission-flow-route-active-loop ${
                  activeStageIndex === 5 && !animationComplete
                    ? "submission-flow-route-current"
                    : ""
                } ${animationComplete ? "submission-flow-route-complete" : ""}`}
                d="M98 286 C48 245 48 205 98 178"
                pathLength="1"
              />
            </svg>

            <div className="submission-flow-route-label submission-flow-route-label-request">
              JWT request
            </div>
            <div className="submission-flow-route-label submission-flow-route-label-content">
              pinned revision
            </div>
            <div className="submission-flow-route-label submission-flow-route-label-checks">
              source + tests
            </div>
            <div className="submission-flow-route-label submission-flow-route-label-verdict">
              trusted verdict
            </div>
            <div className="submission-flow-route-label submission-flow-route-label-safe">
              safe data only
            </div>
            <div className="submission-flow-route-label submission-flow-route-label-feedback">
              feedback
            </div>

            {submissionNodes.map((node) => {
              const isActive =
                activeStageIndex >= 0 &&
                !animationComplete &&
                activeStage.nodeIds.includes(node.id);
              const isComplete = completedNodeIds.has(node.id);

              return (
                <article
                  className={`submission-flow-node submission-flow-node-${node.id} ${
                    isActive ? "submission-flow-node-active" : ""
                  } ${isComplete ? "submission-flow-node-complete" : ""}`}
                  key={node.id}>
                  <span className="submission-flow-node-mark" aria-hidden="true">
                    <img
                      className={node.id === "editor" ? "submission-flow-codemirror-logo" : ""}
                      src={node.image}
                      alt=""
                    />
                  </span>
                  <span className="submission-flow-node-copy">
                    <span className="submission-flow-node-eyebrow">{node.eyebrow}</span>
                    <strong>{node.label}</strong>
                    <small>{node.detail}</small>
                  </span>
                  <span className="submission-flow-node-status" aria-hidden="true" />
                </article>
              );
            })}

            <p className="submission-flow-source-note">
              <span aria-hidden="true">//</span> learner source is evaluated, not stored
            </p>
          </div>

          <ol className="submission-flow-mobile-list" aria-label="Submission flow">
            {submissionStages.map((stage, index) => {
              const node = submissionNodes.find(({ id }) => id === stage.nodeIds.at(-1));
              const isActive = index === activeStageIndex && !animationComplete;
              const isComplete = index < activeStageIndex || animationComplete;

              return (
                <li
                  className={`${isActive ? "submission-flow-mobile-active" : ""} ${
                    isComplete ? "submission-flow-mobile-complete" : ""
                  }`}
                  key={stage.number}>
                  <span className="submission-flow-mobile-number">{stage.number}</span>
                  <span className="submission-flow-mobile-mark" aria-hidden="true">
                    <img
                      className={node.id === "editor" ? "submission-flow-codemirror-logo" : ""}
                      src={node.image}
                      alt=""
                    />
                  </span>
                  <span>
                    <strong>{stage.mobileLabel}</strong>
                    <small>{stage.mobileDetail}</small>
                  </span>
                </li>
              );
            })}
          </ol>

          <p className="submission-flow-mobile-retention">
            Your source is evaluated, not stored. Only safe progress data is kept.
          </p>

          <footer className="submission-flow-outcome">
            <span className="submission-flow-outcome-kicker">
              {animationComplete
                ? "Loop complete"
                : activeStageIndex < 0
                  ? "Flow begins when this chapter is in view"
                  : "Following the submission flow"}
            </span>
            <strong>
              {animationComplete
                ? "A trusted result becomes focused practice."
                : activeStageIndex < 0
                  ? "Ready to follow the request"
                  : activeStage.shortAction}
            </strong>
          </footer>
        </div>
      </div>
    </section>
  );
}

export default SubmissionFlowStory;
