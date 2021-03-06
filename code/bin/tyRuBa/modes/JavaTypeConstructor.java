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

import java.io.Serializable;

public class JavaTypeConstructor
  extends TypeConstructor
  implements Serializable
{
  private final Class javaClass;
  
  public JavaTypeConstructor(Class javaclass)
  {
    if ((javaclass.isInterface()) || (javaclass.isPrimitive())) {
      throw new Error("no interfaces or primitives types are allowed");
    }
    this.javaClass = javaclass;
  }
  
  public String getName()
  {
    String name = this.javaClass.getName();
    if (name.startsWith("java.lang.")) {
      return name.substring("java.lang.".length());
    }
    return name;
  }
  
  public boolean equals(Object other)
  {
    if ((other != null) && (getClass().equals(other.getClass()))) {
      return getName().equals(((TypeConstructor)other).getName());
    }
    return false;
  }
  
  public int hashCode()
  {
    return getName().hashCode();
  }
  
  public void addSuperType(TypeConstructor superType)
    throws TypeModeError
  {
    throw new TypeModeError("Can not add super type for java types " + this);
  }
  
  public TypeConstructor getSuperTypeConstructor()
  {
    if (this.javaClass.getSuperclass() == null) {
      return null;
    }
    return new JavaTypeConstructor(this.javaClass.getSuperclass());
  }
  
  public int getTypeArity()
  {
    return 0;
  }
  
  public String getParameterName(int i)
  {
    throw new Error("This is not a user defined type");
  }
  
  public boolean isInitialized()
  {
    throw new Error("This is not a user defined type");
  }
  
  public ConstructorType getConstructorType()
  {
    return ConstructorType.makeJava(this.javaClass);
  }
  
  public boolean isJavaTypeConstructor()
  {
    return true;
  }
  
  public String toString()
  {
    return "JavaTypeConstructor(" + this.javaClass + ")";
  }
  
  public Class javaEquivalent()
  {
    return this.javaClass;
  }
}
