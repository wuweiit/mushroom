package org.marker.mushroom.interceptor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 请求信息打印拦截器
 *
 * 目前答应了请求参数
 *
 *
 * @author marker
 *
 * */
public class RequestParamsInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(RequestParamsInterceptor.class);

    @Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("============== Request Info ================");
		String METHOD = request.getMethod();
		String uri = request.getRequestURI();
		logger.info("{} {}",METHOD, uri);
		logger.info("{}", JSON.toJSON(request.getParameterMap()));
		logger.info("============== Request Info END================");
		return true;
	}


	private void warnInfo(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {


    }

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
