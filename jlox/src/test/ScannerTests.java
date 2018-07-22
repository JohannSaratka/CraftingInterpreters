package test;

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

	private void testReturnsSingleValidToken( String lexeme, TokenType expectedType){
		Scanner scan = new Scanner(lexeme);
		List<Token> tokenList = scan.scanTokens();
		assertEquals(2, tokenList.size()); // returns expected token and EOF
		assertEquals(expectedType, tokenList.get(0).getType());
	}
	
	@Test
	public void testScanTokens_invalidCharacterIsReported() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setErr(new PrintStream(outContent));

	    Scanner scan = new Scanner("@");
		scan.scanTokens();
		
		System.setErr(System.err);
	    //check if there was any error reported
	    assertTrue( outContent.size() > 0);
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
}
