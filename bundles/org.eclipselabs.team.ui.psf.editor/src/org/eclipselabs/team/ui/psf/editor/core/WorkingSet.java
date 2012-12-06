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
import java.util.Set;


/**
 * @author Nirmal Sasidharan
 */
public class WorkingSet implements IWorkingSet {

  private String editPageId;
  private String id;
  private String label;
  private String name;
  private final IProjectSet projectSet;
  private final Set<IWorkingSetItem> workingSetItems = new HashSet<IWorkingSetItem>();


  /**
   * @param projectSet
   * @param id
   */
  public WorkingSet(final IProjectSet projectSet, final String id, final String editPageId, final String label,
      final String name) {
    this.projectSet = projectSet;
    this.editPageId = editPageId;
    this.label = label;
    this.name = name;
    this.id = id;
    // this.projectSet.getWorkingSets().add(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEditPageId() {
    return this.editPageId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEditPageId(final String editPageId) {
    this.editPageId = editPageId;
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
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLabel() {
    return this.label;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLabel(final String label) {
    this.label = label;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IWorkingSetItem> getWorkingSetItems() {
    return this.workingSetItems;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.editPageId == null) ? 0 : this.editPageId.hashCode());
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.label == null) ? 0 : this.label.hashCode());
    result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
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
    WorkingSet other = (WorkingSet) obj;
    if (this.editPageId == null) {
      if (other.editPageId != null) {
        return false;
      }
    }
    else if (!this.editPageId.equals(other.editPageId)) {
      return false;
    }
    // if (this.id == null) {
    // if (other.id != null) {
    // return false;
    // }
    // }
    // else if (!this.id.equals(other.id)) {
    // return false;
    // }
    if (this.label == null) {
      if (other.label != null) {
        return false;
      }
    }
    else if (!this.label.equals(other.label)) {
      return false;
    }
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    }
    else if (!this.name.equals(other.name)) {
      return false;
    }
    return true;
  }


}
