/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.psf.editor;

import org.eclipse.osgi.util.NLS;

/**
 * @author Nirmal Sasidharan
 */
@SuppressWarnings("javadoc")
public class ProjectSetEditorMessages extends NLS {

  private static final String BUNDLE_NAME = "org.eclipselabs.team.psf.editor.messages"; //$NON-NLS-1$

  public static String ProjectSection_AddButton_name;
  public static String ProjectSection_ContentProvider_assertion;
  public static String ProjectSection_description;
  public static String ProjectSection_ProjectAdd_error;
  public static String ProjectSection_ProjectSelectionDialog_title;
  public static String ProjectSection_RemoveButton_name;
  public static String ProjectSection_title;
  public static String ProjectSetEditor_Error_title;
  public static String ProjectSetEditor_FormPage_Create_error;
  public static String ProjectSetEditor_Input_error;
  public static String ProjectSetEditor_Load_error;
  public static String ProjectSetEditor_Save_error;
  public static String ProjectSetEditor_Save_MessageBox_message;

  public static String ProjectSetHandler_Invalid_Project_error_01;

  public static String ProjectSetHandler_Invalid_Project_error_02;
  public static String ProjectSetImportWizard_5;

  public static String ProjectSetImportWizard_6;

  public static String ProjectSetImportWizard_Container_error_01;

  public static String ProjectSetImportWizard_Container_error_02;

  public static String ProjectSetImportWizard_Create_info;

  public static String ProjectSetImportWizard_Editor_Open_info;

  public static String ProjectSetImportWizard_Wizard_error;

  public static String ProjectSetPage_Import_link;

  public static String ProjectSetPage_Page_id;

  public static String ProjectSetPage_title;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, ProjectSetEditorMessages.class);
  }

  private ProjectSetEditorMessages() {}
}
