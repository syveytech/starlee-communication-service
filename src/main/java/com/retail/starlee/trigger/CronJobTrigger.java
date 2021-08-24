package com.retail.starlee.trigger;

import java.sql.Connection;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.data.daoimpl.CommunicationParametersDaoImpl;
import com.retail.starlee.data.factory.ConnectionFactory;
import com.retail.starlee.jobs.processor.SendSmsProcessor;

public class CronJobTrigger {
	private static Logger logger = LoggerFactory.getLogger(CronJobTrigger.class.getName());

	public static void main(String[] args) throws Exception{
		try{
			logger.debug("Starting CronJob for send Sms Job");

			String cronExpSms = CommunicationParametersDaoImpl.getParameter("CRON_EXP_SMS_JOB");
			Connection connectionForSendSmsJob = ConnectionFactory.createConnection();
			
			//Send SMS API Job
			JobKey jobKeyResendJob = new JobKey("SendSMSJob", "group");
			JobDetail sendSmsJob =  JobBuilder.newJob(SendSmsProcessor.class).withIdentity(jobKeyResendJob).build();
			sendSmsJob.getJobDataMap().put("connectionForSendSmsJob", connectionForSendSmsJob);

			Trigger triggerSendSmsJob = TriggerBuilder.newTrigger().withIdentity("TriggerResendJob", "group")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpSms)).build();
			
			//schedule it
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(sendSmsJob, triggerSendSmsJob);
		}catch(Exception e){
			logger.error("Exception in CronJobTrigger :", e);
		}
	}
}
