package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper ibItemDescMapper;
	@Autowired
	private JmsTemplate JmsTemplate;
	@Resource
	private Destination topicDestination;
	
	@Override
	public TbItem getItemById(long itemId) {
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	public E3Result addItem(TbItem item, String desc) {
		// 生成商品id
		final long itemId = IDUtils.genItemId();
		// 补全item属性
		item.setId(itemId);
		// 1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(item);
		// 创建商品描述表pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		ibItemDescMapper.insertSelective(itemDesc);
		//发送消息
		JmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId+"");
				return message;
			}
		});
		// 返回成功
		return E3Result.ok();
	}
	@Override
	public E3Result updateItem(TbItem item, TbItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKey(item);
		// 向商品描述表插入数据
		itemDesc.setUpdated(new Date());
		ibItemDescMapper.updateByPrimaryKey(itemDesc);
		// 返回成功
		return E3Result.ok();
	}

	@Override
	public E3Result upItem(TbItem item) {
		// 1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		itemMapper.updateByPrimaryKey(item);
		// 返回成功
		return E3Result.ok();
	}

	@Override
	public E3Result downItem(TbItem item) {
		// 1-正常，2-下架，3-删除
		item.setStatus((byte) 2);
		itemMapper.updateByPrimaryKey(item);
		// 返回成功
		return E3Result.ok();
	}

	@Override
	public E3Result deleteItem(TbItem item) {
		// 1-正常，2-下架，3-删除
		item.setStatus((byte) 3);
		itemMapper.updateByPrimaryKey(item);
		// 返回成功
		return E3Result.ok();
	}

	
	
}
