package test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import junit.framework.TestCase;
import main.Scanner;
import main.TokenType;

public class ScannerTests extends TestCase {

	private void testIsValidToken( String lexeme, TokenType expectedType){
		// TODO use introspection
		Scanner scan = new Scanner(lexeme);		
		assertEquals(expectedType + " " + lexeme + " null", scan.scanTokens().get(0).toString());
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
		testIsValidToken("(", TokenType.LEFT_PAREN);
		testIsValidToken(")", TokenType.RIGHT_PAREN);
		testIsValidToken("{", TokenType.LEFT_BRACE);
		testIsValidToken("}", TokenType.RIGHT_BRACE);
		testIsValidToken(",", TokenType.COMMA);
		testIsValidToken(".", TokenType.DOT);
		testIsValidToken("-", TokenType.MINUS);
		testIsValidToken("+", TokenType.PLUS);
		testIsValidToken(";", TokenType.SEMICOLON);
		testIsValidToken("*", TokenType.STAR);
	}

}
