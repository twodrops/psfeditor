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

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;
import org.eclipselabs.team.ui.psf.editor.importer.ProjectImporter;



/**
 * @author Nirmal Sasidharan
 */
public class ProjectSetPage extends FormPage {

  /**
   * @param editor
   */
  public ProjectSetPage(final FormEditor editor) {
    super(editor, "projectSet", null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createFormContent(final IManagedForm managedForm) {

    super.createFormContent(managedForm);

    final FormToolkit toolkit = managedForm.getToolkit();
    ScrolledForm form = managedForm.getForm();
    form.setText("Project Set");
    toolkit.decorateFormHeading(form.getForm());

    fillbody(managedForm, toolkit, form);
    ((ProjectSetEditor) getEditor()).addForm(managedForm);

    createToolbar(toolkit, form);

    form.updateToolBar();

  }


  /**
   * @param toolkit
   * @param form
   */
  private void createToolbar(final FormToolkit toolkit, final ScrolledForm form) {
    ControlContribution setAsTarget = new ControlContribution("Set") { //$NON-NLS-1$

          @Override
          protected Control createControl(final Composite parent) {
            final ImageHyperlink hyperlink = new ImageHyperlink(parent, SWT.NONE);
            hyperlink.setText("Import ProjectSet");
            hyperlink.setUnderlined(true);
            hyperlink.setForeground(toolkit.getHyperlinkGroup().getForeground());
            hyperlink.addHyperlinkListener(new IHyperlinkListener() {

              @Override
              public void linkActivated(final HyperlinkEvent e) {
                try {
                  ProjectImporter.INSTANCE.doImport(((FileEditorInput) getEditorInput()).getFile().getRawLocation()
                      .toOSString());
                }
                catch (ProjectSetException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
                }
              }

              @Override
              public void linkEntered(final HyperlinkEvent e) {
                hyperlink.setForeground(toolkit.getHyperlinkGroup().getActiveForeground());
              }

              @Override
              public void linkExited(final HyperlinkEvent e) {
                hyperlink.setForeground(toolkit.getHyperlinkGroup().getForeground());
              }
            });
            return hyperlink;
          }
        };

    form.getToolBarManager().add(setAsTarget);
  }


  /**
   * @param managedForm
   * @param toolkit
   * @param form
   */
  private void fillbody(final IManagedForm managedForm, final FormToolkit toolkit, final ScrolledForm form) {

    Composite body = form.getBody();
    FillLayout bodyLayout = new FillLayout();
    bodyLayout.marginHeight = 5;
    bodyLayout.marginWidth = 5;
    body.setLayout(bodyLayout);
    managedForm.addPart(new ProjectSection(this, body));

  }

  /**
   * @return
   */
  public IProjectSet getProjectSet() {
    return ((ProjectSetEditor) getEditor()).getProjectSet();
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.forms.editor.FormPage#canLeaveThePage()
   */
  @Override
  public boolean canLeaveThePage() {
    ((ProjectSetEditor) getEditor()).setDirty(isDirty());
    return true;
  }


}
