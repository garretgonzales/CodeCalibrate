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
        'exercism-resistor-color-001',
        'Resistor Color',
        'Implement a Java method that converts a resistor band color name into its numeric code by finding its position in the standard color sequence.',
        'Beginner',
        'Exercism'
    ),
    (
        'exercism-resistor-color-duo-001',
        'Resistor Color Duo',
        'Implement Java methods that convert two resistor band colors into a two-digit numeric value using the standard resistor color sequence.',
        'Beginner',
        'Exercism'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @resistor_color_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-resistor-color-001'
    );

set
    @resistor_color_duo_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-resistor-color-duo-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@resistor_color_exercise_id, @arrays_skill_id),
    (@resistor_color_duo_exercise_id, @arrays_skill_id) as incoming
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
        'exercism-two-fer-001',
        'Two Fer',
        'Implement a Java method that builds the two-fer sentence for a given name, defaulting to "you" when no name is provided.',
        'Beginner',
        'Exercism'
    ),
    (
        'exercism-acronym-001',
        'Acronym',
        'Implement a Java method that converts a multi-word phrase into its uppercase acronym, correctly handling punctuation, hyphens, and mixed casing.',
        'Intermediate',
        'Exercism'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @two_fer_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-two-fer-001'
    );

set
    @acronym_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'Exercism'
          and external_id = 'exercism-acronym-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@two_fer_exercise_id, @strings_skill_id),
    (@acronym_exercise_id, @strings_skill_id),
    (@acronym_exercise_id, @methods_skill_id) as incoming
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
        'java-array-statistics-001',
        'Array Statistics',
        'Write Java methods that find the minimum, maximum, and sum of a list of integers read from input.',
        'Intermediate',
        'CodeCalibrate'
    ),
    (
        'java-palindrome-check-001',
        'Palindrome Check',
        'Write a Java method that determines whether a string reads the same forwards and backwards.',
        'Intermediate',
        'CodeCalibrate'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @array_statistics_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-array-statistics-001'
    );

set
    @palindrome_check_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-palindrome-check-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@array_statistics_exercise_id, @arrays_skill_id),
    (@array_statistics_exercise_id, @methods_skill_id),
    (@palindrome_check_exercise_id, @strings_skill_id),
    (@palindrome_check_exercise_id, @methods_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;

insert into skills (name, description, difficulty)
values ('Recursion',
        'Solve problems by having a method call itself with a smaller input.',
        'Intermediate'),
       ('Classes and Objects',
        'Define classes with fields, constructors, and instance methods that model data.',
        'Intermediate') as incoming
on duplicate key update description = incoming.description,
                        difficulty  = incoming.difficulty;

set
    @recursion_skill_id = (select id
                           from skills
                           where name = 'Recursion');

set
    @classes_objects_skill_id = (select id
                                 from skills
                                 where name = 'Classes and Objects');

insert into learning_path_skills (learning_path_id, skill_id, sequence_order)
values (@java_path_id, @recursion_skill_id, 5),
       (@java_path_id, @classes_objects_skill_id, 6) as incoming
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
        'java-factorial-001',
        'Factorial',
        'Write a recursive Java method that computes the factorial of a non-negative integer.',
        'Beginner',
        'CodeCalibrate'
    ),
    (
        'java-fibonacci-001',
        'Fibonacci Number',
        'Write a recursive Java method that returns the nth Fibonacci number, where fibonacci(0) is 0 and fibonacci(1) is 1.',
        'Intermediate',
        'CodeCalibrate'
    ),
    (
        'java-digit-sum-001',
        'Sum of Digits',
        'Write a recursive Java method that returns the sum of the digits of a non-negative integer.',
        'Intermediate',
        'CodeCalibrate'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @factorial_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-factorial-001'
    );

set
    @fibonacci_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-fibonacci-001'
    );

set
    @digit_sum_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-digit-sum-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@factorial_exercise_id, @recursion_skill_id),
    (@factorial_exercise_id, @methods_skill_id),
    (@fibonacci_exercise_id, @recursion_skill_id),
    (@fibonacci_exercise_id, @methods_skill_id),
    (@digit_sum_exercise_id, @recursion_skill_id),
    (@digit_sum_exercise_id, @methods_skill_id) as incoming
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
        'java-rectangle-001',
        'Rectangle Class',
        'Define a Rectangle class with width and height fields, and instance methods that compute its area and perimeter.',
        'Beginner',
        'CodeCalibrate'
    ),
    (
        'java-bank-account-001',
        'Bank Account',
        'Define a BankAccount class that supports deposits and withdrawals, rejecting any withdrawal that would overdraw the balance.',
        'Intermediate',
        'CodeCalibrate'
    ),
    (
        'java-student-grades-001',
        'Student Grades',
        'Define a Student class that stores a name and test scores, and computes the average score and whether the student passed.',
        'Intermediate',
        'CodeCalibrate'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @rectangle_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-rectangle-001'
    );

