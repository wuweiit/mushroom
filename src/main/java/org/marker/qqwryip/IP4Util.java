/**
 *  
 *  吴伟 版权所有
 */
package org.marker.qqwryip;

/**
 * @author marker
 * @date 2013-11-16 下午4:11:44
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public final class IP4Util {
	public static byte[] toBytes(int address) {
		return new byte[] {
			(byte) ((address >>> 24) & 0xFF),
			(byte) ((address >>> 16) & 0xFF),
			(byte) ((address >>>  8) & 0xFF),
			(byte) ((address       ) & 0xFF)
		};
	}

	private IP4Util() {
	}
}