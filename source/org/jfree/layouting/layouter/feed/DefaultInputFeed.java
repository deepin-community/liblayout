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
 * $Id: DefaultInputFeed.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.feed;

import java.io.IOException;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DefaultDocumentMetaNode;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.DocumentMetaNode;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.util.AttributeMap;

/**
 * Creation-Date: 05.12.2005, 18:19:03
 *
 * @author Thomas Morgner
 */
public class DefaultInputFeed implements InputFeed
{
  public static final int DOCUMENT_STARTING = 0;
  public static final int META_EXPECTED = 1;
  public static final int META_PROCESSING = 2;
  public static final int META_NODE_START = 3;
  public static final int META_NODE_ATTRIBUTES = 4;
  public static final int ELEMENT_EXPECTED = 5;
  public static final int ELEMENT_STARTED = 6;
  public static final int ELEMENT_ATTRIBUTES = 7;
  public static final int ELEMENT_CONTENT = 8;
  public static final int DOCUMENT_FINISHED = 9;

  private static final String[] STATE_NAMES = new String[]{
          "DOCUMENT_STARTING", "META_EXPECTED", "META_PROCESSING",
          "META_NODE_START", "META_NODE_ATTRIBUTES",
          "ELEMENT_EXPECTED", "ELEMENT_STARTED", "ELEMENT_ATTRIBUTES",
          "ELEMENT_CONTENT", "DOCUMENT_FINISHED"
  };

  private static class DefaultInputFeedState implements State
  {
    private boolean initialized;
    private int state;
    private DocumentMetaNode metaNode;
    private State normalizerState;
    private boolean pagebreakEncountered;
    private int treeDepth;
    private AttributeMap currentAttributes;
    private String namespace;
    private String tagName;

    private DefaultInputFeedState()
    {
    }

    public int getTreeDepth()
    {
      return treeDepth;
    }

    public void setTreeDepth(final int treeDepth)
    {
      this.treeDepth = treeDepth;
    }

    public AttributeMap getCurrentAttributes()
    {
      return currentAttributes;
    }

    public void setCurrentAttributes(final AttributeMap currentAttributes)
    {
      this.currentAttributes = currentAttributes;
    }

    public String getNamespace()
    {
      return namespace;
    }

    public void setNamespace(final String namespace)
    {
      this.namespace = namespace;
    }

    public String getTagName()
    {
      return tagName;
    }

    public void setTagName(final String tagName)
    {
      this.tagName = tagName;
    }

    public boolean isPagebreakEncountered()
    {
      return pagebreakEncountered;
    }

    public void setPagebreakEncountered(final boolean pagebreakEncountered)
    {
      this.pagebreakEncountered = pagebreakEncountered;
    }

    public boolean isInitialized()
    {
      return initialized;
    }

    public void setInitialized(final boolean initialized)
    {
      this.initialized = initialized;
    }

    public int getState()
    {
      return state;
    }

    public void setState(final int state)
    {
      this.state = state;
    }

    public DocumentMetaNode getMetaNode()
    {
      return metaNode;
    }

    public void setMetaNode(final DocumentMetaNode metaNode)
    {
      this.metaNode = metaNode;
    }

    public State getNormalizerState()
    {
      return normalizerState;
    }

    public void setNormalizerState(final State normalizerState)
    {
      this.normalizerState = normalizerState;
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
      final DefaultInputFeed inputFeed = new DefaultInputFeed(layoutProcess, false);
      inputFeed.initialized = initialized;
      inputFeed.state = state;
      inputFeed.metaNode = metaNode;
      inputFeed.normalizer =
              (Normalizer) normalizerState.restore(layoutProcess);
      inputFeed.pagebreakEncountered = pagebreakEncountered;
      inputFeed.treeDepth = treeDepth;
      inputFeed.currentAttributes = currentAttributes;
      inputFeed.namespace = namespace;
      inputFeed.tagName = tagName;
      return inputFeed;
    }
  }

  private static boolean [][] validStateTransitions;

