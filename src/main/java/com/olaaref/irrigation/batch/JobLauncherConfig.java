package com.olaaref.irrigation.batch;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import com.olaaref.irrigation.entity.Plot;
import com.olaaref.irrigation.service.PlotService;

@Component
public class JobLauncherConfig{
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;
	
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	
	@Autowired
	private PlotService plotService;
	
	private void executeIrrigation(Plot plot) throws InterruptedException {
		taskScheduler.scheduleWithFixedDelay(() -> {
			try {
				runJob();
			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, plot.getDelay()*1000);
	}
	
	@PostConstruct
	public void automateIrrigationSystem() {
		
		List<Plot> plots = plotService.getAllPlots();
		
		plots.forEach((plot) -> {
			try {
				executeIrrigation(plot);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
	}
	
	public void runJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException  {
		System.out.println("Batch job starting");
		
		JobParameters jobParameters = new JobParametersBuilder()
					.addString("time", LocalDateTime.now().toString())
					.toJobParameters();
		
		jobLauncher.run(job, jobParameters);
		
		System.out.println("Batch job executed successfully\n");
	}


}
