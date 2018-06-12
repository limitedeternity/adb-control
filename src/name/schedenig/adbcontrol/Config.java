/*******************************************************************************
 * Copyright (c) 2014 Marian Schedenig
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marian Schedenig - initial API and implementation
 *    Marise Hayashi - fixes
 *******************************************************************************/
package name.schedenig.adbcontrol;

import java.io.InputStream;
import java.util.Properties;

public class Config
{
	private String adbCommand;
	private long screenshotDelay;
	private String localImageFilePath;
	private String phoneImageFilePath;

  Utils utils = new Utils();

	public void load()
	{
		this.adbCommand = utils.lookForProgramInPath("adb");
		this.screenshotDelay = Long.parseLong("20");
		this.localImageFilePath = utils.buildAbsPath("adbcontrol_screenshot.png");
		this.phoneImageFilePath = "/sdcard/adbcontrol_screenshot.png";
	}

	public String getAdbCommand()
	{
		return this.adbCommand;
	}

	public long getScreenshotDelay()
	{
		return this.screenshotDelay;
	}

	public String getLocalImageFilePath()
	{
		return this.localImageFilePath;
	}

	public String getPhoneImageFilePath()
	{
		return this.phoneImageFilePath;
	}
}
