package org.wso2.developerstudio.codenvy.ext.runner.server.runner;

import com.codenvy.api.core.notification.EventService;
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.internal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CarbonServer extends Runner {

	private static final Logger LOG = LoggerFactory.getLogger(CarbonServer.class);

	public CarbonServer(File deployDirectoryRoot, int cleanupDelay, ResourceAllocators allocators,
	                    EventService eventService) {
		super(deployDirectoryRoot, cleanupDelay, allocators, eventService);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public RunnerConfigurationFactory getRunnerConfigurationFactory() {
		return null;
	}

	@Override
	protected ApplicationProcess newApplicationProcess(DeploymentSources deploymentSources,
	                                                   RunnerConfiguration runnerConfiguration)
			throws RunnerException {
		return null;
	}
}
