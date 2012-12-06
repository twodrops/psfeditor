/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.ui.psf.editor.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * The "New" wizard page allows setting the container for the new file as well as the file name. The page will only
 * accept file name without the extension OR with the extension that matches the expected one (psf).
 */

public class ProjectSetImportWizardPage extends WizardPage {

  private Text containerText;

  private Text fileText;

  private final ISelection selection;

  /**
   * Constructor for SampleNewWizardPage.
   * 
   * @param pageName
   */
  public ProjectSetImportWizardPage(final ISelection selection) {
    super("wizardPage");
    setTitle("ProjectSet");
    setDescription("Create a new projectset..");
    this.selection = selection;
  }

  /**
   * @see IDialogPage#createControl(Composite)
   */
  public void createControl(final Composite parent) {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 3;
    layout.verticalSpacing = 9;
    Label label = new Label(container, SWT.NULL);
    label.setText("&Project:");

    this.containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    this.containerText.setLayoutData(gd);
    this.containerText.addModifyListener(new ModifyListener() {

      public void modifyText(final ModifyEvent e) {
        dialogChanged();
      }
    });

    Button button = new Button(container, SWT.PUSH);
    button.setText("Browse...");
    button.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        handleBrowse();
      }
    });
    label = new Label(container, SWT.NULL);
    label.setText("&File name:");

    this.fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
    gd = new GridData(GridData.FILL_HORIZONTAL);
    this.fileText.setLayoutData(gd);
    this.fileText.addModifyListener(new ModifyListener() {

      public void modifyText(final ModifyEvent e) {
        dialogChanged();
      }
    });
    initialize();
    dialogChanged();
    setControl(container);
  }

  /**
   * Tests if the current workbench selection is a suitable container to use.
   */

  private void initialize() {
    if ((this.selection != null) && (this.selection.isEmpty() == false) &&
        (this.selection instanceof IStructuredSelection)) {
      IStructuredSelection ssel = (IStructuredSelection) this.selection;
      if (ssel.size() > 1) {
        return;
      }
      Object obj = ssel.getFirstElement();
      if (obj instanceof IResource) {
        IContainer container;
        if (obj instanceof IContainer) {
          container = (IContainer) obj;
        }
        else {
          container = ((IResource) obj).getParent();
        }
        this.containerText.setText(container.getFullPath().toString());
      }
    }
    this.fileText.setText("new_file.psf");
  }

  /**
   * Uses the standard container selection dialog to choose the new value for the container field.
   */

  private void handleBrowse() {
    ContainerSelectionDialog dialog =
        new ContainerSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
            "Select new file container");
    if (dialog.open() == Window.OK) {
      Object[] result = dialog.getResult();
      if (result.length == 1) {
        this.containerText.setText(((Path) result[0]).toString());
      }
    }
  }

  /**
   * Ensures that both text fields are set.
   */

  private void dialogChanged() {
    IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()));
    String fileName = getFileName();

    if (getContainerName().length() == 0) {
      updateStatus("File container must be specified");
      return;
    }
    if ((container == null) || ((container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0)) {
      updateStatus("File container must exist");
      return;
    }
    if (!container.isAccessible()) {
      updateStatus("Project must be writable");
      return;
    }
    if (fileName.length() == 0) {
      updateStatus("File name must be specified");
      return;
    }
    if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
      updateStatus("File name must be valid");
      return;
    }
    int dotLoc = fileName.lastIndexOf('.');
    if (dotLoc != -1) {
      String ext = fileName.substring(dotLoc + 1);
      if (ext.equalsIgnoreCase("psf") == false) {
        updateStatus("File extension must be \"psf\"");
        return;
      }
    }
    updateStatus(null);
  }

  private void updateStatus(final String message) {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  public String getContainerName() {
    return this.containerText.getText();
  }

  public String getFileName() {
    return this.fileText.getText();
  }
}