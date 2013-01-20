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

import org.eclipse.ui.IEditorPart;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;


/**
 * @author Nirmal Sasidharan
 */
public interface IProjectSetEditor extends IEditorPart {

  /**
   * @return {@link IProjectSet}
   */
  public IProjectSet getProjectSet();

}