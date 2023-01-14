package com.darainfo.lunar;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.darainfo.lunar.vo.LunarInfo;
import com.darainfo.lunar.vo.LunarYmd;
import com.darainfo.lunar.vo.SolarLunarDayInfo;

/**
 * 음력-> 양력 변환 util
 * -----------------------------------------------------------------------------
* @fileName : LunarUtils.java
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2022. 3. 17 			ytkim			최초작성
*-----------------------------------------------------------------------------
 */
public final class LunarUtils {

	/**
	 * solar date -> 음력 일자로 변경 SolarLunarDayInfo
	 * 
	 * @method  : getSolarToLunarDay
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param solarDate
	 * @return
	 */
	public static SolarLunarDayInfo getSolarToLunarDay(Date solarDate){
		return getSolarToLunarDayList(solarDate, solarDate).get(0);
	}
	
	/**
	 * solar date -> 음력 일자로 변경 2005-12-25 -> "2005-11-24"
	 * 
	 * @method  : getSolarToStringLunarDay
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param solarDate
	 * @return
	 */
	public static String getSolarToStringLunarDay(Date solarDate){
		return getSolarToLunarDayList(solarDate, solarDate).get(0).getLunYmd();
	}
	
	/**
	 * 양력 시작일 ~ 음력 시작일 -> 음력 날짜 목록으로 리턴
	 * 
	 * @method  : getSolarToLunarDayList
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param solarStartYmd
	 * @param solarEndYmd
	 * @return
	 */
	public static List<SolarLunarDayInfo> getSolarToLunarDayList(Date solarStartYmd, Date solarEndYmd){
		return getSolarToLunarDayList(dateToLocalDate(solarStartYmd), dateToLocalDate(solarEndYmd));
	}
	
