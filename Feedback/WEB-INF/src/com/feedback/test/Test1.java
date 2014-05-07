package com.feedback.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {

	public static void main(String[] args) {
		Date d = null;
		Date d1 = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		try {
			d = ft.parse("1989-09-05");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("D  :"+d);
		System.out.println("D 1  :"+d1);
		int diff = d1.getYear() - d.getYear();
		System.out.println("Diff :"+diff);
		
	}
}
