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
package org.wso2.developerstudio.workspaceselector.launcher;

import org.apache.commons.io.FilenameUtils;
import org.wso2.developerstudio.workspaceselector.utils.SWTShellManager;
import org.wso2.developerstudio.workspaceselector.utils.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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

/**
 * This class creates the workspace selection dialog for user on developer studio start up.
 * It creates the user input workspace if it does not exist, and save the workspace into the IDE properties file,
 * to be used to save projects.
 */
public class SWTWorkspaceSelector {
	private static final Logger log = LoggerFactory.getLogger(SWTWorkspaceSelector.class);

	private static final class WorkSpaceDesignParameters {
		/**
		 * Left hand side margin for the workspace shell
		 */
		private static final int SHELL_MARGIN = 0;
		/**
		 * horizontal indentation for the workspace shell
		 */
		private static final int HORIZONTAL_INDENT = 5;
		/**
		 * width hint to expand the layout elements over the full width in workspace shell
		 */
		private static final int WIDTH_HINT = 605;
		/**
		 * header height hint to expand over available height
		 */
		private static final int HEADER_HEIGHT_HINT = 25;
		/**
		 * description height hint to expand over available height
		 */
		private static final int DESCRIPTION_HEIGHT_HINT = 39;
		/**
		 * composite height hint to expand over available height
		 */
		private static final int MIDDLE_COMPOSITE_HEIGHT_HINT = 202;
		/**
		 * workspace label width hint to expand over available width
		 */
		private static final int WORKSPACE_LBL_WIDTH_HINT = 84;
		/**
		 * workspace text width hint to expand over available width
		 */
		private static final int WORKSPACE_TXT_WIDTH_HINT = 374;
		/**
		 * browse button width hint to expand over available width
		 */
		private static final int BROWSE_BTN_WIDTH_HINT = 85;
		/**
		 * bottom composite height hint to expand over available height
		 */
		private static final int BOTTOM_COMPOSITE_HEIGHT_HINT = 118;
		/**
		 * bottom composite width hint to expand over available width
		 */
		private static final int BOTTOM_COMPOSITE_WIDTH_HINT = 584;
		/**
		 * check button width hint to expand over available width
		 */
		private static final int CHECK_BTN_WIDTH_HINT = 382;
		/**
		 * cancel button width hint to expand over available width
		 */
		private static final int CANCEL_BTN_WIDTH_HINT = 87;
		/**
		 * ok button width hint to expand over available width
		 */
		private static final int OK_BTN_WIDTH_HINT = 89;
		/**
		 * horizontal span constant for grid data
		 */
		private static final int HORIZONTAL_SPAN = 1;
		/**
		 * vertical span constant for grid data
		 */
		private static final int VERTICAL_SPAN = 1;
		/**
		 * horizontal span of 4 for grid data
		 */
		private static final int HORIZONTAL_SPAN_4 = 4;
		/**
		 * horizontal span of 3 for grid data
		 */
		private static final int HORIZONTAL_SPAN_3 = 3;
		/**
		 * no of columns for grid layouts
		 */
		private static final int NO_OF_COLUMNS = 5;
		/**
		 * Error dialog window shell width
		 */
		private static final int ERROR_DIALOG_WIDTH = 400;
		/**
		 * Error dialog window shell height
		 */
		private static final int ERROR_DIALOG_HEIGHT = 100;
		/**
		 * constant to  multiply current monitor to get the shell size dynamically
		 */
		private static final double WIDTH_CONSTANT = 0.3;
		private static final double HEIGHT_CONSTANT = 0.3;
	}

	/**
	 * passing the SWT specific parameters to disable shell resizing
	 */
	private static final int SHELL_TRIM = SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX | SWT.RESIZE;

	public static final String DEVSTUDIO_WORKSPACE_PROPERTY = "developerstudio.workspace";
	private static final ResourceBundle LAUNCHER_BUNDLE =
			PropertyResourceBundle.getBundle("org.wso2.developerstudio.workspaceselector.launcher.messages");
	private static final File DEV_CONFIG_PROPERTY_FILE = new File(System.getProperty(
			ConfigurationContext.DEV_STUDIO_CONF_DIR) + File.separator + ConfigurationContext.DEV_STUDIO_PROPERTIES_FILE);

	private Shell workSpaceShell;
	private Text workSpaceText;
	private Button okButton;
	private boolean isDefaultSet;
	private boolean userWorkSpaceSet;
	private boolean workspaceTextModified;
	/**
	 * Internationalized user message parameters
	 */
	private static final String USER_HOME = "user.home";

