package idv.shawnyang.poc.spring.cloud;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
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
	private Gson gson;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private ConsulRegistration consulRegistration;

	@RequestMapping("/service/consul/print")
	public String serviceConsulPrint() {
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances("consul");
		String message = gson.toJson(serviceInstances);
		log.info(message);
		return message;
	}

	@RequestMapping("/service/me/print")
	public String serviceMePrint() {
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances(consulRegistration.getServiceId());
		String message = gson.toJson(serviceInstances);
		log.info(message);
		return message;
	}

}
