package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0") long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 添加分类节点
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCategory(long parentId,String name){
		//调用服务
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
	}
	
	/**
	 * 添加删除节点
	 */
	@RequestMapping(value="/content/category/delete",method=RequestMethod.POST)
	@ResponseBody
	public void deleteContentCategory(long id){
		//调用服务
		contentCategoryService.deleteContentCategory(id);
		
	}
	
	/**
	 * 添加修改节点
	 */
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public void updateContentCategory(long id,String name){
		//调用服务
		contentCategoryService.updateContentCategory(id, name);
	}
	
}
