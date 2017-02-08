package com.teshehui.jvm;

public class MemoryTest {

	public static void main(String[] args) {
		
		testStringBuffer();
		
	}

	private static void testStringBuffer() {
		// TODO Auto-generated method stub
		
		StringBuilder sb1 = new StringBuilder("Hello");
		System.out.println("Before sb="+sb1);
		changeValue(sb1);
		changeValue2(sb1);
		System.out.println("After sb="+sb1);
		
	}

	private static void changeValue2(StringBuilder sb1) {
		sb1.append("World");
	}

	private static void changeValue(StringBuilder sb1) {
		sb1 = new StringBuilder("World");
	}
	
}
