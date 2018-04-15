package com.lanking.uxb.service.job.config;

import java.util.List;

import javax.sql.DataSource;

import org.quartz.CalendarIntervalTrigger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.spi.JobFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.job.api.impl.CalendarIntervalTriggerJob;
import com.lanking.uxb.service.job.api.impl.LddpCronTriggerJob;

@Configuration
@ConditionalOnBean(name = "lddpJobQuartzProperties")
public class LddpJobQuartzAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext appContext;
	@Autowired
	@Qualifier(value = "lddpJobQuartzProperties")
	private LddpJobQuartzProperties properties;

	@Bean
	public JobFactory jobFactory(ApplicationContext applicationContext) {
		LddpJobQuartzAutowireSpringBeanJobFactory jobFactory = new LddpJobQuartzAutowireSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean(name = "cronTriggerJobScheduler")
	public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("bizDataSource") DataSource dataSource,
			@Qualifier("bizDataSourceTransactionManager") PlatformTransactionManager transactionManager,
			JobFactory jobFactory) throws Exception {
		List<JobDetail> jobDetails = Lists.newArrayList();
		List<Trigger> triggers = Lists.newArrayList();
		for (LddpCronTriggerJob lddpCronTriggerJob : appContext.getBeansOfType(LddpCronTriggerJob.class).values()) {
			if (!lddpCronTriggerJob.legal() || !lddpCronTriggerJob.isCluster()) {
				continue;
			}
			JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
			jobDetailFactoryBean.setGroup(lddpCronTriggerJob.jobGroup());
			jobDetailFactoryBean.setName(lddpCronTriggerJob.jobName());
			jobDetailFactoryBean.setDescription(lddpCronTriggerJob.jobDescription());
			jobDetailFactoryBean.setJobClass(lddpCronTriggerJob.getClass());
			jobDetailFactoryBean.setDurability(true);
			jobDetailFactoryBean.setRequestsRecovery(lddpCronTriggerJob.jobRequestsRecovery());
			jobDetailFactoryBean.afterPropertiesSet();
			JobDetail jobDetail = jobDetailFactoryBean.getObject();
			jobDetails.add(jobDetail);

			CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
			cronTriggerFactoryBean.setGroup(lddpCronTriggerJob.triggerGroup());
			cronTriggerFactoryBean.setName(lddpCronTriggerJob.triggerName());
			cronTriggerFactoryBean.setDescription(lddpCronTriggerJob.triggerDescription());
			cronTriggerFactoryBean.setCronExpression(lddpCronTriggerJob.cron());
			cronTriggerFactoryBean.setJobDetail(jobDetail);
			cronTriggerFactoryBean.afterPropertiesSet();
			CronTrigger cronTrigger = cronTriggerFactoryBean.getObject();
			triggers.add(cronTrigger);
		}

		for (CalendarIntervalTriggerJob calendarIntervalTriggerJob : appContext
				.getBeansOfType(CalendarIntervalTriggerJob.class).values()) {
			if (!calendarIntervalTriggerJob.legal() || !calendarIntervalTriggerJob.isCluster()) {
				continue;
			}
			JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
			jobDetailFactoryBean.setGroup(calendarIntervalTriggerJob.jobGroup());
			jobDetailFactoryBean.setName(calendarIntervalTriggerJob.jobName());
			jobDetailFactoryBean.setDescription(calendarIntervalTriggerJob.jobDescription());
			jobDetailFactoryBean.setJobClass(calendarIntervalTriggerJob.getClass());
			jobDetailFactoryBean.setDurability(true);
			jobDetailFactoryBean.setRequestsRecovery(calendarIntervalTriggerJob.jobRequestsRecovery());
			jobDetailFactoryBean.afterPropertiesSet();
			JobDetail jobDetail = jobDetailFactoryBean.getObject();
			jobDetails.add(jobDetail);

			CalendarIntervalTrigger trigger = new CalendarIntervalTriggerImpl(calendarIntervalTriggerJob.triggerName(),
					calendarIntervalTriggerJob.triggerGroup(), calendarIntervalTriggerJob.jobName(),
					calendarIntervalTriggerJob.jobGroup(), calendarIntervalTriggerJob.startTime(),
					calendarIntervalTriggerJob.endTime(), calendarIntervalTriggerJob.intervalUnit(),
					calendarIntervalTriggerJob.repeatInterval());
			triggers.add(trigger);
		}

		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.setOverwriteExistingJobs(true);
		factory.setJobFactory(jobFactory);
		factory.setJobDetails(jobDetails.toArray(new JobDetail[jobDetails.size()]));
		factory.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
		factory.setQuartzProperties(properties.createQuartzProperties());
		return factory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
}
