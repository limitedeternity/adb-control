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

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;

public class AdbControl extends JFrame
{
	public AdbControl()
	{
		Config config = new Config();
		config.load();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("ADB Control");
		setSize(1280 / 3, 720);

		AdbControlPanel panel = new AdbControlPanel(config);
		panel.setAdbHelper(new AdbHelper(config));

		Runtime.getRuntime().addShutdownHook(
		  new Thread() {
				public void run() {
					panel.getAdbHelper().cleanUp();
					System.out.println("Program finished.");
				}
			}
		);

		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public static void main(String[] args)
	{
		try
		{
			AdbControl frame = new AdbControl();
			frame.setVisible(true);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
