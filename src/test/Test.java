package test;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		File f = new File("/Users/lukabaljak/Downloads/oi/ru2.jpg");
		if(f.exists())
			System.out.println("postoji");
		else
			System.out.println("nepostoji");
	}
	
}
