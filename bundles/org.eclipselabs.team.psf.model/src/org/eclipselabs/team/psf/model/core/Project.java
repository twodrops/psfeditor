/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.psf.model.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.PlatformObject;


/**
 * @author Nirmal Sasidharan
 */
public class Project extends PlatformObject implements IProject {

  private final IProvider provider;
  private final List<String> references = new ArrayList<String>();

  /**
   * @param provider
   */
  public Project(final IProvider provider) {
    this.provider = provider;
    // TODO: The created project is automatically added to the parent provider. This is inconsisent with other model
    // elements where addition to parent is not automatic
    this.provider.getProjects().add(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getReferences() {
    return this.references;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IProvider getProvider() {
    return this.provider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getName();
  }


  /**
   * @return
   */
  protected org.eclipse.core.resources.IProject getProject() {
    return ResourcesPlugin.getWorkspace().getRoot().getProject(getName());
  }

  /**
   * @return
   */
  protected String getName() {
    return getProvider().getProjectName(getReferences());
  }


}
