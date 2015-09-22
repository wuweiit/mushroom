package org.marker.mushroom.servlet;

import javax.servlet.http.HttpServlet;

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
//	private static final long serialVersionUID = 1L;
//
//	@SuppressWarnings("unused")
//	private ISupportDao commonDao;
//	
//	
//	public WeixinServlet() {
//		commonDao = SpringContextHolder.getBean(DAO.COMMON);
//	}
//	
//	
//	//TOKEN 是你在微信平台开发模式中设置的哦
//	public static final String TOKEN = "cms";
//
//	/**
//	 * 处理微信服务器验证
//	 * 
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	@Override
//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String signature = request.getParameter("signature");// 微信加密签名
//		String timestamp = request.getParameter("timestamp");// 时间戳
//		String nonce = request.getParameter("nonce");// 随机数
//		String echostr = request.getParameter("echostr");// 随机字符串
//
//		// 重写totring方法，得到三个参数的拼接字符串
//		List<String> list = new ArrayList<String>(3) {
//			private static final long serialVersionUID = 2621444383666420433L;
//			@Override
//			public String toString() {
//				return this.get(0) + this.get(1) + this.get(2);
//			}
//		};
//		list.add(TOKEN);
//		list.add(timestamp);
//		list.add(nonce);
//		Collections.sort(list);// 排序
//		String tmpStr = new MySecurity().encode(list.toString(),
//				MySecurity.SHA_1);// SHA-1加密
//		Writer out = response.getWriter();
//		if (signature.equals(tmpStr)) {
//			out.write(echostr);// 请求验证成功，返回随机码
//		} else {
//			out.write("");
//		}
//		out.flush();
//		out.close();
//	}
//
//	
//	/**
//	 * 处理微信服务器发过来的各种消息，包括：文本、图片、地理位置、音乐等等
//	 * 
//	 * 
//	 */
//	@Override
//	protected void doPost(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		request.setCharacterEncoding("utf-8");
//		response.setContentType("text/html; charset=utf-8");
//		InputStream is  = request.getInputStream();
//		OutputStream os = response.getOutputStream();
//		
//		
//		final DefaultSession session = DefaultSession.newInstance(); 
//		
//		session.addOnHandleMessageListener(new HandleMessageAdapter(){
//			
//			@Override
//			public void onTextMsg(Msg4Text msg) {
//				if("1".equals(msg.getContent())){// 菜单选项1
//					Msg4Text reMsg = new Msg4Text();
//					reMsg.setFromUserName(msg.getToUserName());
//					reMsg.setToUserName(msg.getFromUserName());
//					reMsg.setCreateTime(msg.getCreateTime());
//					reMsg.setContent("【菜单】\n" +
//							"1. 功能菜单\n" +
//							"2. 图文消息测试\n" +
//							"3. 图片消息测试\n"); 
//					session.callback(reMsg);//回传消息
//				}else if("2".equals(msg.getContent())){
//					//回复一条消息
//					Data4Item d1 = new Data4Item("蘑菇建站系统", "CMS不解释", "http://cms.yl-blog.com/themes/blue/images/logo.png", "cms.yl-blog.com"); 
//					Data4Item d2 = new Data4Item("雨林博客", "发布各种技术文章", "http://www.yl-blog.com/template/ylblog/images/logo.png", "www.yl-blog.com"); 
//					      
//					Msg4ImageText mit = new Msg4ImageText();
//					mit.setFromUserName(msg.getToUserName());
//					mit.setToUserName(msg.getFromUserName()); 
//					mit.setCreateTime(msg.getCreateTime());
//					mit.addItem(d1);
//					mit.addItem(d2); 
//					session.callback(mit);
//				}else if("3".equals(msg.getContent())){
//					Msg4Image rmsg = new Msg4Image();
//					rmsg.setFromUserName(msg.getToUserName());
//					rmsg.setToUserName(msg.getFromUserName());
//					rmsg.setPicUrl("http://www.yl-blog.com/template/ylblog/images/logo.png");
//					session.callback(rmsg);
//				}else {
//					Msg4Text reMsg = new Msg4Text();
//					reMsg.setFromUserName(msg.getToUserName());
//					reMsg.setToUserName(msg.getFromUserName());
//					reMsg.setCreateTime(msg.getCreateTime());
//					
//					reMsg.setContent("消息命令错误，谢谢您的支持！@wuweiit");
//				 
//					
//					session.callback(reMsg);//回传消息
//				}
//			}
//		});
//		
//		// 语音识别消息
//		session.addOnHandleMessageListener(new HandleMessageAdapter(){ 
//			
//			@Override
//			public void onVoiceMsg(Msg4Voice msg) {
//				Msg4Text reMsg = new Msg4Text();
//				reMsg.setFromUserName(msg.getToUserName());
//				reMsg.setToUserName(msg.getFromUserName());
//				reMsg.setCreateTime(msg.getCreateTime()); 
//				reMsg.setContent("识别结果: "+msg.getRecognition()); 
//				session.callback(reMsg);// 回传消息 
//			}
//		});
//		
//		// 处理事件
//		session.addOnHandleMessageListener(new HandleMessageAdapter() {
//			@Override
//			public void onEventMsg(Msg4Event msg) {
//				String eventType = msg.getEvent();
//				if(Msg4Event.SUBSCRIBE.equals(eventType)){// 订阅
//					System.out.println("关注人："+ msg.getFromUserName());
//					System.out.println("参数值：" + msg.getEventKey());
//					
//					Msg4Text reMsg = new Msg4Text();
//					reMsg.setFromUserName(msg.getToUserName());
//					reMsg.setToUserName(msg.getFromUserName());
//					reMsg.setCreateTime(msg.getCreateTime());
//					 
//					reMsg.setContent("【菜单】\n" +
//							"1. 功能菜单\n" +
//							"2. 图文消息测试\n" +
//							"3. 图片消息测试\n"); 
//					
//					session.callback(reMsg);//回传消息
//					
//					
//				}else if(Msg4Event.UNSUBSCRIBE.equals(eventType)){// 取消订阅
//					System.out.println("取消关注："+msg.getFromUserName());
//					
//				}else if(Msg4Event.CLICK.equals(eventType)){// 点击事件
//					System.out.println("用户："+ msg.getFromUserName());
//					System.out.println("点击Key："+msg.getEventKey());
//				}
//			} 
//		});
//		
//		// 处理地理位置
//		session.addOnHandleMessageListener(new HandleMessageAdapter(){
//			@Override
//			public void onLocationMsg(Msg4Location msg) {
//				System.out.println("收到地理位置消息：");
//				System.out.println("X:"+msg.getLocation_X());
//				System.out.println("Y:"+msg.getLocation_Y());
//				System.out.println("Scale:"+msg.getScale());
//			}
//			
//		});
//		
//		session.process(is, os);//处理微信消息 
//		session.close();//关闭Session
//		 
//	}

}
