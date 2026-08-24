create table users
(
    id         int unsigned AUTO_INCREMENT primary key,
    username   varchar(50)  NOT null unique,
    email      varchar(255) NOT null unique,
    password   varchar(255) NOT null,
    created_at TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP
);

create table skills
(
    id          int unsigned AUTO_INCREMENT primary key,
    name        varchar(255) NOT null unique,
    description text,
    difficulty  varchar(32)  NOT null
);

create table learning_paths
(
    id          int unsigned AUTO_INCREMENT primary key,
    name        varchar(255) NOT null,
    description text,
    language    varchar(64)  NOT null,
    created_at  TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP
);

create table exercises
(
    id          int unsigned AUTO_INCREMENT primary key,
    external_id varchar(64)  NOT null,
    title       varchar(255) NOT null,
    description text,
    difficulty  varchar(32),
    source      varchar(32)  NOT null,
    created_at  TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP,
    unique (source, external_id)
);

create table learning_path_skills
(
    learning_path_id int unsigned NOT null,
    skill_id         int unsigned NOT null,
    sequence_order   int unsigned NOT null,
    primary key (learning_path_id, skill_id),
    unique (learning_path_id, sequence_order),
    foreign key (learning_path_id) references learning_paths (id),
    foreign key (skill_id) references skills (id)
);

create table exercise_skills
(
    exercise_id int unsigned NOT null,
    skill_id    int unsigned NOT null,
    primary key (exercise_id, skill_id),
    foreign key (exercise_id) references exercises (id),
    foreign key (skill_id) references skills (id)
);

create table attempts
(
    id               int unsigned AUTO_INCREMENT primary key,
    user_id          int unsigned NOT null,
    exercise_id      int unsigned NOT null,
    submitted_answer text,
    is_correct       boolean      NOT null DEFAULT false,
    attempted_at     TIMESTAMP    NOT null DEFAULT CURRENT_TIMESTAMP,
    foreign key (user_id) references users (id),
    foreign key (exercise_id) references exercises (id)
);

create table user_mastery
(
    id                  int unsigned AUTO_INCREMENT primary key,
    user_id             int unsigned  NOT null,
    skill_id            int unsigned  NOT null,
    mastery_score       decimal(5, 2) NOT null DEFAULT 0.00,
    questions_attempted int unsigned  NOT null DEFAULT 0,
    questions_correct   int unsigned  NOT null DEFAULT 0,
    last_practiced_at   TIMESTAMP     null,
    unique (user_id, skill_id),
    foreign key (user_id) references users (id),
    foreign key (skill_id) references skills (id)
);

create table github_projects
(
    id                int unsigned AUTO_INCREMENT primary key,
    github_id         varchar(64)  NOT null unique,
    name              varchar(255) NOT null,
    description       text,
    url               varchar(500) NOT null,
    language          varchar(64),
    stars             int unsigned NOT null DEFAULT 0,
    forks             int unsigned NOT null DEFAULT 0,
    github_created_at TIMESTAMP    null,
    github_updated_at TIMESTAMP    null
);

create table github_project_skills
(
    github_project_id int unsigned NOT null,
    skill_id          int unsigned NOT null,
    primary key (github_project_id, skill_id),
    foreign key (github_project_id) references github_projects (id),
    foreign key (skill_id) references skills (id)
);