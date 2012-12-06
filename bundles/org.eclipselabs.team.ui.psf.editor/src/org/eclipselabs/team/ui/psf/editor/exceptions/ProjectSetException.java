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
package org.eclipselabs.team.ui.psf.editor.exceptions;


/**
 * @author Nirmal Sasidharan
 */
public class ProjectSetException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -4895267289526545116L;


  /**
   * 
   */
  public ProjectSetException(final String message) {
    super(message);
  }

  public ProjectSetException(final Exception e) {
    super(e);
  }


}
