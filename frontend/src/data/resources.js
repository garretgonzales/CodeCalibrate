export const RESOURCE_TECHNOLOGIES = [
  "Spring",
  "Testing",
  "Judge0",
  "Railway",
  "Docker",
  "MySQL",
  "Tailwind CSS",
  "React",
  "React Router",
  "Vite",
  "CodeMirror",
  "JJWT",
  "GitHub API",
  "Caddy",
  "Java",
  "Design",
];

export const resources = [
  {
    id: "spring-mvc-testing",
    title: "Spring MVC Testing",
    description:
      "Testing controllers, request handling, authentication boundaries, and API responses with MockMvc.",
    url: "https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-client.html",
    technologies: ["Spring", "Testing"],
    type: "Official documentation",
  },
  {
    id: "spring-boot-reference",
    title: "Spring Boot Reference",
    description:
      "Configuration, application startup, external properties, web applications, and production behavior.",
    url: "https://docs.spring.io/spring-boot/reference/",
    technologies: ["Spring", "Java"],
    type: "Official documentation",
  },
  {
    id: "spring-data-jpa",
    title: "Spring Data JPA",
    description:
      "Repository interfaces, entity persistence, query methods, transactions, and relational data access.",
    url: "https://docs.spring.io/spring-data/jpa/reference/",
    technologies: ["Spring", "MySQL"],
    type: "Official documentation",
  },
  {
    id: "spring-security-authentication",
    title: "Spring Security Authentication Architecture",
    description:
      "The authentication context, filters, providers, and security boundaries used by Spring Security.",
    url: "https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html",
    technologies: ["Spring", "JJWT"],
    type: "Official documentation",
  },
  {
    id: "spring-security-regex-request-matcher",
    title: "Spring Security RegexRequestMatcher",
    description:
      "Reference for restricting public exercise routes to numeric exercise identifiers.",
    url: "https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/web/util/matcher/RegexRequestMatcher.html",
    technologies: ["Spring"],
    type: "Official documentation",
  },
  {
    id: "spring-mvc-cors",
    title: "Spring MVC CORS",
    description:
      "Configuring allowed origins for communication between the deployed frontend and backend.",
    url: "https://docs.spring.io/spring-framework/reference/web/webmvc-cors.html",
    technologies: ["Spring", "React"],
    type: "Official documentation",
  },
  {
    id: "jjwt",
    title: "JJWT",
    description:
      "Creating, signing, parsing, and validating JSON Web Tokens in Java.",
    url: "https://github.com/jwtk/jjwt",
    technologies: ["JJWT", "Java", "Spring"],
    type: "Official documentation",
  },
  {
    id: "judge0-authentication",
    title: "Judge0 Authentication",
    description:
      "Authentication headers required to access the external code-execution provider.",
    url: "https://docs.judge0.com/products/judge0/http_api/authentication/",
    technologies: ["Judge0"],
    type: "Official documentation",
  },
  {
    id: "judge0-submissions",
    title: "Judge0 Submissions",
    description:
      "Creating Java submissions and retrieving asynchronous execution verdicts.",
    url: "https://docs.judge0.com/products/judge0/http_api/submissions/",
    technologies: ["Judge0", "Java"],
    type: "Official documentation",
  },
  {
    id: "github-repository-contents-api",
    title: "GitHub Repository Contents API",
    description:
      "Retrieving private, commit-pinned exercise definitions through the GitHub REST API.",
    url: "https://docs.github.com/en/rest/repos/contents?apiVersion=2022-11-28",
    technologies: ["GitHub API"],
    type: "Official documentation",
  },
  {
    id: "mysql-on-duplicate-key",
    title: "MySQL INSERT ON DUPLICATE KEY UPDATE",
    description:
      "Creating repeatable curriculum seed scripts without duplicating existing records.",
    url: "https://dev.mysql.com/doc/refman/8.4/en/insert-on-duplicate.html",
    technologies: ["MySQL"],
    type: "Official documentation",
  },
  {
    id: "java-learning",
    title: "Learn Java",
    description:
      "Official Java language guidance used to support exercises and learner references.",
    url: "https://dev.java/learn/",
    technologies: ["Java"],
    type: "Official documentation",
  },
  {
    id: "react",
    title: "React",
    description:
      "Components, state, effects, conditional rendering, and interactive user interfaces.",
    url: "https://react.dev/learn",
    technologies: ["React"],
    type: "Official documentation",
  },
  {
    id: "react-router",
    title: "React Router",
    description:
      "Public, authenticated, dashboard, and exercise routing in the single-page application.",
    url: "https://reactrouter.com/home",
    technologies: ["React Router", "React"],
    type: "Official documentation",
  },
  {
    id: "vite",
    title: "Vite",
    description:
      "Frontend development, environment configuration, and optimized production builds.",
    url: "https://vite.dev/guide/",
    technologies: ["Vite", "React"],
    type: "Official documentation",
  },
  {
    id: "tailwind-transitions",
    title: "Tailwind CSS Transitions",
    description:
      "Official transition utilities, animation timing, and reduced-motion variants.",
    url: "https://tailwindcss.com/docs/transition-property",
    technologies: ["Tailwind CSS"],
    type: "Official documentation",
  },
  {
    id: "codemirror-system-guide",
    title: "CodeMirror System Guide",
    description:
      "Editor state, views, extensions, updates, Java support, and custom themes.",
    url: "https://codemirror.net/docs/guide/",
    technologies: ["CodeMirror", "Java"],
    type: "Official documentation",
  },
  {
    id: "docker-multi-stage-builds",
    title: "Docker Multi-Stage Builds",
    description:
      "Separating build and runtime stages to produce smaller frontend and backend images.",
    url: "https://docs.docker.com/build/building/multi-stage/",
    technologies: ["Docker"],
    type: "Official documentation",
  },
  {
    id: "caddyfile",
    title: "The Caddyfile",
    description:
      "Serving the compiled React application and supporting client-side route fallback.",
    url: "https://caddyserver.com/docs/caddyfile",
    technologies: ["Caddy"],
    type: "Official documentation",
  },
  {
    id: "railway-spa-routing",
    title: "Railway SPA Routing",
    description:
      "Configuring fallback routing so direct browser requests reach React routes.",
    url: "https://docs.railway.com/guides/spa-routing-configuration",
    technologies: ["Railway", "React Router", "Caddy"],
    type: "Official documentation",
  },
  {
    id: "railway-dockerfiles",
    title: "Railway Dockerfiles",
    description:
      "Building and deploying services from repository-owned Dockerfiles.",
    url: "https://docs.railway.com/builds/dockerfiles",
    technologies: ["Railway", "Docker"],
    type: "Official documentation",
  },
  {
    id: "railway-monorepo",
    title: "Deploying a Monorepo to Railway",
    description:
      "Deploying the frontend and backend as separate services from one repository.",
    url: "https://docs.railway.com/guides/deploying-a-monorepo",
    technologies: ["Railway"],
    type: "Official documentation",
  },
  {
    id: "railway-github-autodeploys",
    title: "Controlling GitHub Autodeploys",
    description:
      "Selecting deployment branches and controlling automatic production builds.",
    url: "https://docs.railway.com/deployments/github-autodeploys",
    technologies: ["Railway", "GitHub API"],
    type: "Official documentation",
  },
  {
    id: "railway-connect",
    title: "Railway CLI Connect",
    description:
      "Connecting local database tools and commands to a deployed Railway database.",
    url: "https://docs.railway.com/cli/connect",
    technologies: ["Railway", "MySQL"],
    type: "Official documentation",
  },
  {
    id: "railway-mysql",
    title: "MySQL on Railway",
    description:
      "Provisioning MySQL and connecting services through Railway's private network.",
    url: "https://docs.railway.com/databases/mysql",
    technologies: ["Railway", "MySQL"],
    type: "Official documentation",
  },
  {
    id: "railway-domains",
    title: "Railway Custom Domains",
    description:
      "Connecting codecalibrate.dev and its API subdomain to deployed services.",
    url: "https://docs.railway.com/networking/domains",
    technologies: ["Railway"],
    type: "Official documentation",
  },
  {
    id: "railway-variables",
    title: "Railway Variables",
    description:
      "Managing database settings, API addresses, allowed origins, and production secrets.",
    url: "https://docs.railway.com/guides/variables",
    technologies: ["Railway"],
    type: "Official documentation",
  },
  {
    id: "tailwind-transition-guide",
    title: "Tailwind CSS Transitions Guide",
    description:
      "Additional examples for transition duration, easing, and delay utilities.",
    url: "https://unwiredlearning.com/blog/tailwind-transitions-guide",
    technologies: ["Tailwind CSS"],
    type: "Community guide",
  },
  {
    id: "tailwind-animation-playground",
    title: "Tailwind CSS Animation Playground",
    description:
      "Interactive animation examples used while exploring entrance transitions.",
    url: "https://tailwind-animations.com/playground?a=fade-in-left&p=card",
    technologies: ["Tailwind CSS", "Design"],
    type: "Design inspiration",
  },
  {
    id: "responsive-navbar-codepen",
    title: "Premium Responsive Navbar",
    description: "Navigation layout and responsive-interaction inspiration.",
    url: "https://codepen.io/themrsami/pen/KwKeXdm",
    technologies: ["Design", "Tailwind CSS"],
    type: "Design inspiration",
  },
  {
    id: "dashboard-codepen",
    title: "Flat Sales Dashboard UI",
    description:
      "Modular dashboard layout and information-hierarchy inspiration.",
    url: "https://codepen.io/jkantner/pen/vEBQZYe",
    technologies: ["Design"],
    type: "Design inspiration",
  },
  {
    id: "number-animation-codepen",
    title: "Numbers Animation",
    description:
      "Count-up animation inspiration for the dashboard's learner metrics.",
    url: "https://codepen.io/Marina_Os/pen/QWjMGEW",
    technologies: ["Design"],
    type: "Design inspiration",
  },
  {
    id: "resource-card-animation-codepen",
    title: "Card Animation",
    description:
      "Layered card movement and action-reveal inspiration for the resource library.",
    url: "https://codepen.io/GopiAkshay13/pen/PooNQrX",
    technologies: ["Design"],
    type: "Design inspiration",
  },
  {
    id: "smartega-verdict-loader",
    title: "SmartegaAgency CSS Loader",
    description:
      "Motion reference used while planning the exercise-verdict loading state.",
    url: "https://uiverse.io/SmartegaAgency/wise-robin-91",
    technologies: ["Design"],
    type: "Design inspiration",
  },
  {
    id: "mrhyddenn-loader",
    title: "mrhyddenn CSS Loader",
    description:
      "Alternative loading-animation reference considered for submission feedback.",
    url: "https://uiverse.io/mrhyddenn/plastic-panther-51",
    technologies: ["Design"],
    type: "Design inspiration",
  },
];
