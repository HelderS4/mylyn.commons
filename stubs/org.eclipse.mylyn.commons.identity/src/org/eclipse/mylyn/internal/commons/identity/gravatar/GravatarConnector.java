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

package org.eclipse.mylyn.internal.commons.identity.gravatar;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.identity.Account;
import org.eclipse.mylyn.commons.identity.IIdentity;
import org.eclipse.mylyn.commons.identity.spi.IdentityConnector;
import org.eclipse.mylyn.commons.identity.spi.Profile;
import org.eclipse.mylyn.commons.identity.spi.ProfileImage;

/**
 * @author Steffen Pingel
 * @deprecated use classes in the <code>org.eclipse.mylyn.commons.identity.ui</code> bundle instead
 */
@Deprecated
public class GravatarConnector extends IdentityConnector {

	public static final String KIND = "org.eclipse.mylyn.commons.identity.gravatar"; //$NON-NLS-1$

	private final GravatarStore store;

	private final int DEFAULT_SIZE = 80;

	private final Map<String, Long> noImageHashByTimeStamp = new ConcurrentHashMap<String, Long>();

	public GravatarConnector() {
		this.store = new GravatarStore();
	}

	@Override
	public ProfileImage getImage(IIdentity identity, int preferredWidth, int preferredHeight, IProgressMonitor monitor)
			throws CoreException {
		String id = getHash(identity);
		if (id == null) {
			return null;
		}

		// avoid retrieving image again i
		if (noImageHashByTimeStamp.containsKey(id)) {
			return null;
		}

		// store id for future retrieval
		identity.addAccount(Account.id(id).kind(KIND));

		int size = getSize(preferredWidth);
		Gravatar gravatar;
		try {
			gravatar = store.loadGravatarByHash(id, size, null);
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, KIND, e.getMessage(), e));
		}

		if (gravatar != null) {
			return new ProfileImage(gravatar.getBytes(), size, size, "jpg"); //$NON-NLS-1$
		} else {
			noImageHashByTimeStamp.put(id, Long.valueOf(System.currentTimeMillis()));
		}
		return null;
	}

	private int getSize(int preferredSize) {
		if (preferredSize < 1 && preferredSize > 512) {
			return DEFAULT_SIZE;
		}
		return preferredSize;
	}

	@Override
	public boolean supportsImageSize(int preferredWidth, int preferredHeight) {
		return (preferredWidth >= 1 && preferredWidth <= 512 && preferredHeight >= 1 && preferredHeight <= 512 && preferredWidth == preferredHeight);
	}

	@Override
	public void updateProfile(Profile profile, IProgressMonitor monitor) throws CoreException {
		// TODO retrieve Gravatar profile information
	}

	private String getHash(IIdentity identity) {
		Account account = identity.getAccountByKind(KIND);
		if (account != null) {
			if (GravatarUtils.isValidHash(account.getId())) {
				return account.getId();
			}
		}

		for (String alias : identity.getAliases()) {
			if (GravatarUtils.isValidEmail(alias)) {
				return GravatarUtils.getHash(alias);
			}
		}

		return null;
	}

}
