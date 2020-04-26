create table qrtz_trigger_histories
(
    sched_name varchar(120) not null,
    entry_id varchar(50) not null,
    job_name varchar(200) not null,
    job_group varchar(200) not null,
    start_time datetime null,
    end_time datetime null,
    primary key (sched_name, entry_id)
);