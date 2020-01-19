package cn.e3mall.cart.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车处理
 * <p>Title: CartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */

@Controller
public class CartController {

	@Autowired 
	private ItemService itemService; 
	@Autowired
	private CartService cartService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(HttpServletRequest request,HttpServletResponse response,
			@PathVariable Long itemId,@RequestParam(defaultValue="1")Integer num){
		// 判断是否登陆
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null){
			// 判断是否 存在购物车 存在则合并
			cartService.addCart(itemId, user.getId(), num);
			return "cartSuccess";
		}
		
		//从cookie中取购物车列表
		List<TbItem> list = getCookieItemList(request);
		//判断是否存在商品
		boolean flag = false;
		for (TbItem tbItem : list) {
			if(tbItem.getId().longValue()==itemId.longValue()){
				//存在则数量相加
				flag = true;
				tbItem.setNum(tbItem.getNum()+num);
				break;
			}
		}
		//不存在则增加商品
		if(!flag){
			TbItem tbItem = itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)){
				tbItem.setImage(image.split(",")[0]);
			}
			list.add(tbItem);
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE, true);
		//返回
		return "cartSuccess";
	}
	
	/**
	 * 展示购物车列表
	 * <p>Title: showCatList</p>
	 * <p>Description: </p>
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCatList(HttpServletRequest request,HttpServletResponse response){
		List<TbItem> cartList = getCookieItemList(request);
		
		//查看登陆状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			//合并cookie到session服务器中
			cartService.mergeCart(user.getId(), cartList);
			//删除cookie中数据
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 更新购物车数量
	 * <p>Title: getCookieItemList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//查看登陆状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
		}
		
		//取购物车列表
		List<TbItem> cartList = getCookieItemList(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue()==itemId.longValue()){
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return E3Result.ok();
	}
	
	/**
	 * 删除购物车商品
	 * <p>Title: getCookieItemList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response){
		//查看登陆状态
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null){
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		//取购物车列表
		List<TbItem> cartList = getCookieItemList(request);
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue()==itemId.longValue()){
				//更新数量
				cartList.remove(tbItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return "redirect:/cart/cart.html";
	}
	
	private List<TbItem> getCookieItemList(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isBlank(json)){
			return new ArrayList<TbItem>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
}
