package cn.e3mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 * 首页展示
 * @author tao
 *
 */
@Controller
public class IndexController {
	
	@RequestMapping("/index")
	public String showIndex(){
		return "index";
	}
}
