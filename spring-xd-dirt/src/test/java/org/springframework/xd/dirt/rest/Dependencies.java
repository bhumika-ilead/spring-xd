/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.xd.dirt.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.xd.analytics.metrics.core.AggregateCounterRepository;
import org.springframework.xd.analytics.metrics.core.CounterRepository;
import org.springframework.xd.analytics.metrics.core.FieldValueCounterRepository;
import org.springframework.xd.analytics.metrics.core.GaugeRepository;
import org.springframework.xd.analytics.metrics.core.RichGaugeRepository;
import org.springframework.xd.dirt.stream.DeploymentMessageSender;
import org.springframework.xd.dirt.stream.EnhancedStreamParser;
import org.springframework.xd.dirt.stream.JobDefinitionRepository;
import org.springframework.xd.dirt.stream.JobDeployer;
import org.springframework.xd.dirt.stream.StreamDefinitionRepository;
import org.springframework.xd.dirt.stream.StreamDeployer;
import org.springframework.xd.dirt.stream.StreamParser;
import org.springframework.xd.dirt.stream.StreamRepository;
import org.springframework.xd.dirt.stream.TapDefinitionRepository;
import org.springframework.xd.dirt.stream.TapDeployer;
import org.springframework.xd.dirt.stream.TapInstanceRepository;
import org.springframework.xd.dirt.stream.TriggerDefinitionRepository;
import org.springframework.xd.dirt.stream.TriggerDeployer;
import org.springframework.xd.dirt.stream.memory.InMemoryJobDefinitionRepository;
import org.springframework.xd.dirt.stream.memory.InMemoryStreamDefinitionRepository;
import org.springframework.xd.dirt.stream.memory.InMemoryStreamRepository;
import org.springframework.xd.dirt.stream.memory.InMemoryTapDefinitionRepository;
import org.springframework.xd.dirt.stream.memory.InMemoryTapInstanceRepository;
import org.springframework.xd.dirt.stream.memory.InMemoryTriggerDefinitionRepository;

import static org.mockito.Mockito.*;

/**
 * Provide a mockito mock for any of the business layer dependencies. Adding yet another configuration class on top, one
 * can selectively override those mocks (with <i>e.g.</i> in memory implementations).
 * 
 * @author Eric Bottard
 * 
 */
@Configuration
public class Dependencies {

	@Bean
	@Qualifier("simple")
	public CounterRepository counterRepository() {
		return mock(CounterRepository.class);
	}

	@Bean
	public AggregateCounterRepository aggregateCounterRepository() {
		return mock(AggregateCounterRepository.class);
	}

	@Bean
	public GaugeRepository gaugeRepository() {
		return mock(GaugeRepository.class);
	}

	@Bean
	public RichGaugeRepository richGaugeRepository() {
		return mock(RichGaugeRepository.class);
	}

	@Bean
	public FieldValueCounterRepository fieldValueCounterRepository() {
		return mock(FieldValueCounterRepository.class);
	}

	@Bean
	public DeploymentMessageSender deploymentMessageSender() {
		return mock(DeploymentMessageSender.class);
	}

	@Bean
	public StreamParser parser() {
		return new EnhancedStreamParser(streamDefinitionRepository());
	}

	@Bean
	public JobDefinitionRepository jobDefinitionRepository() {
		return new InMemoryJobDefinitionRepository();
	}

	@Bean
	public JobDeployer jobDeployer() {
		return new JobDeployer(jobDefinitionRepository(), deploymentMessageSender(), parser());
	}

	@Bean
	public StreamDefinitionRepository streamDefinitionRepository() {
		return new InMemoryStreamDefinitionRepository();
	}

	@Bean
	public StreamDeployer streamDeployer() {
		return new StreamDeployer(streamDefinitionRepository(), deploymentMessageSender(), streamRepository(), parser());
	}

	@Bean
	public StreamRepository streamRepository() {
		return new InMemoryStreamRepository();
	}

	@Bean
	public TapDefinitionRepository tapDefinitionRepository() {
		return new InMemoryTapDefinitionRepository();
	}

	@Bean
	public TapDeployer tapDeployer() {
		return new TapDeployer(tapDefinitionRepository(), streamDefinitionRepository(), deploymentMessageSender(),
				parser(), tapInstanceRepository());
	}

	@Bean
	public TapInstanceRepository tapInstanceRepository() {
		return new InMemoryTapInstanceRepository();
	}

	@Bean
	public TriggerDefinitionRepository triggerDefinitionRepository() {
		return new InMemoryTriggerDefinitionRepository();
	}

	@Bean
	public TriggerDeployer triggerDeployer() {
		return new TriggerDeployer(triggerDefinitionRepository(), deploymentMessageSender(), parser());
	}
}