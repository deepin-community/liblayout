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
 * $Id: UnmodifiableStyleSheetException.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

/**
 * Creation-Date: 01.12.2005, 20:59:34
 *
 * @author Thomas Morgner
 */
public class UnmodifiableStyleSheetException extends RuntimeException
{
  /**
   * Constructs a new runtime exception with <code>null</code> as its detail
   * message.  The cause is not initialized, and may subsequently be initialized
   * by a call to {@link #initCause}.
   */
  public UnmodifiableStyleSheetException()
  {
  }

  /**
   * Constructs a new runtime exception with the specified detail message. The
   * cause is not initialized, and may subsequently be initialized by a call to
   * {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later
   *                retrieval by the {@link #getMessage()} method.
   */
  public UnmodifiableStyleSheetException(final String message)
  {
    super(message);
  }
}
