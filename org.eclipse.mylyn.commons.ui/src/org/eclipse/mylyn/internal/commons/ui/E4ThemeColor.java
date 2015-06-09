/*******************************************************************************
 * Copyright (c) 2015 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.internal.commons.ui;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.reflect.MethodUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.core.StatusHandler;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class E4ThemeColor {

	private static boolean loggedError = false;

	public static RGB getRGBFromCssString(String cssValue) {
		try {
			if (cssValue.startsWith("rgb(") && cssValue.endsWith(")")) { //$NON-NLS-1$ //$NON-NLS-2$
				String[] rgbValues = cssValue.substring(4, cssValue.length() - 1).split(","); //$NON-NLS-1$
				if (rgbValues.length == 3) {
					return new RGB(Integer.parseInt(rgbValues[0].trim()), Integer.parseInt(rgbValues[1].trim()),
							Integer.parseInt(rgbValues[2].trim()));
				}
			}

			throw new E4CssParseException("RGB", cssValue); //$NON-NLS-1$
		} catch (NumberFormatException | E4CssParseException e) {
			logOnce(e);
			return null;
		}
	}

	public static String getCssValueFromTheme(Display display, String value) {
		// use reflection so that this can build against Eclipse 3.x
		BundleContext context = FrameworkUtil.getBundle(E4ThemeColor.class).getBundleContext();
		try {
			Object reference = MethodUtils.invokeMethod(context, "getServiceReference", //$NON-NLS-1$
					"org.eclipse.e4.ui.css.swt.theme.IThemeManager"); //$NON-NLS-1$
			if (reference != null) {
				Object iThemeManager = MethodUtils.invokeMethod(context, "getService", reference); //$NON-NLS-1$
				if (iThemeManager != null) {
					Object themeEngine = MethodUtils.invokeMethod(iThemeManager, "getEngineForDisplay", display); //$NON-NLS-1$
					Shell shell = display.getActiveShell();
					if (themeEngine != null && shell != null) {
						Object shellStyle = MethodUtils.invokeMethod(themeEngine, "getStyle", shell); //$NON-NLS-1$

						if (shellStyle instanceof CSSStyleDeclaration) {
							CSSValue cssValue = ((CSSStyleDeclaration) shellStyle).getPropertyCSSValue(value);
							if (cssValue != null) {
								return cssValue.getCssText();
							}
						}
					}
				}
			}
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			logOnce(e);
			return null;
		}

		return null;
	}

	private static void logOnce(Exception e) {
		if (!loggedError) {
			StatusHandler.log(new Status(IStatus.ERROR, CommonsUiConstants.ID_PLUGIN, e.getMessage(), e));
			loggedError = true;
		}
	}
}
