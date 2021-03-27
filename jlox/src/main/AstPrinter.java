package main;

import java.util.List;

import main.Expr.Assign;
import main.Expr.Binary;
import main.Expr.Grouping;
import main.Expr.Literal;
import main.Expr.Unary;
import main.Expr.Variable;
import main.Stmt.Block;
import main.Stmt.Expression;
import main.Stmt.If;
import main.Stmt.Print;
import main.Stmt.Var;
/**
 * @todo challenge: define visitor class that takes expression converts it to
 * reverse polish notation and returns resulting string
 */
class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String>{
	
	String print(Expr expr) {
		return expr.accept(this);
	}

	@Override
	public String visitBinaryExpr(Binary expr) {
		return parenthesize(expr.operator.lexeme, expr.left, expr.right);
	}

	@Override
	public String visitGroupingExpr(Grouping expr) {
		return parenthesize("group", expr.expression);
	}

	@Override
	public String visitLiteralExpr(Literal expr) {
		if(expr.value == null){
			return "nil";
		}
		return expr.value.toString();
	}

	@Override
	public String visitUnaryExpr(Unary expr) {
		return parenthesize(expr.operator.lexeme, expr.right);
	}
	
	private String parenthesize(String name, Expr... exprs) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(").append(name);
		for(Expr expr : exprs){
			builder.append(" ");
			builder.append(expr.accept(this));
		}
		builder.append(")");
		
		return builder.toString();
	}

	// Note: AstPrinting other types of syntax trees is not shown in the
	// book, but this is provided here as a reference for those reading
	// the full code.
	private String parenthesize2(String name, Object... parts) {
		StringBuilder builder = new StringBuilder();

		builder.append("(").append(name);
		transform(builder, parts);
		builder.append(")");

		return builder.toString();
	}

	private void transform(StringBuilder builder, Object... parts) {
		for (Object part : parts) {
			builder.append(" ");
			if (part instanceof Expr) {
				builder.append(((Expr) part).accept(this));
			} else if (part instanceof Stmt) {
				builder.append(((Stmt) part).accept(this));
			} else if (part instanceof Token) {
				builder.append(((Token) part).lexeme);
			} else if (part instanceof List) {
				transform(builder, ((List) part).toArray());
			} else {
				builder.append(part);
			}
		}
	}
	@Override
	public String visitAssignExpr(Assign expr) {
		return parenthesize2("=", expr.name.lexeme, expr.value);
	}

	@Override
	public String visitVariableExpr(Variable expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitBlockStmt(Block stmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitExpressionStmt(Expression stmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitPrintStmt(Print stmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitVarStmt(Var stmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visitIfStmt(If stmt) {
		// TODO Auto-generated method stub
		return null;
	}

}
