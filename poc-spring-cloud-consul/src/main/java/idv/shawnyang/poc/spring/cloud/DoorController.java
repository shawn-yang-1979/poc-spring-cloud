package idv.shawnyang.poc.spring.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.event.CommandOpenDoor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DoorController {
	private static final Logger log = LoggerFactory.getLogger(Reader.class);

	@EventListener(classes = CommandOpenDoor.class)
	public void openDoor(CommandOpenDoor event) {
		try {
			log.info("BEGIN");

			log.info("END");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}
}
