/* Java code for the class */
package core.lexer;

import core.lexer.models.SymbolTable;
import core.lexer.models.atomic.Token;
import core.lexer.models.atomic.LexerError;
import java.util.ArrayList;
import java.util.List;

%%

%class Scanner
%public
%unicode
%line
%column
%type Token

%{
  private SymbolTable symbolTable = new SymbolTable();
  private List<LexerError> errors = new ArrayList<>();

  public SymbolTable getSymbolTable() {
      return symbolTable;
  }

  public List<LexerError> getErrors() {
      return errors;
  }

  private Token token(String type) {
      return symbolTable.insert(type, yytext(), yyline + 1, yycolumn + 1);
  }
%}

/* Regular expressions and tokens */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment = ";" {InputCharacter}* {LineTerminator}?

Digit  = [0-9]
Number = -? {Digit}+ (\. {Digit}+)?

StringCharacter = [^\r\n\"\\]
String = \" ({StringCharacter} | \\\" | \\\\)* \"

Letter = [a-zA-Z]
SpecialInitial = [!\$%&\*\/\:\<\=\>\?\^\_\~]
Initial = {Letter} | {SpecialInitial}
SpecialSubsequent = [\+\-\.\@]
Subsequent = {Initial} | {Digit} | {SpecialSubsequent}
Identifier = {Initial} {Subsequent}* | \+ | - | \.\.\.

%%

/* Lexical rules */
<YYINITIAL> {
  
  "#t"               { return token("TRUE"); }
  "#f"               { return token("FALSE"); }

  "("                { return token("LPAREN"); }
  ")"                { return token("RPAREN"); }
  "["                { return token("LBRACKET"); }
  "]"                { return token("RBRACKET"); }
  "'"                { return token("QUOTE"); }
  "`"                { return token("QUASIQUOTE"); }
  ",@"               { return token("UNQUOTE_SPLICING"); }
  ","                { return token("UNQUOTE"); }
  "."                { return token("DOT"); }

  {Number}           { return token("NUMBER"); }
  {String}           { return token("STRING"); }
  {Identifier}       { return token("IDENTIFIER"); }

  {Comment}          { /* ignore */ }
  {WhiteSpace}       { /* ignore */ }
}

/* Error messages */
[^] 
{ 
  LexerError err = new LexerError(yyline + 1, yycolumn + 1, "Illegal character <" + yytext() + ">");
  errors.add(err);
  // We do not return a token here, allowing the lexer to continue finding errors
}