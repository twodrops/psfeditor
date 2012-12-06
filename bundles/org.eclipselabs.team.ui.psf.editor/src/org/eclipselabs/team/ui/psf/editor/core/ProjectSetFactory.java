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
package org.eclipselabs.team.ui.psf.editor.core;


import java.util.Map;

import org.eclipselabs.team.ui.psf.editor.providers.CVSProvider;
import org.eclipselabs.team.ui.psf.editor.providers.SVNProvider;



/**
 * A Factory class to create Project Set
 * 
 * @author Nirmal Sasidharan
 */
public class ProjectSetFactory {


  public static final ProjectSetFactory INSTANCE = new ProjectSetFactory();

  /**
   * Creates a Project Set root
   * 
   * @return Project Set root
   */
  public IProjectSet createProjectSet() {
    IProjectSet projectSet = new ProjectSet();
    return projectSet;
  }

  /**
   * Creates a Provider within the Project Set
   * 
   * @param projectSet the parent Project Set
   * @param id of the team provider. For exampple, SVN Team Provider has id as
   *          "org.tigris.subversion.subclipse.core.svnnature"
   * @return created Provider
   */
  public IProvider createProvider(final IProjectSet projectSet, final String id) {

    IProvider provider = doCreateProvider(projectSet, id);
    return provider;
  }

  /**
   * Delegator method to create specific providers
   * 
   * @param projectSet the parent Project Set
   * @param id of the team provider. For exampple, SVN Team Provider has id as
   *          "org.tigris.subversion.subclipse.core.svnnature"
   * @return created Provider
   */
  protected IProvider doCreateProvider(final IProjectSet projectSet, final String id) {

    IProvider provider = null;

    if (id.equals("org.tigris.subversion.subclipse.core.svnnature")) { //$NON-NLS-1$
      provider = new SVNProvider(projectSet, id);
    }
    else if (id.equals("org.eclipse.team.cvs.core.cvsnature")) { //$NON-NLS-1$
      provider = new CVSProvider(projectSet, id);
    }
    //    else if (id.equals("org.eclipse.team.git.core.gitnature")) { //$NON-NLS-1$
    // provider = new GitProvider(projectSet, id);
    // }
    else {
      provider = new Provider(projectSet, id);
    }

    IProvider existingProvider = getExistingProvider(projectSet, provider);
    if (existingProvider == null) {
      projectSet.getProviders().add(provider);
    }
    else {
      provider = existingProvider;
    }

    return provider;
  }

  /**
   * @param projectSet
   * @param provider
   */
  private IProvider getExistingProvider(final IProjectSet projectSet, final IProvider provider) {
    for (IProvider existingProvider : projectSet.getProviders()) {
      if (existingProvider.equals(provider)) {
        return existingProvider;
      }
    }

    return null;
  }

  /**
   * Creates a Project with in the Provider
   * 
   * @param provider the parent Provider
   * @return created Project
   */
  public IProject createProject(final IProvider provider) {
    IProject project = new Project(provider);
    return project;
  }

  /**
   * @return created WorkingSet
   */
  public IWorkingSet createWorkingSet(final IProjectSet projectSet, final String id, final String editPageId,
      final String label, final String name) {
    return doCreateWorkingSet(projectSet, id, editPageId, label, name);
  }

  /**
   * @param projectSet
   * @param id
   * @return
   */
  protected IWorkingSet doCreateWorkingSet(final IProjectSet projectSet, final String id, final String editPageId,
      final String label, final String name) {
    IWorkingSet workingSet = new WorkingSet(projectSet, id, editPageId, label, name);

    IWorkingSet existingWorkingSet = getExistingWorkingSet(projectSet, workingSet);
    if (existingWorkingSet == null) {
      projectSet.getWorkingSets().add(workingSet);
    }
    else {
      workingSet = existingWorkingSet;
    }
    return workingSet;
  }

  private IWorkingSet getExistingWorkingSet(final IProjectSet projectSet, final IWorkingSet workingSet) {
    for (IWorkingSet existingWorkingSet : projectSet.getWorkingSets()) {
      if (existingWorkingSet.equals(workingSet)) {
        return existingWorkingSet;
      }
    }

    return null;
  }


  /**
   * @return created WorkingSetItem
   */
  public IWorkingSetItem createWorkingSetItem(final IWorkingSet workingSet, final String factoryId,
      final Map<String, String> properties) {
    return doCreateWorkingSetItem(workingSet, factoryId, properties);
  }

  /**
   * @param projectSet
   * @param id
   * @return
   */
  protected IWorkingSetItem doCreateWorkingSetItem(final IWorkingSet workingSet, final String factoryId,
      final Map<String, String> properties) {
    IWorkingSetItem workingSetItem = new WorkingSetItem(workingSet, factoryId, properties);
    IWorkingSetItem existingWorkingSetItem = getExistingWorkingSetItem(workingSet, workingSetItem);
    if (existingWorkingSetItem == null) {
      workingSet.getWorkingSetItems().add(workingSetItem);
    }
    else {
      workingSetItem = existingWorkingSetItem;
    }
    return workingSetItem;
  }

  private IWorkingSetItem getExistingWorkingSetItem(final IWorkingSet workingSet, final IWorkingSetItem workingSetItem) {
    for (IWorkingSetItem existingWorkingSetItem : workingSet.getWorkingSetItems()) {
      if (existingWorkingSetItem.equals(workingSetItem)) {
        return existingWorkingSetItem;
      }
    }
    return null;
  }


}
