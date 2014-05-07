package com.feedback.test;

import com.feedback.bl.Service;

public class SMSTest {
	
	public SMSTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			new Service("feedback").sendSms("7588515594", "Hi.....");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
