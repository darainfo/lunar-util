package com.darainfo.lunar;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.darainfo.lunar.test.BaseTest;
import com.darainfo.lunar.vo.LunarInfo;

public class LunarFactoryTest extends BaseTest{
	@Test
	public void lunarListTest() {
		
		LunarInfo item = LunarFactory.getInstance().lunarInfo(2000);
		
		assertNotNull(item, "lunar info empty : 2000");
			 
	}
}
