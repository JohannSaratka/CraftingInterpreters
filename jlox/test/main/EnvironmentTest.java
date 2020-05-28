package main;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EnvironmentTest {

	private Environment environment;
	@Before
	public void setUp() throws Exception {
		environment = new Environment();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_defineVariable() {
		environment.define("x", 3);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		assertEquals(3, environment.get(t));
	}
	
	@Test
	public void test_redefineVariable() {
		environment.define("x", 3);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		assertEquals(3, environment.get(t));
		environment.define("x", 5);
		assertEquals(5, environment.get(t));
	}
	
	@Test(expected=RuntimeError.class)
	public void test_undefinedVariableShouldThrowError() {
		environment.define("x", 3);
		Token t = new Token(TokenType.VAR, "y", null, 1);
		assertEquals(3, environment.get(t));
	}
}
