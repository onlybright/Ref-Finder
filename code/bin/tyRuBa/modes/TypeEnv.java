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
package tyRuBa.modes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import tyRuBa.engine.RBSubstitutable;

public class TypeEnv
  extends Hashtable
{
  public Type basicGet(RBSubstitutable v)
  {
    return (Type)super.get(v);
  }
  
  public Type get(RBSubstitutable v)
  {
    Type result = (Type)super.get(v);
    if (result == null)
    {
      result = Factory.makeTVar(v.name().substring(1));
      put(v, result);
    }
    return result;
  }
  
  public Object clone()
  {
    TypeEnv cl = new TypeEnv();
    HashMap varRenamings = new HashMap();
    for (Iterator iter = keySet().iterator(); iter.hasNext();)
    {
      RBSubstitutable element = (RBSubstitutable)iter.next();
      cl.put(element, get(element).clone(varRenamings));
    }
    return cl;
  }
  
  public String toString()
  {
    StringBuffer result = new StringBuffer("TypeEnv(");
    Enumeration keys = keys();
    while (keys.hasMoreElements())
    {
      RBSubstitutable key = (RBSubstitutable)keys.nextElement();
      result.append(" " + key + "= " + get(key));
    }
    result.append(" )");
    return result.toString();
  }
  
  public TypeEnv union(TypeEnv other)
    throws TypeModeError
  {
    TypeEnv result = new TypeEnv();
    for (Iterator iter = other.keySet().iterator(); iter.hasNext();)
    {
      RBSubstitutable var = (RBSubstitutable)iter.next();
      if (containsKey(var)) {
        result.put(var, get(var).union(other.get(var)));
      }
    }
    return result;
  }
  
  public TypeEnv intersect(TypeEnv other)
    throws TypeModeError
  {
    TypeEnv result = (TypeEnv)clone();
    other = (TypeEnv)other.clone();
    for (Iterator iter = other.keySet().iterator(); iter.hasNext();)
    {
      RBSubstitutable var = (RBSubstitutable)iter.next();
      if (result.containsKey(var)) {
        result.put(var, result.get(var).intersect(other.get(var)));
      } else {
        result.put(var, other.get(var));
      }
    }
    return result;
  }
}
