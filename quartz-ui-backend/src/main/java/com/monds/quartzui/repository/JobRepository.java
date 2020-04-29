package com.monds.quartzui.repository;

import com.monds.quartzui.entity.JobTrigger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = JobTrigger.class, idClass = String.class)
public interface JobRepository {

    @Query(
        value = "select a.job_name, a.job_class_name, b.trigger_type, b.trigger_state, " +
        "       from_unixtime(b.start_time / 1000, '%Y-%m-%d %T') start_time,\n" +
        "       from_unixtime(b.end_time / 1000, '%Y-%m-%d %T') end_time,\n" +
        "       from_unixtime(b.prev_fire_time / 1000, '%Y-%m-%d %T') prev_fire_time,\n" +
        "       from_unixtime(b.next_fire_time / 1000, '%Y-%m-%d %T') next_fire_time,\n" +
        "        c.cron_expression\n" +
        "from qrtz_job_details a, qrtz_triggers b, qrtz_cron_triggers c\n" +
        "where a.job_name = b.job_name\n" +
        "and b.trigger_name = c.trigger_name \n-- #pageable\n",
        countQuery = "select count(1) from qrtz_job_details a, qrtz_triggers b, qrtz_cron_triggers c " +
            "where a.job_name = b.job_name and b.trigger_name = c.trigger_name",
        nativeQuery = true
    )
    Page<JobTrigger> findAll(Pageable pageable);
}
