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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Utils
{
  private final static Logger LOGGER = Logger.getLogger(Utils.class.getName());

  public static String lookForProgramInPath(String desiredProgram) {
      ProcessBuilder pb = new ProcessBuilder(isWindows() ? "where" : "which", desiredProgram);
      Path foundProgram = null;

      try {
          Process proc = pb.start();
          int errCode = proc.waitFor();
          if (errCode == 0) {

              try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                  foundProgram = Paths.get(reader.readLine());
              }

              LOGGER.info(desiredProgram + " has been found at : " + foundProgram);

          } else {
              LOGGER.warning(desiredProgram + " not in PATH");
              System.exit(1);
          }

      } catch (IOException | InterruptedException ex) {
          LOGGER.warning("Something went wrong while searching for " + desiredProgram);
          System.exit(1);
      }

      return foundProgram.toString();
  }

  private static boolean isWindows() {
      return System.getProperty("os.name").toLowerCase().contains("windows");
  }

  public static String buildAbsPath(String filename) {
      Path currentDir = Paths.get("").toAbsolutePath();
      return currentDir.resolve(filename).toString();
  }
}
