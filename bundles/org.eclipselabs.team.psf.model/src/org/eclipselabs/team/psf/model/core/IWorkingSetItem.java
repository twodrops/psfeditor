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
public interface IWorkingSetItem {


  /**
   * @return the properties
   */
  public abstract Map<String, String> getProperties();

  public String getFactoryId();

}