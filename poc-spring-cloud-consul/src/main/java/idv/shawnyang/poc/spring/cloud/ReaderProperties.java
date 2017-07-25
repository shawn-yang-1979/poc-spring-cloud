package idv.shawnyang.poc.spring.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "reader")
public class ReaderProperties {
	private int power = 250;

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

}
