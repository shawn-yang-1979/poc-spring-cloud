package idv.shawnyang.poc.spring.cloud;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.event.CommandRestartReader;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@RequestMapping("/")
	public String home() {
		List<ServiceInstance> serviceInstances = serviceUrl();
		for (ServiceInstance serviceInstance : serviceInstances) {
			log.info(serviceInstance.getHost());
			log.info(serviceInstance.getServiceId());
			log.info("port=" + serviceInstance.getPort());
			for (Entry<String, String> entry : serviceInstance.getMetadata().entrySet()) {
				log.info(entry.getKey());
				log.info(entry.getValue());
			}
		}

		log.info("Qos=" + testProperties.getQos());

		return "Hello World";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private TestProperties testProperties;

	public List<ServiceInstance> serviceUrl() {
		return discoveryClient.getInstances("poc-spring-cloud-consul");
	}

	@EventListener(classes = CommandRestartReader.class)
	public void restartReader(CommandRestartReader event) {
		log.info("Restart reader");
	}

}
