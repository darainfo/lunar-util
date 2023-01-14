package com.darainfo.lunar.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LunarYmd {
	private int year;
	private int month;
	private int day;
	private boolean leap;
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("{year=").append(year)
				.append(", month=").append(month)
				.append(", day=").append(day)
				.append(", leap=").append(leap)
				.append("}")
				.toString();
	}
}