  static
  {
    validStateTransitions = new boolean[10][];
    // after startDocument we expect metadata...
    validStateTransitions[DOCUMENT_STARTING] = new boolean[]{
            false, true, false,
            false, false, false,
            false, false, false, false, false
    };
    // either we get meta-data or the first element
    validStateTransitions[META_EXPECTED] = new boolean[]{
            false, false, true, false, false,
            false, true, false, false, false
    };
    // we either get more meta-data or proceed to the element processing
    validStateTransitions[META_PROCESSING] = new boolean[]{
            false, false, true, true, false,
            true, false, false, false, false, false
    };
    // now, either we get attributes or the meta-node is finished
    validStateTransitions[META_NODE_START] = new boolean[]{
            false, false, false, false, true,
            false, false, false, false, false
    };
    // we expect more attributes or the end of the node
    validStateTransitions[META_NODE_ATTRIBUTES] = new boolean[]{
            false, false, true, false, true,
            false, false, false, false, false
    };
    validStateTransitions[ELEMENT_EXPECTED] = new boolean[]{
            false, false, false, false, false,
            true, true, false, true, true
    };
    validStateTransitions[ELEMENT_STARTED] = new boolean[]{
            false, false, false, false, false,
            true, true, true, true, true
    };
    validStateTransitions[ELEMENT_ATTRIBUTES] = new boolean[]{
            false, false, false, false, false,
            true, true, true, true, true
    };
    validStateTransitions[ELEMENT_CONTENT] = new boolean[]{
            false, false, false, false, false,
            true, true, false, true, true
    };
    validStateTransitions[DOCUMENT_FINISHED] = new boolean[]{
            false, false, false, false, false,
            false, false, false, false, false
    };
  }

  private boolean initialized;
  private int state;
//  private Stack elements;
  private LayoutProcess process;
  private DocumentMetaNode metaNode;
  private DocumentContext documentContext;
  private Normalizer normalizer;
  private boolean pagebreakEncountered;

  private int treeDepth;
  private AttributeMap currentAttributes;
  private String namespace;
  private String tagName;

  public DefaultInputFeed(final LayoutProcess process)
  {
    this(process, true);
  }

  protected DefaultInputFeed(final LayoutProcess process, final boolean init)
  {
    this.process = process;
    this.documentContext = process.getDocumentContext();
    if (init)
    {
      this.normalizer = process.getOutputProcessor().createNormalizer(process);

      this.state = DOCUMENT_STARTING;
    }
  }

  public void resetPageBreakFlag()
  {
    this.pagebreakEncountered = false;
  }

  public void handlePageBreakEncountered(final CSSValue pageName,
                                         final PseudoPage[] pseudoPages)
          throws NormalizationException
  {
    this.pagebreakEncountered = true;
    // OK, we got a pagebreak. (We should get only one, but who knows ...
    // lets save the states anyway ..
    normalizer.handlePageBreak (pageName, pseudoPages);
  }

  public boolean isPagebreakEncountered()
  {
    return this.pagebreakEncountered;
  }

  private int checkState(final int newState)
  {
    if (validStateTransitions[state][newState] == false)
    {
      throw new IllegalStateException
              ("illegal transition from " + STATE_NAMES[state] +
                      " to " + STATE_NAMES[newState]);
    }
    final int oldState = this.state;
    this.state = newState;
    return oldState;
  }

  public final void startDocument()
  {
    checkState(META_EXPECTED);
    // resetPageBreakFlag();
    performStartDocument();
  }

  protected void performStartDocument()
  {
    // todo do nothing?
  }

  public final void startMetaInfo()
  {
    checkState(META_PROCESSING);
    performStartMetaInfo();
  }

  protected void performStartMetaInfo()
  {
  }

  public final void addDocumentAttribute(final String name, final Object attr)
  {
    checkState(META_PROCESSING);
    performAddDocumentAttribute(name, attr);
  }

  protected void performAddDocumentAttribute(final String name, final Object attr)
  {
    documentContext.setMetaAttribute(name, attr);
  }

  public void startMetaNode()
  {
    checkState(META_NODE_START);
    performStartMetaNode();
  }

  protected void performStartMetaNode()
  {
    metaNode = new DefaultDocumentMetaNode();// create new DocumentMetaNode(type)
    documentContext.addMetaNode(metaNode);
  }

  public final void setMetaNodeAttribute(final String name, final Object attr)
  {
    checkState(META_NODE_ATTRIBUTES);
    performSetMetaNodeAttribute(name, attr);
  }

  protected void performSetMetaNodeAttribute(final String name, final Object attr)
  {
    metaNode.setMetaAttribute(name, attr);
  }

  public void endMetaNode()
  {
    checkState(META_PROCESSING);
    performEndMetaNode();
  }

  protected void performEndMetaNode()
  {
    metaNode = null;
  }

  public final void endMetaInfo()
          throws InputFeedException
  {
    checkState(ELEMENT_EXPECTED);
    performEndMetaInfo();
  }

  public NamespaceCollection getNamespaceCollection()
  {
    if (initialized == false)
    {
      throw new IllegalStateException("Not yet!");
    }
    return documentContext.getNamespaces();
  }

