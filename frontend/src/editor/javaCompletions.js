import { snippetCompletion } from "@codemirror/autocomplete";

const JAVA_KEYWORDS = [
  "abstract",
  "boolean",
  "break",
  "byte",
  "case",
  "catch",
  "char",
  "class",
  "continue",
  "default",
  "do",
  "double",
  "else",
  "enum",
  "extends",
  "false",
  "final",
  "finally",
  "float",
  "for",
  "if",
  "implements",
  "import",
  "int",
  "interface",
  "long",
  "new",
  "null",
  "package",
  "private",
  "protected",
  "public",
  "return",
  "short",
  "static",
  "super",
  "switch",
  "this",
  "throw",
  "throws",
  "true",
  "try",
  "void",
  "while",
];

const JAVA_TYPES = [
  "ArrayList",
  "Boolean",
  "Character",
  "Double",
  "Exception",
  "Float",
  "Integer",
  "List",
  "Long",
  "Math",
  "Object",
  "Scanner",
  "String",
  "StringBuilder",
  "System",
];

const keywordCompletions = JAVA_KEYWORDS.map((keyword) => ({
  label: keyword,
  type: "keyword",
  detail: "Java keyword",
}));

const typeCompletions = JAVA_TYPES.map((typeName) => ({
  label: typeName,
  type: "type",
  detail: "Java type",
}));

const snippetCompletions = [
  snippetCompletion(
    "public static void main(String[] args) {\n\t${0}\n}",
    {
      label: "main",
      type: "function",
      detail: "Create a main method",
      boost: 10,
    },
  ),
  snippetCompletion("System.out.println(${value});", {
    label: "sout",
    type: "function",
    detail: "Print a line",
    boost: 10,
  }),
  snippetCompletion(
    "if (${condition}) {\n\t${0}\n}",
    {
      label: "if",
      type: "keyword",
      detail: "Create an if statement",
      boost: 8,
    },
  ),
  snippetCompletion(
    "if (${condition}) {\n\t${thenBody}\n} else {\n\t${elseBody}\n}",
    {
      label: "ifelse",
      type: "keyword",
      detail: "Create an if/else statement",
      boost: 8,
    },
  ),
  snippetCompletion(
    "for (int ${index} = 0; ${index} < ${length}; ${index}++) {\n\t${0}\n}",
    {
      label: "fori",
      type: "keyword",
      detail: "Create an indexed for loop",
      boost: 8,
    },
  ),
  snippetCompletion(
    "for (${type} ${item} : ${collection}) {\n\t${0}\n}",
    {
      label: "foreach",
      type: "keyword",
      detail: "Create an enhanced for loop",
      boost: 8,
    },
  ),
  snippetCompletion(
    "while (${condition}) {\n\t${0}\n}",
    {
      label: "while",
      type: "keyword",
      detail: "Create a while loop",
      boost: 8,
    },
  ),
  snippetCompletion(
    "try {\n\t${tryBody}\n} catch (${Exception} ${exception}) {\n\t${0}\n}",
    {
      label: "trycatch",
      type: "keyword",
      detail: "Create a try/catch block",
      boost: 8,
    },
  ),
];

const JAVA_COMPLETIONS = [
  ...snippetCompletions,
  ...keywordCompletions,
  ...typeCompletions,
];

export function javaCompletionSource(context) {
  const word = context.matchBefore(/[\w$]*/);

  if (word.from === word.to && !context.explicit) {
    return null;
  }

  return {
    from: word.from,
    options: JAVA_COMPLETIONS,
    validFor: /^[\w$]*$/,
  };
}