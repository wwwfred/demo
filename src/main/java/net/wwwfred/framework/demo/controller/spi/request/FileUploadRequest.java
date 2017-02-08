package net.wwwfred.framework.demo.controller.spi.request;

import net.wwwfred.framework.core.web.FieldTypeAnnotation;
import net.wwwfred.framework.core.web.FieldTypeEnum;
import net.wwwfred.framework.core.web.UploadFilePO;
import net.wwwfred.framework.demo.po.UserPO;
import net.wwwfred.framework.spi.request.BaseRequest;

public class FileUploadRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;
	
	@FieldTypeAnnotation(FieldTypeEnum.json)
	private UserPO data;
	
	private UploadFilePO userHead;

	public UserPO getData() {
		return data;
	}

	public void setData(UserPO data) {
		this.data = data;
	}

	public UploadFilePO getUserHead() {
		return userHead;
	}

	public void setUserHead(UploadFilePO userHead) {
		this.userHead = userHead;
	}
	
}
