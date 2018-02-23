package idv.shawnyang.poc.spring.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.stereotype.Component;

@Component
public class TaskExecutionListener {
	private static final Logger log = LoggerFactory.getLogger(TaskExecutionListener.class);

	@BeforeTask
	public void beforeTask(TaskExecution taskExecution) {
		log.info(taskExecution.getTaskName() + " begin.");
	}

	@AfterTask
	public void afterTask(TaskExecution taskExecution) {
		log.info(taskExecution.getTaskName() + " end.");
	}

	@FailedTask
	public void failedTask(TaskExecution taskExecution, Throwable throwable) {
		log.error(taskExecution.getTaskName(), throwable);

	}
}
