package com.teshehui.demo.test.controller;

import net.wwwfred.framework.util.io.IOUtil;

public class TestController {

	public static void main(String[] args) {
		
		String url = "http://localhost:9090/demo-app/test/fileDownload.do";
		byte[] data = IOUtil.getByteArrayFromHttpUrl(url, null, null);
		byte[] orignalData = IOUtil.readLoacalData("/temp/fileUpload/", "test.jpg");
		System.out.println(data.length==orignalData.length);
		System.out.println(IOUtil.writeLocalData(data, "/", "test.data"));
		
	}
	
}
