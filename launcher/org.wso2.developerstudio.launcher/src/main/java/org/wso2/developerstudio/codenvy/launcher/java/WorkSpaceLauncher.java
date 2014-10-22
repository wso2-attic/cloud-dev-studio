/*
* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.developerstudio.codenvy.launcher.java;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.CLabel;

public class WorkSpaceLauncher {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private static final String DEV_SWORK_SPACE = "DevStudioWorkSpace";
	private static final String NOT_SET = "NOT_SET";
	private static final String OK_BUTTON_TEXT = "Ok";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	private static final String BROWSE_BUTTON_TEXT = "Browse";
	private static final String MENU_HEADER = "Select workspace";
	private static final String USER_MESSAGE_TO_SELECT_WORKSPACE =
			" WSO2 Developer Studio stores your projects in a directory called workspace. Please choose workspace folder for this session ";
	private static final String SET_DEFAULT_WORKSPACE_MESSAGE =
			"Use this as default and do not ask again";
	private static final String BROWSE_DIALOG_MENU_MESSAGE = "Select your workspace directory";
	private static final String WARNING_DIALOG_MENU_MESSAGE = "Warning";
	public static final int ERROR_DIALOG_WIDTH = 400;
	public static final int ERROR_DIALOG_HEIGHT = 100;
	public static final String WORKSPACE_LABEL = "Workspace :";

	static boolean userWorkSpaceSet = false;
	String userHome = null;
	private static final String SHELL_TITLE = "WSO2 Developer Studio";
	Display display = new Display();
	public static final int SHELL_TRIM = SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE;

	private static final int SHELL_WIDTH = 602;
	private static final int SHELL_HEIGHT = 300;
	Shell shell = new Shell(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
	Text workSpaceText;
	static boolean isDefaultSet = false;
	private static String modifiedWorkSpaceLoc = null;
	private static String currentWorkSpaceLoc = null;

	public WorkSpaceLauncher() {
		init();
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.pack();
		shell.setSize(SHELL_WIDTH, SHELL_HEIGHT);
		// making the shell appear in the center of the monitor
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int shellXLoc = bounds.x + (bounds.width - rect.width) / 2;
		int shellYLoc = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(shellXLoc, shellYLoc);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

	/**
	 *
	 */
	private void init() {

		shell.setText(SHELL_TITLE);

		String iconRoot = Bootstrap.getRootDir()+ File.separator + "icons";

		Image icon32 = new Image(display, iconRoot + File.separator + "icon-32.png");
		Image icon64 = new Image(display, iconRoot + File.separator + "icon-64.png");
		Image icon128 = new Image(display, iconRoot + File.separator + "icon-128.png");
		Image icon256 = new Image(display, iconRoot + File.separator + "icon-256.png");

		//shell.setImages(new Image[]{icon32, icon64, icon128, icon256});

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		composite.setBounds(0, 0, 600, 118);

		Label lblSelectWorkspace = new Label(composite, SWT.NONE);
		lblSelectWorkspace.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblSelectWorkspace.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		lblSelectWorkspace.setBounds(10, 10, 141, 23);
		lblSelectWorkspace.setText(MENU_HEADER);

		Label mesageLabel = new Label(composite, SWT.WRAP);
		mesageLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		mesageLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		mesageLabel.setBounds(10, 50, 580, 41);
		mesageLabel.setText("WSO2 Developer Studio stores your projects in a directory called workspace. Please choose workspace folder for this session ");

		Composite selectionComposite = new Composite(shell, SWT.NONE);
		selectionComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		selectionComposite.setBounds(0, 141, 600, 77);

		CLabel lblWorkspace = new CLabel(selectionComposite, SWT.NONE);
		lblWorkspace.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblWorkspace.setBounds(10, 23, 89, 23);
		lblWorkspace.setText(WORKSPACE_LABEL);

		workSpaceText = new Text(selectionComposite, SWT.BORDER);
		workSpaceText.setBounds(105, 23, 368, 27);
		currentWorkSpaceLoc = getWorkSpaceLoc();
		modifiedWorkSpaceLoc = getUserWorkspace();
		if (!currentWorkSpaceLoc.equals(NOT_SET)) {
			workSpaceText.setText(currentWorkSpaceLoc);
		} else {
			workSpaceText.setText(modifiedWorkSpaceLoc);
		}
		workSpaceText.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent modifyEvent) {
				modifiedWorkSpaceLoc = workSpaceText.getText();
			}
		});

		Button browseButton = new Button(selectionComposite, SWT.NONE);
		browseButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		browseButton.setBounds(479, 21, 93, 29);
		browseButton.setText(BROWSE_BUTTON_TEXT);
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dirDialog = new DirectoryDialog(shell);
				dirDialog.setText(BROWSE_DIALOG_MENU_MESSAGE);
				String selectedDir = dirDialog.open();
				if (selectedDir != null) {
					workSpaceText.setText(selectedDir);
					modifiedWorkSpaceLoc = selectedDir;
				}
			}
		});

		Composite buttonComposite = new Composite(shell, SWT.NONE);
		buttonComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		buttonComposite.setBounds(10, 224, 582, 44);

		Button btnCancel = new Button(buttonComposite, SWT.NONE);
		btnCancel.setBounds(371, 10, 91, 29);
		btnCancel.setText(CANCEL_BUTTON_TEXT);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

		Button btnOk = new Button(buttonComposite, SWT.NONE);
		btnOk.setBounds(481, 10, 91, 29);
		btnOk.setText(OK_BUTTON_TEXT);
		btnOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				okPressed();
			}
		});

		final Button btnCheckButton = new Button(buttonComposite, SWT.CHECK);
		btnCheckButton.setBounds(10, 10, 339, 29);
		btnCheckButton.setText(SET_DEFAULT_WORKSPACE_MESSAGE);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckButton.getSelection()) {
					isDefaultSet = true;
				} else {
					isDefaultSet = false;
				}
			}
		});

	}

	public static boolean openWorkSpaceBrowser() {
		new WorkSpaceLauncher();
		return isDefaultSet;
	}

	public static boolean isUserWorkSpaceSet() {
		return userWorkSpaceSet;
	}

	/**
	 * read the workspace location set for the IDE
	 *
	 * @return the current workspace location set for the IDE
	 */
	private String getWorkSpaceLoc() {
		String defaultWorkSpace = null;
		try {
			defaultWorkSpace = ConfigManager.getWorkspaceRootDirectory();

		} catch (IOException e) {
			logger.error("error in reading the workspace directory from properties" + e);
		}
		return defaultWorkSpace;
	}

	/**
	 * save the user selected workspace location for the IDE
	 *
	 * @param selectedDirectory
	 */
	private void setWorkSpaceLoc(String selectedDirectory) {
		try {
			ConfigManager.setWorkspaceDirectory(selectedDirectory);
		} catch (IOException e1) {
			logger.error("error in writing the workspace directory to IDE properties" + e1);
		}
	}

	private void setIsDefaultWorkSpaceSet(boolean defaultSet) {
		try {
			ConfigManager.setDefaultWorkSpaceProperty(String.valueOf(defaultSet));
		} catch (IOException e1) {
			logger.error("error in writing whether user has set default workspace to IDE properties" +
			             e1);
		}
	}

	/**
	 * create the default workspace location
	 */
	private boolean createDefaultWorkSpaceDirectory(String workSpace) {
		boolean workSpaceCreationSuccess = false;
		if (userHome != null) {
			File workspaceDir = new File(workSpace);
			if (!workspaceDir.exists()) {
				logger.info(
						"creating the workspace directory DevSWorkSpace in your home directory");
				try {
					workspaceDir.mkdir();
					workSpaceCreationSuccess = true;
				} catch (SecurityException se) {
					createErrorMessageDialog(
							"Please set permission to create your workspace directory");
				}
				if (workSpaceCreationSuccess) {
					logger.info(
							"successfully created the workspace directory DevSWorkSpace in your home directory at " +
							workSpace);
				}
			} else {
				workSpaceCreationSuccess = true;
			}
		}

		return workSpaceCreationSuccess;
	}

	/**
	 * get the users home directory and set the new default workspace location
	 *
	 * @return new works space location
	 */
	private String getUserWorkspace() {
		String fileSeparator = File.separator;
		try {
			userHome = System.getProperty("user.home");
		} catch (SecurityException se) {
			logger.error(
					"unable to get the system home directory, security exception, please set permission");
		} catch (NullPointerException ne) {
			logger.error(
					"unable to get the system home directory, the home directory returns null");
		} catch (IllegalArgumentException ie) {
			logger.error("unable to get the system root directory, the home directory is empty");
		}
		String newWorkSpace = userHome + fileSeparator + DEV_SWORK_SPACE;
		return newWorkSpace;
	}

	/**
	 * when the default workspace newWorkSpace is already existing
	 *
	 * @param errorMessage
	 * @return the status of the user input, yes or cancel
	 */
	private int createErrorMessageDialog(String errorMessage) {
		Shell errorDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		errorDialog.setSize(ERROR_DIALOG_WIDTH, ERROR_DIALOG_HEIGHT);
		MessageBox messageDialog = new MessageBox(errorDialog, SWT.ICON_WARNING | SWT.OK);
		messageDialog.setText(WARNING_DIALOG_MENU_MESSAGE);
		messageDialog.setMessage(errorMessage);
		return messageDialog.open();
	}

	private void okPressed() {
		boolean createdWorkSpace = false;
		String defaultNewWorkspace = getUserWorkspace();
		if (!modifiedWorkSpaceLoc.equals(null)) {
			if (modifiedWorkSpaceLoc.equals(defaultNewWorkspace)) {
				createdWorkSpace = createDefaultWorkSpaceDirectory(modifiedWorkSpaceLoc);
			}
			setWorkSpaceLoc(modifiedWorkSpaceLoc);
			if (isDefaultSet) {
				setIsDefaultWorkSpaceSet(isDefaultSet);
			}
			if (createdWorkSpace || !modifiedWorkSpaceLoc.equals(defaultNewWorkspace)) {
				shell.close();
				userWorkSpaceSet = true;
			}
		} else {
			logger.warn("workspace location is null hence using the default location");
		}
	}
}
