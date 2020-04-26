package com.monds.quartzui.controller;

import com.monds.quartzui.entity.JobTrigger;
import com.monds.quartzui.entity.TriggerHistory;
import com.monds.quartzui.repository.JobRepository;
import com.monds.quartzui.repository.TriggerHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class JobController {

    public JobController() {
        log.info("*********************************************");
    }

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TriggerHistoryRepository triggerHistoryRepository;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostMapping(value = "/job/{jobName}/pause")
    public String pauseJob(@PathVariable String jobName) {
        JobKey jobKey = new JobKey(jobName, "DEFAULT");
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return "DONE";
    }

    @PostMapping(value = "/job/{jobName}/resume")
    public String resumeJob(@PathVariable String jobName) {
        JobKey jobKey = new JobKey(jobName, "DEFAULT");
        try {
            schedulerFactoryBean.getScheduler().resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return "DONE";
    }

    @GetMapping("/jobs")
    public Page<JobTrigger> findAll(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @GetMapping("/job/{jobName}/history")
    public Page<TriggerHistory> findAllTriggerHistories(@PathVariable String jobName,
                                                        @PageableDefault(size = 10, sort = "startTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return triggerHistoryRepository.findByJobName(jobName, pageable);
    }
}
