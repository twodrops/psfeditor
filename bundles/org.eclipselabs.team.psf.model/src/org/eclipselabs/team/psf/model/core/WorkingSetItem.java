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

import java.util.Map;


/**
 * @author Nirmal Sasidharan
 */
public class WorkingSetItem implements IWorkingSetItem {


  private final Map<String, String> properties;
  private final IWorkingSet workingSet;
  private final String factoryId;


  /**
   * 
   */
  public WorkingSetItem(final IWorkingSet workingSet, final String factoryId, final Map<String, String> properties) {
    this.workingSet = workingSet;
    this.factoryId = factoryId;
    this.properties = properties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getProperties() {
    return this.properties;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.factoryId == null) ? 0 : this.factoryId.hashCode());
    result = (prime * result) + ((this.properties == null) ? 0 : this.properties.hashCode());
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
    WorkingSetItem other = (WorkingSetItem) obj;
    if (this.factoryId == null) {
      if (other.factoryId != null) {
        return false;
      }
    }
    else if (!this.factoryId.equals(other.factoryId)) {
      return false;
    }
    if (this.properties == null) {
      if (other.properties != null) {
        return false;
      }
    }
    else if (!this.properties.equals(other.properties)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFactoryId() {
    return this.factoryId;
  }


}
