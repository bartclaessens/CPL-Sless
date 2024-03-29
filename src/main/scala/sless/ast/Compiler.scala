package sless.ast

import sless.dsl.Compilable

trait Compiler extends Compilable with Base {
  override def compile(sheet: Css): String = compileHelper(sheet)

  override def pretty(sheet: Css, spaces: Int): String = prettyHelper(sheet,spaces)

  def compileHelper(compilable: CompilableAST): String =  compilable match {
    case ACss(rules) => rules.map(r => compileHelper(r)).mkString("")
    case CommentRule(s,declarations,comment) => (if (comment==null) "" else "/* " + comment + " */") +
      compileHelper(s) + "{" + declarations.map(d => compileHelper(d)).mkString("") + "}"
    case CommentDeclaration(p, value,comment) => compileHelper(p) + ":" + compileHelper(value) + ";" +
      (if (comment==null) "" else "/* " + comment + " */")
    case AValue(value) => value
    case AProperty(value) => value
    case UniversalSelector => "*"
    case TypeSelector(tipe) => tipe
    case GroupSelector(selectors) => selectors.map(s => compileHelper(s)).mkString(",")
    case ClassNameSelector(s,className) => compileHelper(s) + "." + className
    case IdSelector(s,id) => compileHelper(s) + "#" + id
    case AttributeSelector(s,attr,value) => compileHelper(s) + "[" + attr + "=" +  value + "]"
    case PseudoClassSelector(s,pseudoClass) => compileHelper(s) + ":" + pseudoClass
    case PseudoElementSelector(s,psuedoElement) =>compileHelper(s) + "::" + psuedoElement
    case DescendantSelector(s,selector) => compileHelper(s) + " " + compileHelper(selector)
    case ChildSelector(s,selector) => compileHelper(s) + ">" + compileHelper(selector)
    case AdjacentSelector(s,selector) => compileHelper(s) + "+" + compileHelper(selector)
    case GeneralSelector(s,selector) => compileHelper(s) + "~" + compileHelper(selector)
    case EmptySelector => throw new Exception("EmptySelector should be removed before compile")
  }

  def prettyHelper(compilable: CompilableAST, spaces: Int): String =  compilable match {
    case ACss(rules) => rules.map(r => prettyHelper(r,spaces)).mkString("\n\n")
    case CommentRule(s, declarations, comment) => (if (comment==null) "" else "/* " + comment + " */\n") +
      prettyHelper(s,spaces) + " {\n" + declarations.map(d => prettyHelper(d,spaces)).mkString("\n") + "\n}"
    case CommentDeclaration(p, value, comment) => " " * spaces + prettyHelper(p,spaces) + ": " + prettyHelper(value,spaces) + ";" +
      (if (comment==null) "" else " /* " + comment + " */")
    case AValue(value) => value
    case AProperty(value) => value
    case UniversalSelector => "*"
    case TypeSelector(tipe) => tipe
    case GroupSelector(selectors) => selectors.map(s => prettyHelper(s,spaces)).mkString(", ")
    case ClassNameSelector(s, className) => prettyHelper(s,spaces) + "." + className
    case IdSelector(s, id) => prettyHelper(s,spaces) + "#" + id
    case AttributeSelector(s,attr,value) =>prettyHelper(s,spaces) + "[" + attr + "=" +  value + "]"
    case PseudoClassSelector(s, pseudoClass) => prettyHelper(s,spaces) + ":" + pseudoClass
    case PseudoElementSelector(s, pseudoElement) => prettyHelper(s,spaces) + "::" + pseudoElement
    case DescendantSelector(s,selector) => prettyHelper(s,spaces) + " " + prettyHelper(selector,spaces)
    case ChildSelector(s,selector) => prettyHelper(s,spaces) + " > " + prettyHelper(selector,spaces)
    case AdjacentSelector(s,selector) => prettyHelper(s,spaces) + " + " + prettyHelper(selector,spaces)
    case GeneralSelector(s,selector) => prettyHelper(s,spaces) + " ~ " + prettyHelper(selector,spaces)
    case EmptySelector => throw new Exception("EmptySelector should be removed before compile")
  }
}