set
    @bank_account_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-bank-account-001'
    );

set
    @student_grades_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-student-grades-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@rectangle_exercise_id, @classes_objects_skill_id),
    (@bank_account_exercise_id, @classes_objects_skill_id),
    (@bank_account_exercise_id, @control_flow_skill_id),
    (@student_grades_exercise_id, @classes_objects_skill_id),
    (@student_grades_exercise_id, @arrays_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;

insert into skills (name, description, difficulty)
values ('Collections',
        'Store, search, and transform groups of values using Java''s List and Map collections.',
        'Intermediate'),
       ('Exception Handling',
        'Handle runtime errors gracefully using try, catch, and validation that throws.',
        'Intermediate') as incoming
on duplicate key update description = incoming.description,
                        difficulty  = incoming.difficulty;

set
    @collections_skill_id = (select id
                             from skills
                             where name = 'Collections');

set
    @exception_handling_skill_id = (select id
                                    from skills
                                    where name = 'Exception Handling');

insert into learning_path_skills (learning_path_id, skill_id, sequence_order)
values (@java_path_id, @collections_skill_id, 7),
       (@java_path_id, @exception_handling_skill_id, 8) as incoming
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
        'java-word-frequency-001',
        'Word Frequency',
        'Write a Java method that counts how many times each word appears in a line of text using a Map.',
        'Intermediate',
        'CodeCalibrate'
    ),
    (
        'java-remove-duplicates-001',
        'Remove Duplicate Values',
        'Write a Java method that returns the distinct values from a list of integers, preserving their first-seen order, using a List.',
        'Intermediate',
        'CodeCalibrate'
    ),
    (
        'java-two-sum-001',
        'Two Sum',
        'Write a Java method that finds the indices of the two numbers in an array that add up to a target value, using a Map for fast lookup.',
        'Intermediate',
        'CodeCalibrate'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @word_frequency_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-word-frequency-001'
    );

set
    @remove_duplicates_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-remove-duplicates-001'
    );

set
    @two_sum_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-two-sum-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@word_frequency_exercise_id, @collections_skill_id),
    (@word_frequency_exercise_id, @strings_skill_id),
    (@remove_duplicates_exercise_id, @collections_skill_id),
    (@remove_duplicates_exercise_id, @arrays_skill_id),
    (@two_sum_exercise_id, @collections_skill_id),
    (@two_sum_exercise_id, @arrays_skill_id) as incoming
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
        'java-safe-division-001',
        'Safe Division',
        'Write a Java method that divides two integers and returns 0 when dividing by zero, catching the ArithmeticException instead of checking beforehand.',
        'Beginner',
        'CodeCalibrate'
    ),
    (
        'java-safe-parse-001',
        'Safe Integer Parsing',
        'Write a Java method that parses a string to an integer, catching NumberFormatException and returning -1 for invalid input.',
        'Beginner',
        'CodeCalibrate'
    ),
    (
        'java-age-validator-001',
        'Age Validator',
        'Write a Java method that validates an age is between 0 and 120 inclusive, throwing an IllegalArgumentException when it is not.',
        'Intermediate',
        'CodeCalibrate'
    ) as incoming
on duplicate key update
                     title = incoming.title,
                     description = incoming.description,
                     difficulty = incoming.difficulty;

set
    @safe_division_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-safe-division-001'
    );

set
    @safe_parse_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-safe-parse-001'
    );

set
    @age_validator_exercise_id = (
        select
            id
        from
            exercises
        where
            source = 'CodeCalibrate'
          and external_id = 'java-age-validator-001'
    );

insert into
    exercise_skills (exercise_id, skill_id)
values
    (@safe_division_exercise_id, @exception_handling_skill_id),
    (@safe_division_exercise_id, @methods_skill_id),
    (@safe_parse_exercise_id, @exception_handling_skill_id),
    (@safe_parse_exercise_id, @strings_skill_id),
    (@age_validator_exercise_id, @exception_handling_skill_id),
    (@age_validator_exercise_id, @control_flow_skill_id) as incoming
on duplicate key update
    skill_id = incoming.skill_id;
