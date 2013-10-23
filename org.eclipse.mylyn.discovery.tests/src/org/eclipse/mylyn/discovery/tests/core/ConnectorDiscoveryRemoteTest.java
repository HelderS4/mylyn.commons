/*******************************************************************************
 * Copyright (c) 2009, 2010 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.discovery.tests.core;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.commons.core.CoreUtil;
import org.eclipse.mylyn.internal.discovery.core.DiscoveryCore;
import org.eclipse.mylyn.internal.discovery.core.model.ConnectorDiscovery;
import org.eclipse.mylyn.internal.discovery.core.model.DiscoveryConnector;
import org.eclipse.mylyn.internal.discovery.core.model.RemoteBundleDiscoveryStrategy;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.osgi.framework.Bundle;

/**
 * A test that uses the real discovery directory and verifies that it works, and that all referenced update sites appear
 * to be available.
 * 
 * @author David Green
 */
public class ConnectorDiscoveryRemoteTest extends TestCase {

	private ConnectorDiscovery connectorDiscovery;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		connectorDiscovery = new ConnectorDiscovery();
		connectorDiscovery.setVerifyUpdateSiteAvailability(false);

		connectorDiscovery.getDiscoveryStrategies().clear();
		RemoteBundleDiscoveryStrategy remoteStrategy = new RemoteBundleDiscoveryStrategy();
		remoteStrategy.setDirectoryUrl(DiscoveryCore.getDiscoveryUrl());
		connectorDiscovery.getDiscoveryStrategies().add(remoteStrategy);
	}

	public void testRemoteDirectory() throws CoreException {
		connectorDiscovery.performDiscovery(new NullProgressMonitor());

		assertFalse(connectorDiscovery.getCategories().isEmpty());
		assertFalse(connectorDiscovery.getConnectors().isEmpty());
	}

	public void testVerifyAvailability() throws Exception {
		// XXX e3.5 skip test in Tycho build
		Bundle bundle = Platform.getBundle("org.eclipse.equinox.p2.engine"); //$NON-NLS-1$
		if (bundle != null && new VersionRange("[1.0.0,1.1.0)").isIncluded(CoreUtil.getVersion(bundle))) { //$NON-NLS-1$
			System.err.println("Skipping testVerifyAbility() on Eclipse 3.5 due to lack of proxy support");
			return;
		}

		connectorDiscovery.performDiscovery(new NullProgressMonitor());
		for (DiscoveryConnector connector : connectorDiscovery.getConnectors()) {
			assertNull(connector.getAvailable());
		}
		connectorDiscovery.verifySiteAvailability(new NullProgressMonitor());

		assertFalse(connectorDiscovery.getConnectors().isEmpty());

		int unavailableCount = 0;
		for (DiscoveryConnector connector : connectorDiscovery.getConnectors()) {
			if (connector.getAvailable() == null) {
				// connectors that can't be verified need to have a valid install message set
				assertNotNull("Failed to verify availability for " + connector.getId(),
						connector.getAttributes().get(DiscoveryConnector.ATTRIBUTE_INSTALL_MESSAGE));
			} else if (!connector.getAvailable()) {
				++unavailableCount;
			}
		}
		if (unavailableCount > 0) {
			fail(String.format("%s unavailable: %s", unavailableCount, computeUnavailableConnetorDescriptorNames()));
		}
	}

	private String computeUnavailableConnetorDescriptorNames() {
		String message = "";
		for (DiscoveryConnector connector : connectorDiscovery.getConnectors()) {
			if (connector.getAvailable() != null && !connector.getAvailable()) {
				if (message.length() > 0) {
					message += ", ";
				}
				message += connector.getName();
			}
		}
		return message;
	}
}
