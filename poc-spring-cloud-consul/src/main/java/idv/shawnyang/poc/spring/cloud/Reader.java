package idv.shawnyang.poc.spring.cloud;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.event.CommandStopReader;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewCheck;
import com.google.gson.Gson;

@Component
@RestController
public class Reader {

	private static final Logger log = LoggerFactory.getLogger(Reader.class);
	private boolean started = false;
	private boolean checkingStarted = false;

	@Autowired
	private ConsulClient consulClient;

	@Autowired
	private ConsulRegistration consulRegistration;

	@Autowired
	private ReaderProperties readerProperties;

	@Autowired
	private Gson gson;

	@PostConstruct
	public void postConstruct() {
		try {
			log.info("BEGIN");

			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@EventListener(ContextRefreshedEvent.class)
	public void init() {
		try {
			log.info("BEGIN");

			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@EventListener(ContextClosedEvent.class)
	public void closed() {
		try {
			log.info("BEGIN");

			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * If PLC is not ready, for example, not connected, not power on, disconnect
	 * due to some error, etc, the component will retry until it get back.
	 */
	@Scheduled(initialDelay = 10000, fixedDelay = 10000)
	void scheduledManage() {
		try {
			log.info("BEGIN");

			if (started != readerProperties.isStarted()) {
				if (readerProperties.isStarted()) {
					this.start();
				} else {
					this.stop();
				}
			}

			if (checkingStarted != readerProperties.isCheckingStarted()) {
				if (readerProperties.isCheckingStarted()) {
					this.startChecking();
				} else {
					this.stopChecking();
				}
			}

			if (checkingStarted) {
				if (started) {
					consulClient.agentCheckPass("service:" + Reader.class.getName());
				} else {
					consulClient.agentCheckFail("service:" + Reader.class.getName());
				}
			}

			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@PreDestroy
	void preDestroy() {
		try {
			log.info("BEGIN");
			if (checkingStarted) {
				stopChecking();
			}
			if (started) {
				stop();
			}
			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@RequestMapping("/reader/print/")
	public String printReader() {
		String message = gson.toJson(readerProperties);
		log.info(message);
		return message;
	}

	@EventListener(classes = CommandStopReader.class)
	public void stopReader(CommandStopReader event) {
		try {
			log.info("BEGIN");
			stop();
			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	private synchronized void start() {
		log.info(this.getClass().getName() + " starting");
		started = true;
		log.info(this.getClass().getName() + " started");
	}

	private synchronized void startChecking() {
		NewCheck newCheck = new NewCheck();
		newCheck.setId("service:" + Reader.class.getName());
		newCheck.setName("Sub service '" + Reader.class.getSimpleName() + "' check");
		newCheck.setTtl("30s");
		newCheck.setServiceId(consulRegistration.getServiceId());
		consulClient.agentCheckRegister(newCheck);
		checkingStarted = true;
	}

	private synchronized void stopChecking() {
		consulClient.agentCheckDeregister(Reader.class.getName());
		checkingStarted = false;
	}

	private synchronized void stop() {
		log.info(this.getClass().getName() + " stopping");
		started = false;
		log.info(this.getClass().getName() + " stopped");
	}

}
