package com.monds.quartzui;

import com.monds.quartzui.entity.TriggerHistory;
import com.monds.quartzui.repository.TriggerHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class QuartzTriggerListener implements TriggerListener {

    private static final String TRIGGER_LISTENER_NAME = "GlobalTriggerListener";

    @Autowired @Lazy
    private TriggerHistoryRepository triggerHistoryRepository;

    @Override
    public String getName() {
        return TRIGGER_LISTENER_NAME;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        jobExecutionContext.getJobDetail().getJobDataMap().put("trigger.fired.time", LocalDateTime.now());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {}

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        JobKey jobKey = trigger.getJobKey();
        TriggerHistory triggerHistory = new TriggerHistory();
        try {
            triggerHistory.setSchedName(jobExecutionContext.getScheduler().getSchedulerName());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        triggerHistory.setEntryId(jobExecutionContext.getFireInstanceId());
        triggerHistory.setJobName(jobKey.getName());
        triggerHistory.setJobGroup(jobKey.getGroup());
//        triggerHistory.setStartTime(LocalDateTime.ofInstant(trigger.getPreviousFireTime().toInstant(), ZoneId.systemDefault()));
        triggerHistory.setStartTime((LocalDateTime) jobExecutionContext.getJobDetail().getJobDataMap().get("trigger.fired.time"));
        triggerHistory.setEndTime(LocalDateTime.now());
        triggerHistoryRepository.save(triggerHistory);
    }
}
