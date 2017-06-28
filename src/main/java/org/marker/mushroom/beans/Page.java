package org.marker.mushroom.beans;
 
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 说明：通用的分页对象<br />
 * 使用该对象封装页面显示的记录信息，包含数据：<br />
 * 1. 记录信息列表  data  <br />
 * 2. 当前页码  currentPageNo，默认值为1 <br />
 * 3. 总行数 totalRows <br /> 
 * 4. 每页显示记录数 pageSize，默认值为10 <br />
 * @author marker
 */
public class Page implements Serializable, Cloneable {
	private static final long serialVersionUID = -4851306316030938455L;

	/** 查询数据 */
	private List<Map<String, Object>> data;
	
	/** 当前页号，默认值为1 */
	private int currentPageNo = 1;
	
	/** 总行数 */
	private long totalRows;
	/** 总页数 */
	private int totalPages = 1;
	
	/** 每页显示条数，默认值为10 */
	private int pageSize = 15;
	
	/** 最后一页的页码 */
	private int lastPageNo;

	private Date serverTime = new Date();
	
	
	
	public Page() { }
	
	
	
	
	public void setLastPageNo(int lastPageNo) {
		this.lastPageNo = lastPageNo;
	}
	
	
	/**
	 * 构造方法，初始化分页对象
	 * @param currentPageNo
	 * @param pageSize
	 */
	public Page(int currentPageNo,int totalRows, int pageSize) {
		if (currentPageNo <=0 ) {currentPageNo = 1;};
		this.currentPageNo = currentPageNo;
		this.pageSize = pageSize;
		this.totalRows = totalRows;
		this.totalPages = (int) (totalRows%pageSize == 0 ? totalRows/pageSize : totalRows/pageSize + 1);
	}
	
	
	/**
	 * 构造方法，初始化分页对象
	 * @param currentPageNo
	 * @param totalRows
	 * @param pageSize
	 * @param data
	 */
	public Page(int currentPageNo, int totalRows, int pageSize, List<Map<String, Object>> data) {
		if (currentPageNo <=0 ) {currentPageNo = 1;};
		this.data = data;
		this.currentPageNo = currentPageNo;
		this.totalRows = totalRows;
		this.pageSize = pageSize;
		this.totalPages = (int) (totalRows%pageSize == 0 ? totalRows/pageSize : totalRows/pageSize + 1);
	}

	/**
	 *  得到总页数
	 * @return
	 */
	public int getTotalPages() {
		if (totalRows == 0) {
            return 1;
        }
        return this.totalPages;
	}

	/**
	 *  得到第一页页号
	 * @return
	 */
	public int getFirstPageNo() {
		return 1;
	}

	/**
	 *  得到最后一页页号
	 * @return
	 */
	public int getLastPageNo() {
		this.lastPageNo = getTotalPages();
		return this.lastPageNo;
	}

	/**
	 *  得到上一页页号
	 * @return
	 */
	public int getPrevPageNo() {
		if (currentPageNo == 1) {
			return 1;
		}
		return currentPageNo - 1;
	}

	/**
	 *  得到下一页页号
	 * @return
	 */
	public int getNextPageNo() {
		if (currentPageNo == getTotalPages()) {
			return currentPageNo;
		}
		return currentPageNo + 1;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 获取记录索引号
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
        if (0 > pageNo)
            throw new IllegalArgumentException("页面索引不能小于0!");
        return (pageNo - 1) * pageSize;
    }
	
	/**
	 * 计算该页起始下标
	 * @return
	 */
	public int computerFirstRowIndex() {
		return (currentPageNo - 1) * pageSize;
	}

	/**
	 * 克隆新的分页对象
	 * @return
	 */
	public Page clonePage() {
		Page p = null;
		try {
			p = (Page)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	/**
	 * 是否有下一页
	 * @return
	 */
	public boolean getHasNext() {
		return currentPageNo < this.getTotalPages();
	}
	
	/**
	 * 是否有上一页
	 * @return
	 */
	public boolean getHasPre() {
		return currentPageNo >1;
	}

    public Date getServerTime() {
        return serverTime;
    }

}
