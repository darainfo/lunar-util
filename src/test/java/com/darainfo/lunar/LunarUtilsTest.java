package com.darainfo.lunar;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.darainfo.lunar.test.BaseTest;
import com.darainfo.lunar.vo.SolarLunarDayInfo;

public class LunarUtilsTest extends BaseTest{
	
	@Test
	public void getLunarDayToSolarDayTest() {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			String result = null;
			Date checkDt = null;
			
			String[] lunarDt = {"1990-05-12", "2003-06-14", "2009-04-28", "2022-07-23", "2023-02-26", "2026-07-05", "2032-03-23", "2038-10-05", "2048-07-06"};
			String[] solarDt = {"1990-06-04", "2003-07-13", "2009-05-22", "2022-08-20", "2023-03-17", "2026-08-17", "2032-05-02", "2038-11-01", "2048-08-15"};
			
			for (int i =0 ;i < lunarDt.length;i++) {
				checkDt = df.parse(lunarDt[i]);
				result = LunarUtils.getLunarToStringSolarDay(checkDt);
				assertEquals(solarDt[i], result,  "checkDt : "+checkDt+" result : "+ result);
			}
		
			checkDt = df.parse("2023-02-26");
			result = LunarUtils.getLunarToStringSolarDay(checkDt,true);
			assertEquals("2023-04-16", result,  "checkDt : "+checkDt+" result : "+ result);
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void getSolarToStringLunarDayTest() {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			String result = null;
			Date checkDt = null;
			
			
			String[] solarDt = {"1995-01-01", "1990-06-04", "2003-07-13", "2009-05-22", "2022-08-20", "2023-04-16", "2026-08-17", "2032-05-02", "2038-11-01", "2048-08-15"};
			String[] lunarDt = {"1994-12-01", "1990-05-12", "2003-06-14", "2009-04-28", "2022-07-23", "2023-02-26", "2026-07-05", "2032-03-23", "2038-10-05", "2048-07-06"};
			
			for (int i =0 ;i < solarDt.length;i++) {
				checkDt = df.parse(solarDt[i]);
				result = LunarUtils.getSolarToStringLunarDay(checkDt);
				assertEquals(lunarDt[i], result,  "checkDt : "+checkDt+" result : "+ result);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void todayToLunarDayTest() {
		SolarLunarDayInfo item = LunarUtils.todayToLunarDay();
		System.out.println("today -> "+ String.format("%d-%02d-%02d", item.getLunYear(),item.getLunMonth(),item.getLunDay()));
		assertNotNull(item,  "lunar info empty");
	}
	
	@Test
	public void lunarDayTest() {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date startDt = df.parse("2005-12-25");
			
			SolarLunarDayInfo item = LunarUtils.getSolarToLunarDay(startDt);
			//System.out.println("solDate : " + df.format(startDt)+" -> "+ String.format("%d-%02d-%02d", item.getLunYear(),item.getLunMonth(),item.getLunDay()));
			assertNotNull(item,  "lunar info empty");
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void lunarListTest() {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date startDt = df.parse("2023-01-01");
			
			df = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = df.parse("2050-12-31");
			
			long dateLen = ChronoUnit.DAYS.between(LunarUtils.dateToLocalDate(startDt), LunarUtils.dateToLocalDate(endDate))+1;
			
			
			List<SolarLunarDayInfo> lunarList = LunarUtils.getSolarToLunarDayList(startDt, endDate);
			
//			for (SolarLunarDayInfo item: lunarList) {
//				System.out.println(String.format("%d-%02d-%02d", item.getSolYear(),item.getSolMonth(),item.getSolDay())+ " ;; " + item.getLunYear()+"\t"+item.getLunMonth()+"\t"+ item.getLunDay());
//				//System.out.println( item.getLunYear()+"\t"+item.getLunMonth()+"\t"+ item.getLunDay());
//				
//			}
			
			assertEquals(dateLen, lunarList.size(), "startDt : "+ startDt+ " endDate: " +endDate+" dateLen : "+ dateLen +" lunar day size : "+ lunarList.size());
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
