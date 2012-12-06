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
package org.eclipselabs.team.ui.psf.editor.providers;

import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.core.Provider;


/**
 * @author Nirmal Sasidharan
 */
public class GitProvider extends Provider {

  // TODO: Check if the index is correct
  private static final int PROJECT_INDEX = 2;

  /**
   * @param projectSet Project Set model
   * @param id id of team provider
   */
  public GitProvider(final IProjectSet projectSet, final String id) {
    super(projectSet, id);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public int getProjectIndex() {
    return PROJECT_INDEX;
  }
}
