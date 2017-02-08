package net.wwwfred.framework.demo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wwwfred.framework.core.jms.JmsMessageSender;
import net.wwwfred.framework.core.web.ServletUtil;
import net.wwwfred.framework.core.web.UploadFilePO;
import net.wwwfred.framework.demo.controller.spi.request.CreateUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.DeleteUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.FileUploadRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.QueryUserInfoRequest;
import net.wwwfred.framework.demo.controller.spi.request.QueryUserListRequest;
import net.wwwfred.framework.demo.controller.spi.request.SayHelloRequest;
import net.wwwfred.framework.demo.controller.spi.request.UpdateUserRequest;
import net.wwwfred.framework.demo.manager.MyManager;
import net.wwwfred.framework.demo.po.UserPO;
import net.wwwfred.framework.spi.request.BaseRequest;
import net.wwwfred.framework.spi.response.BaseResponse;
import net.wwwfred.framework.util.io.IOUtil;
import net.wwwfred.framework.util.json.JSONUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class MyController {
	
//	@Resource(name="remoteHessianMyManager")
	@Resource(name="remoteDubboMyManager")
	private MyManager myManager;
	
	@Resource
	private JmsMessageSender jmsMessageSender;
	
	@RequestMapping("/hello.do")
	@ResponseBody
	public String hello(HttpServletRequest request, HttpServletResponse response)
	{
		String responseData = "hello world controller do";
		jmsMessageSender.sendMessage(false, "test", responseData);
		jmsMessageSender.sendMessage(true, "test", responseData);
		return JSONUtil.toString(new BaseResponse<String>(responseData));
	}
	
	@RequestMapping("/fileUpload.do")
	@ResponseBody
	public String fileUpload(HttpServletRequest request, HttpServletResponse response)
	{        
        FileUploadRequest requestPO = ServletUtil.getModelFromRequest(request, FileUploadRequest.class);
        UserPO user = requestPO.getData();
        UploadFilePO userHead = requestPO.getUserHead();
        byte[] userHeadData = userHead.getFielData();
        String fileName = userHead.getFileName();
		
        String filePath = IOUtil.writeLocalData(userHeadData, "/temp/fileUpload/", fileName);
		user.setUserHeadUrl(filePath);
		return JSONUtil.toString(new BaseResponse<UserPO>(user));
	}
	
	@RequestMapping("/fileDownload.do")
	@ResponseBody
	public String fileDownload(HttpServletRequest request, HttpServletResponse response)
	{        
        byte[] fileData = IOUtil.readLoacalData("/temp/fileUpload/", "test.jpg");
        ServletUtil.response(request, fileData, response);
		
		return JSONUtil.toString(new BaseResponse<Object>(null));
	}
	
	@RequestMapping("/sayHello.do")
	@ResponseBody
	public String sayHello(HttpServletRequest request, HttpServletResponse response)
	{
		SayHelloRequest requestPO = ServletUtil.getModelFromRequest(request, SayHelloRequest.class);
		return JSONUtil.toString(myManager.sayHello(requestPO));
	}
	
	@RequestMapping("/getDataFromCache.do")
	@ResponseBody
	public String getDataFromCache(HttpServletRequest request, HttpServletResponse response)
	{
		BaseRequest requestPO = ServletUtil.getModelFromRequest(request, BaseRequest.class);
		return JSONUtil.toString(myManager.getDataFromCache(requestPO));
	}
	
	@RequestMapping("/createUser.do")
	@ResponseBody
	public String createUser(HttpServletRequest request, HttpServletResponse response)
	{
		CreateUserRequest requestPO = ServletUtil.getModelFromRequest(request, CreateUserRequest.class);
		return JSONUtil.toString(myManager.createUser(requestPO));
	}
	
	@RequestMapping("/queryUserInfo.do")
	@ResponseBody
	public String queryUserInfo(HttpServletRequest request, HttpServletResponse response)
	{
		QueryUserInfoRequest requestPO = ServletUtil.getModelFromRequest(request, QueryUserInfoRequest.class);
		return JSONUtil.toString(myManager.queryUserInfo(requestPO));
	}
	
	@RequestMapping("/getUserInfo.do")
	@ResponseBody
	public String getUserInfo(HttpServletRequest request, HttpServletResponse response)
	{
		GetUserRequest requestPO = ServletUtil.getModelFromRequest(request, GetUserRequest.class);
		return JSONUtil.toString(myManager.getUser(requestPO));
	}
	
	@RequestMapping("/getUserByNameInfo.do")
	@ResponseBody
	public String getUserByNameInfo(HttpServletRequest request, HttpServletResponse response)
	{
		QueryUserListRequest requestPO = ServletUtil.getModelFromRequest(request, QueryUserListRequest.class);
		return JSONUtil.toString(myManager.queryMemberByName(requestPO));
	}
	
	@RequestMapping("/updateUser.do")
	@ResponseBody
	public String updateUser(HttpServletRequest request, HttpServletResponse response)
	{
		UpdateUserRequest requestPO = ServletUtil.getModelFromRequest(request, UpdateUserRequest.class);
		return JSONUtil.toString(myManager.updateUser(requestPO));
	}
	
	@RequestMapping("/deleteUser.do")
	@ResponseBody
	public String deleteUser(HttpServletRequest request, HttpServletResponse response)
	{
		DeleteUserRequest requestPO = ServletUtil.getModelFromRequest(request, DeleteUserRequest.class);
		return JSONUtil.toString(myManager.deleteUser(requestPO));
	}
	
}
