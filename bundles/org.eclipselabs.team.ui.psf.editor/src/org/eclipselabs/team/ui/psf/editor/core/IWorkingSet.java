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

import java.util.Set;


/**
 * @author Nirmal Sasidharan
 */
public interface IWorkingSet {

  /**
   * @return the editPageId
   */
  public abstract String getEditPageId();

  /**
   * @param editPageId the editPageId to set
   */
  public abstract void setEditPageId(final String editPageId);

  /**
   * @return the id
   */
  public abstract String getId();

  /**
   * @param id the id to set
   */
  public abstract void setId(final String id);

  /**
   * @return the label
   */
  public abstract String getLabel();

  /**
   * @param label the label to set
   */
  public abstract void setLabel(final String label);

  /**
   * @return the name
   */
  public abstract String getName();

  /**
   * @param name the name to set
   */
  public abstract void setName(final String name);

  /**
   * @return the workingSetItems
   */
  public abstract Set<IWorkingSetItem> getWorkingSetItems();

}