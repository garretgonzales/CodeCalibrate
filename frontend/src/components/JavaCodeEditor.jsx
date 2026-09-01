import { useEffect, useRef } from "react";
import { java } from "@codemirror/lang-java";
import { EditorState } from "@codemirror/state";
import { EditorView, keymap } from "@codemirror/view";
import { HighlightStyle, syntaxHighlighting } from "@codemirror/language";
import { tags } from "@lezer/highlight";
import { basicSetup } from "codemirror";
import { indentWithTab } from "@codemirror/commands";

const editorTheme = EditorView.theme({
  "&": {
    minHeight: "24rem",
    backgroundColor: "var(--theme-editor-surface)",
    color: "var(--theme-editor-foreground)",
    fontFamily: "var(--font-mono)",
    fontSize: "0.95rem",
  },
  ".cm-scroller": {
    overflow: "auto",
  },
  ".cm-content": {
    minHeight: "24rem",
    padding: "1rem 0",
    caretColor: "var(--theme-editor-caret)",
  },
  ".cm-line": {
    padding: "0 1rem",
  },
  ".cm-gutters": {
    backgroundColor: "var(--theme-editor-gutter)",
    color: "var(--theme-editor-gutter-foreground)",
    borderRight: "1px solid var(--theme-border)",
  },
  ".cm-activeLine": {
    backgroundColor: "var(--theme-editor-active-line)",
  },
  ".cm-activeLineGutter": {
    backgroundColor: "var(--theme-editor-active-line)",
  },
  ".cm-selectionBackground, &.cm-focused .cm-selectionBackground, ::selection": {
    backgroundColor: "var(--theme-editor-selection) !important",
  },
  ".cm-cursor, .cm-dropCursor": {
    borderLeftColor: "var(--theme-editor-caret)",
  },
  ".cm-matchingBracket": {
    color: "var(--theme-editor-foreground)",
    backgroundColor: "var(--theme-editor-selection)",
    outline: "1px solid var(--theme-editor-caret)",
  },
  ".cm-tooltip, .cm-panels": {
    color: "var(--theme-editor-foreground)",
    backgroundColor: "var(--theme-editor-tooltip)",
    borderColor: "var(--theme-border-strong)",
  },
  "&.cm-focused": {
    outline: "3px solid var(--theme-editor-focus)",
    outlineOffset: "2px",
  },
});

const editorHighlightStyle = HighlightStyle.define([
  { tag: [tags.keyword, tags.modifier], color: "var(--theme-editor-keyword)" },
  { tag: [tags.string, tags.character], color: "var(--theme-editor-string)" },
  { tag: [tags.number, tags.bool, tags.null], color: "var(--theme-editor-number)" },
  { tag: [tags.typeName, tags.className], color: "var(--theme-editor-type)" },
  { tag: [tags.comment, tags.meta], color: "var(--theme-editor-comment)" },
  { tag: [tags.variableName, tags.propertyName], color: "var(--theme-editor-variable)" },
  { tag: tags.invalid, color: "var(--theme-editor-invalid)", textDecoration: "underline" },
]);

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
          syntaxHighlighting(editorHighlightStyle),
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
      className="overflow-hidden rounded-xl border border-brand-100 bg-surface shadow-inner"
      ref={editorParentRef}
    />
  );
}

export default JavaCodeEditor;
