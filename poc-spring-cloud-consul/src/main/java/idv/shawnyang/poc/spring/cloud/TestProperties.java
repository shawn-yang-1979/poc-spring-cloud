package idv.shawnyang.poc.spring.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "test")
public class TestProperties {
	private int qos = 0;
	private int localBrokerPort = 1883;
	private boolean defaultBackendPayloadAsBytes;
	private boolean defaultBadgeDetectionOutboundEnable;
	private boolean defaultDoorEntryOutboundEnable;

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public int getLocalBrokerPort() {
		return localBrokerPort;
	}

	public void setLocalBrokerPort(int localBrokerPort) {
		this.localBrokerPort = localBrokerPort;
	}

	public boolean isDefaultBackendPayloadAsBytes() {
		return defaultBackendPayloadAsBytes;
	}

	public void setDefaultBackendPayloadAsBytes(boolean defaultBackendPayloadAsBytes) {
		this.defaultBackendPayloadAsBytes = defaultBackendPayloadAsBytes;
	}

	public boolean isDefaultBadgeDetectionOutboundEnable() {
		return defaultBadgeDetectionOutboundEnable;
	}

	public void setDefaultBadgeDetectionOutboundEnable(boolean defaultBadgeDetectionOutboundEnable) {
		this.defaultBadgeDetectionOutboundEnable = defaultBadgeDetectionOutboundEnable;
	}

	public boolean isDefaultDoorEntryOutboundEnable() {
		return defaultDoorEntryOutboundEnable;
	}

	public void setDefaultDoorEntryOutboundEnable(boolean defaultDoorEntryOutboundEnable) {
		this.defaultDoorEntryOutboundEnable = defaultDoorEntryOutboundEnable;
	}

}