	private static final String DEV_STUDIO_WORK_SPACE = "value.default_workspace";
	private static final String OK_BUTTON_TEXT = "btn.ok";
	private static final String CANCEL_BUTTON_TEXT = "btn.cancel";
	private static final String BROWSE_BUTTON_TEXT = "btn.browse";
	private static final String USER_MESSAGE_TO_SELECT_WORKSPACE =
			"label.workspace_selector_details";
	private static final String SET_DEFAULT_WORKSPACE_MESSAGE = "label.use_as_default";
	private static final String BROWSE_DIALOG_MENU_MESSAGE = "label.select_workspace";
	private static final String WARNING_DIALOG_MENU_MESSAGE = "label.warning";
	private static final String WORKSPACE_LABEL = "label.workspace";
	private static final String WSO2_DEVELOPER_STUDIO = "label.developer_studio";

	private static final SWTShellManager CENTER_SWT_SHELL = new SWTShellManager();
	private static final Display WORK_SPACE_DISPLAY = Display.getDefault();

	public SWTWorkspaceSelector() {
		workSpaceWindowInit();
		workSpaceShell.pack();
		CENTER_SWT_SHELL.centerShellInDisplay(workSpaceShell); // center the shell in the current screen
		workSpaceShell.open();
		/**
		 * Standard Eclipse recommended way of keeping an SWT widget alive until disposed by source,
		 * eg: http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fswt.htm
		 */
		while (!workSpaceShell.isDisposed()) {
			if (!WORK_SPACE_DISPLAY.readAndDispatch()) {
				WORK_SPACE_DISPLAY.sleep();
			}
		}
	}

