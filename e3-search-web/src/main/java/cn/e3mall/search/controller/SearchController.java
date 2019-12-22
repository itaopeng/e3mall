package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * 搜索结果页面
 * <p>Title: SearchController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Controller
public class SearchController {

	@Value("${SEARCH_DEFAULT_ROW}")
	private Integer SEARCH_DEFAULT_ROW;
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String getSearchResult(String keyword,@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
		SearchResult searchResult = searchService.search(keyword, page, SEARCH_DEFAULT_ROW);
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());
		//异常测试
		//int i = 1/0;
		//返回逻辑视图
		return "search";
	}
}
