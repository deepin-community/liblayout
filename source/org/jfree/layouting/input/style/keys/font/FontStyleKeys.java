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
 * $Id: FontStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-fonts/
 *
 * @author Thomas Morgner
 */
public class FontStyleKeys
{
  /**
   * Font-Effects are not used yet. It should be implemented later, if possible.
   */
  public static final StyleKey FONT_EFFECT =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-effect", false, true, StyleKey.ALWAYS);

  /**
   * Font-Emphasize is not used yet. It is needed for proper Asian font
   * support.
   */
  public static final StyleKey FONT_EMPHASIZE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-emphasize-style", false, true, StyleKey.ALWAYS);

  /**
   * Font-Emphasize is not used yet. It is needed for proper Asian font
   * support.
   */
  public static final StyleKey FONT_EMPHASIZE_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-emphasize-position", false, true, StyleKey.ALWAYS);
  /**
   * The font-family holds the fully resolved name of an valid font.
   * The font-family value may be null, if the specified font resolved to 'none'.
   */
  public static final StyleKey FONT_FAMILY =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-family", false, true, StyleKey.ALWAYS);

  /**
   * The font-size holds the size of the font in points.
   */
  public static final StyleKey FONT_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-size", false, true, StyleKey.ALWAYS);

  /**
   * The font-size-adjust is not used for now.
   */
  public static final StyleKey FONT_SIZE_ADJUST =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-size-adjust", false, true, StyleKey.ALWAYS);

  /**
   * The font-smooth controls the anti-aliasing for the rendering process.
   * This may affect the rendered font size. Resolving the font-smooth
   * property must be done elsewhere (translating auto, never, always or the
   * size specifications into a boolean).
   */
  public static final StyleKey FONT_SMOOTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-smooth", false, true, StyleKey.ALWAYS);

  /**
   * A simple unconditional flag. This value will be computed during the resolve
   * pass. It results in either FontSmooth.ALWAYS or FontSmooth.NEVER.
   */
  public static final StyleKey X_FONT_SMOOTH_FLAG =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-font-smooth-flag", false, true, StyleKey.ALWAYS);

  public static final StyleKey FONT_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                    ("font-style", false, true, StyleKey.ALWAYS);

  public static final StyleKey FONT_VARIANT =
          StyleKeyRegistry.getRegistry().createKey
                    ("font-variant", false, true, StyleKey.ALWAYS);

  public static final StyleKey FONT_WEIGHT =
          StyleKeyRegistry.getRegistry().createKey
                    ("font-weight", false, true, StyleKey.ALWAYS);

  public static final StyleKey FONT_STRETCH =
          StyleKeyRegistry.getRegistry().createKey
                  ("font-stretch", false, true, StyleKey.ALWAYS);
  /**
   *  Used in conjunction with text-align-last: size;
   */
  public static final StyleKey MIN_FONT_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("min-font-size", false, true, StyleKey.ALWAYS);
  /**
   *  Used in conjunction with text-align-last: size;
   */
  public static final StyleKey MAX_FONT_SIZE =
          StyleKeyRegistry.getRegistry().createKey
                  ("max-font-size", false, true, StyleKey.ALWAYS);

  /**
   * If used in a @font rule, this defines an unique name/handle for a font
   * definition. If used in any other style rule, this references to a previously
   * defined font. If that font is not defined, the key is ignored.
   *
   * This is another idea stolen from OpenOffice :)
   */
  public static final StyleKey FONT_NAME =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-font-name", false, true, StyleKey.ALWAYS);
  /**
   * A descriptor for @font rules, telling the system that the font either has
   * a fixed width for each char ('fixed') or that each char may have a different
   * width ('variable').
   *
   * This is another idea stolen from OpenOffice :)
   */
  public static final StyleKey FONT_PITCH =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-font-pitch", false, true, StyleKey.ALWAYS);

  private FontStyleKeys () {}


}
