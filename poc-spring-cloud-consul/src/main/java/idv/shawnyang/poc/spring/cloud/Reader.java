package idv.shawnyang.poc.spring.cloud;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.event.CommandStopReader;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewCheck;

@Component
public class Reader {

	private static final Logger log = LoggerFactory.getLogger(Reader.class);
	private boolean running = false;
	private boolean checking = false;

	@Autowired
	private ConsulClient consulClient;

	@Autowired
	private ConsulRegistration consulRegistration;

	@Autowired
	private TtlScheduler ttlScheduler;

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
	void scheduledRecover() {
		try {
			log.info("BEGIN");

			if (!running) {
				this.startRunning();
			}

			if (!checking) {
				this.startChecking();
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
			if (checking) {
				stopChecking();
			}
			if (running) {
				stopRunning();
			}
			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	@EventListener(classes = CommandStopReader.class)
	public void stopReader(CommandStopReader event) {
		try {
			log.info("BEGIN");
			stopRunning();
			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	private synchronized void startRunning() {
		log.info(this.getClass().getName() + " starting");
		running = true;
		log.info(this.getClass().getName() + " started");
	}

	private synchronized void startChecking() {
		NewCheck newCheck = new NewCheck();
		newCheck.setId("service:" + Reader.class.getName());
		newCheck.setName("Sub service '" + Reader.class.getSimpleName() + "' check");
		newCheck.setTtl("30s");
		newCheck.setServiceId(consulRegistration.getServiceId());
		consulClient.agentCheckRegister(newCheck);
		ttlScheduler.add(Reader.class.getName());
		checking = true;
	}

	private synchronized void stopChecking() {
		ttlScheduler.remove(Reader.class.getName());
		consulClient.agentCheckDeregister(Reader.class.getName());
		checking = false;
	}

	private synchronized void stopRunning() {
		log.info(this.getClass().getName() + " stopping");
		running = false;
		log.info(this.getClass().getName() + " stopped");
	}

}
