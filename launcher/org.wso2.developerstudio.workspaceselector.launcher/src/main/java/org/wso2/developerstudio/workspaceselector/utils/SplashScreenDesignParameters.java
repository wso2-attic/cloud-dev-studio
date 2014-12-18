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

package org.wso2.developerstudio.workspaceselector.utils;

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

	public String getSplashImageLoc() {
		return splashImageLoc;
	}

	public void setSplashImageLoc(String splashImageLoc) {
		this.splashImageLoc = splashImageLoc;
	}

	public int getShellWidth() {
		return shellWidth;
	}

	public void setShellWidth(int shellWidth) {
		this.shellWidth = shellWidth;
	}

	public int getShellHeight() {
		return shellHeight;
	}

	public void setShellHeight(int shellHeight) {
		this.shellHeight = shellHeight;
	}

	public int getMaximumProgress() {
		return maximumProgress;
	}

	public void setMaximumProgress(int maximumProgress) {
		this.maximumProgress = maximumProgress;
	}

	public 	int getImageLabelWidth() {
		return imageLabelWidth;
	}

	public void setImageLabelWidth(int imageLabelWidth) {
		this.imageLabelWidth = imageLabelWidth;
	}

	public int getImageLabelHeight() {
		return imageLabelHeight;
	}

	public void setImageLabelHeight(int imageLabelHeight) {
		this.imageLabelHeight = imageLabelHeight;
	}

	public int getProgressBarWidth() {
		return progressBarWidth;
	}

	public void setProgressBarWidth(int progressBarWidth) {
		this.progressBarWidth = progressBarWidth;
	}

	public int getProgressBarHeight() {
		return progressBarHeight;
	}

	public void setProgressBarHeight(int progressBarHeight) {
		this.progressBarHeight = progressBarHeight;
	}

	public int getProgressBarXLoc() {
		return progressBarXLoc;
	}

	public void setProgressBarXLoc(int progressBarXLoc) {
		this.progressBarXLoc = progressBarXLoc;
	}

	public int getProgressBarYLoc() {
		return progressBarYLoc;
	}

	public void setProgressBarYLoc(int progressBarYLoc) {
		this.progressBarYLoc = progressBarYLoc;
	}
}