	public static List<SolarLunarDayInfo> getSolarToLunarDayList(LocalDate startLocalDate, LocalDate endLocalDate){
		
		long dateLen = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
	    if(dateLen < 0) {
	    	throw new InvalidParameterException("Start date cannot be greater than end date.");
	    }
	    
	    // end date check
	    LocalDate supportEndDate = LocalDate.of(LunarConstants.END_SUPPORT_YEAR, 12, 31);
	    if(ChronoUnit.DAYS.between(supportEndDate, endLocalDate) > 0){
			throw new InvalidParameterException(String.format("Out of date range supported. end date : %s, support date : %s", endLocalDate, supportEndDate));
		}
	    
	    //start date check
	    LocalDate supportStartDate = LocalDate.of(LunarConstants.START_SUPPORT_YEAR, 01, 01);
	    if(ChronoUnit.DAYS.between(supportStartDate, startLocalDate) < 0){
	    	throw new InvalidParameterException(String.format("Out of date range supported. start date : %s, support date : %s", startLocalDate, supportStartDate));
	    }
	    
	    int solYear = startLocalDate.getYear()
	    	,solMonth= startLocalDate.getMonthValue()
    		,solDay = startLocalDate.getDayOfMonth();

    	int lastYear = endLocalDate.getYear()
    		,lastMonth= endLocalDate.getMonthValue()
    		,lastDay = endLocalDate.getDayOfMonth();
	    
    	LunarInfo lunarYearInfo = LunarFactory.getInstance().lunarInfo(solYear);
	    
    	int lunDay = lunarYearInfo.getYearStart().getDay();

    	int[] monthArr = getMonthLastDayArr(solYear);
	    
    	int solElapseDay = solDay-1; // 첫시작일 부터 1 이므로 1을 빼기

    	for (int i = 0; i < solMonth-1; i++){
    		solElapseDay += monthArr[i];
    	}
    	
    	int lunStartDay;
    	int lunMonthEndDayInfoIdx = 0;
    	LinkedList<LunarYmd> lunMonthEndDays = lunarYearInfo.getMonthEndDays();
    	
    	if(solElapseDay == 0){
    		lunStartDay = lunDay;
    	}else{
    		int lunMonthIdx = 0;
    		LunarYmd startLunMonthInfo = lunMonthEndDays.get(0);
    		int lunElapseDay = startLunMonthInfo.getDay() - lunDay;
    		
    		if(lunElapseDay <= solElapseDay){
    			lunMonthIdx = 1;
    			// 경과일 계산
    			for (; lunMonthIdx < lunMonthEndDays.size(); lunMonthIdx++){
    				startLunMonthInfo = lunMonthEndDays.get(lunMonthIdx);
    				lunElapseDay += startLunMonthInfo.getDay();

    				if(lunElapseDay >= solElapseDay){
    					break; 
    				}
    			}
    		}
    		
    		// 경과일이 해당년의 max 월을 지났을때.
    		if(lunMonthEndDays.size() == lunMonthIdx && solElapseDay > lunElapseDay){
    			lunarYearInfo = LunarFactory.getInstance().lunarInfo(solYear+1);
    			lunMonthEndDays = lunarYearInfo.getMonthEndDays();
    			lunMonthIdx = 0;
    			startLunMonthInfo = lunMonthEndDays.get(lunMonthIdx);
    			lunElapseDay += startLunMonthInfo.getDay();
    		}
    		
    		lunMonthEndDayInfoIdx = lunMonthIdx;

    		if(lunElapseDay > solElapseDay){
    			lunStartDay = (startLunMonthInfo.getDay() -(lunElapseDay - solElapseDay));
    		}else if(lunElapseDay == solElapseDay){
    			lunStartDay = startLunMonthInfo.getDay();
    		}else{
    			lunMonthEndDayInfoIdx = lunMonthIdx-1;
    			lunStartDay = solElapseDay - lunElapseDay;
    		}
    	}
    	
    	int idx = 0;
		List<SolarLunarDayInfo> reval = new LinkedList<>();
		SolarLunarDayInfo solarLunarDayInfo = null; 
    	while(idx <= dateLen) {
    		
    		LunarYmd endLunarYmd = lunMonthEndDays.get(lunMonthEndDayInfoIdx);

			solarLunarDayInfo = SolarLunarDayInfo.builder()
				.solYear(solYear)
				.solMonth(solMonth)
				.solDay(solDay)
				.solYmd(String.format("%d-%02d-%02d", solYear, solMonth, solDay))
				.lunYear(endLunarYmd.getYear())
				.lunMonth(endLunarYmd.getMonth())
				.lunDay(lunStartDay)
				.isLeap(endLunarYmd.isLeap())
				.lunYmd(String.format("%d-%02d-%02d", endLunarYmd.getYear(), endLunarYmd.getMonth(), lunStartDay))
				.build();
			
			if(endLunarYmd.getDay() == lunStartDay){
				solarLunarDayInfo.setLunEndDay(true);
				lunMonthEndDayInfoIdx++;
				lunStartDay = 0;
				
				if(lunMonthEndDayInfoIdx == lunMonthEndDays.size()){
					if(LunarConstants.END_SUPPORT_YEAR < solYear){ // 지원하지 않는 날짜를 빈값처리.
						throw new Error(String.format("Out of date range supported. end date : %s, support date : %s", String.format("%d-%02d-%02d", solYear, solMonth, solDay), supportEndDate));
					}
					
					if(LunarConstants.END_SUPPORT_YEAR == solYear){ // 제한 년도의 마지막 달의 값을 처리.
						lunMonthEndDays.add(LunarYmd.builder().year(solYear).month(endLunarYmd.getMonth()+1).day(30).build());
					}else{
						lunMonthEndDayInfoIdx = 0;
						lunMonthEndDays = LunarFactory.getInstance().lunarInfo(solYear+1).getMonthEndDays();
					}
				}
			}
			
			lunStartDay++;
			reval.add(solarLunarDayInfo);
			
			if(solYear == lastYear && solMonth ==lastMonth && solDay ==lastDay){
				break; 
			}
			
			/* add a day of solar calendar */
			if (solMonth == 12 && solDay == 31)	{
				solYear++;
				solMonth = 1;
				solDay = 1;
				monthArr = getMonthLastDayArr(solYear);
			}else if (monthArr[solMonth - 1] == solDay){
				solMonth++;
				solDay = 1; 
			}else{
				solDay++;
			}
		}
		
		return reval; 
	}
	
	/**
	 * 음력 날자 -> 양력 날짜 
	 * 
	 * @method  : getSolarToLunarDayList
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param lunarDate 음력날짜
	 * @param leap 윤달여부
	 * @return
	 */
	public static String getLunarToStringSolarDay(Date solarDate){
		return getLunarToSolarDay(dateToLocalDate(solarDate)).getSolYmd();
	}
	
