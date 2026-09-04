import { useEffect, useRef } from "react";

function easeOutCubic(progress) {
  return 1 - Math.pow(1 - progress, 3);
}

function AnimatedNumber({ value, decimals = 0, duration = 1000, suffix = "" }) {
  const numberRef = useRef(null);
  const numericValue = Number(value);
  const targetValue = Number.isFinite(numericValue) ? numericValue : 0;

  const formatOptions = {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  };

  const finalText =
    targetValue.toLocaleString(undefined, formatOptions) + suffix;

  useEffect(() => {
    const numberElement = numberRef.current;

    if (!numberElement) {
      return undefined;
    }

    const formatter = new Intl.NumberFormat(undefined, {
      minimumFractionDigits: decimals,
      maximumFractionDigits: decimals,
    });

    function formatNumber(currentValue) {
      return formatter.format(currentValue) + suffix;
    }

    const reducedMotion = window.matchMedia(
      "(prefers-reduced-motion: reduce)",
    ).matches;

    if (reducedMotion || duration <= 0) {
      numberElement.textContent = formatNumber(targetValue);
      return undefined;
    }

    let animationFrameId;
    const startTime = performance.now();

    numberElement.textContent = formatNumber(0);

    function updateNumber(currentTime) {
      const elapsed = currentTime - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const easedProgress = easeOutCubic(progress);

      numberElement.textContent = formatNumber(targetValue * easedProgress);

      if (progress < 1) {
        animationFrameId = window.requestAnimationFrame(updateNumber);
      } else {
        numberElement.textContent = formatNumber(targetValue);
      }
    }

    animationFrameId = window.requestAnimationFrame(updateNumber);

    return () => {
      window.cancelAnimationFrame(animationFrameId);
    };
  }, [decimals, duration, suffix, targetValue]);

  return (
    <span>
      <span className="sr-only">{finalText}</span>

      <span ref={numberRef} aria-hidden="true">
        {finalText}
      </span>
    </span>
  );
}

export default AnimatedNumber;
