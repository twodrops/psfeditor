/*******************************************************************************
 * Copyright (c) 2012 Nirmal Sasidharan. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Nirmal Sasidharan - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.team.psf.model.core;


import java.util.Map;

import org.eclipselabs.team.psf.model.ProjectSetModelMessages;
import org.eclipselabs.team.psf.model.exceptions.ProjectSetException;
import org.eclipselabs.team.psf.model.providers.CVSProvider;
import org.eclipselabs.team.psf.model.providers.GitProvider;
import org.eclipselabs.team.psf.model.providers.SVNSubclipseProvider;
import org.eclipselabs.team.psf.model.providers.SVNSubversiveProvider;


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
   * @throws ProjectSetException
   */
  public IProvider createProvider(final IProjectSet projectSet, final String id) throws ProjectSetException {

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
   * @throws ProjectSetException
   */
  protected IProvider doCreateProvider(final IProjectSet projectSet, final String id) throws ProjectSetException {

    IProvider provider = null;

    if (id.equals(SVNSubclipseProvider.PROVIDER_ID)) {
      provider = new SVNSubclipseProvider(projectSet, id);
    }
    else if (id.equals(SVNSubversiveProvider.PROVIDER_ID)) {
      provider = new SVNSubversiveProvider(projectSet, id);
    }
    else if (id.equals(CVSProvider.PROVIDER_ID)) {
      provider = new CVSProvider(projectSet, id);
    }
    else if (id.equals(GitProvider.PROVIDER_ID)) {
      provider = new GitProvider(projectSet, id);
    }
    else {
      throw new ProjectSetException(ProjectSetModelMessages.ProjectSetFactory_Provider_error_01 + id +
          ProjectSetModelMessages.ProjectSetFactory_Provider_error_02);
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
