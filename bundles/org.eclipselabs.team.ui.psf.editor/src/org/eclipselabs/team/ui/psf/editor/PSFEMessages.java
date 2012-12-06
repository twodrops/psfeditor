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
package org.eclipselabs.team.ui.psf.editor;

import org.eclipse.osgi.util.NLS;

/**
 * @author SNI1SI
 */
public class PSFEMessages extends NLS {

  private static final String BUNDLE_NAME = "org.eclipselabs.team.ui.psf.editor.messages"; //$NON-NLS-1$
  public static String ProjectSection_AddButton_name;
  public static String ProjectSection_ContentProvider_assertion;
  public static String ProjectSection_description;
  public static String ProjectSection_ProjectAdd_error;
  public static String ProjectSection_ProjectSelectionDialog_title;
  public static String ProjectSection_RemoveButton_name;
  public static String ProjectSection_title;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, PSFEMessages.class);
  }

  private PSFEMessages() {}
}
