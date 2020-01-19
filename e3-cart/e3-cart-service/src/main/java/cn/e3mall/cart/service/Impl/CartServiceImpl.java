package cn.e3mall.cart.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public E3Result addCart(long itemId, long userId, int num) {
		//查询 有没有购物车
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId,itemId+"");
		if(hexists){
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId,itemId+"");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//回写redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"", JsonUtils.objectToJson(item));
		}else {
			TbItem item = itemMapper.selectByPrimaryKey(itemId);
			item.setNum(num);
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)){
				item.setImage(image.split(",")[0]);
			}
			//添加购物车列表
			jedisClient.hset(REDIS_CART_PRE+":"+userId,itemId+"", JsonUtils.objectToJson(item));
		}
		return E3Result.ok();
	}

	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		// 遍历商品列表
		// 把商品列表加入购物车
		// 判断购物车是否有这个商品
		// 如果有，数量相加
		//如果没有，添加新商品
		for (TbItem tbItem : itemList) {
			addCart(tbItem.getId(), userId, tbItem.getNum());
		}
		//返回成功
		return E3Result.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		// 根据用户id查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			//创建一个tbitem对象
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			//添加到列表
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {
		//redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}

	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		// 删除购物车商品
		jedisClient.hdel(REDIS_CART_PRE+":"+userId, itemId+"");
		return E3Result.ok();
	}

	@Override
	public E3Result clearCartItem(long userId) {
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return null;
	}

}