	/**
	 * This method initiates the workspace selector SWT widget with all UI components
	 */
	private void workSpaceWindowInit() {
		workSpaceShell = new Shell(WORK_SPACE_DISPLAY, SHELL_TRIM & (~SWT.RESIZE));
		workSpaceShell.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		setShellSize(workSpaceShell);

		workSpaceShell.setText(LAUNCHER_BUNDLE.getString(WSO2_DEVELOPER_STUDIO));
		GridLayout gl_workspaceShell = new GridLayout(WorkSpaceDesignParameters.HORIZONTAL_SPAN, false);
		gl_workspaceShell.marginWidth = WorkSpaceDesignParameters.SHELL_MARGIN;
		workSpaceShell.setLayout(gl_workspaceShell);

		Label headerLabel = new Label(workSpaceShell, SWT.NONE); // header line of the workspace dialog
		headerLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		headerLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_headerLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                       WorkSpaceDesignParameters.HORIZONTAL_SPAN,
		                                       WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_headerLabel.horizontalIndent = WorkSpaceDesignParameters.HORIZONTAL_INDENT;
		gd_headerLabel.heightHint = WorkSpaceDesignParameters.HEADER_HEIGHT_HINT;
		gd_headerLabel.widthHint = WorkSpaceDesignParameters.WIDTH_HINT;
		headerLabel.setLayoutData(gd_headerLabel);
		headerLabel.setText(LAUNCHER_BUNDLE.getString(BROWSE_DIALOG_MENU_MESSAGE));

		Label descriptionLabel = new Label(workSpaceShell, SWT.WRAP); // description label of the workspace dialog
		descriptionLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		descriptionLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		GridData gd_descriptionLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                            WorkSpaceDesignParameters.HORIZONTAL_SPAN,
		                                            WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_descriptionLabel.horizontalIndent = WorkSpaceDesignParameters.HORIZONTAL_INDENT;
		gd_descriptionLabel.widthHint = WorkSpaceDesignParameters.WIDTH_HINT;
		gd_descriptionLabel.heightHint = WorkSpaceDesignParameters.DESCRIPTION_HEIGHT_HINT;
		descriptionLabel.setLayoutData(gd_descriptionLabel);
		descriptionLabel.setText(LAUNCHER_BUNDLE.getString(USER_MESSAGE_TO_SELECT_WORKSPACE));

		/**
		 * composite to separate workspace entry text box, checkbox element and the buttons
		 */
		Composite middleComposite = new Composite(workSpaceShell, SWT.NONE);
		middleComposite.setLayout(new GridLayout(WorkSpaceDesignParameters.HORIZONTAL_SPAN_4, false));
		GridData gd_middleComposite = new GridData(SWT.CENTER, SWT.CENTER, false, false,
		                                           WorkSpaceDesignParameters.HORIZONTAL_SPAN_3,
		                                           WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_middleComposite.heightHint = WorkSpaceDesignParameters.MIDDLE_COMPOSITE_HEIGHT_HINT;
		gd_middleComposite.widthHint = WorkSpaceDesignParameters.WIDTH_HINT;
		middleComposite.setLayoutData(gd_middleComposite);

		/**
		 *  additional labels to fill the gap (invisible labels added on design)
		 */
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);
		new Label(middleComposite, SWT.NONE);

		Label workspaceLabel = new Label(middleComposite, SWT.NONE); // label workspace
		GridData gd_workspaceLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
		                                          WorkSpaceDesignParameters.HORIZONTAL_SPAN,
		                                          WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_workspaceLabel.widthHint = WorkSpaceDesignParameters.WORKSPACE_LBL_WIDTH_HINT;
		workspaceLabel.setLayoutData(gd_workspaceLabel);
		workspaceLabel.setText(LAUNCHER_BUNDLE.getString(WORKSPACE_LABEL));

		workSpaceText = new Text(middleComposite, SWT.BORDER); // text box to input the user preferred workspace
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false,
		                                WorkSpaceDesignParameters.HORIZONTAL_SPAN,
		                                WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_text.widthHint = WorkSpaceDesignParameters.WORKSPACE_TXT_WIDTH_HINT;
		workSpaceText.setLayoutData(gd_text);

		setInitialWorkSpaceText();
		workSpaceText.addModifyListener(new ModifyListener() {
			@Override public void modifyText(ModifyEvent modifyEvent) {
				okButton.setEnabled(!workSpaceText.getText().isEmpty()); // disable ok button if workspace is empty
				workspaceTextModified = true;
			}
		});

		Button browseButton = new Button(middleComposite, SWT.NONE); // browse button file dialog
		GridData gd_btnBrowseButton = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                           WorkSpaceDesignParameters.HORIZONTAL_SPAN,
				                                   WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_btnBrowseButton.widthHint = WorkSpaceDesignParameters.BROWSE_BTN_WIDTH_HINT;
		browseButton.setLayoutData(gd_btnBrowseButton);
		browseButton.setText(LAUNCHER_BUNDLE.getString(BROWSE_BUTTON_TEXT));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				workspaceTextModified = false;
				fileBrowsing();
			}
		});

		Composite bottomComposite =
				new Composite(middleComposite, SWT.NONE); // final composite to separate the ok and cancel buttons
		bottomComposite.setLayout(new GridLayout(WorkSpaceDesignParameters.NO_OF_COLUMNS, false));
		GridData gd_bottomComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                           WorkSpaceDesignParameters.HORIZONTAL_SPAN_4,
		                                           WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_bottomComposite.heightHint = WorkSpaceDesignParameters.BOTTOM_COMPOSITE_HEIGHT_HINT;
		gd_bottomComposite.widthHint = WorkSpaceDesignParameters.BOTTOM_COMPOSITE_WIDTH_HINT;
		bottomComposite.setLayoutData(gd_bottomComposite);

		/**
		 *  additional labels to fill the gap (invisible labels added on design)
		 */
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);

		final Button btnCheckButton =
				new Button(bottomComposite, SWT.CHECK); // check button to set workspace as default
		GridData gd_btnCheckButton = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                          WorkSpaceDesignParameters.HORIZONTAL_SPAN,
		                                          WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_btnCheckButton.widthHint = WorkSpaceDesignParameters.CHECK_BTN_WIDTH_HINT;
		btnCheckButton.setLayoutData(gd_btnCheckButton);
		btnCheckButton.setText(LAUNCHER_BUNDLE.getString(SET_DEFAULT_WORKSPACE_MESSAGE));
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isDefaultSet = btnCheckButton.getSelection();
			}
		});

		/**
		 *  additional labels to fill the gap (invisible labels added on design)
		 */
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);

		Button cancelButton = new Button(bottomComposite, SWT.NONE); // cancel button in the workspace dialog
		GridData gd_cancelButton = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                        WorkSpaceDesignParameters.HORIZONTAL_SPAN,
				                                WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_cancelButton.widthHint = WorkSpaceDesignParameters.CANCEL_BTN_WIDTH_HINT;
		cancelButton.setLayoutData(gd_cancelButton);
		cancelButton.setText(LAUNCHER_BUNDLE.getString(CANCEL_BUTTON_TEXT));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				workSpaceShell.close();
			}
		});

		/**
		 *  additional labels to fill the gap (invisible labels added on design)
		 */
		new Label(bottomComposite, SWT.NONE);
		new Label(bottomComposite, SWT.NONE);

		okButton = new Button(bottomComposite, SWT.NONE); // ok button in the workspace dialog
		GridData gd_okButton = new GridData(SWT.LEFT, SWT.CENTER, false, false,
		                                    WorkSpaceDesignParameters.HORIZONTAL_SPAN,
		                                    WorkSpaceDesignParameters.VERTICAL_SPAN);
		gd_okButton.widthHint = WorkSpaceDesignParameters.OK_BTN_WIDTH_HINT;
		okButton.setLayoutData(gd_okButton);
		okButton.setText(LAUNCHER_BUNDLE.getString(OK_BUTTON_TEXT));
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onOkPress();
			}
		});
	}

	/**
	 * This method sets the initial value for the workspace text field on workspace window creation
	 * currentWorkSpaceLoc the current value saved in the properties file
	 * defaultWorkSpace the default workspace hardcoded for developer studio to be used on first time run,
	 */
	private void setInitialWorkSpaceText() {
		String currentWorkSpaceLoc = getWorkSpaceLoc();
		if (currentWorkSpaceLoc != null) { // read the currently set workspace value in IDE properties
			workSpaceText.setText(currentWorkSpaceLoc);
		} else {
			String defaultWorkSpace = getDefaultUserWorkspace();
			if (defaultWorkSpace != null) {
				workSpaceText.setText(defaultWorkSpace);
			}
		}
	}

	/**
	 * This method is invoked on browse button click to browse files, opens an SWT directory dialog
	 */
	private void fileBrowsing() {
		try {
			DirectoryDialog dirDialog = new DirectoryDialog(workSpaceShell);
			dirDialog.setText(BROWSE_DIALOG_MENU_MESSAGE);
			String selectedDir = dirDialog.open();
			if (selectedDir != null) {
				workSpaceText.setText(selectedDir);
			}
		} catch (SWTException swtE) {
			log.error("Unable to open the directory dialog for file browsing " + swtE.getMessage(), swtE);
			createErrorMessageDialog(LAUNCHER_BUNDLE.getString(
					"error.msg.filebrowser_error"));
		}
	}

	private boolean createWorkSpaceDir(File workSpace) {
		try {
			if (workSpace.mkdir()) {
				log.info("successfully created the workspace directory " + workSpace.getName());
				return true;
			}
		} catch (SecurityException se) {
			createErrorMessageDialog(LAUNCHER_BUNDLE.getString(
					"msg.permission_error"));
			log.error("no permission to create workspace directory, " + se.getMessage(), se);
		}
		return false;
	}

	/**
	 * This method get the users home directory and set the new default workspace location
	 *
	 * @return the default works space location, this is a hard coded directory name in user home
	 */
	private String getDefaultUserWorkspace() {
		String userHome = null;
		String fileSeparator = File.separator;
		try {
			userHome = System.getProperty(USER_HOME);
		} catch (SecurityException se) {
			log.error("unable to get the system home directory, security exception, please set permission", se);
		} catch (NullPointerException ne) {
			log.error("unable to get the system home directory, the home directory returns null", ne);
		} catch (IllegalArgumentException ie) {
			log.error("unable to get the system root directory, the home directory is empty", ie);
		}
		if (null != userHome) {
			return userHome + fileSeparator + LAUNCHER_BUNDLE.getString(DEV_STUDIO_WORK_SPACE);
		} else {
			return LAUNCHER_BUNDLE.getString(DEV_STUDIO_WORK_SPACE);
		}
	}

	private void createErrorMessageDialog(String errorMessage) {
		Shell errorDialog = new Shell(workSpaceShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		errorDialog.setSize(WorkSpaceDesignParameters.ERROR_DIALOG_WIDTH,
		                    WorkSpaceDesignParameters.ERROR_DIALOG_HEIGHT);
		MessageBox messageDialog = new MessageBox(errorDialog, SWT.ICON_WARNING | SWT.OK);
		messageDialog.setText(LAUNCHER_BUNDLE.getString(WARNING_DIALOG_MENU_MESSAGE));
		messageDialog.setMessage(errorMessage);
		messageDialog.open();
	}

	/**
	 * This method is invoked on ok press to call to validate workspace and to call the method to close the window okPress
	 */
	private void onOkPress() {
		String userSetWorkspace = workSpaceText.getText();
		if (workspaceTextModified) {
			userSetWorkspace = validateUserWorkSpace(userSetWorkspace);
			if (userSetWorkspace != null) {
				okPressed(userSetWorkspace);
			} else {
				createErrorMessageDialog(LAUNCHER_BUNDLE.getString("msg.invalid_workspace"));
			}
		} else {
			okPressed(userSetWorkspace);
		}
	}

	/**
	 * This method is called on ok button press of workspace selector class
	 * after validating the user workspace
	 */
	private void okPressed(String userWorkspace) {
		File newWorkspace = new File(userWorkspace);
		if (!newWorkspace.exists()) { // if the user specified workspace is non existing
			if (createWorkSpaceDir(newWorkspace)) {
				if (saveWorkSpaceProperty(userWorkspace)) {
					userWorkSpaceSet = true; // user has set the workspace
					workSpaceShell.close();
				} else {
					createErrorMessageDialog(MessageFormat.format(LAUNCHER_BUNDLE.getString(
							LAUNCHER_BUNDLE.getString("error.msg.unable_to_save_workspace")),
					                                              userWorkspace));
				}
			} else {
				createErrorMessageDialog(MessageFormat.format(LAUNCHER_BUNDLE.getString(
						"msg.workspace_creation_error"), userWorkspace));
			}
		} else {
			if (saveWorkSpaceProperty(userWorkspace)) {
				userWorkSpaceSet = true;
				workSpaceShell.close();
			} else {
				createErrorMessageDialog(MessageFormat.format(LAUNCHER_BUNDLE.getString(
						"error.msg.unable_to_save_workspace"), userWorkspace));
			}
		}
	}

	/**
	 * This method validates the user workspace , when entered by hand
	 * if it is just a string with no slashes create it in the home directory
	 * else check if the root level directory is existing, else create the directory in the home directory
	 *
	 * @param workspace test input by user
	 * @return the workspace to be created
	 */
	private String validateUserWorkSpace(String workspace) {
		workspace = FilenameUtils.normalize(workspace); // if not a valid file name will return null
//TODO	redo this code thinking of all environments (windows)
//		String homeLoc = System.getProperty(USER_HOME);
//		if (workspace != null) {
//			if (workspace.contains(File.separator)) {
//				String[] workSpaceToCheck = workspace.split(File.separator);
//				File fileLocToCheck = new File(File.separator + workSpaceToCheck[1]);
//				if (null != homeLoc && !fileLocToCheck.exists()) {
//					return homeLoc + File.separator + workspace;
//				}
//			} else if (homeLoc != null) {
//				return homeLoc + File.separator + workspace;
//			}
//		}
		return workspace;
	}

	public boolean isUserWorkSpaceSet() {
		return userWorkSpaceSet;
	}

	/**
	 * This method read the workspace location set for the IDE
	 *
	 * @return the current workspace location set for the IDE
	 */
	private String getWorkSpaceLoc() {
		if (DEV_CONFIG_PROPERTY_FILE.exists()) {
			try {
				return ConfigurationContext.getWorkspaceRoot();
			} catch (IOException e) {
				log.error("error in reading the workspace directory from properties" + e, e);
				return null;
			}
		}
		return null;
	}

	/**
	 * This method to set height and width values of the workspace shell according to current monitor
	 *
	 * @param inputShell the shell to set size
	 */
	private void setShellSize(Shell inputShell) {
		Monitor currentMonitor = CENTER_SWT_SHELL.getClosestMonitor(inputShell.getDisplay());
		double width = currentMonitor.getBounds().width * WorkSpaceDesignParameters.WIDTH_CONSTANT;
		double height = currentMonitor.getBounds().height * WorkSpaceDesignParameters.HEIGHT_CONSTANT;

		Long longWidth = Math.round(width);
		Long longHeight = Math.round(height);

		inputShell.setSize(longWidth.intValue(), longHeight.intValue());
	}

	/**
	 * This method saves the user set workspace property into IDE property file
	 *
	 * @param workspace value set by user
	 * @return status of writing to property file true if success
	 */
	private boolean saveWorkSpaceProperty(String workspace) {
		/*TODO need to finalize with Eclipse Che dev team to how to save this property to be taken as the workspace for the IDE*/
		try {
			if (!DEV_CONFIG_PROPERTY_FILE.exists()) { // create the properties file if not existing
				if (DEV_CONFIG_PROPERTY_FILE.createNewFile()) {
					if (log.isDebugEnabled()) {
						log.debug(
								"Successfully created the developer studio configurations file on initial run of developer studio . ");
					}
				} else {
					if (log.isDebugEnabled()) {
						log.debug(
								"Could not create the developer studio configurations file on initial run of developer studio . ");
					}
				}
			}
			System.setProperty(DEVSTUDIO_WORKSPACE_PROPERTY, workspace);
			ConfigurationContext.setWorkspaceRoot(workspace); // set the workspace into properties file
			ConfigurationContext.setAsDefaultWorkSpace(isDefaultSet); // set the check button result to properties file
			return true;
		} catch (IOException e) {
			log.error("Error in writing to workspace properties in to IDE properties " + e);
			return false;
		}
	}
}
