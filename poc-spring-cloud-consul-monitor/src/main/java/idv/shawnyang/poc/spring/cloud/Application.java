package idv.shawnyang.poc.spring.cloud;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.health.model.Check;
import com.google.gson.Gson;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
		log.info("Beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			log.info(beanName);
		}
	}

	@Autowired
	private ConsulClient consul;
	@Autowired
	private MailSender mailSender;
	@Autowired
	private SimpleMailMessage simpleMailMessageTemplate;
	@Autowired
	private Gson gson;

	private Map<String, State> stateByDataCenter = new HashMap<>();

	@Bean
	public SimpleMailMessage simpleMailMessageTemplate() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("SERVICE.TBSVRADMIN@deltaww.com");
		return message;
	}

	@Scheduled(initialDelay = 10000, fixedDelay = 10000)
	public void scheduledCheck() {
		List<String> dataCenters = consul.getCatalogDatacenters().getValue();
		for (String dataCenter : dataCenters) {
			State state = stateByDataCenter.get(dataCenter);
			if (state == null) {
				state = new State(new HealthService(dataCenter, consul));
				stateByDataCenter.put(dataCenter, state);
			}

			String healthState = null;
			try {
				List<Check> checks = state.getHealthService().watch();
				if (!checks.isEmpty()) {
					healthState = gson.toJson(checks);
				}
			} catch (Throwable t) {
				log.error(t.getMessage(), t);
				healthState = t.getMessage();
			}

			if (healthState == null) {
				continue;
			}

			if (healthState.equals(state.getHealthState())) {
				continue;
			}

			state.setHealthState(healthState);

			SimpleMailMessage message = new SimpleMailMessage(simpleMailMessageTemplate);
			message.setSubject(dataCenter + " health status changed");
			message.setTo("shawn.sh.yang@deltaww.com");

			try {
				message.setText(healthState);
				this.mailSender.send(message);
			} catch (MailException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private static class State {
		private HealthService healthService;
		private String healthState;

		public State(HealthService healthService) {
			super();
			this.healthService = healthService;
		}

		public HealthService getHealthService() {
			return healthService;
		}

		public String getHealthState() {
			return healthState;
		}

		public void setHealthState(String healthState) {
			this.healthState = healthState;
		}

	}

}
