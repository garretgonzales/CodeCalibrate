## ERD

![ERD](assets/CC-ERD.png)

---

## Schema


```mysql
create table users
(
    id         INT unsigned AUTO_INCREMENT primary key,
    username   VARCHAR(50)  NOT null unique,
    email      VARCHAR(255) NOT null unique,
    password   VARCHAR(255) NOT null,
    created_at TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP
);

create table skills
(
    id          INT unsigned AUTO_INCREMENT primary key,
    name        VARCHAR(255) NOT null unique,
    description TEXT,
    difficulty  VARCHAR(32)  NOT null
);

create table learning_paths
(
    id          INT unsigned AUTO_INCREMENT primary key,
    name        VARCHAR(255) NOT null,
    description TEXT,
    language    VARCHAR(64)  NOT null,
    created_at  TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP
);

create table exercises
(
    id          INT unsigned AUTO_INCREMENT primary key,
    external_id VARCHAR(64)  NOT null,
    title       VARCHAR(255) NOT null,
    description TEXT,
    difficulty  VARCHAR(32),
    source      VARCHAR(32)  NOT null,
    unique (source, external_id)
);

create table learning_path_skills
(
    learning_path_id INT unsigned NOT null,
    skill_id         INT unsigned NOT null,
    sequence_order   INT unsigned NOT null,
    primary key (learning_path_id, skill_id),
    unique (learning_path_id, sequence_order),
    foreign key (learning_path_id) references learning_paths (id),
    foreign key (skill_id) references skills (id)
);

create table exercise_skills
(
    exercise_id INT unsigned NOT null,
    skill_id    INT unsigned NOT null,
    primary key (exercise_id, skill_id),
    foreign key (exercise_id) references exercises (id),
    foreign key (skill_id) references skills (id)
);

create table attempts
(
    id               INT unsigned AUTO_INCREMENT primary key,
    user_id          INT unsigned NOT null,
    exercise_id      INT unsigned NOT null,
    submitted_answer TEXT,
    is_correct       BOOLEAN      NOT null DEFAULT false,
    attempted_at     TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP,
    foreign key (user_id) references users (id),
    foreign key (exercise_id) references exercises (id)
);

create table user_mastery
(
    id                  INT unsigned AUTO_INCREMENT primary key,
    user_id             INT unsigned  NOT null,
    skill_id            INT unsigned  NOT null,
    mastery_score       DECIMAL(5, 2) NOT null DEFAULT 0.00,
    questions_attempted INT unsigned  NOT null DEFAULT 0,
    questions_correct   INT unsigned  NOT null DEFAULT 0,
    last_practiced_at   TIMESTAMP     null,
    unique (user_id, skill_id),
    foreign key (user_id) references users (id),
    foreign key (skill_id) references skills (id)
);

create table github_projects
(
    id                INT unsigned AUTO_INCREMENT primary key,
    github_id         VARCHAR(64)  NOT null unique,
    name              VARCHAR(255) NOT null,
    description       TEXT,
    url               VARCHAR(500) NOT null,
    language          VARCHAR(64),
    stars             INT unsigned NOT null DEFAULT 0,
    forks             INT unsigned NOT null DEFAULT 0,
    github_created_at TIMESTAMP    null,
    github_updated_at TIMESTAMP    null
);

create table github_project_skills
(
    github_project_id INT unsigned NOT null,
    skill_id          INT unsigned NOT null,
    primary key (github_project_id, skill_id),
    foreign key (github_project_id) references github_projects (id),
    foreign key (skill_id) references skills (id)
);

```