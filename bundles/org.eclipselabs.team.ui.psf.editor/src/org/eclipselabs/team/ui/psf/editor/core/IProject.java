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

import java.util.List;


/**
 * @author Nirmal Sasidharan
 */
public interface IProject {

  /**
   * @return the references
   */
  public abstract List<String> getReferences();

  /**
   * @return the provider
   */
  public abstract IProvider getProvider();


}