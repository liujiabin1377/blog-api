package com.liujiabin.vo;

public enum ErrorCode {
	ACCOUNT_BLANK(10004,"请填写账号和密码"),
	TOKEN_NOT_EXIST(10003,"账户已过期/令牌不存在"),
	PARAMS_ERROR(10001,"参数有误"),
	ACCOUNT_EXIST(10005,"账户已经存在"),
	ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码不存在"),
	NO_PERMISSION(70001,"无访问权限"),
	SESSION_TIME_OUT(90001,"会话超时"),
	NO_LOGIN(90002,"未登录"),;

	private int code;
	private String msg;

	ErrorCode(int code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}


}
