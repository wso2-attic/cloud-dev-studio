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
package org.wso2.developerstudio.workspace.launcher.java;

import org.developerstudio.workspace.utils.CenterSWTShell;
import org.developerstudio.workspace.utils.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import static org.developerstudio.workspace.utils.CenterSWTShell.centerShellInDisplay;

/**
 * creates the workspace launcher to get user details on workspace directory and to set a default workspace at developer studio startup
 */
public class WorkSpaceWindow {
	private static final ResourceBundle
			launcherBundle = PropertyResourceBundle.getBundle("wso2.developerstudio.workspace.launcher.java.launcher");
	private static final Logger logger = LoggerFactory.getLogger(WorkSpaceWindow.class);

	public static final int SHELL_MARGIN = 0; //margin for the swt shell
	public static final int HORIZONTAL_INDENT = 5; //horizontal indent for the shell
	public static final int WIDTH_HINT = 605;  // width hint to expand the layout elements over the full width
	public static final int HEADER_HEIGHT_HINT = 25; // header height hint to expand over available height
	public static final int DESCRIPTION_HEIGHT_HINT = 39; // description height hint to expand over available height
	public static final int MIDDLE_COMPOSITE_HEIGHT_HINT = 202; // composite height hint to expand over available height
	public static final int WORKSPACE_LBL_WIDTH_HINT = 84; // workspace label width hint to expand over available width
	public static final int WORKSPACE_TXT_WIDTH_HINT = 374; // workspace text width hint to expand over available width
	public static final int BROWSE_BTN_WIDTH_HINT = 85; // browse button width hint to expand over available width
	public static final int BOTTOM_COMPOSITE_HEIGHT_HINT = 118; // bottom composite height hint to expand over available height
	public static final int BOTTOM_COMPOSITE_WIDTH_HINT = 584; // bottom composite width hint to expand over available width
	public static final int CHECK_BTN_WIDTH_HINT = 382; // check button width hint to expand over available width
	public static final int CANCEL_BTN_WIDTH_HINT = 87; // cancel button width hint to expand over available width
	public static final int OK_BTN_WIDTH_HINT = 89; // ok button width hint to expand over available width
	public static final int HORIZONTAL_SPAN = 1; // horizontal span constant for grid data
	public static final int VERTICAL_SPAN = 1; // vertical span constant for grid data
	public static final int HORIZONTAL_SPAN_4 = 4; // horizontal span of 4 for grid data
	public static final int HORIZONTAL_SPAN_3 = 3; // horizontal span of 3 for grid data
	public static final int NO_OF_COLUMNS = 5; // no of columns for grid layouts
	public static final int ERROR_DIALOG_WIDTH = 400; //Error dialog window shell width
	public static final int ERROR_DIALOG_HEIGHT = 100; //Error dialog window shell height
	public static final double WIDTH_CONSTANT = 0.297; // constant to  multiply current monitor to get the shell size dynamically
	public static final double HEIGHT_CONSTANT = 0.278;

	private Display workSpaceDisplay;
	public static final int SHELL_TRIM = SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE;

	private Shell workSpaceShell;
	private Text workSpaceText;
	private Button okButton;
	private boolean isDefaultSet = false;
	private String modifiedWorkSpaceLoc = null;
	private boolean userWorkSpaceSet = false;
	private boolean workspaceTextModified = false;

	private static final String USER_HOME = "user.home";
	private static final String DEV_STUDIO_WORK_SPACE = "devstudioworkspace";
	private static final String NOT_SET = "NOT_SET";
	private static final String OK_BUTTON_TEXT = "ok";
	private static final String CANCEL_BUTTON_TEXT = "cancel";
	private static final String BROWSE_BUTTON_TEXT = "browse";
	private static final String USER_MESSAGE_TO_SELECT_WORKSPACE =
			"wso2.developer.studio.stores.your.projects.in.a.folder.called.workspace.n.choose.a.workspace.folder.to.use.for.this.session";
	private static final String SET_DEFAULT_WORKSPACE_MESSAGE =
			"use.this.as.default.and.do.not.ask.again";
	private static final String BROWSE_DIALOG_MENU_MESSAGE =
			"select.your.workspace.directory";
	private static final String WARNING_DIALOG_MENU_MESSAGE = "warning";


