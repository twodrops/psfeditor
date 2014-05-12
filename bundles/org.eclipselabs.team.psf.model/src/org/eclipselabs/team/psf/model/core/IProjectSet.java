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
package org.eclipselabs.team.psf.model.core;

import java.util.Set;

/**
 * Defines a Project Set. A Project Set is a collection of projects and working sets which are part of a team repository
 * 
 * @author Nirmal Sasidharan
 */
public interface IProjectSet {

  /**
   * @return the version
   */
  public String getVersion();

  /**
   * @param version the version to set
   */
  public void setVersion(final String version);

  /**
   * @return the providers
   */
  public Set<IProvider> getProviders();

  /**
   * @param id
   * @return
   */
  public IProvider getProvider(final String id);

  /**
   * @return the projects
   */
  public Set<IProject> getProjects();

  /**
   * @return the workingSets
   */
  public Set<IWorkingSet> getWorkingSets();

}