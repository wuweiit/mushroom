package org.marker.qqwryip;

/**
 * 修改于互联网的代码
 * 采用NIO方式
 * @author marker
 * @date 2013-11-16 下午4:09:33
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */  
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

public class IPSeeker {
	
	
	protected ByteBuffer buffer;
	protected Helper h;
	protected int offsetBegin, offsetEnd;

	
	
	
	public IPSeeker(String path) throws IOException { 
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(path);
		
		FileChannel fileChannel = fis.getChannel();
		
		buffer = ByteBuffer.allocate((int)fileChannel.size());
	
		fileChannel.read(buffer);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		offsetBegin = buffer.getInt(0);
		offsetEnd   = buffer.getInt(4);
		if (offsetBegin == -1 || offsetEnd == -1) {
			throw new IllegalArgumentException("File Format Error");
		}
		h = new Helper(this); 
	}
	
	public IPLocation getLocation(byte ip1, byte ip2, byte ip3, byte ip4) {
		return getLocation(new byte[] { ip1, ip2, ip3, ip4 });
	}
	
	protected final IPLocation getLocation(byte[] ip) {
		return h.getLocation(h.locateOffset(ip));
	}
	
	public IPLocation getLocation(Inet4Address address) {
		return getLocation(address.getAddress());
	}
}