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
import java.util.Set;


/**
 * @author Nirmal Sasidharan
 */
public interface IProvider {

  /**
   * @return the projects
   */
  public abstract Set<Project> getProjects();

  /**
   * @return the id
   */
  public abstract String getId();

  public abstract String getProjectName(List<String> references);

}