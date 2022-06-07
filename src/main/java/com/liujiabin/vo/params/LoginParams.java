package com.liujiabin.vo.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginParams {

	private String account;
	private String password;
	private String nickname;
}
