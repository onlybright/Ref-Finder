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
package tyRuBa.engine.visitor;

import tyRuBa.engine.Frame;
import tyRuBa.engine.RBExpression;
import tyRuBa.engine.RBTemplateVar;
import tyRuBa.engine.RBTerm;
import tyRuBa.engine.RBUniqueQuantifier;
import tyRuBa.engine.RBVariable;

public class SubstituteVisitor
  extends SubstituteOrInstantiateVisitor
{
  public SubstituteVisitor(Frame frame)
  {
    super(frame);
  }
  
  public Object visit(RBUniqueQuantifier unique)
  {
    Frame f = getFrame();
    for (int i = 0; i < unique.getNumVars(); i++) {
      f.remove(unique.getVarAt(i));
    }
    RBExpression exp = (RBExpression)unique.getExp().accept(this);
    return new RBUniqueQuantifier(unique.getQuantifiedVars(), exp);
  }
  
  public Object visit(RBVariable var)
  {
    RBTerm val = getFrame().get(var);
    if (val == null) {
      return var;
    }
    return val.accept(this);
  }
  
  public Object visit(RBTemplateVar var)
  {
    RBTerm val = getFrame().get(var);
    if (val == null) {
      return var;
    }
    return val.accept(this);
  }
}
