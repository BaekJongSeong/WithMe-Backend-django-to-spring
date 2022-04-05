package com.server.withme.enumclass;

import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 상수와 특정값을 연결시켜놓은건데 특정값을 연결시키려면 해당 값들을 리턴할 수 있는 함수가 선언되어있어야한다.
 * ex) SKT - 에스케이텔레콤
 * 
 * 주로 사용되는 메서드는 values(), ordinal(), valueOf()
 * 
 * valueOf(String arg) String 값을 enum에서 가져온다. 값이 없으면 Exception 발생
 * values()  enum의 요소들을 순서대로 enum 타입의 배열로 리턴한다.
 * ordinal() 해당 값이 enum에 정의된 순서를 리턴한다. (index 값 리턴)
 * 
 * for(Company type : Company.values()){ 
 * 	System.out.println(type.getValue()); // 에스케이, 엘쥐, 케이티, 삼성, 애플 
 * 	System.out.println(type); // SK, LG, KT, SAMSUNG, APPLE
} 
 * System.out.println(Company.APPLE.getValue()); // 애플
 * 
 * System.out.println(Company.APPLE.ordinal()); //4
 */

@Getter
public enum Company { 
	
	SKT("에스케이텔레콤"), 
	LG("엘쥐"), 
	KT("케이티"), 
	SAMSUNG("삼성"), 
	APPLE("애플"); 
	
	// 지금 보면 Company() 이런 형태임. 즉 SKT는 company라는 클래스 네임이고 클래스를 생성하기 위해서 field값인 value를 초기화하기 위해 String이 들어간거고~~
	private final String value;
	
	Company(String value){ 
		this.value = value; 
		} 
	
	}