	public static String getLunarToStringSolarDay(Date solarDate, boolean leap){
		return getLunarToSolarDay(dateToLocalDate(solarDate), leap).getSolYmd();
	}
	
	public static SolarLunarDayInfo getLunarToSolarDay(LocalDate lunarDate) {
		return getLunarToSolarDay(lunarDate, false);
	}
	public static SolarLunarDayInfo getLunarToSolarDay(LocalDate lunarDate, boolean leap) {
		int lunYear = lunarDate.getYear()
			,lunMonth = lunarDate.getMonthValue() 
			,lunDay = lunarDate.getDayOfMonth();
		
		int solYear = lunYear;
		LunarInfo lunarYearInfo = LunarFactory.getInstance().lunarInfo(solYear);
		
		LunarYmd yearEnd = lunarYearInfo.getYearEnd();
		if(yearEnd.getMonth() < lunMonth 
			|| (!yearEnd.isLeap() && yearEnd.getMonth() == lunMonth && yearEnd.getDay() < lunDay) // 마지막 달이 윤달 일 경우 체크
		){
			solYear +=1;
			lunarYearInfo = LunarFactory.getInstance().lunarInfo(solYear);
		}
		
		LinkedList<LunarYmd> lunMonthEndDays = lunarYearInfo.getMonthEndDays();
		
		int lunElapseDay;
		LunarYmd yearStart = lunarYearInfo.getYearStart();
		if(yearStart.getYear() == lunYear && yearStart.getMonth() == lunMonth){
			lunElapseDay = lunDay - yearStart.getDay() + 1;
		}else{
			lunElapseDay = lunMonthEndDays.get(0).getDay()+1-yearStart.getDay() + lunDay;
			
			for(int i=1; i < lunMonthEndDays.size(); i++){
				LunarYmd lunMonthEndItem = lunMonthEndDays.get(i);
			
				if(lunMonthEndItem.getYear() == lunYear && lunMonthEndItem.getMonth() == lunMonth){
					boolean breakFlag = true; 
					if(leap) {
						if(lunMonthEndDays.size() > i+1) {
							if(lunMonthEndDays.get(i+1).isLeap()) {
								breakFlag = false; 
							}
						}else {
							breakFlag = false; 
							lunMonthEndItem = LunarFactory.getInstance().lunarInfo(solYear+1).getMonthEndDays().get(i);
						}
					}
					
					if(breakFlag) break;
					
				}
				lunElapseDay += lunMonthEndItem.getDay();
			}
		}
		
		int[] monthArr = getMonthLastDayArr(solYear);
		int solMonth=0;
		
		for (; solMonth<monthArr.length; solMonth++){
			if(lunElapseDay <= monthArr[solMonth]){
				break;
			}else{
				lunElapseDay -= monthArr[solMonth];
			}
		}
		
		int solDay = lunElapseDay;
		solMonth = solMonth+1;
		
		return SolarLunarDayInfo.builder()
				.solYear(solYear)
				.solMonth(solMonth)
				.solDay(solDay)
				.solYmd(String.format("%d-%02d-%02d", solYear, solMonth, solDay))
				.lunYear(lunYear)
				.lunMonth(lunMonth)
				.lunDay(lunDay)
				.isLeap(leap)
				.lunYmd(String.format("%d-%02d-%02d", lunYear, lunMonth,lunDay))
				.build();
		
	}
	
	/**
	 * today date -> 음력 일자로 변경. 
	 * 
	 * @method  : todayToLunarDay
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param dt
	 * @return
	 */
	public static SolarLunarDayInfo todayToLunarDay(){
		Date today = new Date();
		return getSolarToLunarDayList(today, today).get(0);
	}
	
	/**
	 * Date -> to LocalDate 
	 * 
	 * @method  : dateToLocalDate
	 * @author   : ytkim
	 * @date   : 2022. 1. 23. 
	 * @param dt
	 * @return
	 */
	public static LocalDate dateToLocalDate(Date dt) {
	    return new java.sql.Date(dt.getTime()).toLocalDate();
	}
	
	private static int [] getMonthLastDayArr(int year){
		int [] monthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (year % 400 == 0) monthDay[1]=29;
		else if (year % 100 == 0) monthDay[1]=28;
		else if (year % 4 == 0) monthDay[1]=29;

		return monthDay;
	}
}
