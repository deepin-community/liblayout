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
 * $Id: BackgroundImageResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.border;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.layouter.context.BackgroundSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.output.OutputProcessorFeature;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceKeyCreationException;

/**
 * Creation-Date: 14.12.2005, 13:17:40
 *
 * @author Thomas Morgner
 */
public class BackgroundImageResolveHandler implements ResolveHandler
{
  public BackgroundImageResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve(final LayoutProcess process,
                      final LayoutElement currentNode,
                      final StyleKey key)
  {
    // start loading all images, assume that they are found and include them
    // in the list.

    if (process.getOutputMetaData().isFeatureSupported
            (OutputProcessorFeature.BACKGROUND_IMAGE) == false)
    {
      return;
    }

    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue value = layoutContext.getValue(key);
    if (value == null)
    {
      return;
    }
    if (value instanceof CSSValueList == false)
    {
      return;
    }

    final CSSValueList list = (CSSValueList) value;
    final int length = list.getLength();
    if (length == 0)
    {
      return;
    }
    final BackgroundSpecification backgroundSpecification =
            layoutContext.getBackgroundSpecification();
    final ResourceKey baseURL = DocumentContextUtility.getBaseResource
        (process.getDocumentContext());

    for (int i = 0; i < length; i++)
    {
      final CSSValue item = list.getItem(i);

      if (CSSValueResolverUtility.isURI(item))
      {
        final CSSStringValue svalue = (CSSStringValue) item;
        try
        {
          final ResourceKey sourceURL = process.getResourceManager().deriveKey
                  (baseURL, svalue.getValue());
          // todo: We have to rethink the image feeding ..
//          backgroundSpecification.setBackgroundImage
//                  (i, new URLLayoutImageData(sourceURL, svalue.getValue()));
        }
        catch (ResourceKeyCreationException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
