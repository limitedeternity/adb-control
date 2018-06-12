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

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class AdbControlPanel extends JPanel implements MouseListener, KeyListener
{
	private AdbHelper adbHelper;
	private File imageFile;
	private BufferedImage image;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private double ratio;
	protected Thread updateThread;
	private int downX;
	private int downY;
	private Config config;

	public AdbControlPanel(Config config)
	{
		this.config = config;

		this.imageFile = new File(this.config.getLocalImageFilePath());

		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentHidden(ComponentEvent e)
			{
				stopUpdateThread();
			}

			@Override
			public void componentResized(ComponentEvent e)
			{
				requestFocus();
				requestFocusInWindow();

				startUpdateThread();
			}
		});

		addMouseListener(this);
		addKeyListener(this);
	}

	protected void stopUpdateThread()
	{
		if(this.updateThread != null)
		{
			this.updateThread.interrupt();
			this.updateThread = null;
		}
	}

	protected void startUpdateThread()
	{
		if(this.updateThread == null)
		{
			this.updateThread = new Thread()
			{
				@Override
				public void run()
				{
					while(!Thread.interrupted())
					{
						makeScreenshot();

						try
						{
							Thread.sleep(config.getScreenshotDelay());
						}
						catch(InterruptedException ex)
						{
							break;
						}
					}
				}
			};

			this.updateThread.start();
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if(this.image != null)
		{
			this.screenWidth = this.image.getWidth();
			this.screenHeight = this.image.getHeight();

			int width = getWidth();
			int height = getHeight();

			double ratioX = (double) width / (double) screenWidth;
			double ratioY = (double) height / (double) screenHeight;

			this.ratio = Math.min(1, Math.min(ratioX, ratioY));

			double scaledWidth = (double) screenWidth * ratio;
			double scaledHeight = (double) screenHeight * ratio;

			g.drawImage(this.image, 0, 0, (int) scaledWidth, (int) scaledHeight, null);
		}
	}

	public AdbHelper getAdbHelper()
	{
		return this.adbHelper;
	}

	public void setAdbHelper(AdbHelper adbHelper)
	{
		this.adbHelper = adbHelper;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(this.downX < 0 || this.downY < 0)
		{
			return;
		}

		if(this.screenWidth <= 0)
		{
			return;
		}

		int x = (int) ((double) e.getX() / ratio);
		int y = (int) ((double) e.getY() / ratio);

		if(this.adbHelper != null)
		{
			this.adbHelper.sendClick(x, y);
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.downX = e.getX();
		this.downY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		int upX = e.getX();
		int upY = e.getY();

		int dx = Math.abs(downX - upX);
		int dy = Math.abs(downY - upY);

		if(dx < 5 && dy < 5)
		{
			return;
		}

		int screenDownX = (int) ((double) downX / ratio);
		int screenDownY = (int) ((double) downY / ratio);
		int screenUpX = (int) ((double) upX / ratio);
		int screenUpY = (int) ((double) upY / ratio);

		this.adbHelper.sendSwipe(screenDownX, screenDownY, screenUpX, screenUpY);

		this.downX = -1;
		this.downY = -1;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
				this.adbHelper.sendKey(AndroidKey.ENTER);
				return;

			case KeyEvent.VK_ESCAPE:
				this.adbHelper.sendKey(AndroidKey.BACK);
				return;

			case KeyEvent.VK_HOME:
				this.adbHelper.sendKey(AndroidKey.HOME);
				return;

			case KeyEvent.VK_BACK_SPACE:
				this.adbHelper.sendKey(AndroidKey.DEL);
				return;

			case KeyEvent.VK_UP:
				this.adbHelper.sendKey(AndroidKey.DPAD_UP);
				return;

			case KeyEvent.VK_DOWN:
				this.adbHelper.sendKey(AndroidKey.DPAD_DOWN);
				return;

			case KeyEvent.VK_LEFT:
				this.adbHelper.sendKey(AndroidKey.DPAD_LEFT);
				return;

			case KeyEvent.VK_RIGHT:
				this.adbHelper.sendKey(AndroidKey.DPAD_RIGHT);
				return;

			case KeyEvent.VK_SPACE:
			  this.adbHelper.sendKey(AndroidKey.SPACE);
			  return;

			case KeyEvent.VK_END:
			  this.adbHelper.sendKey(AndroidKey.POWER);
			  return;
		}
	}

	private void makeScreenshot()
	{
		this.adbHelper.screenshot(this.imageFile);
		loadImage(this.imageFile);
	}

	private void loadImage(File file)
	{
		try
		{
			this.image = ImageIO.read(file);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return;
		}

		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();

		if(c > 32 && c < 128)
		{
			this.adbHelper.sendText(new String(new char[]{c}));
		}
	}
}
