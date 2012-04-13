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

import org.eclipse.mylyn.commons.identity.IProfileImage;

/**
 * @author Steffen Pingel
 * @since 0.8
 * @deprecated use classes in the <code>org.eclipse.mylyn.commons.identity.ui</code> bundle instead
 */
@Deprecated
public final class ProfileImage implements IProfileImage, Serializable {

	private static final long serialVersionUID = 8211724823497362719L;

	byte[] data;

	int width;

	int height;

	String format;

	long timestamp;

	public ProfileImage(byte[] data, int width, int height, String format) {
		this.data = data;
		this.width = width;
		this.height = height;
		this.format = format;
		this.timestamp = System.currentTimeMillis();
	}

	public byte[] getData() {
		return data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getFormat() {
		return format;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
