/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.psf.model.io;

import org.eclipse.osgi.util.NLS;

/**
 * @author Nirmal Sasidharan
 */
@SuppressWarnings("javadoc")
public class ProjectSetIOMessages extends NLS {

  private static final String BUNDLE_NAME = "org.eclipselabs.team.psf.model.io.messages"; //$NON-NLS-1$


  public static String ProjectSetHandler_Invalid_Project_error_01;

  public static String ProjectSetHandler_Invalid_Project_error_02;

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, ProjectSetIOMessages.class);
  }

  private ProjectSetIOMessages() {}
}
