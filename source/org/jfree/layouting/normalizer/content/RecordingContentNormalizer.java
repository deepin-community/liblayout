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
 * $Id: RecordingContentNormalizer.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.content;

import java.io.IOException;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.layouting.util.ChainingComponent;

/**
 * This class records all incoming calls and replays them later.
 *
 * @author Thomas Morgner
 */
public final class RecordingContentNormalizer
        extends ChainingComponent implements Normalizer
{
  private static final int TYPE_START_DOCUMENT = 1;
  private static final int TYPE_START_ELEMENT = 2;
  private static final int TYPE_ADD_TEXT = 3;
  private static final int TYPE_END_ELEMENT = 4;
  private static final int TYPE_END_DOCUMENT = 5;

  private static class RecodingContentNormalizerState implements State
  {
    private RecordedCall[] calls;
    private String text;
    private int elementDepth;

    private RecodingContentNormalizerState(final RecordedCall[] calls,
                                          final String text,
                                          final int elementDepth)
    {
      this.text = text;
      this.elementDepth = elementDepth;
      this.calls = (RecordedCall[]) calls.clone();
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
            throws StateException
    {
      final RecordingContentNormalizer rcn =
              new RecordingContentNormalizer();
      rcn.setRecordedCalls(calls);
      rcn.text.append(text);
      rcn.elementDepth = elementDepth;
      return rcn;
    }
  }

  private StringBuffer text;
  private int elementDepth;

  public RecordingContentNormalizer()
  {
    this.text = new StringBuffer();
  }

  /**
   * Start document is the first call to the normalizer. At this point, all
   * meta-data has been given and the document context is filled correctly.
   * <p/>
   * Starting the document also starts a new PageContext.
   *
   * @throws NormalizationException
   * @throws IOException
   */
  public void startDocument()
  {
    // addCall(new RecordedCall(TYPE_START_DOCUMENT, null));
    throw new UnsupportedOperationException("Recoding CN cannot start documents.");
    // should not happen ..
  }

  /**
   * Starts a new element. The element uses the given namespace and tagname. The
   * element's attributes are given as collection, each attribute is keyed with
   * a namespace and attributename. The values contained in the attributes are
   * not defined.
   *
   * @param namespace
   * @param tag
   * @param attributes
   * @throws NormalizationException
   * @throws IOException
   */
  public void startElement(final String namespace,
                           final String tag,
                           final AttributeMap attributes)
  {
    addCall(new RecordedCall(TYPE_START_ELEMENT,
            new Object[]{namespace, tag, attributes}));
    elementDepth += 1;
  }

  /**
   * Adds text content to the current element.
   *
   * @param text
   * @throws NormalizationException
   * @throws IOException
   */
  public void addText(final String text)
  {
    addCall(new RecordedCall(TYPE_ADD_TEXT, text));
    this.text.append(text);
  }

  /**
   * Ends the current element. The namespace and tagname are given for
   * convienience.
   *
   * @param namespace
   * @param tag
   * @throws NormalizationException
   * @throws IOException
   */
  public void endElement()
  {
    elementDepth -= 1;
    addCall(new RecordedCall(TYPE_END_ELEMENT, new Object[0]));
  }

  /**
   * Ends the document. No other events will be fired against this normalizer
   * once this method has been called.
   *
   * @throws NormalizationException
   * @throws IOException
   */
  public void endDocument()
  {
    throw new UnsupportedOperationException("Recoding CN cannot start documents.");
//    addCall(new RecordedCall(TYPE_END_DOCUMENT, null));
  }

  protected void invoke(final Object target, final int methodId, final Object params)
          throws Exception
  {
    final Normalizer normalizer = (Normalizer) target;
    switch (methodId)
    {
      case TYPE_START_DOCUMENT:
      {
        normalizer.startDocument();
        break;
      }
      case TYPE_START_ELEMENT:
      {
        final Object[] parameters = (Object[]) params;
        normalizer.startElement((String) parameters[0],
                (String) parameters[1], (AttributeMap) parameters[2]);
        break;
      }
      case TYPE_ADD_TEXT:
      {
        normalizer.addText((String) params);
        break;
      }
      case TYPE_END_ELEMENT:
      {
        final Object[] parameters = (Object[]) params;
        normalizer.endElement();
        break;
      }
      case TYPE_END_DOCUMENT:
      {
        normalizer.endDocument();
        break;
      }
      default:
        throw new IllegalStateException("Invalid call type");
    }
  }

  public void clear()
  {
    super.clear();
    elementDepth = 0;
    text = new StringBuffer();
  }

  public int getElementDepth()
  {
    return elementDepth;
  }

  public State saveState()
  {
    final RecordedCall[] calls = retrieveRecordedCalls();
    final RecodingContentNormalizerState state = new RecodingContentNormalizerState
        (calls, this.text.toString(), elementDepth);
    setRecordedCalls(calls);
    return state;
  }

  public String getText()
  {
    return text.toString();
  }

  public void handlePageBreak(final CSSValue pageName,
                              final PseudoPage[] pseudoPages)
  {
    // can be empty, as we replay everything later ...
  }

  /**
   * Returns the renderer. The renderer is the last step in the processing
   * chain. The ModelBuilder and ContentGenerator steps are considered internal,
   * as they may refeed the normalizer.
   *
   * @return
   */
  public Renderer getRenderer()
  {
    throw new UnsupportedOperationException();
  }

  public StyleResolver getStyleResolver()
  {
    throw new UnsupportedOperationException();
  }
}
