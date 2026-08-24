## ERD

![ERD](assets/CodeForge-ERD.png)

---

## Schema


```mysql
CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skills (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    difficulty VARCHAR(32) NOT NULL
);

CREATE TABLE learning_paths (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    language VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exercises (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    external_id VARCHAR(64) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    difficulty VARCHAR(32),
    source VARCHAR(32) NOT NULL,
    UNIQUE (source, external_id)
);

CREATE TABLE learning_path_skills (
    learning_path_id INT UNSIGNED NOT NULL,
    skill_id INT UNSIGNED NOT NULL,
    sequence_order INT UNSIGNED NOT NULL,
    PRIMARY KEY (learning_path_id, skill_id),
    UNIQUE (learning_path_id, sequence_order),
    FOREIGN KEY (learning_path_id) REFERENCES learning_paths(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);

CREATE TABLE exercise_skills (
    exercise_id INT UNSIGNED NOT NULL,
    skill_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (exercise_id, skill_id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);

CREATE TABLE attempts (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NOT NULL,
    exercise_id INT UNSIGNED NOT NULL,
    submitted_answer TEXT,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    attempted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);

CREATE TABLE user_mastery (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNSIGNED NOT NULL,
    skill_id INT UNSIGNED NOT NULL,
    mastery_score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    questions_attempted INT UNSIGNED NOT NULL DEFAULT 0,
    questions_correct INT UNSIGNED NOT NULL DEFAULT 0,
    last_practiced_at TIMESTAMP NULL,
    UNIQUE (user_id, skill_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);

CREATE TABLE github_projects (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    github_id VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    url VARCHAR(500) NOT NULL,
    language VARCHAR(64),
    stars INT UNSIGNED NOT NULL DEFAULT 0,
    forks INT UNSIGNED NOT NULL DEFAULT 0,
    github_created_at TIMESTAMP NULL,
    github_updated_at TIMESTAMP NULL
);

CREATE TABLE github_project_skills (
    github_project_id INT UNSIGNED NOT NULL,
    skill_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (github_project_id, skill_id),
    FOREIGN KEY (github_project_id) REFERENCES github_projects(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);

```