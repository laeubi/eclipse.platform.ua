/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.help.ui;

import java.util.Dictionary;

import org.eclipse.jface.preference.IPreferencePage;

/**
 * Preference pages that are used for editing help search
 * scope settings should implement this interface.
 * 
 * @since 3.1
 */
public interface ISearchScopePage extends IPreferencePage {
/**
 * Initializes the search scope page.
 * @param engineId the unique identifier of the search engine
 * that owns this scope page
 * @param scopeSetName the name of the current scope set
 * that is used to group data shown in this page
 * @param parameters optional parameters passed to the
 * concrete instance of the search engine type in order
 * to configure it
 */
	void init(String engineId, String scopeSetName, Dictionary parameters);
}