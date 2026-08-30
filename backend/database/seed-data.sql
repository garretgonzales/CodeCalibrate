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
        'Beginner')
on duplicate key update description = VALUES(description),
                        difficulty  = VALUES(difficulty);

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

insert into learning_path_skills (learning_path_id, skill_id, sequence_order)
values (@java_path_id, @variables_skill_id, 0),
       (@java_path_id, @control_flow_skill_id, 1),
       (@java_path_id, @methods_skill_id, 2)
on duplicate key update sequence_order = VALUES(sequence_order);

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
    )
on duplicate key update
                     title = VALUES(title),
                     description = VALUES(description),
                     difficulty = VALUES(difficulty);

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
    (@variables_exercise_id, @variables_skill_id)
on duplicate key update
    skill_id = VALUES(skill_id);
