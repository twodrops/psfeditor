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
package org.eclipselabs.team.psf.model.change.service;


/**
 * @author Nirmal Sasidharan
 */
public interface IContentDelta {

  public static final int UNCHANGED = 0x1;
  public static final int ADDED = 0x2;
  public static final int REMOVED = 0x3;
  public static final int UPDATED = 0x4;

  /**
   * @return
   */
  public int getType();

  /**
   * @return
   */
  public Object getObject();

}
