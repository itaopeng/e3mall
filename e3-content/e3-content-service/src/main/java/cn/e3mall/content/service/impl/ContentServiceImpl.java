package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容管理service
 * @author tao
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public E3Result addContent(TbContent content) {
		// 将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//缓存同步，删除缓存数据 
		content.getCategoryId();
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}
	
	/***
	 * 根据内容分类id查询内容列表
	 */
	@Override
	public List<TbContent> getContentByCid(long cid) {
		//查询缓存
		try{
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			if(StringUtils.isNotBlank(json)) {
				List<TbContent> contentList = JsonUtils.jsonToList(json, TbContent.class);
				return contentList;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//有就响应  没有查数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//结果添加到缓存
		try{
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public E3Result updateContent(TbContent content) {
		// 将内容数据插入到内容表
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKey(content);
		//缓存同步，删除缓存数据 
		content.getCategoryId();
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}
}
