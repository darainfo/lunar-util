package com.darainfo.lunar.vo;

import java.util.LinkedList;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LunarInfo {
	private LunarYmd yearStart; 
	private LunarYmd yearEnd; 
	private LinkedList<LunarYmd> monthEndDays;
	
	@Builder
	public LunarInfo(LunarYmd yearStart, LunarYmd yearEnd, LinkedList<LunarYmd> monthEndDays) {
		this.yearStart = yearStart;
		this.yearEnd = yearEnd;
		this.monthEndDays = monthEndDays;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("yearStart=").append(yearStart)
				.append("; yearEnd=").append(yearEnd)
				.append("; monthEndDays=").append(monthEndDays)
				.toString();
	}
}
