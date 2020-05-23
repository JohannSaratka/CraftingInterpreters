package main;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ParserTests extends TestCase  {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}	
	
	private List<Token> generateTokenList(TokenType... types) {
		List<Token> tList = new ArrayList<>();	
		for(TokenType type : types){
			switch(type){
			case NUMBER: tList.add(new Token(type,"0",0,1)); break;
			case EQUAL_EQUAL: tList.add(new Token(type,"==",null,1)); break;			
			case BANG_EQUAL: tList.add(new Token(type,"!=",null,1)); break;
			case GREATER: tList.add(new Token(type,">",null,1)); break;
			case GREATER_EQUAL: tList.add(new Token(type,">=",null,1)); break;
			case LESS: tList.add(new Token(type,"<",null,1)); break;
			case LESS_EQUAL: tList.add(new Token(type,"<=",null,1)); break;
			case PLUS: tList.add(new Token(type,"+",null,1)); break;
			case MINUS: tList.add(new Token(type,"-",null,1)); break;
			case SLASH: tList.add(new Token(type,"/",null,1)); break;
			case STAR: tList.add(new Token(type,"*",null,1)); break;
			case BANG: tList.add(new Token(type,"!",null,1)); break;
			case TRUE: tList.add(new Token(type,"",null,1)); break;
			case FALSE: tList.add(new Token(type,"",null,1)); break;
			case NIL: tList.add(new Token(type,"",null,1)); break;
			case STRING: tList.add(new Token(type,"ab","ab",1)); break;
			case LEFT_PAREN: tList.add(new Token(type,"(",null,1)); break;
			case RIGHT_PAREN: tList.add(new Token(type,")",null,1)); break;
			case EOF: tList.add(new Token(type,"",null,1));break;
			default: fail(String.format("Cant add token %s to token list", type));
				break;
			}
		}
		tList.add(new Token(TokenType.EOF,"",null,1));
		return tList;
	}
	
	private Expr parseTypes(TokenType... types){
		List<Token> tList = generateTokenList(types);

		Parser p = new Parser(tList);
		return p.parse();
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
	
	@Test
	public void testParse_precedence() {
		List<Token> tList = new ArrayList<>();
		tList.add(new Token(TokenType.NUMBER,"6",6,1));
		tList.add(new Token(TokenType.SLASH,"/",null,1));
		tList.add(new Token(TokenType.NUMBER,"3",3,1));
		tList.add(new Token(TokenType.MINUS,"-",null,1));
		tList.add(new Token(TokenType.NUMBER,"1",1,1));
		tList.add(new Token(TokenType.SEMICOLON,";",null,1));
		Parser p = new Parser(tList);
		fail("Not yet implemented");
	}

	@Test
	public void testParse_error_recovery() {
		fail("Not yet implemented");
	}
}
