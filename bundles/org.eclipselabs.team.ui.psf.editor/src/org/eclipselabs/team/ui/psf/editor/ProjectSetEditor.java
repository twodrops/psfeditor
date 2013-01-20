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
package org.eclipselabs.team.ui.psf.editor;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;
import org.eclipselabs.team.ui.psf.editor.reader.ProjectSetReader;
import org.eclipselabs.team.ui.psf.editor.writer.ProjectSetWriter;


/**
 * Editor for Project Set (*.psf) files. Uses {@link IProjectSet} model to load, edit and save project set file. A
 * project viewer lists the projects available in Project Set. The editor also allows importing of files within the
 * project set
 * 
 * @author Nirmal Sasidharan
 */
public class ProjectSetEditor extends FormEditor implements IProjectSetEditor {

  private final List<IManagedForm> formPages = new ArrayList<IManagedForm>(1);
  private final ProjectSetInputHandler projectSetInputHandler = new ProjectSetInputHandler();
  private boolean dirty;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void addPages() {

    try {
      setActiveEditor(this);
      addPage(new ProjectSetPage(this));
    }
    catch (PartInitException e) {
      showError(ProjectSetEditorMessages.ProjectSetEditor_FormPage_Create_error, new ProjectSetException(e));
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  protected void setInput(final IEditorInput input) {
    super.setInput(input);
    setPartName(getEditorInput().getName());
    this.projectSetInputHandler.setInput(input);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    this.projectSetInputHandler.dispose();
    super.dispose();
  }


  /**
   * Saves the psf file.
   */
  @Override
  public void doSave(final IProgressMonitor monitor) {

    try {
      this.projectSetInputHandler.saveProjectSet(((IFileEditorInput) getEditorInput()).getFile());
    }
    catch (ProjectSetException e) {
      showError(ProjectSetEditorMessages.ProjectSetEditor_Save_error, e);
    }


  }

  /**
   * Saves the psf file as another file.
   */
  @Override
  public void doSaveAs() {

    SaveAsDialog dialog = new SaveAsDialog(getSite().getShell());
    dialog.create();
    dialog.setMessage(ProjectSetEditorMessages.ProjectSetEditor_Save_MessageBox_message, IMessageProvider.NONE);
    dialog.open();

    IPath path = dialog.getResult();

    if (path == null) {
      return;
    }
    if (!"psf".equalsIgnoreCase(path.getFileExtension())) { //$NON-NLS-1$
      path = path.addFileExtension("psf"); //$NON-NLS-1$
    }

    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IFile file = workspace.getRoot().getFile(path);

    try {
      this.projectSetInputHandler.saveProjectSet(file);
    }
    catch (ProjectSetException e) {
      showError(ProjectSetEditorMessages.ProjectSetEditor_Save_error, e);
    }

    // Opening the newly saved file
    setInput(new FileEditorInput(file));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {
    if (!(editorInput instanceof IFileEditorInput) && !(editorInput instanceof IURIEditorInput)) {
      throw new PartInitException(ProjectSetEditorMessages.ProjectSetEditor_Input_error);
    }
    super.init(site, editorInput);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSaveAsAllowed() {
    return true;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirty() {
    return this.dirty || super.isDirty();
  }

  /**
   * Set editor as dirty
   * 
   * @param dirty true to set dirty, else false
   */
  protected void setDirty(final boolean dirty) {
    this.dirty = this.dirty || dirty;
    editorDirtyStateChanged();
  }


  /**
   * Opens an error dialog using the editor's shell.
   * 
   * @param message error message to display
   * @param exception exception to display in details section
   */
  public void showError(final String message, final ProjectSetException exception) {
    Display display = getSite().getShell().getDisplay();
    display.asyncExec(new Runnable() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void run() {
        MessageDialog.openError(getSite().getShell(), ProjectSetEditorMessages.ProjectSetEditor_Error_title,
            exception.getMessage());
      }
    });
  }

  /**
   * Adds the given form to the list of forms to be refreshed when reverting
   * 
   * @param managedForm Form to be added
   */
  public void addForm(final IManagedForm managedForm) {
    this.formPages.add(managedForm);
  }

  /**
   * Handles the revert action
   */
  public void doRevert() {
    this.projectSetInputHandler.reset();
    for (IManagedForm iManagedForm : this.formPages) {
      IFormPart[] parts = iManagedForm.getParts();
      for (IFormPart part : parts) {
        if (part instanceof AbstractFormPart) {
          ((AbstractFormPart) part).markStale();
        }
      }
    }
    setActivePage(getActivePage());
    editorDirtyStateChanged();
  }

  /**
   * @return {@link IProjectSet}
   */
  @Override
  public IProjectSet getProjectSet() {

    IProjectSet projectSet = null;

    try {
      projectSet = this.projectSetInputHandler.getProjectSet();
    }
    catch (ProjectSetException e) {
      showError(ProjectSetEditorMessages.ProjectSetEditor_Load_error, e);
      close(false);
    }

    return projectSet;
  }


  /**
   * Input handler class for ProjectSet
   */
  private class ProjectSetInputHandler implements IResourceChangeListener {

    private IEditorInput editorInput;
    private IProjectSet projectSet;
    private IFile projectSetFileInWorkspace;
    // Check if saving is active
    private boolean saving = false;

    public void dispose() {
      ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    }

    public void reset() {
      setInput(this.editorInput);
    }

    /**
     * Set input to ProjectSet Handler
     * 
     * @param input editor input
     */
    public void setInput(final IEditorInput input) {
      this.editorInput = input;
      this.projectSetFileInWorkspace = null;
      this.projectSet = null;
      File projectSetFile = null;
      if (input instanceof IFileEditorInput) {
        this.projectSetFileInWorkspace = ((IFileEditorInput) input).getFile();
        projectSetFile = this.projectSetFileInWorkspace.getLocation().toFile();
      }
      else if (input instanceof IURIEditorInput) {
        String part = ((IURIEditorInput) input).getURI().getSchemeSpecificPart();
        Path path = new Path(part);
        this.projectSetFileInWorkspace = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
        projectSetFile = path.toFile();
      }

      // If the file is not available or invalid, close the target editor
      if ((this.projectSetFileInWorkspace == null) || (projectSetFile == null) || !projectSetFile.exists()) {
        close(false);
      }
      // TODO: Should this be changed to a init() method as setInput() could be called multiple times
      ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    /**
     * @return the project set that is the input to the editor
     * @throws ProjectSetException thrown during errors encounter while loading
     */
    public IProjectSet getProjectSet() throws ProjectSetException {

      if (this.projectSet == null) {
        loadProjectSet();
        ProjectSetEditor.this.dirty = false;
        editorDirtyStateChanged();
      }
      return this.projectSet;

    }

    /**
     * Loads Project set
     * 
     * @throws ProjectSetException thrown during errors encounter while loading
     */
    protected void loadProjectSet() throws ProjectSetException {
      this.editorInput = getEditorInput();
      String filename = ((FileEditorInput) this.editorInput).getFile().getRawLocation().toOSString();
      this.projectSet = ProjectSetReader.INSTANCE.load(filename);
    }

    /**
     * Save Project set to psf file
     * 
     * @param file Eclipe resource file
     * @throws ProjectSetException Thrown during errors while saving
     */
    public void saveProjectSet(final IFile file) throws ProjectSetException {
      this.saving = true;
      try {
        ProjectSetWriter.INSTANCE.save(this.projectSet, file, new NullProgressMonitor());
      }
      finally {
        this.saving = false;
      }


      ProjectSetEditor.this.dirty = false;
      editorDirtyStateChanged();
    }

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent
     * )
     */
    @Override
    public void resourceChanged(final IResourceChangeEvent event) {
      if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
        IResourceDelta delta = event.getDelta().findMember(this.projectSetFileInWorkspace.getFullPath());
        if (delta != null) {
          if (delta.getKind() == IResourceDelta.REMOVED) {
            close(false);
          }
          else if ((delta.getKind() == IResourceDelta.CHANGED) || (delta.getKind() == IResourceDelta.REPLACED)) {
            if (!this.saving) {
              Display display = getSite().getShell().getDisplay();
              display.asyncExec(new Runnable() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void run() {
                  doRevert();
                }
              });
            }
          }
        }
      }
    }


  }

}
