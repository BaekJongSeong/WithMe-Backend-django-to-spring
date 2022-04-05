package com.server.withme.enumclass;

import java.util.function.Function;

//아래는 람다버전, 위는 추상 메소드 버전, enum 필드에 추상 메소드 설정하고 각 enum요소들에서 구현....크

public enum CalculatorType {
	CALC_A{
		long calculate(long value) {return value;}
	},
	CALC_B{
		long calculate(long value){return value * 10;}
	},
	CALC_C{
		long calculate(long value){return value * 3;}
	},
	CALC_ETC{
		long calculate(long value){return 0L;}
	};
	
	abstract long calculate(long value);

}
/*
public enum CalculatorType {
	CALC_A(value -> value),
	CALC_B(value -> value * 10),
	CALC_C(value -> value * 3),
	CALC_ETC(value -> 0L);
	
	private Function<Long,Long> expression;
	
	CalculatorType(Function<Long,Long> expression) {this.expression = expression;}
	
	public long calculate(long value) {return expression.apply(value);}

}*/
