/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.help.ui.internal.views;

import java.util.*;
import java.util.Observer;

import org.eclipse.help.IHelpResource;
import org.eclipse.help.internal.base.*;
import org.eclipse.help.ui.internal.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class BookmarksPart extends HyperlinkTreePart implements IHelpPart,
		Observer {
	private Image containerWithTopicImage;

	class BookmarksProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			if (parentElement == BookmarksPart.this)
				return new Object[] { BaseHelpSystem.getBookmarkManager() };
			if (parentElement instanceof BookmarkManager)
				return ((BookmarkManager) parentElement).getBookmarks();
			return new Object[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class BookmarksLabelProvider extends LabelProvider {
		public String getText(Object obj) {
			if (obj instanceof BookmarkManager)
				return HelpUIResources.getString("BookmarksPart.savedTopics"); //$NON-NLS-1$
			if (obj instanceof IHelpResource)
				return ((IHelpResource) obj).getLabel();
			return super.getText(obj);
		}

		public Image getImage(Object obj) {
			if (obj instanceof BookmarkManager)
				return HelpUIResources
						.getImage(IHelpUIConstants.IMAGE_BOOKMARKS);
			if (obj instanceof IHelpResource)
				return HelpUIResources
						.getImage(IHelpUIConstants.IMAGE_BOOKMARK);
			return super.getImage(obj);
		}
	}

	/**
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public BookmarksPart(Composite parent, final FormToolkit toolkit,
			IToolBarManager tbm) {
		super(parent, toolkit, tbm);
		BaseHelpSystem.getBookmarkManager().addObserver(this);
	}

	public void dispose() {
		BaseHelpSystem.getBookmarkManager().deleteObserver(this);
		super.dispose();
	}

	protected void configureTreeViewer() {
		treeViewer.setContentProvider(new BookmarksProvider());
		treeViewer.setLabelProvider(new BookmarksLabelProvider());
		treeViewer.setAutoExpandLevel(2);
	}

	public boolean fillContextMenu(IMenuManager manager) {
		boolean value = super.fillContextMenu(manager);
		ISelection selection = treeViewer.getSelection();
		if (canDelete((IStructuredSelection) selection)) {
			if (value)
				manager.add(new Separator());
			addDeleteAction((IStructuredSelection) selection, manager);
			return true;
		}
		return value;
	}

	private boolean canDelete(IStructuredSelection ssel) {
		Object obj = ssel.getFirstElement();
		return obj instanceof BookmarkManager.Bookmark;
	}

	private void addDeleteAction(final IStructuredSelection ssel,
			IMenuManager manager) {
		Action action = new Action("") { //$NON-NLS-1$
			public void run() {
				BookmarkManager.Bookmark b = (BookmarkManager.Bookmark) ssel
						.getFirstElement();
				BaseHelpSystem.getBookmarkManager().removeBookmark(b);
			}
		};
		action.setText(HelpUIResources.getString("BookmarksPart.delete")); //$NON-NLS-1$
		manager.add(action);
		manager.add(new Separator());
	}

	protected void doOpen(Object obj) {
		if (obj instanceof BookmarkManager) {
			treeViewer.setExpandedState(obj, !treeViewer.getExpandedState(obj));
		} else if (obj instanceof IHelpResource) {
			IHelpResource res = (IHelpResource) obj;
			if (res.getHref() != null)
				parent.showURL(res.getHref());
		}
	}

	public void update(Observable o, Object arg) {
		BookmarkManager.BookmarkEvent event = (BookmarkManager.BookmarkEvent) arg;
		switch (event.getType()) {
		case BookmarkManager.ADD:
			treeViewer.add(BaseHelpSystem.getBookmarkManager(), event
					.getBookmark());
			break;
		case BookmarkManager.REMOVE:
			treeViewer.remove(event.getBookmark());
			break;
		case BookmarkManager.REMOVE_ALL:
			treeViewer.refresh();
			break;
		}
	}
}