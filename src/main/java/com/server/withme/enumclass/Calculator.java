package com.server.withme.enumclass;

import java.util.function.Function;

public enum Calculator {
	CALC_A(value -> value),
	CALC_B(value -> value * 10),
	CALC_C(value -> value * 3),
	CALC_ETC(value -> 0L);
	
	private Function<Long,Long> expression;
	
	Calculator(Function<Long,Long> expression) {this.expression = expression;}
	
	public long calculate(long value) {return expression.apply(value);}

}
