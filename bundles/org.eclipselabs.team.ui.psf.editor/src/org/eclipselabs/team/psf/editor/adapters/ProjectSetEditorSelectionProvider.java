/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.psf.editor.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;


/**
 * @author Nirmal Sasidharan
 */
public class ProjectSetEditorSelectionProvider implements ISelectionProvider {

  private final ISelectionProvider fProvider;
  private SelectionChangedListener fListener;

  private class SelectionChangedListener implements ISelectionChangedListener {

    ListenerList fListeners = new ListenerList();

    public void selectionChanged(final SelectionChangedEvent event) {
      ISelection selection = convertFrom(event.getSelection());
      SelectionChangedEvent newEvent = new SelectionChangedEvent(ProjectSetEditorSelectionProvider.this, selection);
      Object[] listeners = this.fListeners.getListeners();
      for (Object listener : listeners) {
        ((ISelectionChangedListener) listener).selectionChanged(newEvent);
      }
    }

    public void addListener(final ISelectionChangedListener listener) {
      this.fListeners.add(listener);
    }

    public void removeListener(final ISelectionChangedListener listener) {
      this.fListeners.remove(listener);
    }

    public boolean isEmpty() {
      return this.fListeners.isEmpty();
    }
  }

  public ProjectSetEditorSelectionProvider(final ISelectionProvider provider) {
    Assert.isNotNull(provider);
    this.fProvider = provider;
  }

  public ISelection convertFrom(final ISelection selection) {

    if (selection.isEmpty()) {
      return selection;
    }

    if (selection instanceof IStructuredSelection) {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      List<IProject> projects = new ArrayList<IProject>();
      for (Iterator<?> iter = structuredSelection.iterator(); iter.hasNext();) {
        Object element = iter.next();
        if (element instanceof org.eclipselabs.team.psf.model.core.IProject) {
          projects.add(getProject(((org.eclipselabs.team.psf.model.core.IProject) element)));
        }
      }
      return new StructuredSelection(projects);
    }
    return StructuredSelection.EMPTY;
  }


  protected org.eclipse.core.resources.IProject getProject(final org.eclipselabs.team.psf.model.core.IProject project) {
    return ResourcesPlugin.getWorkspace().getRoot().getProject(project.toString());
  }


  public ISelection convertTo(final ISelection selection) {
    return selection;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public final ISelection getSelection() {
    return convertFrom(this.fProvider.getSelection());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setSelection(final ISelection selection) {
    this.fProvider.setSelection(convertTo(selection));
  }

  /**
   * {@inheritDoc}
   */
  public void addSelectionChangedListener(final ISelectionChangedListener listener) {
    if (this.fListener == null) {
      this.fListener = new SelectionChangedListener();
      this.fProvider.addSelectionChangedListener(this.fListener);
    }
    this.fListener.addListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
    if (this.fListener == null) {
      return;
    }
    this.fListener.removeListener(listener);
    if (this.fListener.isEmpty()) {
      this.fProvider.removeSelectionChangedListener(this.fListener);
      this.fListener = null;
    }
  }
}
