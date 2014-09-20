/**
 *  
 *  吴伟 版权所有
 */
package org.marker.qqwryip;

/**
 * @author marker
 * @date 2013-11-16 下午4:10:40
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */ 

public final class IPLocation {
	public static final IPLocation Unknown = new IPLocation("未知国家", "未知地区");

	static IPLocation of(String country, String area) {
		if (country == null || country.isEmpty()) {
			country = Unknown.country;
		}
		if (area == null || area.isEmpty()) {
			area = Unknown.area;
		}
		return new IPLocation(country, area);
	}
	
	private final String area;

	private final String country;

	private IPLocation(String country, String area) {
		this.country = country;
		// 如果为局域网,纯真IP地址库的地区会显示CZ88.NET,这里把它去掉
		this.area = area.equals("CZ88.NET") ? "局域网" : area;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPLocation other = (IPLocation) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		return true;
	}

	public String getArea() {
		return area;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + area.hashCode();
		result = prime * result + country.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(area).append(' ').append(country).toString();
	}
}
