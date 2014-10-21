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


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;


/**
 * creating the dialog box for workspace selection before opening the dev studio
 */
public class WorkSpaceSelector {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	public static final int SHELL_WIDTH = 600; //height and width setting for the shell window
	public static final int SHELL_HEIGHT = 300;
	public static final int INFODATA_HEIGHT = 80;
	public static final int SHELL_MARGIN = 5;

	private static final String DEV_SWORK_SPACE = "DevStudioWorkSpace";
	private static final String NOT_SET = "NOT_SET";
	private static final String OK_BUTTON_TEXT = "    Ok    ";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	private static final String BROWSE_BUTTON_TEXT = "Browse";
	private static final String MENU_HEADER = "Select a workspace";
	private static final String USER_MESSAGE_TO_SELECT_WORKSPACE =
			" Codenvy Developer Studio stores your projects in a directory called workspace. Please choose workspace folder for this session ";
	private static final String SET_DEFAULT_WORKSPACE_MESSAGE =
			"set workspace as default and do not ask again";
	private static final String BROWSE_DIALOG_MENU_MESSAGE = "Select your workspace directory";
	private static final String ERROR_DIALOG_MENU_MESSAGE = "Error occurred";
	public static final int ERROR_DIALOG_WIDTH = 400;
	public static final int ERROR_DIALOG_HEIGHT = 100;

	int useSameWorkSpace;
	static boolean userWorkSpaceSet = false;
	String userHome = null;
	Display display = new Display();
	Shell shell = new Shell(display);
	Text workSpaceText;
	static boolean isDefaultSet = false;
	private static String modifiedWorkSpaceLoc = null;
	private static String currentWorkSpaceLoc = null;

	public WorkSpaceSelector() {
		init();
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

	private void init() {

		// create a new GridLayout with two columns
		// of different size
		currentWorkSpaceLoc = getWorkSpaceLoc();
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.marginBottom = SHELL_MARGIN;
		gridLayout.marginTop = SHELL_MARGIN;
		gridLayout.marginLeft = SHELL_MARGIN;
		gridLayout.marginRight = SHELL_MARGIN;
		shell.setText("Select your workspace directory");
		shell.setLayout(gridLayout);

		// create a label and a button
		Label headerLabel = new Label(shell, SWT.NONE);
		headerLabel.setText(MENU_HEADER);
		Label SeparatorLabel;
		SeparatorLabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData separaterLayout = new GridData(SWT.FILL, SWT.TOP, true, false, 4,1);
		SeparatorLabel.setLayoutData(separaterLayout);

		final GridData infoLabelData = new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1);
		infoLabelData.heightHint = INFODATA_HEIGHT; //text field will consume 60% of total shell
		Label messageLabel = new Label(shell, SWT.WRAP);
		messageLabel.setLayoutData(infoLabelData);
		messageLabel.setText(USER_MESSAGE_TO_SELECT_WORKSPACE);

		// create a new label which is used as a separator
		SeparatorLabel = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		SeparatorLabel.setLayoutData(separaterLayout);

		workSpaceText = new Text(shell, SWT.BORDER);
		modifiedWorkSpaceLoc = getUserWorkspace();
		if(!currentWorkSpaceLoc.equals(NOT_SET)){
			workSpaceText.setText(currentWorkSpaceLoc);
		}else{
			workSpaceText.setText(modifiedWorkSpaceLoc);
		}

		workSpaceText.setEditable(false);
		workSpaceText.setLayoutData(new GridData (SWT.FILL, SWT.TOP, true, true, 3, 1));
		Button browseButton = new Button(shell, SWT.BORDER);
		browseButton.setText(BROWSE_BUTTON_TEXT);
		browseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));

		final Button isWorkSpaceDefault = new Button(shell, SWT.CHECK);
		isWorkSpaceDefault.setText(SET_DEFAULT_WORKSPACE_MESSAGE);
		isWorkSpaceDefault.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 4, 1));
		isWorkSpaceDefault.pack();
		isWorkSpaceDefault.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (isWorkSpaceDefault.getSelection()){
					isDefaultSet = true;
				}
				else{
					isDefaultSet = false;
				}
			}
		});

		Button okButton = new Button(shell, SWT.BORDER);
		okButton.setText(OK_BUTTON_TEXT);
		okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 3, 1));
		final Button cancelButton = new Button(shell, SWT.BORDER);
		cancelButton.setText(CANCEL_BUTTON_TEXT);
		cancelButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));

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
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
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
					if (createdWorkSpace || !modifiedWorkSpaceLoc.equals(defaultNewWorkspace) || useSameWorkSpace == SWT.OK) {
						shell.close();
						userWorkSpaceSet = true;
					}
				} else {
					logger.warn("workspace location is null hence using the default location");
				}
			}
		});
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

	}

	public static boolean openWorkSpaceBrowser() {
		new WorkSpaceSelector();
		return isDefaultSet;
	}

	public static boolean isUserWorkSpaceSet(){
		return  userWorkSpaceSet;
	}

	/**
	 * read the workspace location set for the IDE
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
			logger.error("error in writing whether user has set default workspace to IDE properties" + e1);
		}
	}

	/**
	 *  create the default workspace location
	 */
	private boolean createDefaultWorkSpaceDirectory(String workSpace) {
		boolean workSpaceCreationSuccess = false;
		if (userHome != null) {
			File workspaceDir = new File(workSpace);
			if (!workspaceDir.exists()) {
				logger.info("creating the workspace directory DevSWorkSpace in your home directory");
				try {
					workspaceDir.mkdir();
					workSpaceCreationSuccess = true;
				} catch (SecurityException se) {
					createErrorMessageDialog("Please set permission to create your workspace directory");
				}
				if (workSpaceCreationSuccess) {
					logger.info("successfully created the workspace directory DevSWorkSpace in your home directory at " + workSpace);
				}
			} else {
				useSameWorkSpace = createErrorMessageDialog("unable to create the workspace directory, your home directory " +
				 "already has a directory called DevSWorkSpace. Please delete or rename the directory and retry.");
			}
		}
		return workSpaceCreationSuccess;
	}

	/**
	 * get the users home directory and set the new default workspace location
	 * @return new works space location
	 */
	private String getUserWorkspace(){
		String fileSeparator = File.separator;
		try {
			userHome = System.getProperty("user.home");
		}catch (SecurityException se){
			logger.error("unable to get the system home directory, security exception, please set permission");
		}catch (NullPointerException ne){
			logger.error("unable to get the system home directory, the home directory returns null");
		}catch (IllegalArgumentException ie){
			logger.error("unable to get the system root directory, the home directory is empty");
		}
		String newWorkSpace = userHome + fileSeparator + DEV_SWORK_SPACE;
		return  newWorkSpace;
	}

	private int createErrorMessageDialog(String errorMessage){
		Shell errorDialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		errorDialog.setSize(ERROR_DIALOG_WIDTH, ERROR_DIALOG_HEIGHT);
		MessageBox messageDialog = new MessageBox(errorDialog,SWT.ICON_WARNING | SWT.OK | SWT.CANCEL );
		messageDialog.setText(ERROR_DIALOG_MENU_MESSAGE);
		messageDialog.setMessage(errorMessage);
		return messageDialog.open();
	}

}