	private static final String WORKSPACE_LABEL = "workspace";
	private static final String WSO2_DEVELOPER_STUDIO = "wso2.developer.studio";

	/**
	 * This class creates the workspace selection dialog for user on developer studio start up.
	 * It creates the user input workspace if it does not exist, and save the workspace into the IDE properties file,
	 * to be used to save projects.
	 */
	public WorkSpaceWindow() {
		workSpaceDisplay = Display.getDefault();

		init();
		workSpaceShell.pack();

		centerShellInDisplay(workSpaceShell); // center the shell in the current screen
		workSpaceShell.open();

		while (!workSpaceShell.isDisposed()) {
			if (!workSpaceDisplay.readAndDispatch()) {
				workSpaceDisplay.sleep();
			}
		}
	}

	/**
	 * initiates the workspace selector SWT widget
	 */
	private void init() {

		String currentWorkSpaceLoc;
		workSpaceShell = new Shell(workSpaceDisplay, SHELL_TRIM & (~SWT.RESIZE));
		workSpaceShell.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		setShellSize(workSpaceShell);

		workSpaceShell.setText(launcherBundle.getString(WSO2_DEVELOPER_STUDIO));
		GridLayout gl_workspaceShell = new GridLayout(HORIZONTAL_SPAN, false);
		gl_workspaceShell.marginWidth = SHELL_MARGIN;
		workSpaceShell.setLayout(gl_workspaceShell);

		Label headerLabel = new Label(workSpaceShell, SWT.NONE);
		headerLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		headerLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, HORIZONTAL_SPAN,
		                                       VERTICAL_SPAN);
		gd_lblNewLabel.horizontalIndent = HORIZONTAL_INDENT;
		gd_lblNewLabel.heightHint = HEADER_HEIGHT_HINT;
		gd_lblNewLabel.widthHint = WIDTH_HINT;
		headerLabel.setLayoutData(gd_lblNewLabel);
		headerLabel.setText(launcherBundle.getString(BROWSE_DIALOG_MENU_MESSAGE));

		Label descriptionLabel = new Label(workSpaceShell, SWT.WRAP);
		descriptionLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		descriptionLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_lblNewLabel_1.horizontalIndent = HORIZONTAL_INDENT;
		gd_lblNewLabel_1.widthHint = WIDTH_HINT;
		gd_lblNewLabel_1.heightHint = DESCRIPTION_HEIGHT_HINT;
		descriptionLabel.setLayoutData(gd_lblNewLabel_1);
		descriptionLabel.setText(launcherBundle.getString(USER_MESSAGE_TO_SELECT_WORKSPACE));

		Composite middleComposite = new Composite(workSpaceShell, SWT.NONE);
		middleComposite.setLayout(new GridLayout(HORIZONTAL_SPAN_4, false));
		GridData gd_composite = new GridData(SWT.CENTER, SWT.CENTER, false, false, HORIZONTAL_SPAN_3, VERTICAL_SPAN);
		gd_composite.heightHint = MIDDLE_COMPOSITE_HEIGHT_HINT;
		gd_composite.widthHint = WIDTH_HINT;
		middleComposite.setLayoutData(gd_composite);

