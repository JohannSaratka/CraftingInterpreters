package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import main.Scanner;
import main.Token;
import main.TokenType;

public class ScannerTests extends TestCase {
	private ByteArrayOutputStream errorContent = new ByteArrayOutputStream();
	
	@Override
	protected void setUp() throws Exception {
	    System.setErr(new PrintStream(errorContent));
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		System.setErr(System.err);
		super.tearDown();
	}
	
	private boolean hasErrors(){
		return (errorContent.size() != 0);
	}
	private void testReturnsSingleValidToken( String lexeme, TokenType expectedType){
		Scanner scan = new Scanner(lexeme);
		List<Token> tokenList = scan.scanTokens();
		assertEquals(2, tokenList.size()); // returns expected token and EOF
		assertEquals(expectedType, tokenList.get(0).getType());
		assertFalse(hasErrors());
	}
	
	@Test
	public void testScanTokens_invalidCharacterIsReported() {
	    Scanner scan = new Scanner("@");
		scan.scanTokens();		
	    //check if there was any error reported
	    assertTrue( hasErrors());
	}
	
	@Test
	public void testScanTokens_singleCharacterIsValid() {
		testReturnsSingleValidToken("(", TokenType.LEFT_PAREN);
		testReturnsSingleValidToken(")", TokenType.RIGHT_PAREN);
		testReturnsSingleValidToken("{", TokenType.LEFT_BRACE);
		testReturnsSingleValidToken("}", TokenType.RIGHT_BRACE);
		testReturnsSingleValidToken(",", TokenType.COMMA);
		testReturnsSingleValidToken(".", TokenType.DOT);
		testReturnsSingleValidToken("-", TokenType.MINUS);
		testReturnsSingleValidToken("+", TokenType.PLUS);
		testReturnsSingleValidToken(";", TokenType.SEMICOLON);
		testReturnsSingleValidToken("*", TokenType.STAR);
		testReturnsSingleValidToken("/", TokenType.SLASH);
	}
	
	@Test
	public void testScanTokens_OneOrTwoCharacterIsValid() {
		testReturnsSingleValidToken("!", TokenType.BANG);
		testReturnsSingleValidToken("!=", TokenType.BANG_EQUAL);
		testReturnsSingleValidToken("=", TokenType.EQUAL);
		testReturnsSingleValidToken("==", TokenType.EQUAL_EQUAL);
		testReturnsSingleValidToken("<", TokenType.LESS);
		testReturnsSingleValidToken("<=", TokenType.LESS_EQUAL);
		testReturnsSingleValidToken(">", TokenType.GREATER);
		testReturnsSingleValidToken(">=", TokenType.GREATER_EQUAL);
	}

	@Test
	public void testScanTokens_GetOnlyTokenAfterAfterNewLine() {    
		testReturnsSingleValidToken("//only +parse+ token ; after {comment}\n*",TokenType.STAR);
	}
	
	@Test
	public void testScanTokens_MultipleValidLines() {
		String testInput = "// this is\ra\tcomment\n(( )){} // grouping stuff\n!*+-/=<> <= == // operators";
		Scanner scan = new Scanner(testInput);
		List<Token> tokenList = scan.scanTokens();
		assertEquals(17, tokenList.size());
		assertFalse(hasErrors());
		assertEquals(3, scan.getLine());
	}
	
	@Test
	public void testScanTokens_MultilineStringLiteralIsValid() {
		String testInput = "\"This is \n a String\"";
		Scanner scan = new Scanner(testInput);
		List<Token> tokenList = scan.scanTokens();

		assertEquals(2, tokenList.size());
		assertEquals(TokenType.STRING, tokenList.get(0).getType());
		assertFalse(hasErrors());
		assertEquals(2, scan.getLine());
	}
}
