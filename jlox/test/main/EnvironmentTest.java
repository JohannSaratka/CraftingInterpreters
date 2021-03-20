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
	
	@Test
	public void test_assignVariable() {
		Token t = new Token(TokenType.VAR, "x", null, 1);
		environment.define(t.lexeme, 3);
		environment.assign(t, 5);
		
		assertEquals(5, environment.get(t));
	}
	
	@Test(expected=RuntimeError.class)
	public void test_assignWithoutDefinitionShouldThrowError() {
		Token t = new Token(TokenType.VAR, "x", null, 1);
		environment.assign(t, 5);
	}
	
	@Test
	public void test_defineVariableInInnerScope() {
		Environment inner_env = new Environment(environment);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		Token t2 = new Token(TokenType.VAR, "y", null, 1);
		
		environment.define(t.lexeme, 3);
		inner_env.define(t2.lexeme, 2);
		
		assertEquals(3, environment.get(t));
		assertEquals(3, inner_env.get(t));
		assertEquals(2, inner_env.get(t2));
	}
	
	@Test(expected=RuntimeError.class)
	public void test__defineVariableInInnerScopeAccessFromOuterShouldThrowError() {
		Environment inner_env = new Environment(environment);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		Token t2 = new Token(TokenType.VAR, "y", null, 1);
		
		environment.define(t.lexeme, 3);
		inner_env.define(t2.lexeme, 2);
		
		environment.get(t2);
	}
	
	@Test
	public void test_redefineVariableInInnerScope() {
		Environment inner_env = new Environment(environment);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		
		environment.define(t.lexeme, 3);
		inner_env.define(t.lexeme, 2);
		
		assertEquals(3, environment.get(t));
		assertEquals(2, inner_env.get(t));
	}
	
	@Test
	public void test_redefineAndAssignVariableInInnerScope() {
		Environment inner_env = new Environment(environment);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		
		environment.define(t.lexeme, 3);
		inner_env.define(t.lexeme, 2);
		inner_env.assign(t, 5);
		
		assertEquals(3, environment.get(t));
		assertEquals(5, inner_env.get(t));
	}
	
	@Test
	public void test_redefineVariableInInnerAndAssignInOuterScope() {
		Environment inner_env = new Environment(environment);
		Token t = new Token(TokenType.VAR, "x", null, 1);
		
		environment.define(t.lexeme, 3);
		inner_env.define(t.lexeme, 2);
		environment.assign(t, 5);
		
		assertEquals(5, environment.get(t));
		assertEquals(2, inner_env.get(t));
	}
}
