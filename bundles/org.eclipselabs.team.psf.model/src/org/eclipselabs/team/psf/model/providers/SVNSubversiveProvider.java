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
package org.eclipselabs.team.psf.model.providers;

import java.util.List;

import org.eclipselabs.team.psf.model.core.IProjectSet;
import org.eclipselabs.team.psf.model.core.Provider;


/**
 * @author Nirmal Sasidharan
 */
public class SVNSubversiveProvider extends Provider {

  public static final String PROVIDER_ID = "org.eclipse.team.svn.core.svnnature"; //$NON-NLS-1$
  private static final int PROJECT_INDEX = 2;

  /**
   * @param projectSet Project Set model
   * @param id id of team provider
   */
  public SVNSubversiveProvider(final IProjectSet projectSet, final String id) {
    super(projectSet, id);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectName(final List<String> references) {
    return references.get(PROJECT_INDEX);
  }
}
