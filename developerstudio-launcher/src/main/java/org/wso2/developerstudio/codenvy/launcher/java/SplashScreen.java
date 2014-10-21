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

import org.slf4j.LoggerFactory;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.JProgressBar;
import java.awt.Dimension;

/**
 * generating the splash screen custom for WSO2 dev studio
 */
@SuppressWarnings("serial")
public class SplashScreen extends JFrame {
	public static final int PROGRESS_BAR_WIDTH = 300;

	private static final org.slf4j.Logger logger =
			LoggerFactory.getLogger(SplashScreen.class);
	BorderLayout borderLayout1 = new BorderLayout();
	JLabel imageLabel = new JLabel();
	JPanel southPanel = new JPanel();
	FlowLayout southPanelFlowLayout = new FlowLayout();
	JProgressBar progressBar = new JProgressBar();
	ImageIcon imageIcon;

	public SplashScreen(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
		try {
			jbInit();
		} catch (Exception ex) {
			logger.error("Error intializing Splash Screen. " + ex.getMessage(), ex);
		}
	}

	// note - this class created with JBuilder
	void jbInit() throws Exception {
		imageLabel.setIcon(imageIcon);
		this.getContentPane().setLayout(borderLayout1);
		southPanel.setLayout(southPanelFlowLayout);
		southPanel.setBackground(Color.WHITE);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		this.getContentPane().add(imageLabel, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		Color color = new Color(128, 128, 128);
		southPanel.setBackground(color);
		Dimension prefSize = progressBar.getPreferredSize(); // set the width for progress bar
		prefSize.width = PROGRESS_BAR_WIDTH;
		progressBar.setPreferredSize(prefSize);
		southPanel.add(progressBar, null);
		this.pack();
	}

	public void setProgressMax(int maxProgress) {
		progressBar.setMaximum(maxProgress);
	}

	public void setProgress(int progress) {
		final int theProgress = progress;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setValue(theProgress);
			}
		});
	}

	public void setProgress(String message, int progress) {
		final int theProgress = progress;
		final String theMessage = message;
		setProgress(progress);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setValue(theProgress);
				setMessage(theMessage);
			}
		});
	}

	public void setScreenVisible(boolean b) {
		final boolean boo = b;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(boo);
			}
		});
	}

	private void setMessage(String message) {
		if (message == null) {
			message = "";
			progressBar.setStringPainted(false);
		} else {
			progressBar.setStringPainted(true);
		}
		progressBar.setString(message);
	}
}
