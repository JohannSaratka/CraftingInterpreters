package main;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	
	
	public Scanner (String source) {
		this.source = source;
	}
	public int getLine(){
		return line;
	}
	public List<Token> scanTokens(){
		while (!isAtEnd()){
			// We are at the beginning of the next lexeme.
			start = current ;
			scanToken();
		}
		
		tokens.add(new Token(TokenType.EOF, "", null, line ));
		return tokens;
	}

	private void scanToken() {
		char c = advance();
		switch (c) {
		case '(':addToken(TokenType.LEFT_PAREN);break;
		case ')':addToken(TokenType.RIGHT_PAREN);break;
		case '{':addToken(TokenType.LEFT_BRACE);break;
		case '}':addToken(TokenType.RIGHT_BRACE);break;
		case ',':addToken(TokenType.COMMA);break;
		case '.':addToken(TokenType.DOT);break;
		case '-':addToken(TokenType.MINUS);break;
		case '+':addToken(TokenType.PLUS);break;
		case ';':addToken(TokenType.SEMICOLON);break;
		case '*':addToken(TokenType.STAR);break;
		case '!':addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);break;
		case '=':addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);break;
		case '<':addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);break;
		case '>':addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);break;
		case '/':
			if(match('/')) {
				 handleComment();
			} else {
				addToken(TokenType.SLASH);
			}
			break;
		case ' ': // fallthrough
		case '\r':// fallthrough
		case '\t': // ignore whitespace 
			break;
			
		case '\n':
			line++;
			break;
		default:
			Lox.error(line, "Unexpected character " + c);
			break;
		}
		
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	/**
	 * @return next char in source
	 */
	private char advance() {
		current++;
		return source.charAt(current - 1);
	}
	
	private void addToken(TokenType type) {
		addToken(type, null);
		
	}
	
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}
	
	/** Consume current char if its equal to the expected
	 * @param expected char to look for
	 * @return true if current char in source is equal to expected char 
	 */
	private boolean match(char expected) {
		if (isAtEnd()) return false;
		if (source.charAt(current) != expected) return false;
		
		current++;
		return true;
	}
	
	/**
	 * A comment goes until the end of the line
	 */
	private void handleComment(){
		while (peek() != '\n' && !isAtEnd()) advance();
	}
	
	/**
	 * @return Current char without consuming it
	 */
	private char peek() {
		if(isAtEnd()) return '\0';
		return source.charAt(current);
	}
}