package idv.shawnyang.poc.spring.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reader")
public class ReaderProperties {
	private int power = 250;

	private boolean started = true;

	private boolean checkingStarted = true;

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isCheckingStarted() {
		return checkingStarted;
	}

	public void setCheckingStarted(boolean checkingStarted) {
		this.checkingStarted = checkingStarted;
	}

}
