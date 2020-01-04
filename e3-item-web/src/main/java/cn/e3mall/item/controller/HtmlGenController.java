package cn.e3mall.item.controller;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/***
 * 生成静态页面测试
 * <p>Title: HtmlGenController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Controller
public class HtmlGenController {

	@Autowired private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genHtml")
	@ResponseBody
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		Map map = new HashMap();
		map.put("hello", "123456");
		Writer out = new FileWriter("D:/temp/freemarker/hello2.html");
		template.process(map, out);
		return "OK";
	}
}
