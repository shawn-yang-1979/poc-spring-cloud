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
import org.springframework.scheduling.annotation.Scheduled;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.health.model.Check;

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

	private Map<String, HealthService> healthServiceByDataCenter = new HashMap<>();

	@Scheduled(initialDelay = 10000, fixedDelay = 10000)
	public void scheduledCheck() {
		List<String> dataCenters = consul.getCatalogDatacenters().getValue();
		for (String dataCenter : dataCenters) {
			HealthService healthService = healthServiceByDataCenter.get(dataCenter);
			if (healthService == null) {
				healthService = new HealthService(dataCenter, consul);
				healthServiceByDataCenter.put(dataCenter, healthService);
			}
			try {
				List<Check> checks = healthService.watch();
				for (Check check : checks) {
					log.info(dataCenter);
					log.info(check.toString());
					log.info("Send mail to system admin");
				}
			} catch (Throwable t) {
				log.error(t.getMessage(), t);
			}
		}
	}

}
