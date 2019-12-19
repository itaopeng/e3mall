package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/***
 * 商品搜索dao
 * <p>Title: SearchDao</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Repository
public class SearchDao {

	private SolrServer solrServer;
	
	/***
	 * 根据查询条件查询索引
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param solrQuery
	 * @return
	 */
	public SearchResult search(SolrQuery query) throws Exception {
		//根据query 查询索引库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//取查询总记录数
		long numFound = solrDocumentList.getNumFound();
		SearchResult result  = new SearchResult();
		result.setRecordCount(numFound);
		List<SearchItem> searchItemList = new ArrayList<SearchItem>();
		
		//取商品列表 需要高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem item = new SearchItem();
			item.setCategory_name(solrDocument.get("item_category_name").toString());
			item.setId((String) solrDocument.get(solrDocument.get("id")));
			item.setImage(solrDocument.get("item_image").toString());
			item.setPrice((long) solrDocument.get("item_price"));
			item.setTitle(solrDocument.get("item_title").toString());
			item.setSell_point(solrDocument.get("item_sell_point").toString());
			//添加到商品列表
			searchItemList.add(item);
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(list!=null && list.size()>0){
				title = list.get(0);
			}else{
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
		}
		result.setItemList(searchItemList);
		//返回结果
		return result;
	}
}