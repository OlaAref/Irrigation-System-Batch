package com.olaaref.irrigation.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.olaaref.irrigation.entity.Plot;

@EnableScheduling
@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public ItemReader<Plot> irrigationItemReader() {
		JdbcCursorItemReader<Plot> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id, area, delay, water_amount, crop_id FROM plots");
        reader.setRowMapper(new PlotRowMapper());
        reader.setName("jdbcCursorItemReader");
        return reader;
	}
	
	@Bean
	public IrrigationItemWriter irrigationItemWriter() {
		return new IrrigationItemWriter();
	}

	@Bean
	public SendRequestProcessor sendRequestProcessor() {
		return new SendRequestProcessor();
	}
	
	@Bean
	public ConsumeDurationProcessor consumeDurationProcessor() {
		return new ConsumeDurationProcessor();
	}
	
	@Bean
	public OpenSlotProcessor openSlotProcessor() {
		return new OpenSlotProcessor();
	}
	
	@Bean
	public CloseSlotProcessor closeSlotProcessor() {
		return new CloseSlotProcessor();
	}
	
	@Bean
	public SaveLogProcessor saveLogProcessor() {
		return new SaveLogProcessor();
	}
	
	
	@Bean
	public ItemProcessor<Plot, Plot> compositeItemProcessor() {
		return new CompositeItemProcessorBuilder<Plot, Plot>()
				.delegates(sendRequestProcessor(), openSlotProcessor(), consumeDurationProcessor(), closeSlotProcessor(), saveLogProcessor())
				.build();
	}
	
	@Bean
	public Job createJob() {
		return jobBuilderFactory.get("MyJob")
				.start(createStep()).build();
	}

	@Bean
	public Step createStep() {
		
		return stepBuilderFactory.get("MyStep")
				.<Plot, Plot> chunk(1)
				.reader(irrigationItemReader())
				.processor(compositeItemProcessor())
				.writer(irrigationItemWriter())
				.build();
	}

	
}
