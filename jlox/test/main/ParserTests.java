package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ParserTests extends TestCase  {
	private static final Map<TokenType, Token> tokenMap;
	
	static {
		tokenMap = new HashMap<>();

		tokenMap.put(TokenType.NUMBER, new Token(TokenType.NUMBER, "0", 0, 1));
		tokenMap.put(TokenType.EQUAL_EQUAL, new Token(TokenType.EQUAL_EQUAL, "==", null, 1));
		tokenMap.put(TokenType.BANG_EQUAL, new Token(TokenType.BANG_EQUAL, "!=", null, 1));
		tokenMap.put(TokenType.GREATER, new Token(TokenType.GREATER, ">", null, 1));
		tokenMap.put(TokenType.GREATER_EQUAL, new Token(TokenType.GREATER_EQUAL, ">=", null, 1));
		tokenMap.put(TokenType.LESS, new Token(TokenType.LESS, "<", null, 1));
		tokenMap.put(TokenType.LESS_EQUAL, new Token(TokenType.LESS_EQUAL, "<=", null, 1));
		tokenMap.put(TokenType.PLUS, new Token(TokenType.PLUS, "+", null, 1));
		tokenMap.put(TokenType.MINUS, new Token(TokenType.MINUS, "-", null, 1));
		tokenMap.put(TokenType.SLASH, new Token(TokenType.SLASH, "/", null, 1));
		tokenMap.put(TokenType.STAR, new Token(TokenType.STAR, "*", null, 1));
		tokenMap.put(TokenType.BANG, new Token(TokenType.BANG, "!", null, 1));
		tokenMap.put(TokenType.TRUE, new Token(TokenType.TRUE, "", null, 1));
		tokenMap.put(TokenType.FALSE, new Token(TokenType.FALSE, "", null, 1));
		tokenMap.put(TokenType.NIL, new Token(TokenType.NIL, "", null, 1));
		tokenMap.put(TokenType.STRING, new Token(TokenType.STRING, "ab", "ab", 1));
		tokenMap.put(TokenType.LEFT_PAREN, new Token(TokenType.LEFT_PAREN, "(", null, 1));
		tokenMap.put(TokenType.RIGHT_PAREN, new Token(TokenType.RIGHT_PAREN, ")", null, 1));
		tokenMap.put(TokenType.SEMICOLON, new Token(TokenType.SEMICOLON, ";", null, 1));
		tokenMap.put(TokenType.EOF, new Token(TokenType.EOF, "", null, 1));
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}	
	
	private List<Token> generateTokenList(TokenType... types) {
		List<Token> tList = new ArrayList<>();	
		for(TokenType type : types){
			Token token = tokenMap.get(type); 
			if(token != null)
				tList.add(token);
			else
				fail(String.format("Cant add token %s to token list", type));
		}
		// automatically add ';' and EOF to create valid statement/ expression
		tList.add(tokenMap.get(TokenType.SEMICOLON));
		tList.add(tokenMap.get(TokenType.EOF));
		return tList;
	}
	
	private Expr parseTypes(TokenType... types){
		List<Token> tList = generateTokenList(types);

		Parser p = new Parser(tList);
		Stmt.Expression statement = (Stmt.Expression) p.parse().get(0);
		
		return statement.expression;
	}
	
	private void shouldReturnBinaryExpr(TokenType t){
		Expr e = parseTypes(TokenType.NUMBER, t, TokenType.NUMBER);		
		assertThat(e, instanceOf(Expr.Binary.class));		
	}
	
	private void shouldReturnUnaryExpr(TokenType t){
		Expr e = parseTypes(t, TokenType.NUMBER);
		assertThat(e, instanceOf(Expr.Unary.class));		
	}
	
	private void shouldReturnLiteralExpr(TokenType t){
		Expr e = parseTypes(t);
		assertThat(e, instanceOf(Expr.Literal.class));		
	}
	
	@Test
	public void testParse_ValidBinaryExpr() {		
		shouldReturnBinaryExpr(TokenType.EQUAL_EQUAL);
		shouldReturnBinaryExpr(TokenType.BANG_EQUAL);
		shouldReturnBinaryExpr(TokenType.GREATER);
		shouldReturnBinaryExpr(TokenType.GREATER_EQUAL);
		shouldReturnBinaryExpr(TokenType.LESS);
		shouldReturnBinaryExpr(TokenType.LESS_EQUAL);
		shouldReturnBinaryExpr(TokenType.PLUS);
		shouldReturnBinaryExpr(TokenType.MINUS);
		shouldReturnBinaryExpr(TokenType.SLASH);
		shouldReturnBinaryExpr(TokenType.STAR);
	}
	
	@Test
	public void testParse_ValidUnaryExpr() {
		shouldReturnUnaryExpr(TokenType.MINUS);
		shouldReturnUnaryExpr(TokenType.BANG);
	}

	@Test
	public void testParse_ValidLiteralExpr() {
		shouldReturnLiteralExpr(TokenType.TRUE);
		shouldReturnLiteralExpr(TokenType.FALSE);
		shouldReturnLiteralExpr(TokenType.NIL);
		shouldReturnLiteralExpr(TokenType.NUMBER);
		shouldReturnLiteralExpr(TokenType.STRING);
	}

	@Test
	public void testParse_ValidGroupingExpr() {
		Expr e = parseTypes(TokenType.LEFT_PAREN,TokenType.NUMBER,TokenType.RIGHT_PAREN);
		assertThat(e, instanceOf(Expr.Grouping.class));	
	}
	
	private Token generateNumberToken(float num)
	{
		return new Token(TokenType.NUMBER,Float.toString(num),num,1);
	}
	
	@Test
	public void testParse_precedence() {
		List<Token> tList = new ArrayList<>();
		tList.add(generateNumberToken(6));
		tList.add(tokenMap.get(TokenType.SLASH));
		tList.add(generateNumberToken(3));
		tList.add(tokenMap.get(TokenType.MINUS));
		tList.add(generateNumberToken(1));
		tList.add(tokenMap.get(TokenType.SEMICOLON));
		tList.add(tokenMap.get(TokenType.EOF));
		Parser p = new Parser(tList);
		List<Stmt> e = p.parse();
		fail("Not yet implemented");
	}

	@Test
	public void testParse_error_recovery() {
		fail("Not yet implemented");
	}
}
