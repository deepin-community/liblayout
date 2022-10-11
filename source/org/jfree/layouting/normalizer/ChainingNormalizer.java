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
 * $Id: ChainingNormalizer.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer;

import java.io.IOException;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.renderer.ChainingRenderer;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.layouting.util.ChainingCallException;

/**
 * Creation-Date: 16.06.2006, 14:34:44
 *
 * @author Thomas Morgner
 */
public class ChainingNormalizer implements Normalizer
{
  private static class ChainingNormalizerState implements State
  {
    private State chainedNormalizerState;

    private ChainingNormalizerState()
    {
    }

    public State getChainedNormalizerState()
    {
      return chainedNormalizerState;
    }

    public void setChainedNormalizerState(final State chainedNormalizerState)
    {
      this.chainedNormalizerState = chainedNormalizerState;
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
      final Normalizer normalizer = (Normalizer)
              chainedNormalizerState.restore(layoutProcess);
      return new ChainingNormalizer(normalizer);
    }
  }

  private Normalizer chainedNormalizer;

  public ChainingNormalizer(final Normalizer normalizer)
  {
    if (normalizer == null)
    {
      throw new NullPointerException();
    }
    this.chainedNormalizer = normalizer;
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
  public void startDocument() throws NormalizationException, IOException
  {
    chainedNormalizer.startDocument();
    commitCalls();
  }

  public synchronized void commitCalls() throws NormalizationException
  {
    // now we need access to the chained normalizer's model-builder.
    final ChainingRenderer renderer = (ChainingRenderer) chainedNormalizer.getRenderer();
    if (renderer != null)
    {
      //Log.debug("Committing calls to Renderer: " + renderer);
      try
      {
        renderer.replay(renderer.getRenderer());
      }
      catch (ChainingCallException e)
      {
        throw new NormalizationException("Failed to dispatch calls", e);
      }
    }
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
          throws NormalizationException, IOException
  {
    chainedNormalizer.startElement(namespace, tag, attributes);
    commitCalls();
  }

  /**
   * Adds text content to the current element.
   *
   * @param text
   * @throws NormalizationException
   * @throws IOException
   */
  public void addText(final String text)
          throws NormalizationException, IOException
  {
    chainedNormalizer.addText(text);
    commitCalls();
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
          throws NormalizationException, IOException
  {
    chainedNormalizer.endElement();
    commitCalls();
  }

  /**
   * Ends the document. No other events will be fired against this normalizer
   * once this method has been called.
   *
   * @throws NormalizationException
   * @throws IOException
   */
  public void endDocument() throws NormalizationException, IOException
  {
    chainedNormalizer.endDocument();
    commitCalls();
  }

  public void handlePageBreak(final CSSValue pageName,
                              final PseudoPage[] pseudoPages)
          throws NormalizationException
  {
    chainedNormalizer.handlePageBreak(pageName, pseudoPages);
    commitCalls();
  }


  public State saveState() throws StateException
  {
    final ChainingNormalizerState state = new ChainingNormalizerState();
    state.setChainedNormalizerState(chainedNormalizer.saveState());
    return state;
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
    return chainedNormalizer.getRenderer();
  }
//
//  public StyleResolver getStyleResolver()
//  {
//    return chainedNormalizer.getStyleResolver();
//  }
}
