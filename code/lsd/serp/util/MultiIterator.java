/* 
*    Ref-Finder
*    Copyright (C) <2015>  <PLSE_UCLA>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package serp.util;


import java.util.*;


/**
 *	<p>Iterator type that can combine multiple iterators into one logical
 *	iterator.</p>
 *
 *	@author		Abe White
 */
public abstract class MultiIterator
	implements Iterator
{
	private static final int PAST_END = -1;

	private Iterator	_itr	= Collections.EMPTY_LIST.iterator ();
	private Iterator	_last	= null;
	private int			_index	= 0;


	public boolean hasNext ()
	{
		setIterator ();
		return _itr.hasNext ();
	}


	public Object next ()
	{
		setIterator ();
		return _itr.next ();
	}
	
	
	public void remove ()
	{
		setIterator ();
		_last.remove ();
	}

	
	/**
	 *	Implement this method to return the iterator at the 
	 *	<code>index</code>th position in the list of iterators to process.  
	 *	Indexing starts at 0 and will proceed upward linearly, never repeating
	 *	the same index.  Return null to indicate that there are no more 
	 *	iterators to process.  You may return null to index 0, in which case 
	 *	this iterator will act like an iterator over an empty collection.
 	 */
	protected abstract Iterator newIterator (int index);


	/**
	 *	Sets internal iterator to the iterator to use for processing.
	 */
	private void setIterator ()
	{
		// if already iterated through all, nothing to do
		if (_index == PAST_END)
			return;

		// always remove from the last iterator used
		_last = _itr;

		// find next iterator with a value
		Iterator newItr = _itr;
		while (newItr != null && !newItr.hasNext ())
			newItr = newIterator (_index++);

		// switch current iter if needed
		if (newItr != null && _itr != newItr)
			_itr = newItr;
		else if (newItr == null)
			_index = PAST_END;
	}
}
