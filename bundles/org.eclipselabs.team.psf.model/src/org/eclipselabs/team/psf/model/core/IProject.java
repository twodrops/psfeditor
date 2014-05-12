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

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;


/**
 * Represents a project in Project Set model
 * @author Nirmal Sasidharan
 */
public interface IProject extends IAdaptable {

  /**
   * @return the references
   */
  public abstract List<String> getReferences();

  /**
   * @return the provider
   */
  public abstract IProvider getProvider();


}
