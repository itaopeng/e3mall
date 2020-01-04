package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.RegisterService;

/****
 * 注册功能
 * <p>Title: RegitsterController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Controller
public class RegitsterController {

	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegitster(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		E3Result e3Result = registerService.checkDate(param, type);
		return e3Result;
	}
}
