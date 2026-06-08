/* Java code for the class */
package core.lexer;

import core.lexer.models.atomic.LexerError;
import java_cup.runtime.Symbol;  // Importação OBRIGATÓRIA do JCUP
import core.parser.sym; // IMPORTAÇÃO ESSENCIAL para o Scanner encontrar os Tokens do Parser
import java.util.ArrayList;
import java.util.List;

%%

%class Scanner
%public
%unicode
%line
%column
%cup  /* Diretiva mágica que liga o JFlex ao JCUP */

%{
  private List<LexerError> errors = new ArrayList<>();

  public List<LexerError> getErrors() {
      return errors;
  }

  /* Retorna o token para o JCUP construir a AST, sem mexer na tabela de símbolos */
  private Symbol token(int cupSymbolType, String typeName) {
      return new Symbol(cupSymbolType, yyline, yycolumn, yytext());
  }

  /* Sobrecarga para passar o valor parseado (como Double para NUMBER) */
  private Symbol token(int cupSymbolType, String typeName, Object parsedValue) {
      return new Symbol(cupSymbolType, yyline, yycolumn, parsedValue);
  }
%}

/* Regular expressions and macros */
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

  /* --- PALAVRAS-CHAVE (Devem vir antes de Identifier!) --- */
  "define"           { return token(sym.KW_DEFINE, "KW_DEFINE"); }
  "if"               { return token(sym.KW_IF, "KW_IF"); }
  "set!"             { return token(sym.KW_SET, "KW_SET"); }
  "lambda"           { return token(sym.KW_LAMBDA, "KW_LAMBDA"); }
  "begin"            { return token(sym.KW_BEGIN, "KW_BEGIN"); }
  "cond"             { return token(sym.KW_COND, "KW_COND"); }
  "else"             { return token(sym.KW_ELSE, "KW_ELSE"); }
  "case"             { return token(sym.KW_CASE, "KW_CASE"); }
  "and"              { return token(sym.KW_AND, "KW_AND"); }
  "or"               { return token(sym.KW_OR, "KW_OR"); }
  "let"              { return token(sym.KW_LET, "KW_LET"); }
  "let*"             { return token(sym.KW_LETSTAR, "KW_LETSTAR"); }
  "letrec"           { return token(sym.KW_LETREC, "KW_LETREC"); }
  "do"               { return token(sym.KW_DO, "KW_DO"); }
  "delay"            { return token(sym.KW_DELAY, "KW_DELAY"); }

  /* --- SÍMBOLOS E OPERADORES --- */
  "("                { return token(sym.LPAREN, "LPAREN"); }
  ")"                { return token(sym.RPAREN, "RPAREN"); }
  "."                { return token(sym.DOT, "DOT"); }
  "'"                { return token(sym.QUOTE, "QUOTE"); }
  "#("               { return token(sym.VECTOR_START, "VECTOR_START"); }
  "=>"               { return token(sym.ARROW, "ARROW"); }

  /* --- TIPOS PRIMITIVOS E DADOS --- */
  "#t"               { return token(sym.BOOLEAN, "TRUE", Boolean.TRUE); }
  "#f"               { return token(sym.BOOLEAN, "FALSE", Boolean.FALSE); }
  {Number}           { return token(sym.NUMBER, "NUMBER", Double.parseDouble(yytext())); }
  {String}           { return token(sym.STRING, "STRING", yytext()); }

  /* --- IDENTIFICADORES (Nomes de variáveis/funções) --- */
  {Identifier}       { return token(sym.IDENTIFIER, "IDENTIFIER"); }

  /* --- IGNORADOS --- */
  {Comment}          { /* ignore */ }
  {WhiteSpace}       { /* ignore */ }
}

/* Error messages */
[^]
{
  LexerError err = new LexerError(yyline + 1, yycolumn + 1, "Illegal character <" + yytext() + ">");
  errors.add(err);
  // Não fazemos 'return' aqui. O JFlex simplesmente registra o erro e continua lendo o arquivo.
}