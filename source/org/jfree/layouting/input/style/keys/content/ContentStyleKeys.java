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
 * $Id: ContentStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.content;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-content/
 *
 * @author Thomas Morgner
 */
public class ContentStyleKeys
{
  public static final StyleKey MOVE_TO =
          StyleKeyRegistry.getRegistry().createKey
                  ("move-to", false, false,
                    StyleKey.DOM_ELEMENTS |
                    StyleKey.PSEUDO_BEFORE |
                    StyleKey.PSEUDO_ALTERNATE |
                    StyleKey.PSEUDO_AFTER);

  public static final StyleKey QUOTES =
          StyleKeyRegistry.getRegistry().createKey
                  ("quotes", false, false,
                    StyleKey.All_ELEMENTS |
                    StyleKey.MARGINS |
                    StyleKey.FOOTNOTE_AREA);

  public static final StyleKey COUNTER_INCREMENT =
          StyleKeyRegistry.getRegistry().createKey
                  ("counter-increment", false, false, StyleKey.ALWAYS);

  public static final StyleKey COUNTER_RESET =
          StyleKeyRegistry.getRegistry().createKey
                  ("counter-reset", false, false, StyleKey.ALWAYS);

  /** string-set: <name> <value> */
  public static final StyleKey STRING_SET =
          StyleKeyRegistry.getRegistry().createKey
                  ("string-set", false, false, StyleKey.ALWAYS);
  /**
   * Defines a new string context. This is equal to the counter-reset property,
   * and allows to apply the counter nesting rules to strings. This does *not*
   * define the string content; so you have to add a string-set property as well.
   * <p/>
   * The string-def property is always evaulated before the string-set property
   * gets processed.
   *
   * The format for this property is simple:
   * -x-liblayout-string-def: <name>
   */
  public static final StyleKey STRING_DEFINE =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-string-def", false, false, StyleKey.ALWAYS);

  /**
   * Alternate text for images or other non-displayable content. This is not
   * the same as the ::alternate pseudo-element that gets inserted if content
   * had been moved away.
   */
  public static final StyleKey ALTERNATE_TEXT =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-alternate-text", false, false, StyleKey.ALWAYS);

  public static final StyleKey CONTENT =
           StyleKeyRegistry.getRegistry().createKey
                   ("content", false, false, StyleKey.ALWAYS);

   private ContentStyleKeys()
  {
  }
}
