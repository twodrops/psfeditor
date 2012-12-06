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
package org.eclipselabs.team.ui.psf.editor.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * @author Nirmal Sasidharan
 */
public class ProjectSet implements IProjectSet {

  private String version;
  private final Set<IProvider> providers = new HashSet<IProvider>();
  private final Set<IWorkingSet> workingSets = new HashSet<IWorkingSet>();

  /*
   * (non-Javadoc)
   * @see org.eclipselabs.team.ui.psf.editor.model.IProjectSet#getVersion()
   */
  @Override
  public String getVersion() {
    return this.version;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipselabs.team.ui.psf.editor.model.IProjectSet#setVersion(java.lang.String)
   */
  @Override
  public void setVersion(final String version) {
    this.version = version;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipselabs.team.ui.psf.editor.model.IProjectSet#getProviders()
   */
  @Override
  public Set<IProvider> getProviders() {
    return this.providers;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipselabs.team.ui.psf.editor.model.IProjectSet#getProvider(java.lang.String)
   */
  @Override
  public IProvider getProvider(final String id) {

    IProvider provider = null;
    while (provider == null) {
      Iterator<IProvider> iterator = this.providers.iterator();
      IProvider next = iterator.next();
      if (next.getId().equals(id)) {
        provider = next;
      }
    }
    return provider;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipselabs.team.ui.psf.editor.model.IProjectSet#getWorkingSets()
   */
  @Override
  public Set<IWorkingSet> getWorkingSets() {
    return this.workingSets;
  }

  @Override
  public Set<IProject> getProjects() {

    Set<IProject> projects = new LinkedHashSet<IProject>();
    for (IProvider provider : getProviders()) {
      projects.addAll(provider.getProjects());
    }
    return projects;
  }


}
