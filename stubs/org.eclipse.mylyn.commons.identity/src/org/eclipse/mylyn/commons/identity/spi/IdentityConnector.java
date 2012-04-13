/*******************************************************************************
 * Copyright (c) 2011 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.commons.identity.spi;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.commons.identity.IIdentity;

/**
 * @author Steffen Pingel
 * @since 0.8
 * @deprecated use classes in the <code>org.eclipse.mylyn.commons.identity.ui</code> bundle instead
 */
@Deprecated
public abstract class IdentityConnector {

	public abstract ProfileImage getImage(IIdentity identity, int preferredWidth, int preferredHeight,
			IProgressMonitor monitor) throws CoreException;

	public abstract boolean supportsImageSize(int preferredWidth, int preferredHeight);

	public abstract void updateProfile(Profile profile, IProgressMonitor monitor) throws CoreException;

}
