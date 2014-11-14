package impl.ast2

import java.beans.Expression
import java.io.File

import org.parboiled2.ParseError

import scala.io.Source
import scala.util.{Failure, Success}
import scala.collection.immutable.Seq

object Foo {
  def main(args: Array[String]): Unit = {

    val line = Source.fromFile(new File("src/examples/Sign.flix")).getLines().mkString("\n")

    val parser = new Calculator(line)
    parser.TopLevel.run() match {
      case Success(exprAst) => println("Result: " + exprAst)
      case Failure(e: ParseError) => println("Expression is not valid: " + parser.formatError(e))
      case Failure(e) => println("Unexpected error during parsing run: " + e)
    }
  }
}

sealed trait Ast

object Ast {

  /**
   * The type of all Ast nodes.
   */
  sealed trait Node

  /**
   * The root the Ast. A root consists of a sequence of namespace declarations.
   */
  case class Root(namespaces: Seq[NameSpace])

  sealed trait Name

  case class SimpleName(name: String) extends Name

  case class QualifiedName(prefix: String, rest: Name) extends Name

  case class NameSpace(name: Name, body: Seq[Ast.Declaration]) extends Node

  sealed trait Declaration extends Node

  case class TypeDeclaration(name: String, t: Type) extends Declaration

  case class ValueDeclaration(name: String, t: Type) extends Declaration

  case class FunctionDeclaration(x: String, arguments: Seq[Argument], returnType: Type) extends Declaration

  sealed trait Type extends Node

  case class TypeTag(x: String) extends Type

  case class TypeVariant(xs: Seq[Type])

  case class Argument(name: String, typ: Type) extends Node

}

import org.parboiled2._

class Calculator(val input: ParserInput) extends Parser {
  def TopLevel: Rule1[Ast.Root] = rule {
    oneOrMore(NameSpace) ~ EOI ~> Ast.Root
  }

  def NameSpace: Rule1[Ast.NameSpace] = rule {
    "namespace" ~ WhiteSpace ~ Name ~ WhiteSpace ~ '{' ~ WhiteSpace ~ NameSpaceBody ~ WhiteSpace ~ '}' ~> Ast.NameSpace
  }

  def NameSpaceBody: Rule1[Seq[Ast.Declaration]] = rule {
    zeroOrMore(Declaration)
  }

  def Declaration: Rule1[Ast.Declaration] = rule {
    TypeDeclaration |  ValueDeclaration | FunctionDeclaration
  }

  def TypeDeclaration: Rule1[Ast.TypeDeclaration] = rule {
    "type" ~ WhiteSpace ~ capture(Identifier) ~ WhiteSpace ~ "=" ~ WhiteSpace ~ Type ~ WhiteSpace ~> Ast.TypeDeclaration
  }

  def ValueDeclaration: Rule1[Ast.ValueDeclaration] = rule {
    "val" ~ WhiteSpace ~ capture(Identifier) ~ WhiteSpace ~ "=" ~ WhiteSpace ~ Type ~ WhiteSpace ~> Ast.ValueDeclaration
  }

  def FunctionDeclaration: Rule1[Ast.FunctionDeclaration] = rule {
    "def" ~ WhiteSpace ~ capture(Identifier) ~ "(" ~ ArgumentList ~ ")" ~ ":" ~ WhiteSpace ~ Type ~ WhiteSpace ~ "=" ~ WhiteSpace ~ Expression ~> Ast.FunctionDeclaration
  }

  def ArgumentList: Rule1[Seq[Ast.Argument]] = rule {
    zeroOrMore(Argument).separatedBy("," ~ optional(WhiteSpace))
  }

  def Argument: Rule1[Ast.Argument] = rule {
    capture(Identifier) ~ ":" ~ WhiteSpace ~ Type ~> Ast.Argument
  }

  def Type: Rule1[Ast.TypeTag] = rule {
    capture(Identifier) ~> Ast.TypeTag
  }

  def TypeVariant: Rule1[Ast.TypeVariant] = rule {
    oneOrMore(Type) ~> Ast.TypeVariant
  }

  def Name: Rule1[Ast.Name] = rule {
    QualifiedName | SimpleName
  }

  // TODO: Use separated by.

  def SimpleName: Rule1[Ast.SimpleName] = rule {
    capture(Identifier) ~> Ast.SimpleName
  }

  def QualifiedName: Rule1[Ast.QualifiedName] = rule {
    capture(Identifier) ~ "." ~ Name ~> Ast.QualifiedName
  }

  def Expression: Rule0 = rule {
    MatchExpression | TupleExp | LocalVariable
  }

  def MatchExpression: Rule0 = rule {
    "match" ~ WhiteSpace ~ Expression ~ WhiteSpace ~ "with" ~ WhiteSpace ~ "{" ~ WhiteSpace ~ MatchBody ~ WhiteSpace ~ "}"
  }

  def MatchBody = rule {
    "case" ~ WhiteSpace ~ Pattern
  }

  def Pattern = rule {
    "("
  }

  def TupleExp = rule {
    "(" ~ Expression ~ ", " ~ Expression ~ ")"
  }

  def LocalVariable = rule {
    Identifier
  }

  def Identifier = rule {
    CharPredicate.Alpha ~ zeroOrMore(CharPredicate.AlphaNum)
  }

  def Digits = rule {
    oneOrMore(CharPredicate.Digit)
  }

  /**
   * Whitespace is one or more spaces, tabs or newlines.
   */
  def WhiteSpace: Rule0 = rule {
    oneOrMore(" " | "\t" | "\n")
  }

}
