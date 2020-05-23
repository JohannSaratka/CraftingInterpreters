package main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class AstPrinterTests  extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPrintsExpressions() {
		Expr expression = new Expr.Binary(                     
	        new Expr.Unary(                                    
	            new Token(TokenType.MINUS, "-", null, 1),      
	            new Expr.Literal(123)),                        
	        new Token(TokenType.STAR, "*", null, 1),           
	        new Expr.Grouping(                                 
	            new Expr.Literal(45.67)));
		assertEquals("(* (- 123) (group 45.67))", 
				new AstPrinter().print(expression));
	}

}
