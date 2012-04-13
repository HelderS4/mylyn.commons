/*******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.commons.identity.gravatar;

import java.util.regex.Pattern;

/**
 * Gravatar constants.
 * 
 * @author Kevin Sawicki (kevin@github.com)
 * @deprecated use classes in the <code>org.eclipse.mylyn.commons.identity.ui</code> bundle instead
 */
@Deprecated
public interface IGravatarConstants {

	/**
	 * URL
	 */
	String URL = "http://www.gravatar.com/avatar/"; //$NON-NLS-1$

	/**
	 * HASH_REGEX
	 */
	String HASH_REGEX = "[0-9a-f]{32}"; //$NON-NLS-1$

	/**
	 * HASH_PATTERN
	 */
	Pattern HASH_PATTERN = Pattern.compile(HASH_REGEX);

	/**
	 * HASH_LENGTH
	 */
	int HASH_LENGTH = 32;

	/**
	 * HASH_ALGORITHM
	 */
	String HASH_ALGORITHM = "MD5"; //$NON-NLS-1$

	/**
	 * Charset used for hashing
	 */
	String CHARSET = "CP1252"; //$NON-NLS-1$

}
