package com.darainfo.lunar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.darainfo.lunar.vo.LunarInfo;
import com.darainfo.lunar.vo.LunarYmd;

/**
 * 음력 날짜 
 * -----------------------------------------------------------------------------
* @fileName : LunarFactory.java
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2022. 3. 17 			ytkim			최초작성
*-----------------------------------------------------------------------------
 */
public class LunarFactory {
	
	// all lunar info
	private HashMap<Integer, LunarInfo> allLunarMap = new HashMap<>();
	
	public static LunarFactory getInstance() {
		return ConfigurationHolder.instance;
	}

	private static class ConfigurationHolder {
		private static final LunarFactory instance = new LunarFactory();
	}

	private LunarFactory() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void init() throws FileNotFoundException, IOException {

		try (InputStream in = LunarFactory.class.getClassLoader().getResourceAsStream("lunar-kr.info");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
			
			// lunarInfoStr sample
			//ex : year;yearStart;yearEnd;monthEndDays;
			//1900;y:1899,m:12,d:1;y:1900,m:11,d:10;{y:1899,m:12,d:30},{y:1900,m:1,d:29,leap:true}
			String lunarInfoStr;
			while ((lunarInfoStr = reader.readLine()) != null) {
				if("".equals(lunarInfoStr.trim()) || lunarInfoStr.startsWith("#")) { // 주석
					continue; 
				}
				
				String[] lunarInfoArr = lunarInfoStr.split(";");
				
				allLunarMap.put(Integer.parseInt(lunarInfoArr[0]), LunarInfo.builder()
						.yearStart(getYmdInfo(lunarInfoArr[1]))
						.yearEnd(getYmdInfo(lunarInfoArr[2]))
						.monthEndDays(getMonthEndDays(lunarInfoArr[3]))
						.build());
			}
			reader.close();
			in.close();
		}
	}
	
	public LunarInfo lunarInfo(int year) {
		return allLunarMap.getOrDefault(year, null);
	}
	
	private LinkedList<LunarYmd> getMonthEndDays(String monthEndDays) {
		StringTokenizer stk=new StringTokenizer(monthEndDays,"{}");
		
		LinkedList<LunarYmd> lunarYmdList = new LinkedList<>();
		while(stk.hasMoreTokens()){
			String token =stk.nextToken(); 
			token = token.replaceAll("\\s","");
			if(",".equals(token)) continue; 
			
			lunarYmdList.add(getYmdInfo(token));
		}
		return lunarYmdList;
	}

	private LunarYmd getYmdInfo(String ymdString) {
		String[] ymdArr = ymdString.split(",");
		
		int year = Integer.parseInt(ymdArr[0].split(":")[1]);
		int month = Integer.parseInt(ymdArr[1].split(":")[1]);
		int day = Integer.parseInt(ymdArr[2].split(":")[1]);
		
		boolean leap = ymdArr.length > 3 ? Boolean.valueOf(ymdArr[3].split(":")[1]):false; 
		
		return LunarYmd.builder()
				.year(year)
				.month(month)
				.day(day)
				.leap(leap)
				.build();
	}
}
