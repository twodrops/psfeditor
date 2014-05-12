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
package org.eclipselabs.team.psf.model.change.service;

import java.util.Arrays;

import org.eclipse.core.commands.common.EventManager;


/**
 * @author Nirmal Sasidharan
 */
public class ContentChangedProvider extends EventManager implements IContentChangedProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public void setContentDelta(final IContentDelta contentDelta) {
    notifyAll(contentDelta);
  }


  /**
   * @param contentChangedListener
   */
  @Override
  public void addContentChangedListener(final IContentChangedListener contentChangedListener) {
    addListenerObject(contentChangedListener);
  }

  /**
   * @param contentChangedListener
   */
  @Override
  public void removeContentChangedListener(final IContentChangedListener contentChangedListener) {
    removeListenerObject(contentChangedListener);
  }


  @Override
  public void notifyAll(final IContentDelta contentDelta) {
    Object[] listeners = getListeners();
    IContentChangedListener[] contentChangedListeners =
        Arrays.copyOf(listeners, listeners.length, IContentChangedListener[].class);
    for (IContentChangedListener contentChangedListener : contentChangedListeners) {
      contentChangedListener.contentChanged(contentDelta);
    }

  }


}
