package sless.ast

import sless.dsl.PropertyDSL

trait Property extends PropertyDSL with Base{
  override def prop(string: String): PropertyAST = PropertyAST(string)

  override protected def assign(p: PropertyAST, value: ValueAST): DeclarationAST = DeclarationAST(p,value)
}