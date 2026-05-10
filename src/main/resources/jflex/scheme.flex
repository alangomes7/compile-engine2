/* --------------------------Usercode Section------------------------ */
package scanner;

import java_cup.runtime.*;

%%

/* -----------------Options and Declarations Section----------------- */
%class SchemeLexer
%public
%unicode
%line
%column

/* Tell JFlex we are using CUP. This automatically makes the lexer 
   implement java_cup.runtime.Scanner and return Symbol objects */
%cup

%{
  /** Helper methods to create CUP Symbols */
  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }
  
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

/* -------------------------Macro Declarations----------------------- */
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
/* ------------------------Lexical Rules Section--------------------- */

<YYINITIAL> {
  
  "#t"               { return symbol(sym.TRUE); }
  "#f"               { return symbol(sym.FALSE); }

  "("                { return symbol(sym.LPAREN); }
  ")"                { return symbol(sym.RPAREN); }
  "["                { return symbol(sym.LBRACKET); }
  "]"                { return symbol(sym.RBRACKET); }
  "'"                { return symbol(sym.QUOTE); }
  "`"                { return symbol(sym.QUASIQUOTE); }
  ",@"               { return symbol(sym.UNQUOTE_SPLICING); }
  ","                { return symbol(sym.UNQUOTE); }
  "."                { return symbol(sym.DOT); }

  /* Literals that pass their value to the parser */
  {Number}           { return symbol(sym.NUMBER, Double.valueOf(yytext())); }
  {String}           { return symbol(sym.STRING, yytext()); }
  {Identifier}       { return symbol(sym.IDENTIFIER, yytext()); }

  {Comment}          { /* ignore */ }
  {WhiteSpace}       { /* ignore */ }
}

[^]                  { throw new Error("Illegal character <" + yytext() + ">"); }