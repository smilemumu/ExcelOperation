package com.szc.excel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil<main> {

	private  static Calendar calendar = null;

	public static String dateAddYear(int parseInt,String dateStr) {
		calendar = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			date = (Date) sdf.parse(dateStr.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + parseInt);
		String bt = sdf.format(calendar.getTime());
		return bt;
	}

	//当前时间减去两天
	public  static String getPastDate(int past){
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String bt = sdf.format(calendar.getTime());
		return bt;
	}
	public static String getCurrentTime_yyMMddhhmmss(){
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String bt = sdf.format(calendar.getTime());
		return bt;
	}
	public static boolean isExpired(String date) {
		boolean flag = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date paramDate = sdf.parse(date);
			Date now =new Date();
			if(paramDate.getTime()>now.getTime()){
				flag = false;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	public  static  void main(String []Args){
		System.out.println(DateUtil.dateAddYear(1,"2018-01-01"));
	}

}
