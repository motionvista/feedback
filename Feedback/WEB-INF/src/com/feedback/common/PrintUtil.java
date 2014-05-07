/*
 * Created on Feb 19, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.feedback.common;


import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;


import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.FileOutputStream;


/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrintUtil {

private Date systemDate = null;

static{
	try {
		Properties p = new Properties();
		p.setProperty( "resource.loader", "class" );
	    p.setProperty( "class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );

		Velocity.init(p);
	} catch (Exception e) {
		System.out.println("Velocity init error..." + e);
	}
}

public PrintUtil(){

}

public Date getSystemDate() {
	return systemDate;
}


public void setSystemDate(Date systemDate) {
	this.systemDate = systemDate;
}


public static String replace(String source,String s1,String s2){
	return source.replaceAll(s1, s2);
}
public static boolean isNull(Object obj){
	if(obj == null){
		return true;
	}else{
		return false;
	}
}
public static boolean isLessThanZero(Double obj){
	if(obj == null){
		return false;
	}else{
		if(obj.doubleValue() < 0){
			return true;
		}else{
			return false;
		}
	}
}

public static String getText(String templateName,HashMap env)throws Exception{
	Template template = Velocity.getTemplate(templateName);
		VelocityContext context = new VelocityContext();
		Iterator itr = env.keySet().iterator();
		while(itr.hasNext()){
			String key = (String)itr.next();
			context.put(key,env.get(key));
		}
		StringWriter writer = new StringWriter();
		if (template != null) {
			template.merge(context, writer);
		}
		writer.flush();
		writer.close();
	return writer.toString();
}

}
