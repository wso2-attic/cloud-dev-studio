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

/**
 * This class stores all the design parameters necessary for the SplashScreen
 */
public class SplashScreenDesignParameters {

	private String splashImageLoc;
	private int shellWidth;
	private int shellHeight;

	private int maximumProgress;
	private int imageLabelWidth;
	private int imageLabelHeight;

	private int progressBarWidth;
	private int progressBarHeight;
	private int progressBarXLoc;
	private int progressBarYLoc;

	String getSplashImageLoc() {
		return splashImageLoc;
	}

	void setSplashImageLoc(String splashImageLoc) {
		this.splashImageLoc = splashImageLoc;
	}

	int getShellWidth() {
		return shellWidth;
	}

	void setShellWidth(int shellWidth) {
		this.shellWidth = shellWidth;
	}

	int getShellHeight() {
		return shellHeight;
	}

	void setShellHeight(int shellHeight) {
		this.shellHeight = shellHeight;
	}

	int getMaximumProgress() {
		return maximumProgress;
	}

	void setMaximumProgress(int maximumProgress) {
		this.maximumProgress = maximumProgress;
	}

	int getImageLabelWidth() {
		return imageLabelWidth;
	}

	void setImageLabelWidth(int imageLabelWidth) {
		this.imageLabelWidth = imageLabelWidth;
	}

	int getImageLabelHeight() {
		return imageLabelHeight;
	}

	void setImageLabelHeight(int imageLabelHeight) {
		this.imageLabelHeight = imageLabelHeight;
	}

	int getProgressBarWidth() {
		return progressBarWidth;
	}

	void setProgressBarWidth(int progressBarWidth) {
		this.progressBarWidth = progressBarWidth;
	}

	int getProgressBarHeight() {
		return progressBarHeight;
	}

	void setProgressBarHeight(int progressBarHeight) {
		this.progressBarHeight = progressBarHeight;
	}

	int getProgressBarXLoc() {
		return progressBarXLoc;
	}

	void setProgressBarXLoc(int progressBarXLoc) {
		this.progressBarXLoc = progressBarXLoc;
	}

	int getProgressBarYLoc() {
		return progressBarYLoc;
	}

	void setProgressBarYLoc(int progressBarYLoc) {
		this.progressBarYLoc = progressBarYLoc;
	}
}
