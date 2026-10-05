set @add_tip_column = (
    select if(
        exists(
            select 1
            from information_schema.columns
            where table_schema = database()
              and table_name = 'exercises'
              and column_name = 'tip'
        ),
        'select 1',
        'alter table exercises add column tip text'
    )
);

prepare add_tip_column from @add_tip_column;
execute add_tip_column;
deallocate prepare add_tip_column;
