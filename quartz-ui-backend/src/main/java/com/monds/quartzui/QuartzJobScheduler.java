package com.monds.quartzui;

import com.monds.quartzui.annotation.CronJob;
import com.monds.quartzui.annotation.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Configuration
public class QuartzJobScheduler {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuartzProperties quartzProperties;

    @Autowired
    private QuartzTriggerListener quartzTriggerListener;

    @Value("${scheduler.job.package}")
    private String jobPackage;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        log.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(DataSource quartzDataSource) throws ClassNotFoundException, SchedulerException {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        log.debug("Setting the Scheduler up");
        schedulerFactoryBean.setJobFactory(springBeanJobFactory());

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        schedulerFactoryBean.setQuartzProperties(properties);

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(SimpleJob.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(CronJob.class));

        log.info("scan package: {}", jobPackage);

        List<JobDetail> jobDetails = new ArrayList<>();
        List<Trigger> triggers = new ArrayList<>();

        for (BeanDefinition beanDef : provider.findCandidateComponents(jobPackage)) {
            Class<?> beanClass = Class.forName(beanDef.getBeanClassName());

            if (beanClass.isAnnotationPresent(SimpleJob.class)) {
                SimpleJob simpleJob = beanClass.getAnnotation(SimpleJob.class);

                JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) beanClass)
                        .storeDurably(true)
                        .withIdentity(simpleJob.name())
                        .requestRecovery(true)
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withSchedule(
                                SimpleScheduleBuilder.simpleSchedule()
                                        .withIntervalInSeconds(simpleJob.frequencyInSec())
                                        .repeatForever()
                        )
                        .forJob(jobDetail.getKey())
                        .withIdentity(simpleJob.name() + "_trigger")
                        .build();

                jobDetails.add(jobDetail);
                triggers.add(trigger);

            } else if (beanClass.isAnnotationPresent(CronJob.class)) {
                CronJob cronJob = beanClass.getAnnotation(CronJob.class);

                JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) beanClass)
                        .storeDurably(true)
                        .withIdentity(cronJob.name())
                        .requestRecovery(true)
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronJob.cronExpression()))
                        .forJob(jobDetail.getKey())
                        .withIdentity(cronJob.name() + "_trigger")
                        .build();

                jobDetails.add(jobDetail);
                triggers.add(trigger);
            }

            schedulerFactoryBean.setJobDetails(jobDetails.toArray(new JobDetail[0]));
            schedulerFactoryBean.setTriggers(triggers.toArray(new Trigger[0]));
            schedulerFactoryBean.setGlobalTriggerListeners(quartzTriggerListener);

        }

        // Comment the following line to use the default Quartz job store.
        schedulerFactoryBean.setDataSource(quartzDataSource);

        return schedulerFactoryBean;
    }

//    @Bean(name = "encryptorBean")
//    public StringEncryptor stringEncryptor() {
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//        config.setPassword("blueming");
//        config.setAlgorithm("PBEWithMD5AndDES");
//        config.setKeyObtentionIterations("1000");
//        config.setPoolSize("1");
//        config.setProviderName("SunJCE");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setStringOutputType("base64");
//        encryptor.setConfig(config);
//        return encryptor;
//    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create().build();
    }

}
