/*******************************************************************************
 * Copyright (c) 2011 Tasktop Technologies.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.commons.identity.spi;

import java.io.Serializable;

import org.eclipse.mylyn.commons.identity.IIdentity;
import org.eclipse.mylyn.commons.identity.IProfile;

/**
 * @author Steffen Pingel
 * @since 0.8
 * @deprecated use classes in the <code>org.eclipse.mylyn.commons.identity.ui</code> bundle instead
 */
@Deprecated
public final class Profile implements IProfile, Serializable {

	private static final long serialVersionUID = -1079729573911113939L;

	private String city;

	private String country;

	private String email;

	private final IIdentity identity;

	private String name;

	private int timeZoneOffset;

	public Profile(IIdentity identity) {
		this.identity = identity;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getEmail() {
		return email;
	}

	public IIdentity getIdentity() {
		return identity;
	}

	public String getName() {
		return name;
	}

	public int getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimeZoneOffset(int timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

}
