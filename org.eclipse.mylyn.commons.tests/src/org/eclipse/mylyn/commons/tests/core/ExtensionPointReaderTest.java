/*******************************************************************************
 * Copyright (c) 2012 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.commons.tests.core;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.core.ExtensionPointReader;

/**
 * @author Steffen Pingel
 * @author Sam Davis
 */
public class ExtensionPointReaderTest extends TestCase {

	public static interface ExtensionPointReaderExtension {

	}

	public static class ExtensionPointReaderExtensionImplementation implements ExtensionPointReaderExtension {

		String id;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ExtensionPointReaderExtensionImplementation other = (ExtensionPointReaderExtensionImplementation) obj;
			if (id == null) {
				if (other.id != null) {
					return false;
				}
			} else if (!id.equals(other.id)) {
				return false;
			}
			return true;
		}
	}

	public static class P5ExtensionPointReaderExtensionImplementation extends
			ExtensionPointReaderExtensionImplementation {
	}

	public static class PNegative5ExtensionPointReaderExtensionImplementation extends
			ExtensionPointReaderExtensionImplementation {
	}

	public static class P10ExtensionPointReaderExtensionImplementation extends
			ExtensionPointReaderExtensionImplementation {
	}

	private static final String ID_PLUGIN = "org.eclipse.mylyn.commons.tests";

	public void testRead() {
		ExtensionPointReader<ExtensionPointReaderExtension> reader = new ExtensionPointReader<ExtensionPointReaderExtension>(
				ID_PLUGIN, "extensionPointReaderTest", "extensionElement", ExtensionPointReaderExtension.class);
		IStatus status = reader.read();
		assertEquals(IStatus.OK, status.getSeverity());
		assertEquals(
				Arrays.asList(new ExtensionPointReaderExtension[] {
						new P10ExtensionPointReaderExtensionImplementation(),
						new P5ExtensionPointReaderExtensionImplementation(),
						new ExtensionPointReaderExtensionImplementation(),
						new PNegative5ExtensionPointReaderExtensionImplementation() }), reader.getItems());
	}

	public void testReadWithFiltering() {
		ExtensionPointReader<ExtensionPointReaderExtension> reader = new ExtensionPointReader<ExtensionPointReaderExtension>(
				ID_PLUGIN, "extensionPointReaderTest", "extensionElement", ExtensionPointReaderExtension.class,
				"testFilterAttribute", "value1");
		IStatus status = reader.read();
		assertEquals(IStatus.OK, status.getSeverity());
		assertEquals(Collections.singletonList(new P5ExtensionPointReaderExtensionImplementation()), reader.getItems());
	}

}