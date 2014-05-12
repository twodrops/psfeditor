/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.ui.psf.editor.adapters;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipselabs.team.psf.model.core.IProject;
import org.eclipselabs.team.psf.model.core.Project;


/**
 * @author sni1si
 */
public class ProjectSetAdapterFactory implements IAdapterFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getAdapter(final Object adaptableObject, final Class adapterType) {

    if (adapterType.equals(IProject.class)) {
      return adaptableObject;
    }

    org.eclipse.core.resources.IProject project = getProject((Project) adaptableObject);

    if (adapterType.equals(IWorkbenchAdapter.class)) {
      return Platform.getAdapterManager().getAdapter(project, IWorkbenchAdapter.class);
    }
    else if (adapterType.equals(IResource.class) || adapterType.equals(org.eclipse.core.resources.IProject.class)) {
      return project;
    }
    return null;
  }

  protected org.eclipse.core.resources.IProject getProject(final Project project) {
    return ResourcesPlugin.getWorkspace().getRoot().getProject(project.toString());
  }

  @Override
  public Class[] getAdapterList() {
    return new Class[] { org.eclipse.core.resources.IProject.class, IResource.class, IProject.class };
  }

}
