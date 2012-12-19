package cn.panshihao.pos.tools;

import java.util.Random;

public class GenerateKeyCode {
	
	//生成兑换码的头
	public static String generateKeyCodeHead(){
		
		String keyCodeHead = System.currentTimeMillis() + "";
		keyCodeHead = keyCodeHead.substring(8, 12);
		
		return keyCodeHead;
	}
	
	//生成兑换码的尾
	public static String generateKeyCodeTail(){
		
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   //生成字符串从此序列中取   
		Random random = new Random();       
		StringBuffer sb = new StringBuffer();       
		for (int i = 0; i < 10; i++) {  
			
			int number = random.nextInt(base.length());           
			sb.append(base.charAt(number));       
			
		}      
		return sb.toString();   
		
	}
	
	public static void main(String[] args) {
		System.out.println(GenerateKeyCode.generateKeyCodeHead());
	}
	
}
