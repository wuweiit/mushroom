package org.marker.mushroom.interceptor;

import org.marker.security.Base64;
import org.marker.security.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;


/**
 *  请求签名验证
 *
 *  签名算法: 参数叠加md5 + base64
 *
 *
 *
 * @author marker
 * 
 * */
public class SignInterceptor implements HandlerInterceptor  {
    private Logger logger = LoggerFactory.getLogger(SignInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
        Map map = request.getParameterMap();
        Set<Map.Entry<String,String[]>> sets = map.entrySet();
        StringBuilder str = new StringBuilder();
        String signClient = "";

        for (Map.Entry<String,String[]> bean : sets){

            if("sign".equals(bean.getKey())){
                signClient = bean.getValue()[0];
                continue;
            }
            str.append(bean.getKey()).append('=').append(bean.getValue()[0]).append("&");
        }
        String time = request.getHeader("time");

        String str2 = URLEncoder.encode(str.substring(0,str.length() -1),"UTF-8")+"|time=" + time ;
//        str2 = str2.replaceAll("%3D","=");
//        str2 = str2.replaceAll("%26","&");

        logger.info("sign param: {}",str2);

        if(StringUtils.isEmpty(signClient)){
            logger.error("request sign invalid...");
            return false;
        }

        String md5Str = MD5.getMD5Code(str2);
        String signServer = Base64.encode(md5Str.getBytes());
        if(!signClient.equals(signServer)){
            logger.error("signServer:{} ", signServer);
            logger.error("signClient:{} ", signClient);
            logger.error("request sign invalid...");
            return false;
        }


		return true;
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
