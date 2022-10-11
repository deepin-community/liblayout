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
 * $Id: PageableOutputProcessor.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.output.pageable;

import org.jfree.layouting.output.OutputProcessor;

/**
 * A pageable processor generates zero or more *pysical* pages per event. For
 * the sake of performance, an PageableOutputProcessor should implement some
 * sort of caching so that requesting pages from the same chunk does not result
 * in a full recomputation.
 * <p/>
 * For each logical page, a set of one or more physical pages is generated. The
 * pageable output processor allows to query pages by their logical page number
 * and by their physical number.
 * <p/>
 * The page content should not be exposed to the caller.
 * <p/>
 * An output processor has a page cursor attached. The cursor is used to
 * synchronize restarted input-feeds with the current output state.
 *
 * @author Thomas Morgner
 */
public interface PageableOutputProcessor extends OutputProcessor
{
  public int getPhysicalPageCount();

  public PhysicalPageKey getPhysicalPage(int page);
}