		Label workspaceLabel = new Label(middleComposite, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
		                                         HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_lblNewLabel_2.widthHint = WORKSPACE_LBL_WIDTH_HINT;
		workspaceLabel.setLayoutData(gd_lblNewLabel_2);
		workspaceLabel.setText(launcherBundle.getString(WORKSPACE_LABEL));

		workSpaceText = new Text(middleComposite, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_text.widthHint = WORKSPACE_TXT_WIDTH_HINT;
		workSpaceText.setLayoutData(gd_text);
		currentWorkSpaceLoc = getWorkSpaceLoc();
		modifiedWorkSpaceLoc = getUserWorkspace();
		if (!currentWorkSpaceLoc.equals(NOT_SET)) {
			workSpaceText.setText(currentWorkSpaceLoc);
		} else {
			workSpaceText.setText(modifiedWorkSpaceLoc);
		}
		workSpaceText.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent modifyEvent) {
				okButton.setEnabled(!workSpaceText.getText()
				                                  .isEmpty()); // disable ok button incase workspace entered is empty
				workspaceTextModified = true;
			}
		});

		new Label(middleComposite, SWT.NONE);

		Button browseButton = new Button(middleComposite, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_btnNewButton.widthHint = BROWSE_BTN_WIDTH_HINT;
		browseButton.setLayoutData(gd_btnNewButton);
		browseButton.setText(launcherBundle.getString(BROWSE_BUTTON_TEXT));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dirDialog = new DirectoryDialog(workSpaceShell);
				dirDialog.setText(BROWSE_DIALOG_MENU_MESSAGE);
				String selectedDir = dirDialog.open();
				if (selectedDir != null) {
					workSpaceText.setText(selectedDir);
					modifiedWorkSpaceLoc = selectedDir;
				}
			}
		});
		new Label(middleComposite, SWT.NONE);// additional labels to fill the gap (invisible labels added on design)
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);

		Composite bottomComposite = new Composite(middleComposite, SWT.NONE);
		bottomComposite.setLayout(new GridLayout(NO_OF_COLUMNS, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                       HORIZONTAL_SPAN_4, VERTICAL_SPAN);
		gd_composite_1.heightHint = BOTTOM_COMPOSITE_HEIGHT_HINT;
		gd_composite_1.widthHint = BOTTOM_COMPOSITE_WIDTH_HINT;
		bottomComposite.setLayoutData(gd_composite_1);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);

		final Button btnCheckButton = new Button(bottomComposite, SWT.CHECK);
		GridData gd_btnCheckButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_btnCheckButton.widthHint = CHECK_BTN_WIDTH_HINT;
		btnCheckButton.setLayoutData(gd_btnCheckButton);
		btnCheckButton.setText(launcherBundle.getString(SET_DEFAULT_WORKSPACE_MESSAGE));
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isDefaultSet = btnCheckButton.getSelection();
			}
		});
		new Label(bottomComposite, SWT.NONE);// additional labels to fill the gap (invisible labels added on design)
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);

		Button cancelButton = new Button(bottomComposite, SWT.NONE);
		GridData gd_cancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_cancelButton.widthHint = CANCEL_BTN_WIDTH_HINT;
		cancelButton.setLayoutData(gd_cancelButton);
		cancelButton.setText(launcherBundle.getString(CANCEL_BUTTON_TEXT));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				workSpaceShell.close();
			}
		});

		new Label(bottomComposite, SWT.NONE);// additional labels to fill the gap (invisible labels added on design)
		new Label(bottomComposite, SWT.NONE);

		okButton = new Button(bottomComposite, SWT.NONE);
		GridData gd_okButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, HORIZONTAL_SPAN, VERTICAL_SPAN);
		gd_okButton.widthHint = OK_BTN_WIDTH_HINT;
		okButton.setLayoutData(gd_okButton);
		okButton.setText(launcherBundle.getString(OK_BUTTON_TEXT));
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				okPressed();
			}
		});
	}

	/**
	 * create the default workspace location
	 */
	private boolean createDefaultWorkSpaceDirectory(File workSpace) {
		try {
			if (workSpace.mkdir()) {
				logger.info("successfully created the workspace directory " + workSpace.getName());
				return true;
			}
		} catch (SecurityException se) {
			createErrorMessageDialog(launcherBundle
					   .getString("please.set.permission.to.create.your.workspace.directory")
			);
			logger.error("no permission to create workspace directory, " + se);
		}
		return false;
	}

	/**
	 * get the users home directory and set the new default workspace location
	 *
	 * @return new works space location
	 */
	private String getUserWorkspace() {
		String userHome = null;
		String fileSeparator = File.separator;
		try {
			userHome = System.getProperty(USER_HOME);
		} catch (SecurityException se) {
			logger.error(
				"unable to get the system home directory, security exception, please set permission", se);
		} catch (NullPointerException ne) {
			logger.error(
					"unable to get the system home directory, the home directory returns null", ne);
		} catch (IllegalArgumentException ie) {
			logger.error("unable to get the system root directory, the home directory is empty", ie);
		}
		return userHome + fileSeparator + launcherBundle.getString(DEV_STUDIO_WORK_SPACE);
	}

	/**
	 * when the default workspace newWorkSpace is already existing
	 *
	 * @param errorMessage the message to be displayed on error dialog
	 * @return the status of the user input, yes or cancel
	 */
	private int createErrorMessageDialog(String errorMessage) {
		Shell errorDialog = new Shell(workSpaceShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		errorDialog.setSize(ERROR_DIALOG_WIDTH,
		                    ERROR_DIALOG_HEIGHT);
		MessageBox messageDialog = new MessageBox(errorDialog, SWT.ICON_WARNING | SWT.OK);
		messageDialog.setText(launcherBundle.getString(WARNING_DIALOG_MENU_MESSAGE));
		messageDialog.setMessage(errorMessage);
		return messageDialog.open();
	}

	/**
	 * On ok button press of workspace selector class
	 */
	private void okPressed() {
		if (workspaceTextModified) { //if user enterd the workspace by keyboard, validate the entered string
			modifiedWorkSpaceLoc = validateUserWorkSpace(workSpaceText.getText());
		}
		File newWorkspace = new File(modifiedWorkSpaceLoc);
		if (!newWorkspace.exists()) {
			if (createDefaultWorkSpaceDirectory(newWorkspace)) {
				try {
					ConfigManager.setWorkspaceDirectory(modifiedWorkSpaceLoc);
					ConfigManager.setDefaultWorkSpaceProperty(String.valueOf(isDefaultSet));
					userWorkSpaceSet = true;
					workSpaceShell.close();
				} catch (IOException e) {
					logger.error(
						"Error in writing to workspace properties in to IDE properties " + e);
				}
			} else {
				createErrorMessageDialog(MessageFormat.format(launcherBundle.getString(
					"unable.to.create.the.workspace.0.specified.please.retry"), modifiedWorkSpaceLoc));
			}
		} else {
			userWorkSpaceSet = true;
			workSpaceShell.close();
		}
	}

	/**
	 * validates the user workspace , when entered by hand
	 *
	 * @param workspace test input by user
	 * @return he workspace to be created
	 */
	private String validateUserWorkSpace(String workspace) {
		if (workspace.contains(File.separator)) {
			String[] workSpaceArr = workspace.split(File.separator);
			File checkExistence = new File(File.separator + workSpaceArr[1]);
			if (!checkExistence.exists()) {
				return System.getProperty(USER_HOME) + workspace;

			} else {
				return workspace;
			}
		} else {
			return System.getProperty(USER_HOME) + File.separator + workspace;
		}
	}

	public boolean isUserWorkSpaceSet() {
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
			logger.error("error in reading the workspace directory from properties" + e, e);
		}
		return defaultWorkSpace;
	}

	/**
	 * method to set height and width values of the workspace shell according to current monitor
	 * @param inputShell the shell to set size
	 */
	private void setShellSize(Shell inputShell){
		CenterSWTShell centeringShell = new CenterSWTShell();
		Monitor currentMonitor = centeringShell.getClosestMonitor(inputShell.getDisplay());
		double width = currentMonitor.getBounds().width * WIDTH_CONSTANT;
		double height = currentMonitor.getBounds().height * HEIGHT_CONSTANT;

		Long longWidth = Math.round(width);
		Long longHeight = Math.round(height);

		inputShell.setSize( Integer.valueOf(longWidth.intValue()), Integer.valueOf(longHeight.intValue()));
	}

}
