/*******************************************************************************
 * Copyright (c) 2004, 2011 Tasktop Technologies and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.commons.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ProgressMonitorWrapper;

/**
 * Delegates to all attached monitors.
 * 
 * @author Steffen Pingel
 * @author Thomas Ehrnhoefer
 * @author Robert Elves
 * @since 3.2
 */
public class DelegatingProgressMonitor implements IDelegatingProgressMonitor {

	/**
	 * Returns the parent delegating progress monitor of <code>monitor</code>.
	 * 
	 * @param monitor
	 *            the child monitor
	 * @return the monitor; null, if none
	 * @since 3.5
	 */
	public static IDelegatingProgressMonitor getMonitorFrom(IProgressMonitor monitor) {
		if (monitor == null) {
			return null;
		} else if (monitor instanceof IDelegatingProgressMonitor) {
			return (IDelegatingProgressMonitor) monitor;
		} else if (monitor instanceof ProgressMonitorWrapper) {
			return getMonitorFrom(((ProgressMonitorWrapper) monitor).getWrappedProgressMonitor());
		}
		return null;
	}

	private boolean calledBeginTask;

	private boolean canceled;

	private Object data;

	private boolean done;

	private double internalWorked;

	private final List<IProgressMonitor> monitors;

	private String subTaskName;

	private String taskName;

	private int totalWork;

	private int worked;

	public DelegatingProgressMonitor() {
		monitors = new CopyOnWriteArrayList<IProgressMonitor>();
	}

	public void attach(IProgressMonitor monitor) {
		Assert.isNotNull(monitor);
		if (calledBeginTask) {
			monitor.beginTask(taskName, totalWork);
		}
		if (taskName != null) {
			monitor.setTaskName(taskName);
		}
		if (subTaskName != null) {
			monitor.subTask(subTaskName);
		}
		if (worked > 0) {
			monitor.worked(worked);
		}
		if (internalWorked > 0) {
			monitor.internalWorked(internalWorked);
		}
		if (canceled) {
			monitor.setCanceled(canceled);
		}
		if (done) {
			monitor.done();
		}
		monitors.add(monitor);
	}

	public void beginTask(String name, int totalWork) {
		if (!calledBeginTask) {
			this.taskName = name;
			this.totalWork = totalWork;
			this.calledBeginTask = true;
		}
		for (IProgressMonitor monitor : monitors) {
			monitor.beginTask(name, totalWork);
		}
	}

	public void detach(IProgressMonitor monitor) {
		monitors.remove(monitor);
	}

	public void done() {
		this.done = true;
		for (IProgressMonitor monitor : monitors) {
			monitor.done();
		}
	}

	/**
	 * @see IDelegatingProgressMonitor#getData()
	 * @since 3.5
	 */
	public Object getData() {
		return data;
	}

	public void internalWorked(double work) {
		this.internalWorked += work;
		for (IProgressMonitor monitor : monitors) {
			monitor.internalWorked(work);
		}
	}

	public boolean isCanceled() {
		boolean canceled = false;
		for (IProgressMonitor monitor : monitors) {
			canceled |= monitor.isCanceled();
		}
		if (canceled) {
			setCanceled(canceled);
		}
		return canceled;
	}

	public void setCanceled(boolean value) {
		this.canceled = value;
		for (IProgressMonitor monitor : monitors) {
			monitor.setCanceled(value);
		}
	}

	/**
	 * @see IDelegatingProgressMonitor#setData()
	 * @since 3.5
	 */
	public void setData(Object o) {
		this.data = o;
	}

	public void setTaskName(String name) {
		this.taskName = name;
		for (IProgressMonitor monitor : monitors) {
			monitor.setTaskName(name);
		}
	}

	public void subTask(String name) {
		this.subTaskName = name;
		for (IProgressMonitor monitor : monitors) {
			monitor.subTask(name);
		}
	}

	public void worked(int work) {
		this.worked += work;
		for (IProgressMonitor monitor : monitors) {
			monitor.worked(work);
		}
	}

}
