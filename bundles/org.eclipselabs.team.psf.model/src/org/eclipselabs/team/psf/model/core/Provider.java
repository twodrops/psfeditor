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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Nirmal Sasidharan
 */
public class Provider implements IProvider {


  private final String id;
  private final Set<Project> projects = new LinkedHashSet<Project>();
  private final IProjectSet projectSet;
  private static final int DEFAULT_INDEX = 2;


  /**
   * @param projectSet
   * @param id2
   */
  public Provider(final IProjectSet projectSet, final String id) {
    this.projectSet = projectSet;
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Project> getProjects() {
    return this.projects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return this.id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectName(final List<String> references) {
    return references.get(DEFAULT_INDEX);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Provider other = (Provider) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    }
    else if (!this.id.equals(other.id)) {
      return false;
    }
    return true;
  }


}
