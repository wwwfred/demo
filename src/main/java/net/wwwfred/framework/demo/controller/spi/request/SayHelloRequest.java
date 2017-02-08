package net.wwwfred.framework.demo.controller.spi.request;

import net.wwwfred.framework.core.web.FieldTypeAnnotation;
import net.wwwfred.framework.core.web.FieldTypeEnum;
import net.wwwfred.framework.spi.request.BaseRequest;

public class SayHelloRequest extends BaseRequest{

	private static final long serialVersionUID = 1L;
	@FieldTypeAnnotation(FieldTypeEnum.simple)
	private String data;
	
	public SayHelloRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SayHelloRequest(String version, String clientType,
			String businessType,String data) {
		super(version, clientType, businessType);
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
