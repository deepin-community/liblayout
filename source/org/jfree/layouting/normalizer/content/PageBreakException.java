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
 * $Id: PageBreakException.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.content;

import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * A pagebreak exception is a cheap out-of-order-return (some kind of GoTo,
 * but less chaotic).
 *
 * @author Thomas Morgner
 */
public class PageBreakException extends Exception
{
  /**
   * The name of the page. This has to be resolved against the defined pages
   * before a pagebreak is issued - alternativly (and thats how it is done
   * now, a page break is started whenever the old value is different from the
   * current one.)
   */
  private CSSValue pageName;

  private PseudoPage[] pseudoPages;

  private int pageCount;

  /**
   * Constructs a new exception with <code>null</code> as its detail message.
   * The cause is not initialized, and may subsequently be initialized by a call
   * to {@link #initCause}.
   */
  public PageBreakException(final CSSValue pageName,
                            final PseudoPage[] pseudoPages,
                            final int pageCount)
  {
    this.pageCount = pageCount;
    this.pseudoPages = pseudoPages;
    this.pageName = pageName;
  }

  public CSSValue getPageName()
  {
    return pageName;
  }

  public PseudoPage[] getPseudoPages()
  {
    return pseudoPages;
  }

  public int getPageCount()
  {
    return pageCount;
  }
}
