package com.monds.quartzui.job;

import com.monds.quartzui.annotation.CronJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@CronJob(name = "TestJob", cronExpression = "0 0/1 * * * ?")
public class TestJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

        try {
            Thread.sleep(1000L * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
