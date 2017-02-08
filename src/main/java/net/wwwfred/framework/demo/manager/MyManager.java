package net.wwwfred.framework.demo.manager;

import net.wwwfred.framework.demo.controller.spi.request.CreateUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.DeleteUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserCashAccountRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserFromNameRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.QueryUserInfoRequest;
import net.wwwfred.framework.demo.controller.spi.request.QueryUserListRequest;
import net.wwwfred.framework.demo.controller.spi.request.SayHelloRequest;
import net.wwwfred.framework.demo.controller.spi.request.UpdateUserRequest;
import net.wwwfred.framework.demo.po.UserPO;
import net.wwwfred.framework.po.ResultSetPO;
import net.wwwfred.framework.spi.request.BaseRequest;
import net.wwwfred.framework.spi.response.BaseResponse;

public interface MyManager {
	
	 BaseResponse<String> sayHello(SayHelloRequest requestPO);
	 
	 BaseResponse<ResultSetPO<UserPO>> getDataFromCache(BaseRequest requestPO);
	 
	 BaseResponse<UserPO> createUser(CreateUserRequest requestPO);
	 
	 BaseResponse<UserPO> getUser(GetUserRequest requestPO);
	 
	 BaseResponse<UserPO> getUser(GetUserFromNameRequest requestPO);
	 
	 BaseResponse<UserPO> getUserCashAccount(GetUserCashAccountRequest requestPO);
	 
	 BaseResponse<UserPO> queryUserInfo(QueryUserInfoRequest requestPO);
	 
	 BaseResponse<ResultSetPO<UserPO>> queryMemberByName(QueryUserListRequest requestPO);
	 
	 BaseResponse<Object> updateUser(UpdateUserRequest requestPO);
	 
	 BaseResponse<Object> deleteUser(DeleteUserRequest requestPO);
	 
}
