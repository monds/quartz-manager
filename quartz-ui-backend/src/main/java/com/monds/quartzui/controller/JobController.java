package com.monds.quartzui.controller;

import com.monds.quartzui.JobResponse;
import com.monds.quartzui.ResponseMessage;
import com.monds.quartzui.entity.JobTrigger;
import com.monds.quartzui.entity.TriggerHistory;
import com.monds.quartzui.repository.JobRepository;
import com.monds.quartzui.repository.TriggerHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
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

    @GetMapping(value = "/job/{jobName}/pause")
    public ResponseMessage pauseJob(@PathVariable String jobName) {
        JobKey jobKey = new JobKey(jobName);
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResponseMessage(e.getMessage(), "error");
        }

        return new ResponseMessage("Job " + jobName + " paused.", "success");
    }

    @GetMapping(value = "/job/{jobName}/resume")
    public ResponseMessage resumeJob(@PathVariable String jobName) {
        JobKey jobKey = new JobKey(jobName);
        try {
            schedulerFactoryBean.getScheduler().resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResponseMessage(e.getMessage(), "error");
        }

        return new ResponseMessage("Job " + jobName + " resumed.", "success");
    }

    @GetMapping(value = "/job/{jobName}")
    public JobResponse getJobState(@PathVariable String jobName) {

        Trigger.TriggerState triggerState = Trigger.TriggerState.ERROR;
        try {
            triggerState = schedulerFactoryBean.getScheduler().getTriggerState(new TriggerKey(jobName + "_trigger"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return new JobResponse("success", triggerState.name());
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
