/*******************************************************************************
 * Copyright (c) 2004, 2010 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.commons.core;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * @since 3.0
 * @author Steffen Pingel
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class CoreUtil {

	/**
	 * @since 3.0
	 */
	public static final boolean TEST_MODE;

	static {
		String application = System.getProperty("eclipse.application", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (application.length() > 0) {
			TEST_MODE = application.endsWith("testapplication") || application.endsWith("uitest"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			// eclipse 3.3 does not the eclipse.application property
			String commands = System.getProperty("eclipse.commands", ""); //$NON-NLS-1$ //$NON-NLS-2$
			TEST_MODE = commands.contains("testapplication\n"); //$NON-NLS-1$
		}
	}

	private static final String FRAMEWORK_VERSION = "3.7.1"; //$NON-NLS-1$

	/**
	 * Returns a string representation of <code>object</code>. If object is a map or array the returned string will
	 * contains a comma separated list of contained elements.
	 * 
	 * @since 3.4
	 */
	public static String toString(Object object) {
		StringBuilder sb = new StringBuilder();
		toString(sb, object);
		return sb.toString();
	}

	private static void toString(StringBuilder sb, Object object) {
		if (object instanceof Object[]) {
			sb.append("["); //$NON-NLS-1$
			Object[] entries = (Object[]) object;
			boolean prependSeparator = false;
			for (Object entry : entries) {
				if (prependSeparator) {
					sb.append(", "); //$NON-NLS-1$
				}
				toString(sb, entry);
				prependSeparator = true;
			}
			sb.append("]"); //$NON-NLS-1$
		} else if (object instanceof Map<?, ?>) {
			sb.append("{"); //$NON-NLS-1$
			boolean prependSeparator = false;
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
				if (prependSeparator) {
					sb.append(", "); //$NON-NLS-1$
				}
				toString(sb, entry.getKey());
				sb.append("="); //$NON-NLS-1$
				toString(sb, entry.getValue());
				prependSeparator = true;
			}
			sb.append("}"); //$NON-NLS-1$
		} else {
			sb.append(object);
		}
	}

	/**
	 * Returns the version of the bundle.
	 * 
	 * @since 3.7
	 */
	// TODO e3.5 remove this method and replace with bundle.getVersion()
	public static Version getVersion(Bundle bundle) {
		String header = (String) bundle.getHeaders().get("Bundle-Version"); //$NON-NLS-1$
		return (header != null) ? Version.parseVersion(header) : null;
	}

	/**
	 * Returns true, if <code>o1</code> is equal to <code>o2</code> or <code>o1</code> and <code>o2</code> are
	 * <code>null</code>.
	 * 
	 * @see Object#equals(Object)
	 * @since 3.7
	 */
	public static boolean areEqual(Object o1, Object o2) {
		if (o1 == null) {
			return (o2 == null);
		} else {
			return o1.equals(o2);
		}
	}

	/**
	 * Compares <code>o1</code> and <code>o2</code>.
	 * 
	 * @since 3.7
	 * @return a negative integer, 0, or a positive, if o1 is less than o2, o1 equals o2 or o1 is more than o2; null is
	 *         considered less than any value
	 */
	public static <T> int compare(Comparable<T> o1, T o2) {
		if (o1 == null) {
			return (o2 != null) ? 1 : 0;
		} else if (o2 == null) {
			return -1;
		}
		return o1.compareTo(o2);
	}

	/**
	 * Compares a boolean value.
	 * 
	 * @since 3.7
	 * @see Boolean#equals(Object)
	 */
	public static boolean propertyEquals(boolean value, Object expectedValue) {
		return (expectedValue == null) ? value == true : Boolean.valueOf(value).equals(expectedValue);
	}

	/**
	 * Disables logging through the Apache commons logging system by default. This can be overridden by specifying the
	 * <code>org.apache.commons.logging.Log</code> system property.
	 * 
	 * @since 3.7
	 */
	public static void initializeLoggingSettings() {
		defaultSystemProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Only sets system property if they are not already set to a value.
	 */
	private static void defaultSystemProperty(String key, String defaultValue) {
		if (System.getProperty(key) == null) {
			System.setProperty(key, defaultValue);
		}
	}

	/**
	 * Returns the version of the Java runtime.
	 * 
	 * @since 3.7
	 * @return {@link Version#emptyVersion} if the version can not be determined
	 */
	public static Version getRuntimeVersion() {
		Version result = parseRuntimeVersion(System.getProperty("java.runtime.version")); //$NON-NLS-1$
		if (result == Version.emptyVersion) {
			result = parseRuntimeVersion(System.getProperty("java.version")); //$NON-NLS-1$
		}
		return result;
	}

	private static Version parseRuntimeVersion(String versionString) {
		if (versionString != null) {
			int firstSeparator = versionString.indexOf('.');
			if (firstSeparator != -1) {
				try {
					int secondSeparator = versionString.indexOf('.', firstSeparator + 1);
					if (secondSeparator != -1) {
						int index = findLastNumberIndex(versionString, secondSeparator);
						String qualifier = versionString.substring(index + 1);
						if (qualifier.startsWith("_") && qualifier.length() > 1) { //$NON-NLS-1$
							versionString = versionString.substring(0, index + 1) + "." + qualifier.substring(1); //$NON-NLS-1$
						} else {
							versionString = versionString.substring(0, index + 1);
						}
						return new Version(versionString);
					}
					return new Version(versionString.substring(0,
							findLastNumberIndex(versionString, firstSeparator) + 1));
				} catch (IllegalArgumentException e) {
					// ignore
				}
			}
		}
		return Version.emptyVersion;
	}

	private static int findLastNumberIndex(String versionString, int secondSeparator) {
		int lastDigit = secondSeparator;
		for (int i = secondSeparator + 1; i < versionString.length(); i++) {
			if (Character.isDigit(versionString.charAt(i))) {
				lastDigit++;
			} else {
				break;
			}
		}
		if (lastDigit == secondSeparator) {
			return secondSeparator - 1;
		}
		return lastDigit;
	}

	/**
	 * Returns the running Mylyn version without the qualifier.
	 * 
	 * @since 3.7
	 */
	public static Version getFrameworkVersion() {
		return new Version(FRAMEWORK_VERSION);
	}

	/**
	 * Returns a representation of <code>name</code> that is a valid file name.
	 * 
	 * @since 3.7
	 */
	public static String asFileName(String name) {
		StringBuffer sb = new StringBuffer(name.length());
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if (c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '.') {
				sb.append(c);
			} else {
				sb.append("%" + Integer.toHexString(c).toUpperCase()); //$NON-NLS-1$
			}
		}
		return sb.toString();
	}

}
