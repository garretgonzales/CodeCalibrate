import { useEffect, useRef } from "react";
import { java } from "@codemirror/lang-java";
import { EditorState } from "@codemirror/state";
import { EditorView, keymap } from "@codemirror/view";
import { basicSetup } from "codemirror";
import { indentWithTab } from "@codemirror/commands";

const editorTheme = EditorView.theme({
  "&": {
    minHeight: "24rem",
    backgroundColor: "var(--color-surface)",
    color: "var(--color-ink-950)",
    fontFamily: "var(--font-mono)",
    fontSize: "0.95rem",
  },
  ".cm-scroller": {
    overflow: "auto",
  },
  ".cm-content": {
    minHeight: "24rem",
    padding: "1rem 0",
    caretColor: "var(--color-brand-500)",
  },
  ".cm-line": {
    padding: "0 1rem",
  },
  ".cm-gutters": {
    backgroundColor: "var(--color-brand-50)",
    color: "var(--color-ink-500)",
    borderRight: "1px solid var(--color-brand-100)",
  },
  ".cm-activeLine": {
    backgroundColor: "rgb(134 59 255 / 5%)",
  },
  ".cm-activeLineGutter": {
    backgroundColor: "rgb(134 59 255 / 10%)",
  },
  "&.cm-focused": {
    outline: "3px solid rgb(71 191 255 / 35%)",
    outlineOffset: "2px",
  },
});

function JavaCodeEditor({
  value,
  onChange,
  maxLength = 20000,
  ariaLabel = "Java source code",
  ariaLabelledBy,
  ariaDescribedBy,
}) {
  const editorParentRef = useRef(null);
  const editorViewRef = useRef(null);
  const initialValueRef = useRef(value);
  const onChangeRef = useRef(onChange);

  useEffect(() => {
    onChangeRef.current = onChange;
  }, [onChange]);

  useEffect(() => {
    if (!editorParentRef.current) {
      return undefined;
    }

    const accessibilityAttributes = {
      "aria-multiline": "true",
      ...(ariaLabelledBy
        ? { "aria-labelledby": ariaLabelledBy }
        : { "aria-label": ariaLabel }),
      ...(ariaDescribedBy ? { "aria-describedby": ariaDescribedBy } : {}),
    };

    const view = new EditorView({
      state: EditorState.create({
        doc: initialValueRef.current,
        extensions: [
          basicSetup,
          keymap.of([indentWithTab]),
          java(),
          EditorView.lineWrapping,
          EditorState.changeFilter.of(
            (transaction) => transaction.newDoc.length <= maxLength,
          ),
          EditorView.updateListener.of((update) => {
            if (update.docChanged) {
              onChangeRef.current(update.state.doc.toString());
            }
          }),
          EditorView.contentAttributes.of(accessibilityAttributes),
          editorTheme,
        ],
      }),
      parent: editorParentRef.current,
    });

    editorViewRef.current = view;

    return () => {
      view.destroy();
      editorViewRef.current = null;
    };
  }, [ariaDescribedBy, ariaLabel, ariaLabelledBy, maxLength]);

  useEffect(() => {
    const view = editorViewRef.current;

    if (!view) {
      return;
    }

    const editorValue = view.state.doc.toString();

    if (editorValue !== value) {
      view.dispatch({
        changes: {
          from: 0,
          to: view.state.doc.length,
          insert: value,
        },
      });
    }
  }, [value]);

  return (
    <div
      className="overflow-hidden rounded-xl border border-brand-100 bg-white shadow-inner"
      ref={editorParentRef}
    />
  );
}

export default JavaCodeEditor;
