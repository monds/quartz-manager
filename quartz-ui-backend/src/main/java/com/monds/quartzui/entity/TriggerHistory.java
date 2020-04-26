package com.monds.quartzui.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(TriggerHistoryPK.class)
@Table(name = "qrtz_trigger_histories")
public class TriggerHistory {

    @Id
    @Column(name = "sched_name")
    private String schedName;

    @Id
    @Column(name = "entry_id")
    private String entryId;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_group")
    private String jobGroup;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "end_time")
    private LocalDateTime endTime;
}
