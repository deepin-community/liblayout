/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: NullOutputStream.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A null output stream. All data written to this stream is ignored.
 *
 * @author Thomas Morgner
 */
public class NullOutputStream extends OutputStream
{
  /**
   * Default constructor.
   */
  public NullOutputStream ()
  {
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param i the value.
   * @throws IOException if there is an I/O problem.
   */
  public void write (final int i)
          throws IOException
  {
    // no i wont do anything here ...
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param bytes the bytes.
   * @throws IOException if there is an I/O problem.
   */
  public void write (final byte[] bytes)
          throws IOException
  {
    // no i wont do anything here ...
  }

  /**
   * Writes to the stream (in this case, does nothing).
   *
   * @param bytes the bytes.
   * @param off   the start offset in the data.
   * @param len   the number of bytes to write.
   * @throws IOException if there is an I/O problem.
   */
  public void write (final byte[] bytes, final int off, final int len)
          throws IOException
  {
    // no i wont do anything here ...
  }

}
