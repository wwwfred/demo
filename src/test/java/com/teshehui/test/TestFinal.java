package com.teshehui.test;


public class TestFinal {

	private static int n = 3;
	
	public static void main(String[] args) {
		System.out.println(testReturn());
	}
	
	public static int testReturn() {
		try {
			if(n%3==0)
			{
				throw new RuntimeException("test0");
			}
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
//			throw new RuntimeException("test1");
			System.out.println("test1");
			return 1;
		}
		finally
		{
			System.out.println("2");
//			return 2;
		}
	}
	
}
