package com.feedback.test;

import com.feedback.common.EmailUtil;
import com.feedback.vo.SettingVO;

public class EMAILTest {

	public EMAILTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		SettingVO s = new SettingVO();
		s.setEmailHost("mail.autovista.in");
		s.setEmailFrom("rushikesh.bagul@motionvista.com");
		s.setEmailTo("rushi_bagul@rediffmail.com");
		
	/*	s.setEmailHost("smtp.gmail.com");
		s.setEmailFrom("rushi.s.bagul@gmail.com");
		s.setEmailTo(rushikesh.bagul@motionvista.com");*/
	
		EmailUtil e = new EmailUtil();
		try {
			boolean b = e.sendEmail(s, "Test", "test msg");
			System.out.println("Boolean :"+b);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
