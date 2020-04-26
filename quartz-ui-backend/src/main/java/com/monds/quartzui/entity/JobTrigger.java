package com.monds.quartzui.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
public class JobTrigger {

    @Id
    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_class_name")
    private String jobClassName;

    @Column(name = "trigger_type")
    private String triggerType;

    @Column(name = "trigger_state")
    private String triggerState;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "prev_fire_time")
    private String prevFireTime;

    @Column(name = "next_fire_time")
    private String nextFireTime;

    @Column(name = "cron_expression")
    private String cronExpression;
}
