package idv.shawnyang.poc.spring.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(BeanConfiguration.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job1() {
		return jobBuilderFactory.get("job1").start(stepBuilderFactory.get("job1step1").tasklet(this::run).build())
				.build();
	}

	@Bean
	public Job job2() {
		return jobBuilderFactory.get("job2").start(stepBuilderFactory.get("job2step1").tasklet(this::run).build())
				.build();
	}

	private RepeatStatus run(StepContribution contribution, ChunkContext chunkContext) {
		logger.info("Job was run");
		return RepeatStatus.FINISHED;
	}
}
