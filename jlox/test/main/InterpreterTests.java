package main;

import org.junit.Test;

import junit.framework.TestCase;
import main.Expr;
import main.Expr.Literal;

public class InterpreterTests extends TestCase {
	Interpreter interpreter = new Interpreter();
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testVisitBinary_CorrectInput() {
		checkVisitBinary(3.0, TokenType.MINUS, 1.0, 2.0);
		checkVisitBinary(3.0, TokenType.PLUS, 1.0, 4.0);
		checkVisitBinary(6.0, TokenType.SLASH, 2.0, 3.0);
		checkVisitBinary(6.0, TokenType.STAR, 3.0, 18.0);
		checkVisitBinary("Hello", TokenType.PLUS, " World", "Hello World");
		checkVisitBinary(6.0, TokenType.GREATER, 3.0, true);
		checkVisitBinary(6.0, TokenType.GREATER_EQUAL, 6.0, true);
		checkVisitBinary(6.0, TokenType.LESS, 3.0, false);
		checkVisitBinary(6.0, TokenType.LESS_EQUAL, 3.0, false);
		checkVisitBinary("three", TokenType.BANG_EQUAL, 3.0, true);
		checkVisitBinary(null, TokenType.EQUAL_EQUAL, 3.0, false);
		checkVisitBinary(null, TokenType.EQUAL_EQUAL, null, true);
		checkVisitBinary("null", TokenType.EQUAL_EQUAL, null, false);
		checkVisitBinary(true, TokenType.EQUAL_EQUAL, false, false);
	}
	
	private void checkVisitBinary(Object left, TokenType type, Object right, Object expected) {
		Expr.Binary testData = new Expr.Binary(createLiteral(left), createToken(type), createLiteral(right));
		assertEquals(expected, interpreter.visitBinaryExpr(testData));
	}

	private Token createToken(TokenType type) {
		return new Token(type, "", null, 0);
	}

	private Literal createLiteral(Object val) {
		return new Expr.Literal(val);
	}
	
	@Test
	public void testVisitUnary_CorrectInput() {
		checkVisitUnary(TokenType.MINUS, 3.0, -3.0);
		checkVisitUnary(TokenType.BANG, true, false);
		checkVisitUnary(TokenType.BANG, false, true);
		checkVisitUnary(TokenType.BANG, null, true);
		checkVisitUnary(TokenType.BANG, 0, false);
		checkVisitUnary(TokenType.BANG, 0.0, false);
		checkVisitUnary(TokenType.BANG, "test", false);
	}

	private void checkVisitUnary(TokenType type, Object right, Object expected) {
		Expr.Unary testData = new Expr.Unary(createToken(type), createLiteral(right));
		assertEquals(expected, interpreter.visitUnaryExpr(testData));
	}
}
