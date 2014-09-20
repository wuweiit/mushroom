package org.marker.mushroom.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.mushroom.core.AppStatic;



/**
 * 系统验证码
 * 此验证码接口可以可以在需要使用验证码的地方调用。
 * 内置配置文件，可以配置验证码的难易度、生成内容等等为系统安全提供解决方案。
 * url:/SecurityCode
 * @author marker
 * @version 1.0
 */
public class SecurityCodeServlet extends HttpServlet {

	private static final long serialVersionUID = 4898141833479363528L;

 

	/**
	 * 
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");
		// 验证码图片的宽度
		int width = 70;
		// 验证码图片的高度
		int height = 30;
		//创建图片对象
		BufferedImage buffImg = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
		//创建画笔
		Graphics2D g = buffImg.createGraphics();   
		  
        // 创建一个随机数生成器类。   
        Random random = new Random();   
  
        // 设定图像背景色(因为是做背景，所以偏淡)   
        g.setColor(getRandColor(200, 250));   
        g.fillRect(0, 0, width, height);   
        // 创建字体，字体的大小应该根据图片的高度来定。   
        Font font = new Font("Times New Roman", Font.HANGING_BASELINE, 28);   
        // 设置字体。   
        g.setFont(font);   
  
        // 画边框。   
        g.setColor(Color.BLACK);   
        g.drawRect(0, 0, width - 1, height - 1);   
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到。   
        g.setColor(Color.GRAY);   
        g.setColor(getRandColor(160,200));   
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);   
            int y = random.nextInt(height);   
            int xl = random.nextInt(12);   
            int yl = random.nextInt(12);   
            g.drawLine(x, y, x + xl, y + yl);   
        }   
  
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。   
        StringBuffer randomCode = new StringBuffer();   
  
        // 设置默认生成4个验证码   
        int length = 4;   
        // 设置备选验证码:包括"a-z"和数字"0-9"   
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
  
        int size = base.length();   
  
        // 随机产生4位数字的验证码。   
        for (int i = 0; i < length; i++) {   
            // 得到随机产生的验证码数字。   
            int start = random.nextInt(size);   
            String strRand = base.substring(start, start + 1);   
  
            // 用随机产生的颜色将验证码绘制到图像中。   
            // 生成随机颜色(因为是做前景，所以偏深)   
            g.setColor(getRandColor(1, 100));   
               
            //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成   
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));   
            
            g.drawString(strRand, 15 * i + 6, 24);   
  
            // 将产生的四个随机数组合在一起。   
            randomCode.append(strRand);   
        }   
        // 将四位数字的验证码保存到Session中。   
        HttpSession session = request.getSession();   
        session.setAttribute(AppStatic.WEB_APP_AUTH_CODE, randomCode.toString().toLowerCase());   
  
        //图象生效   
        g.dispose();   
  
        // 禁止图像缓存。   
        response.setHeader("Pragma", "no-cache");   
        response.setHeader("Cache-Control", "no-cache");   
        response.setDateHeader("Expires", 0);   
  
        // 将图像输出到Servlet输出流中。   
        ServletOutputStream sos = response.getOutputStream();   
        ImageIO.write(buffImg, "jpeg", sos);   
        sos.flush();
        sos.close();
	}

	/**
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	
	/**
	 * 获取给定范围的随机颜色
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) { 
        Random random = new Random();   
        if (fc > 255)   
            fc = 255;   
        if (bc > 255)   
            bc = 255;   
        int r = fc + random.nextInt(bc - fc);   
        int g = fc + random.nextInt(bc - fc);   
        int b = fc + random.nextInt(bc - fc);   
        return new Color(r, g, b);   
    }   


}
