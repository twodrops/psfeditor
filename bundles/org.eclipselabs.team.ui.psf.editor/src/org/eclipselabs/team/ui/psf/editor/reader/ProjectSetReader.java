/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.ui.psf.editor.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipselabs.team.ui.psf.editor.core.IProject;
import org.eclipselabs.team.ui.psf.editor.core.IProjectSet;
import org.eclipselabs.team.ui.psf.editor.core.IProvider;
import org.eclipselabs.team.ui.psf.editor.core.IWorkingSet;
import org.eclipselabs.team.ui.psf.editor.core.IWorkingSetItem;
import org.eclipselabs.team.ui.psf.editor.core.ProjectSetFactory;
import org.eclipselabs.team.ui.psf.editor.exceptions.ProjectSetException;


/**
 * Loads a project set file (*.psf) into a Project Set Model
 * 
 * @author Nirmal Sasidharan
 */
public class ProjectSetReader {

  public static final ProjectSetReader INSTANCE = new ProjectSetReader();

  /**
   * Creates a Project Set from a project set file
   * 
   * @param filename project set file
   * @return Project Set root
   * @throws ProjectSetException
   */
  public IProjectSet load(final String filename) throws ProjectSetException {

    Assert.isNotNull(filename);

    XMLMemento xmlMemento = null;
    IProjectSet projectSet = null;

    xmlMemento = loadMemento(filename);
    projectSet = loadMementoIntoProjectSet(xmlMemento);

    return projectSet;
  }

  /**
   * Load project set file into XML Memento
   * 
   * @param filename project set file
   * @return XML Memento
   * @throws ProjectSetException
   */
  protected XMLMemento loadMemento(final String filename) throws ProjectSetException {
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(new FileInputStream(filename), "UTF-8"); //$NON-NLS-1$
      return XMLMemento.createReadRoot(reader);
    }
    catch (UnsupportedEncodingException e) {
      throw new ProjectSetException(e);
    }
    catch (FileNotFoundException e) {
      throw new ProjectSetException(e);
    }
    catch (WorkbenchException e) {
      throw new ProjectSetException(e);
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (IOException e) {
          throw new ProjectSetException(e);
        }
      }
    }
  }


  /**
   * Loads XML Memento into a Project Set model
   * 
   * @param xmlMemento XML Memento
   * @return Project Set root
   * @throws ProjectSetException
   */
  protected IProjectSet loadMementoIntoProjectSet(final XMLMemento xmlMemento) throws ProjectSetException {

    Assert.isNotNull(xmlMemento);

    IProjectSet projectSet = createProjectSetFromMemento(xmlMemento);

    createProjectsFromMemento(xmlMemento, projectSet);

    createWorkingSetsFromMemento(xmlMemento, projectSet);

    return projectSet;

  }

  /**
   * @param xmlMemento
   * @return
   */
  protected IProjectSet createProjectSetFromMemento(final XMLMemento xmlMemento) {
    // create empty project set
    IProjectSet projectSet = ProjectSetFactory.INSTANCE.createProjectSet();
    projectSet.setVersion(xmlMemento.getString("version")); //$NON-NLS-1$
    return projectSet;
  }

  /**
   * @param xmlMemento
   * @param projectSet
   * @throws ProjectSetException
   */
  protected void createProjectsFromMemento(final XMLMemento xmlMemento, final IProjectSet projectSet)
      throws ProjectSetException {
    // load providers and projects
    IMemento[] providersMemento = xmlMemento.getChildren("provider"); //$NON-NLS-1$

    for (IMemento providerMemento : providersMemento) {
      IProvider provider = ProjectSetFactory.INSTANCE.createProvider(projectSet, providerMemento.getString("id"));
      IMemento[] projectsMemento = providerMemento.getChildren("project"); //$NON-NLS-1$
      for (IMemento projectMemento : projectsMemento) {
        IProject project = ProjectSetFactory.INSTANCE.createProject(provider);
        project.getReferences().addAll(Arrays.asList(projectMemento.getString("reference").split(","))); //$NON-NLS-1$
      }
    }
  }

  /**
   * @param xmlMemento
   * @param projectSet
   */
  protected void createWorkingSetsFromMemento(final XMLMemento xmlMemento, final IProjectSet projectSet) {
    // load working sets
    IMemento[] workingSetsMemento = xmlMemento.getChildren("workingSets"); //$NON-NLS-1$
    for (IMemento workingSetMemento : workingSetsMemento) {
      String id = workingSetMemento.getString("id"); //$NON-NLS-1$
      String editPageId = workingSetMemento.getString("editPageId"); //$NON-NLS-1$
      String label = workingSetMemento.getString("label"); //$NON-NLS-1$
      String name = workingSetMemento.getString("name"); //$NON-NLS-1$

      IWorkingSet workingSet = ProjectSetFactory.INSTANCE.createWorkingSet(projectSet, id, editPageId, label, name);

      // load working set items
      IMemento[] workingSetItems = workingSetMemento.getChildren("item"); //$NON-NLS-1$
      for (IMemento workingSetItemMemento : workingSetItems) {
        String factoryId = workingSetItemMemento.getString(IWorkbenchConstants.TAG_FACTORY_ID);
        Map<String, String> properties = new HashMap<String, String>();
        String[] attributeKeys = workingSetItemMemento.getAttributeKeys();
        for (String attributeKey : attributeKeys) {
          if (!attributeKey.equals(IWorkbenchConstants.TAG_FACTORY_ID)) {
            String attributeValue = workingSetItemMemento.getString(attributeKey);
            properties.put(attributeKey, attributeValue);
          }
        }
        IWorkingSetItem workingSetItem =
            ProjectSetFactory.INSTANCE.createWorkingSetItem(workingSet, factoryId, properties);

      }
    }
  }
}
