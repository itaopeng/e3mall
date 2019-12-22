package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class GlobleExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobleExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object arg2,
			Exception ex) {
		// 打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("测试输出的日志");
		logger.info("系统异常");
		logger.error("系统发生异常",ex);
		//发邮件 （用jmail工具包）
		//显示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
