/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.psf.model;

import org.eclipse.osgi.util.NLS;

/**
 * @author Nirmal Sasidharan
 */
@SuppressWarnings("javadoc")
public class ProjectSetModelMessages extends NLS {

  private static final String BUNDLE_NAME = "org.eclipselabs.team.psf.model.messages"; //$NON-NLS-1$

  public static String ProjectSetFactory_Provider_error_01;

  public static String ProjectSetFactory_Provider_error_02;

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, ProjectSetModelMessages.class);
  }

  private ProjectSetModelMessages() {}
}
