-- Known-good starter curriculum for manual development verification.

insert into skills (name, description, difficulty)
values ('Variables',
        'Store and work with values in Java.',
        'Beginner'),
       ('Control Flow',
        'Use conditionals and loops to control program behavior.',
        'Beginner'),
       ('Methods',
        'Organize reusable behavior into methods.',
        'Beginner'),
       ('Arrays',
        'Store and process fixed-size collections of values.',
        'Beginner'),
       ('Strings',
        'Create, inspect, and transform sequences of characters.',
        'Beginner') as incoming
on duplicate key update description = incoming.description,
                        difficulty  = incoming.difficulty;

insert into learning_paths (name, description, language)
select 'Java',
       'Build foundational Java programming skills.',
       'Java'
where NOT exists (select 1
                  from learning_paths
                  where name = 'Java'
                    and language = 'Java');

set
    @java_path_id = (select id
                     from learning_paths
                     where name = 'Java'
                       and language = 'Java'
                     order by id
                     limit 1);

set
    @variables_skill_id = (select id
                           from skills
                           where name = 'Variables');

set
    @control_flow_skill_id = (select id
                              from skills
                              where name = 'Control Flow');

set
    @methods_skill_id = (select id
                         from skills
                         where name = 'Methods');

set
    @arrays_skill_id = (select id
                        from skills
                        where name = 'Arrays');

set
    @strings_skill_id = (select id
                         from skills
                         where name = 'Strings');

insert into learning_path_skills (learning_path_id, skill_id, sequence_order)
values (@java_path_id, @variables_skill_id, 0),
       (@java_path_id, @control_flow_skill_id, 1),
       (@java_path_id, @methods_skill_id, 2),
       (@java_path_id, @arrays_skill_id, 3),
       (@java_path_id, @strings_skill_id, 4) as incoming
on duplicate key update sequence_order = incoming.sequence_order;

insert into
    exercises (
    external_id,
    title,
    description,
    difficulty,
    source
)
values
    (
        'java-variables-001',
        'Print an Age Variable',
        'Write a Java program that declares an int variable named age, assigns it the value 25, and prints it.',
        'Beginner',
        'CodeCalibrate'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @variables_skill_id = (
        select
            id
        from
            skills
        where
            name = 'Variables'
    );

set
    @variables_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-variables-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@variables_exercise_id, @variables_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;

insert into
    exercises (
    external_id,
    title,
    description,
    difficulty,
    source
)
values
    (
        'exercism-lasagna-001',
        'Cook Your Lasagna',
        'Complete four Java methods that calculate the expected oven time, remaining oven time, preparation time, and total working time for a lasagna.',
        'Beginner',
        'Exercism'
    ) as incoming
    on duplicate key update
                         title = incoming.title,
                         description = incoming.description,
                         difficulty = incoming.difficulty;

set
@lasagna_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-lasagna-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@lasagna_exercise_id, @variables_skill_id),
    (@lasagna_exercise_id, @methods_skill_id) as incoming
    on duplicate key update
                         skill_id = incoming.skill_id;


insert into
    exercises (
    external_id,
    title,
    description,
    difficulty,
    source
)
values
    (
        'exercism-annalyns-infiltration-001',
        'Annalyn''s Infiltration',
        'Complete four boolean methods that determine which actions Annalyn can take based on whether the knight, archer, and prisoner are awake and whether her dog is present.',
        'Beginner',
        'Exercism'
    ),
    (
        'exercism-blackjack-001',
        'Blackjack',
        'Implement card parsing and conditional decision methods that choose whether a Blackjack player should stand, hit, split, or automatically win.',
        'Intermediate',
        'Exercism'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @annalyn_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-annalyns-infiltration-001'
    );

set
    @blackjack_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-blackjack-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@annalyn_exercise_id, @control_flow_skill_id),
    (@annalyn_exercise_id, @methods_skill_id),
    (@blackjack_exercise_id, @control_flow_skill_id),
    (@blackjack_exercise_id, @methods_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;

insert into
    exercises (
    external_id,
    title,
    description,
    difficulty,
    source
)
values
    (
        'exercism-cars-assemble-001',
        'Cars, Assemble!',
        'Implement two Java methods that calculate an assembly line''s hourly production rate and its number of working cars produced per minute.',
        'Beginner',
        'Exercism'
    ),
    (
        'exercism-salary-calculator-001',
        'Salary Calculator',
        'Implement four Java methods that apply attendance penalties, sales bonuses, and a maximum salary using ternary operators.',
        'Intermediate',
        'Exercism'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @cars_assemble_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-cars-assemble-001'
    );

set
    @salary_calculator_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-salary-calculator-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@cars_assemble_exercise_id, @variables_skill_id),
    (@cars_assemble_exercise_id, @control_flow_skill_id),
    (@cars_assemble_exercise_id, @methods_skill_id),
    (@salary_calculator_exercise_id, @variables_skill_id),
    (@salary_calculator_exercise_id, @control_flow_skill_id),
    (@salary_calculator_exercise_id, @methods_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;

insert into
    exercises (
    external_id,
    title,
    description,
    difficulty,
    source
)
values
    (
        'exercism-bird-watcher-001',
        'Bird Watcher',
        'Complete six Java methods that inspect, update, and summarize daily bird counts. getLastWeek returns 0, 2, 5, 3, 7, 8, 4. getToday returns the final count, incrementTodaysCount increases that count by one, hasDayWithoutBirds detects a zero, getCountForFirstDays totals the requested number of days without reading past the array, and getBusyDays counts days with at least five birds.',
        'Beginner',
        'Exercism'
    ),
    (
        'exercism-log-levels-001',
        'Log Levels',
        'Process log lines formatted like [ERROR]: Invalid operation. message returns the trimmed text after the colon, logLevel returns the bracketed level in lowercase, and reformat returns the message followed by the lowercase level in parentheses, such as Invalid operation (error).',
        'Beginner',
        'Exercism'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @bird_watcher_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-bird-watcher-001'
    );

set
    @log_levels_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-log-levels-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@bird_watcher_exercise_id, @arrays_skill_id),
    (@bird_watcher_exercise_id, @control_flow_skill_id),
    (@bird_watcher_exercise_id, @methods_skill_id),
    (@log_levels_exercise_id, @strings_skill_id),
    (@log_levels_exercise_id, @methods_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;
