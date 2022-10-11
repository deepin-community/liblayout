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
 * $Id: StyleRuleMatcher.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.CSSPageRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.model.LayoutElement;

/**
 * A (possibly statefull) style matcher. This class is responsible for
 * checking which style rule applies to the given document.
 *
 * It is guaranteed, that the matcher receives the elements in the order
 * in which they appear in the document.
 *
 * Although the style rule matcher does not receive explicit element-opened
 * and element-closed events, these events can be derived from the layout element
 * and its relation to the parent (and possibly previously received element and
 * its parent).
 *
 * @author Thomas Morgner
 */
public interface StyleRuleMatcher
{
  public void initialize (final LayoutProcess layoutProcess);

  /**
   * Creates an independent copy of this style rule matcher.
   *
   * @return
   */
  public StyleRuleMatcher deriveInstance();
  public CSSStyleRule[] getMatchingRules (LayoutElement element);
  public boolean isMatchingPseudoElement (LayoutElement element, String pseudo);
  public CSSPageRule[] getPageRule (CSSValue pageName, PseudoPage[] pseudoPages);
}
