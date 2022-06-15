package com.server.withme.enumclass;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

//아래는 람다버전, 위는 추상 메소드 버전, enum 필드에 추상 메소드 설정하고 각 enum요소들에서 구현....크

public enum DayCalculator {
	ZERO{
		public Calendar calculateDay(Date date) { 
			cal.setTime(date);
			cal.add(Calendar.HOUR, 9);
			return cal;
		}
	},
	ONE{
		public Calendar calculateDay(Date date) {
			cal.setTime(date);
			calStandard.setTime(new Timestamp(System.currentTimeMillis()));
			calStandard.add(Calendar.DATE, 7);
			cal.add(Calendar.DATE, 1);
			if(cal.getTime().getTime() > calStandard.getTime().getTime())
				cal.setTime(date);
			cal.add(Calendar.HOUR, 9);
			return cal;}
	},
	SEVEN{
		public Calendar calculateDay(Date date){
			cal.setTime(date);
			cal.add(Calendar.DATE, 7);
			cal.add(Calendar.HOUR, 9);
			return cal;
			}
	};
	
	private static Calendar cal = Calendar.getInstance();
	private static Calendar calStandard = Calendar.getInstance();
	
	public abstract Calendar calculateDay(Date date);

}