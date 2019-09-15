package cn.e3mall.controller;

import java.util.List;

import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理
 * @author tao
 *
 */
@Controller
public class ContentController {
	@Autowired
	private ContentService contentSerivce;
	
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent content){
		return contentSerivce.addContent(content);
	}
	
	@RequestMapping(value="/content/query/list",method=RequestMethod.GET)
	@ResponseBody
	public List<TbContent> queryContent(long categoryId){
		List<TbContent> result = contentSerivce.getContentByCid(categoryId);
		return result;
	}
	
	@RequestMapping(value="/rest/content/edit",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateContent(TbContent content){
		return contentSerivce.updateContent(content);
	}
	
	
}
