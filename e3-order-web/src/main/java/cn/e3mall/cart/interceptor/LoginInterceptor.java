package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	/*
	 * 前处理  执行handler处理之前 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// cookie中取token、
		String token = CookieUtils.getCookieValue(request, "token");
		if (StringUtils.isBlank(token)){
			// 取不到则跳转到登陆成功页面。登陆成功后跳转到当前请求url
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			return false;
		}else{
			//token存在  调用sso服务 根据token取用户信息
			E3Result e3Result = tokenService.getUserByToken(token);
			if (e3Result.getStatus()!=200){
				// 取不到则跳转到登陆成功页面。登陆成功后跳转到当前请求url
				response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
				return false;
			}
			//取得到  把用户信息写入request
			TbUser user = (TbUser) e3Result.getData();
			request.setAttribute("user", user);
			//判断cookie中是否有数据 有则合并到服务
			String json = CookieUtils.getCookieValue(request, "cart",true);
			if(StringUtils.isNotBlank(json)){
				cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
			}
		}
		return true;
	}
	
	/*
	 * handler执行之后，返回 ModelAndView之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model)
			throws Exception {
		// TODO Auto-generated method stub

	}
	
	/*
	 * 返回 ModelAndView之后  可以在此处理异常
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 

	}
}
