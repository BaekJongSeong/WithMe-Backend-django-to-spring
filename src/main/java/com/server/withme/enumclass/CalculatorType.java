package com.server.withme.enumclass;

//아래는 람다버전, 위는 추상 메소드 버전, enum 필드에 추상 메소드 설정하고 각 enum요소들에서 구현....크

public enum CalculatorType {
	CALC_A("오"){
		long calculate(long value) {return value;}
	},
	CALC_B("우"){
		long calculate(long value){return value * 10;}
	},
	CALC_C("진짜"){
		long calculate(long value){return value * 3;}
	},
	CALC_ETC("개멋있네"){
		long calculate(long value){return 0L;}
	};
	
	private final String val;
	
	CalculatorType(String val){
		this.val=val;
	}
	
	public String getVal() {
		return val;
	}
	
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
