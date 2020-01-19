package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	/*
	 * 前处理  执行handler处理之前 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// cookie中取token、
		String token = CookieUtils.getCookieValue(request, "token");
		// 取不到则直接返回 
		if (StringUtils.isBlank(token)){
			return true;
		}
		// 取到了则 到session服务器中查询用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		// 用户信息 放到request中
		if(e3Result.getStatus()==200){
			TbUser user = (TbUser) e3Result.getData();
			request.setAttribute("user", user);
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
