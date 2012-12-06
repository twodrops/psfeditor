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
package org.eclipselabs.team.ui.psf.editor.importer;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.team.internal.ui.wizards.ImportProjectSetOperation;
import org.eclipse.ui.IWorkingSet;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;



/**
 * @author Nirmal Sasidharan
 */
public class ProjectImporter {

  public static final ProjectImporter INSTANCE = new ProjectImporter();

  /**
   * @param psfFile
   * @throws ProjectSetException
   */
  public void doImport(final String psfFile) throws ProjectSetException {

    final IWorkingSet[] EMPTY_WORKING_SET_ARRAY = new IWorkingSet[0];
    final boolean[] result = new boolean[] { false };

    ImportProjectSetOperation op = new ImportProjectSetOperation(null, psfFile, EMPTY_WORKING_SET_ARRAY);
    try {
      op.run();
    }
    catch (InvocationTargetException e) {
      throw new ProjectSetException(e);
    }
    catch (InterruptedException e) {
      throw new ProjectSetException(e);
    }


  }

}
