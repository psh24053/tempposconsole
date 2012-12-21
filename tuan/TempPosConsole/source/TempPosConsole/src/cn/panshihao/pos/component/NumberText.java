package cn.panshihao.pos.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class NumberText extends Text {

	public NumberText(Composite arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		
		addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				// 几种情况，输入控制键，输入中文，输入字符，输入数字   
			    // 正整数验证   
			    Pattern pattern = Pattern.compile("[0-9]\\d*");   
			    Matcher matcher = pattern.matcher(e.text);   
			    if (matcher.matches()){ // 处理数字   
			    	e.doit = true;   	
			    }
			    else if (e.text.length() > 0){ // 有字符情况,包含中文、空格   
			    	e.doit = false;   
			    }
			    else {
			    	// 控制键   
			    	e.doit = true;   
			    }
			}   
			      
		});  
		
		
	}

	
	
	
}
