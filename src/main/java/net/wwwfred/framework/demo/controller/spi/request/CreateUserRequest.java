package net.wwwfred.framework.demo.controller.spi.request;

import net.wwwfred.framework.core.web.FieldTypeAnnotation;
import net.wwwfred.framework.core.web.FieldTypeEnum;
import net.wwwfred.framework.demo.po.UserPO;
import net.wwwfred.framework.spi.request.BaseRequest;

public class CreateUserRequest extends BaseRequest{
	private static final long serialVersionUID = 1L;
	@FieldTypeAnnotation(FieldTypeEnum.json)
	private UserPO data;
	
	public UserPO getData() {
		return data;
	}

	public void setData(UserPO data) {
		this.data = data;
	}
	
}
