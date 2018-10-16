package com.szc.excel.domain;

public class LoginInfo {

	private String name;
	private String password;
	private String loginType;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	@Override
	public String toString() {
		return "LoginInfo [name=" + name + ", password=" + password + ", loginType=" + loginType + "]";
	}
	
	
}
