/*******************************************************************************
 * Copyright (c) 2010 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

package org.eclipse.mylyn.commons.ui.team;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylyn.internal.commons.ui.team.Messages;
import org.eclipse.mylyn.internal.commons.ui.team.TeamUiPlugin;
import org.eclipse.mylyn.internal.commons.ui.team.wizards.NewRepositoryWizard;
import org.eclipse.mylyn.internal.provisional.commons.ui.dialogs.ValidatableWizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.LegacyResourceSupport;
import org.eclipse.ui.internal.util.Util;

/**
 * @author Steffen Pingel
 * @deprecated use classes in the <code>org.eclipse.mylyn.commons.repositories.ui</code> bundle instead
 */
@Deprecated
public final class RepositoryUi {

	/**
	 * The wizard dialog width.
	 */
	private static final int SIZING_WIZARD_WIDTH = 500;

	/**
	 * The wizard dialog height.
	 */
	private static final int SIZING_WIZARD_HEIGHT = 500;

	public static final String ID_VIEW_REPOSITORIES = "org.eclipse.mylyn.commons.team.navigator.Repositories"; //$NON-NLS-1$

	private RepositoryUi() {
	}

	public static int openNewRepositoryDialog(IWorkbenchWindow workbenchWindow, String categoryId) {
		NewRepositoryWizard wizard = new NewRepositoryWizard();
		wizard.setCategoryId(categoryId);
		wizard.setWindowTitle(Messages.NewRepositoryHandler_New_Repository);

		ISelection selection = workbenchWindow.getSelectionService().getSelection();
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = (IStructuredSelection) selection;
		} else {
			// @issue the following is resource-specific legacy code
			// Build the selection from the IFile of the editor
			Class resourceClass = LegacyResourceSupport.getResourceClass();
			if (resourceClass != null) {
				IWorkbenchPart part = workbenchWindow.getPartService().getActivePart();
				if (part instanceof IEditorPart) {
					IEditorInput input = ((IEditorPart) part).getEditorInput();
					Object resource = Util.getAdapter(input, resourceClass);
					if (resource != null) {
						selectionToPass = new StructuredSelection(resource);
					}
				}
			}
		}

		wizard.init(workbenchWindow.getWorkbench(), selectionToPass);

		IDialogSettings workbenchSettings = TeamUiPlugin.getDefault().getDialogSettings();
		IDialogSettings wizardSettings = workbenchSettings.getSection("NewWizardAction"); //$NON-NLS-1$
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings.addNewSection("NewWizardAction"); //$NON-NLS-1$
		}
		wizard.setDialogSettings(wizardSettings);
		wizard.setForcePreviousAndNextButtons(true);

		Shell parent = workbenchWindow.getShell();
		ValidatableWizardDialog dialog = new ValidatableWizardDialog(parent, wizard);
		dialog.create();
		dialog.getShell().setSize(Math.max(SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x), SIZING_WIZARD_HEIGHT);
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(), IWorkbenchHelpContextIds.NEW_WIZARD);
		return dialog.open();
	}

}
