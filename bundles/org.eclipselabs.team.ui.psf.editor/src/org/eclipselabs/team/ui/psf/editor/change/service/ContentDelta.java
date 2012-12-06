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
package org.eclipselabs.team.ui.psf.editor.change.service;


/**
 * @author Nirmal Sasidharan
 */
public class ContentDelta implements IContentDelta {


  private int type = IContentDelta.UNCHANGED;
  private final Object delta;

  /**
   * 
   */
  public ContentDelta(final Object delta, final int type) {
    this.delta = delta;
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getType() {
    return this.type;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject() {
    return this.delta;
  }


}
