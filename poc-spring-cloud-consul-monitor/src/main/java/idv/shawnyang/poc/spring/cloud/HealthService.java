/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package idv.shawnyang.poc.spring.cloud;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.Check;
import com.ecwid.consul.v1.health.model.Check.CheckStatus;

/**
 * @author Spencer Gibb
 */
public class HealthService {

	private ConsulClient consul;
	private String dataCenter;
	private AtomicReference<Long> lastIndex = new AtomicReference<>();

	public HealthService(String dataCenter, ConsulClient consul) {
		super();
		this.dataCenter = dataCenter;
		this.consul = consul;
		setLastIndex(getHealthCheckStateResponse());
	}

	public List<Check> watch() {
		return watch(lastIndex.get());
	}

	private void setLastIndex(Response<?> response) {
		Long consulIndex = response.getConsulIndex();
		if (consulIndex != null) {
			lastIndex.set(response.getConsulIndex());
		}
	}

	private Response<List<Check>> getHealthCheckStateResponse() {
		return consul.getHealthChecksState(CheckStatus.CRITICAL, new QueryParams(dataCenter));
	}

	private List<Check> watch(Long lastIndex) {
		long index = -1;
		if (lastIndex != null) {
			index = lastIndex;
		}
		Response<List<Check>> watch = consul.getHealthChecksState(CheckStatus.CRITICAL,
				new QueryParams(dataCenter, -1, index));
		return readChecks(watch);
	}

	private List<Check> readChecks(Response<List<Check>> response) {
		setLastIndex(response);
		return response.getValue();
	}

}
