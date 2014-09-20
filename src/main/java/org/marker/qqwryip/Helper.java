/**
 *  
 *  吴伟 版权所有
 */
package org.marker.qqwryip;

/**
 * @author marker
 * @date 2013-11-16 下午4:11:06
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */ 

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

class Helper {
	static final int RecordLength = 7;
	static final byte RedirectMode1 = 0x01;
	static final byte RedirectMode2 = 0x02;

	/** 计算中间位置的偏移 */
	static int calcMiddleOffset(final int begin, final int end) {
		final int records = ((end - begin) / RecordLength) >> 1;
		return begin + ((records == 0) ? 1 : records) * RecordLength;
	}
	
	static int compare(byte[] ip, byte[] begin) {
		for (int i = 0, x, y; i < 4; i++) {
			x = ip[i]; y = begin[i];
			if ((x & 0xFF) > (y & 0xFF)) {
				return 1;
			} else if ((x ^ y) == 0) {
				continue;
			} else {
				return -1;
			}
		}
		return 0;
	}

	private final ByteBuffer buffer;
	private final IPSeeker seeker;

	Helper(IPSeeker seeker) {
		this.buffer = seeker.buffer;
		this.seeker = seeker;
	}

	IPLocation getLocation(int offset) {
		if (offset == -1) {
			return IPLocation.Unknown;
		}
		buffer.position(offset + 4);
		switch (buffer.get()) {
		case RedirectMode1:
			// Read CountryOffset & Set Position
			buffer.position(offset = readInt3());
			final String country;
			switch (buffer.get()) {
			case RedirectMode2:
				country = readString(readInt3());
				buffer.position(offset + 4);
				break;
			default:
				country = readString(offset);
				break;
			}
			return IPLocation.of(country, readArea(buffer.position()));
		case RedirectMode2:
			return IPLocation.of(readString(readInt3()), readArea(offset + 8));
		default:
			return IPLocation.of(readString(buffer.position() - 1), readArea(buffer.position()));
		}
	}

	/** 定位IP的绝对偏移 */
	int locateOffset(final byte[] address) {
		switch (compare(address, readIP(seeker.offsetBegin))) {
		case -1:
			return -1;
		case 0:
			return seeker.offsetBegin;
		}
		int middleOffset = 0;
		for (int begin = seeker.offsetBegin, end = seeker.offsetEnd; begin < end;) {
			switch (compare(address, readIP(middleOffset = calcMiddleOffset(begin, end)))) {
			case 1:
				begin = middleOffset;
				break;
			case -1:
				if (middleOffset == end) {
					middleOffset = (end -= RecordLength);
				} else {
					end = middleOffset;
				}
				break;
			case 0:
				return readInt3(middleOffset + 4);
			}
		}
		middleOffset = readInt3(middleOffset + 4);
		switch (compare(address, readIP(middleOffset))) {
		case -1:
		case 0:
			return middleOffset;
		default:
			return -1;
		}
	}
	
	private String readArea(int offset) {
	    buffer.position(offset);
		switch (buffer.get()) {
		case RedirectMode1:
		case RedirectMode2:
			offset = readInt3(offset + 1);
			return (offset != 0) ? readString(offset) : IPLocation.Unknown.getArea();
		default:
			return readString(offset);
		}
	}

	private int readInt3() {
		return buffer.getInt() & 0x00FFFFFF;
	}

	int readInt3(int offset) {
		buffer.position(offset);
		return buffer.getInt() & 0x00FFFFFF;
	}

	byte[] readIP(int offset) {
		buffer.position(offset);
		return IP4Util.toBytes(buffer.getInt());
	}
	
	private String readString(int offset) {
		buffer.position(offset);
		final byte[] buf = new byte[0xFF];
        offset = -1;
        while ((buf[++offset] = buffer.get()) != 0);
		return (offset != 0) ? new String(buf, 0, offset, Charset.forName("GBK")) : null;
	}
}
