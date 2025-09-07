package org.marker.mushroom.servlet;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.utils.SpringUtils;
import org.marker.weixin.DefaultSession;
import org.marker.weixin.HandleMessageAdapter;
import org.marker.weixin.MySecurity;
import org.marker.weixin.msg.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处理微信服务器请求的Servlet URL地址：http://xxx/weixin/dealwith.do
 * 
 * 注意：官方文档限制使用80端口哦！
 * 
 * @author marker
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class WeixinServlet extends HttpServlet {

	@SuppressWarnings("unused")
	private ISupportDao commonDao;


	public WeixinServlet() {
		commonDao = SpringContextHolder.getBean(DAO.COMMON);
	}


	//TODO 临时写死 TOKEN 是你在微信平台开发模式中设置的哦
	public static final String TOKEN = "cms";

	/**
	 * 处理微信服务器验证
	 *
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String echostr = request.getParameter("echostr");// 随机字符串
		Writer out = response.getWriter();
		if (checkSignature(request)) {
			out.write(echostr);// 请求验证成功，返回随机码
		} else {
			out.write("");
		}
		out.flush();
		out.close();
	}


	/**
	 * 签名验证
	 * @param request
	 * @return
	 */
	private boolean checkSignature(HttpServletRequest request){
		String signature = request.getParameter("signature");// 微信加密签名
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串

		// 重写totring方法，得到三个参数的拼接字符串
		List<String> list = new ArrayList<String>(3) {
			@Override
			public String toString() {
				return this.get(0) + this.get(1) + this.get(2);
			}
		};
		list.add(TOKEN);
		list.add(timestamp);
		list.add(nonce);
		Collections.sort(list);// 排序
		String tmpStr = new MySecurity().encode(list.toString(),
				MySecurity.SHA_1);// SHA-1加密
		if (signature.equals(tmpStr)) {
			return true;
		}
		return false;
	}


	/**
	 * 处理微信服务器发过来的各种消息，包括：文本、图片、地理位置、音乐等等
	 *
	 *
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		if (!checkSignature(request)) { // 签名验证
			response.sendError(403, "Forbidden");
			return;
		}
		InputStream is  = request.getInputStream();
		OutputStream os = response.getOutputStream();

		DefaultSession session = SpringUtils.getBean(DefaultSession.class);

		// 语音识别消息
		session.addOnHandleMessageListener(new HandleMessageAdapter(){

			@Override
			public void onVoiceMsg(Msg4Voice msg) {
				Msg4Text reMsg = new Msg4Text();
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				reMsg.setContent("识别结果: "+msg.getRecognition());
				session.callback(reMsg);// 回传消息
			}
		});

		// 处理地理位置
		session.addOnHandleMessageListener(new HandleMessageAdapter(){
			@Override
			public void onLocationMsg(Msg4Location msg) {
				System.out.println("收到地理位置消息：");
				System.out.println("X:"+msg.getLocation_X());
				System.out.println("Y:"+msg.getLocation_Y());
				System.out.println("Scale:"+msg.getScale());
			}
		});

		session.process(is, os);//处理微信消息
		session.close();//关闭Session
	}

}
