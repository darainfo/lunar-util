package com.darainfo.lunar.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SolarLunarDayInfo {
	private int solYear;
	private int solMonth;
	private int solDay;
	private String solYmd;
	
	private int lunYear;
	private int lunMonth;
	private int lunDay;
	private String lunYmd; 
	private boolean isLeap; 
	private boolean lunEndDay;
	
	
}
