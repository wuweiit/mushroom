package org.marker.mushroom.utils;

import java.util.Calendar;
import java.util.Date;


/**
 * 日期、时间工具类
 * @author marker
 * @date 2014-08-02
 * */
public class DateUtils {

	
	/**
	 * 获取当前剩余时间秒数
	 * @return
	 */
	public static int getCurentDayRemainingTime(){
		Calendar c = Calendar.getInstance();
		Date d = c.getTime(); 
		
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		Date d2 = c.getTime(); 
		return (int) ((d2.getTime() - d.getTime())/60/60/1000); 
	} 

}
