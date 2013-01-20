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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipselabs.team.ui.psf.editor.change.service.IContentChangedListener;
import org.eclipselabs.team.ui.psf.editor.change.service.IContentDelta;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;
import org.eclipselabs.team.ui.psf.editor.reader.ProjectSetHandler;

/**
 * Section for adding and removing projects in Project Set Editor
 * 
 * @see ProjectSetPage
 * @see ProjectSetEditor
 * @author Nirmal Sasidharan
 */
public class ProjectSection extends SectionPart implements IContentChangedListener {

  /**
   * The editor instance
   */
  private final ProjectSetEditor projectSetEditor;

  /**
   * Table Viewer to display projects in Project Set
   */
  private TableViewer projectsViewer;

  /**
   * Button to add new projects to Project Set
   */
  private Button addButton;

  /**
   * Button to remove new projects from Project Set
   */
  private Button removeButton;

  /**
   * Content Provider for Project Set
   */
  class ProjectSetContentProvider implements IStructuredContentProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
      // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
      // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getElements(final Object inputElement) {
      Assert.isTrue((inputElement instanceof IProjectSet),
          ProjectSetEditorMessages.ProjectSection_ContentProvider_assertion);
      return ((IProjectSet) inputElement).getProjects().toArray();
    }
  }

  /**
   * @param page {@link ProjectSetPage}
   * @param parent {@link ProjectSetPage} body
   */
  public ProjectSection(final FormPage page, final Composite parent) {

    super(parent, page.getManagedForm().getToolkit(), Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
    this.projectSetEditor = (ProjectSetEditor) page.getEditor();

    ProjectSetHandler.INSTANCE.addContentChangedListener(this);

    createClient(getSection(), page.getEditor().getToolkit());

    setInput();
  }

  /**
   * @return The project set model model backing this editor
   */
  private IProjectSet getProjectSet() {
    return this.projectSetEditor.getProjectSet();

  }

  /**
   * Sets input to projectsViewer
   */
  protected void setInput() {
    this.projectsViewer.setInput(getProjectSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refresh() {
    // TODO: Setting input to viewer here ends up it multiple input setting under certain cases. Hence removing. This
    // creates issue #6
    // setInput();
    super.refresh();
  }

  /**
   * Creates the UI for this section.
   * 
   * @param section section the UI is being added to
   * @param toolkit form toolkit used to create the widgets
   */
  protected void createClient(final Section section, final FormToolkit toolkit) {

    Composite client = toolkit.createComposite(section);
    GridLayoutFactory.fillDefaults().extendedMargins(2, 2, 2, 2).numColumns(2).applyTo(client);
    section.setText(ProjectSetEditorMessages.ProjectSection_title);
    section.setDescription(ProjectSetEditorMessages.ProjectSection_description);

    createProjectsViewer(toolkit, client);

    createButtons(toolkit, client);

    toolkit.paintBordersFor(client);
    section.setClient(client);
  }

  /**
   * Creates Projects Viewers which displays projects within PSF file
   * 
   * @param toolkit toolkit form toolkit used to create the widgets
   * @param parent client composite
   * @return created ProjectsViewer
   */
  protected TableViewer createProjectsViewer(final FormToolkit toolkit, final Composite parent) {
    this.projectsViewer = new TableViewer(toolkit.createTable(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
    GridDataFactory.fillDefaults().grab(true, true).hint(SWT.NONE, 0).applyTo(this.projectsViewer.getControl());
    this.projectsViewer.setContentProvider(new ProjectSetContentProvider());
    this.projectsViewer.setLabelProvider(new WorkbenchLabelProvider());
    this.projectsViewer.getTable().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);

    this.projectsViewer.getTable().addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(final KeyEvent e) {
        if ((e.keyCode == SWT.DEL) && ProjectSection.this.removeButton.getEnabled()) {
          handleRemove();
        }

      }
    });

    this.projectsViewer.addSelectionChangedListener(new ISelectionChangedListener() {

      @Override
      public void selectionChanged(final SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        ProjectSection.this.removeButton.setEnabled(selection.size() > 0);

      }
    });
    return this.projectsViewer;
  }

  /**
   * Creates buttons which help add/remove projects from PSF file
   * 
   * @param toolkit form toolkit used to create the widgets
   * @param client client composite
   */
  private void createButtons(final FormToolkit toolkit, final Composite client) {

    Composite buttonComposite = toolkit.createComposite(client, SWT.NONE);
    GridLayoutFactory.fillDefaults().numColumns(1).margins(5, 5).applyTo(buttonComposite);
    GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(buttonComposite);

    this.addButton =
        toolkit.createButton(buttonComposite, ProjectSetEditorMessages.ProjectSection_AddButton_name, SWT.PUSH);
    GridDataFactory.fillDefaults().hint(90, SWT.DEFAULT).applyTo(this.addButton);
    this.addButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        handleAdd();
      }
    });

    this.removeButton =
        toolkit.createButton(buttonComposite, ProjectSetEditorMessages.ProjectSection_RemoveButton_name, SWT.PUSH);
    GridDataFactory.fillDefaults().grab(true, false).hint(90, SWT.DEFAULT).applyTo(this.removeButton);
    this.removeButton.setEnabled(false);

    this.removeButton.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(final SelectionEvent e) {
        handleRemove();
      }
    });

  }

  /**
   * Handles adding of new projects to project set
   */
  private void handleAdd() {

    try {

      Set<IProject> differenceProjects = getProjectsToCreate(getProjectSet());

      ListSelectionDialog selectionDialog =
          new ListSelectionDialog(this.projectsViewer.getTable().getShell(), differenceProjects,
              new ArrayContentProvider(), new WorkbenchLabelProvider(),
              ProjectSetEditorMessages.ProjectSection_ProjectSelectionDialog_title);

      if (selectionDialog.open() == Window.OK) {

        Object[] result = selectionDialog.getResult();

        if (result != null) {

          IProject[] selectedProjects = Arrays.copyOf(result, result.length, IProject[].class);
          ProjectSetHandler.INSTANCE.createProjects(getProjectSet(), selectedProjects);
          ProjectSetHandler.INSTANCE.createWorkingSets(getProjectSet(), selectedProjects);
        }
      }
    }
    catch (ProjectSetException e) {
      this.projectSetEditor.showError(ProjectSetEditorMessages.ProjectSection_ProjectAdd_error, e);
    }

  }

  /**
   * Compares the project set model to workspace projects and returns the missing projects in project set
   * 
   * @param projectSet project set model
   * @return returns the difference {@link IProject} in project set
   * @throws ProjectSetException exceptions during checking projects in workspace
   */
  protected Set<IProject> getProjectsToCreate(final IProjectSet projectSet) throws ProjectSetException {

    Set<IProject> projectsToCreate = new HashSet<IProject>();
    Set<String> projectsInProjectSet = new HashSet<String>();

    // TODO: Change equals() of Project to do the matching
    for (org.eclipselabs.team.ui.psf.editor.core.IProject project : projectSet.getProjects()) {
      projectsInProjectSet.add(project.toString());
    }

    IResource[] projectsInWorkspace = null;
    try {
      projectsInWorkspace = ResourcesPlugin.getWorkspace().getRoot().members();
    }
    catch (CoreException e) {
      throw new ProjectSetException(e);
    }

    if (projectsInWorkspace != null) {
      for (IResource member : projectsInWorkspace) {
        if (member instanceof IProject) {
          if (projectsInProjectSet.add(member.getName())) {
            projectsToCreate.add((IProject) member);
          }
        }
      }
    }

    return projectsToCreate;
  }

  /**
   * Handles removing of projects from project set
   */
  @SuppressWarnings("unchecked")
  private void handleRemove() {

    IStructuredSelection selection = (IStructuredSelection) this.projectsViewer.getSelection();

    if (!selection.isEmpty()) {
      ProjectSetHandler.INSTANCE.removeProjects(getProjectSet(),
          new HashSet<org.eclipselabs.team.ui.psf.editor.core.IProject>(selection.toList()));
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void contentChanged(final IContentDelta contentDelta) {

    Object content = contentDelta.getObject();

    if ((content != null) && (content instanceof Set<?>)) {
      @SuppressWarnings("unchecked")
      Set<IProjectSet> delta = (Set<IProjectSet>) content;
      if (delta.size() > 0) {
        this.projectsViewer.refresh();
        if (contentDelta.getType() == IContentDelta.ADDED) {
          this.projectsViewer.setSelection(new StructuredSelection(delta.toArray()));
        }
        ProjectSection.this.projectSetEditor.setDirty(true);
      }

    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    super.dispose();
    ProjectSetHandler.INSTANCE.removeContentChangedListener(this);
  }

}