  protected void performEndMetaInfo()
          throws InputFeedException
  {
    try
    {
      initializeDocument();
    }
    catch (Exception e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  public final void startElement(final String namespace, final String name)
          throws InputFeedException
  {
    final int oldState = checkState(ELEMENT_STARTED);
    // resetPageBreakFlag();

    if (oldState == META_EXPECTED ||
            oldState == ELEMENT_EXPECTED)
    {
      try
      {
        initializeDocument();
      }
      catch (Exception e)
      {
        throw new InputFeedException("Failed to normalize element", e);
      }
    }
    else if (oldState == ELEMENT_ATTRIBUTES ||
            oldState == ELEMENT_STARTED)
    {
      try
      {
        getNormalizer().startElement (this.namespace, tagName, currentAttributes);
        currentAttributes = null;
      }
      catch (NormalizationException e)
      {
        throw new InputFeedException("Failed to normalize element", e);
      }
      catch (IOException e)
      {
        throw new InputFeedException("IOError: Failed to normalize element", e);
      }
    }
    performStartElement(namespace, name);
  }

  private void initializeDocument()
          throws IOException, NormalizationException
  {
    if (initialized)
    {
      return;
    }

    // initialize all factories from the given meta-data.
    // the execution order here is important!
    documentContext.initialize();
    getNormalizer().startDocument();
    initialized = true;
  }

  protected void performStartElement(final String namespace, final String name)
  {
    this.namespace = namespace;
    this.tagName = name;
    this.currentAttributes = new AttributeMap();
  }

  public final void setAttribute(final String namespace, final String name, final Object attr)
  {
    checkState(ELEMENT_ATTRIBUTES);
    performSetAttribute(namespace, name, attr);
  }

  protected void performSetAttribute(final String namespace, final String name, final Object attr)
  {
    this.currentAttributes.setAttribute(namespace, name, attr);
  }

  public final void addContent(final String text)
          throws InputFeedException
  {
    try
    {
      final int oldState = checkState(ELEMENT_CONTENT);
      // resetPageBreakFlag();

      if (oldState == ELEMENT_ATTRIBUTES ||
              oldState == ELEMENT_STARTED)
      {
        getNormalizer().startElement (this.namespace, tagName, currentAttributes);
        currentAttributes = null;
      }
      else if (oldState == ELEMENT_EXPECTED ||
              oldState == META_EXPECTED)
      {
        initializeDocument();
      }

      performAddContent(text);
    }
    catch (NormalizationException ne)
    {
      throw new InputFeedException("Failed to normalize element", ne);
    }
    catch (IOException ioe)
    {
      throw new InputFeedException("Failed to normalize element", ioe);
    }
  }

  protected void performAddContent(final String text)
          throws InputFeedException,
          IOException, NormalizationException
  {
    getNormalizer().addText(text);
  }

  public final void endElement()
          throws InputFeedException
  {
    try
    {
      final int oldState = checkState(ELEMENT_EXPECTED);
      // resetPageBreakFlag();
      if (oldState == ELEMENT_ATTRIBUTES ||
              oldState == ELEMENT_STARTED)
      {
        getNormalizer().startElement (this.namespace, tagName, currentAttributes);
        currentAttributes = null;
      }

      performEndElement();
    }
    catch (NormalizationException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
    catch (IOException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  protected void performEndElement()
          throws IOException, NormalizationException
  {
    this.namespace = null;
    this.tagName = null;
    this.currentAttributes = null;
    getNormalizer().endElement();
  }

  public final void endDocument()
          throws InputFeedException
  {
    checkState(DOCUMENT_FINISHED);
    // resetPageBreakFlag();
    try
    {
      performEndDocument();
    }
    catch (NormalizationException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
    catch (IOException e)
    {
      throw new InputFeedException("Failed to normalize element", e);
    }
  }

  protected void performEndDocument()
          throws IOException, NormalizationException
  {
//    //elements.pop();
//    if (elements.isEmpty() == false)
//    {
//      throw new IllegalStateException("Stack is not yet empty: " + elements);
//    }
    getNormalizer().endDocument();
  }

  protected LayoutProcess getProcess()
  {
    return process;
  }

  protected int getState()
  {
    return state;
  }

  public Normalizer getNormalizer()
  {
    return normalizer;
  }

  public State saveState() throws StateException
  {
    final DefaultInputFeedState state = new DefaultInputFeedState();
    state.setTreeDepth(treeDepth);
    state.setCurrentAttributes(currentAttributes);
    state.setNamespace(namespace);
    state.setTagName(tagName);
    state.setInitialized(initialized);
    state.setMetaNode(metaNode);
    state.setNormalizerState(normalizer.saveState());
    state.setState(this.state);
    state.setPagebreakEncountered(pagebreakEncountered);
    return state;
  }

  /**
   * Warning; This method is needed internally, mess with it from the outside
   * and you will run into trouble. The normalizer is a statefull component and
   * any call to it may mess up the state. From there on, 'Abandon every hope,
   * ye who enter here'.
   *
   * @return
   */
  public Normalizer getCurrentNormalizer()
  {
    return normalizer;
  }
}
