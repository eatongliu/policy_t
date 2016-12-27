package com.gpdata.wanyou.utils.page;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * <p>Description: </p>
 * 数据分页对象
 * @author duanhuazhen
 * @param <T>
 * @date 2016年4月29日下午4:12:46
 */
public class PageDataList<T> implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
	 * 当前页面
	 */
	private Integer currentPage;
	
	/*
	 * 每页显示多少条
	 */
	private Integer pageSize;
	
	/*
	 * 总条数
	 */
	private Integer totalRows;
	
	/*
	 * 数据的封装
	 */
	private List<T> pList;
	
	/*
	 * 总页数
	 */
	private Integer totalPage;
	
	/*
	 * 开始页码
	 */
	private Integer indexPage;
	
	/*
	 * 结束页码
	 */
	private Integer endPage;
	
	
	public PageDataList(int totalRows, int pageSize, int currentPage, List<T> pList) {
		this.totalRows = totalRows;
		this.pList = pList;
		this.currentPage = currentPage;
		this.pageSize =pageSize;
		
		totalPage = (totalRows%pageSize)==0?(totalRows/pageSize):(totalRows/pageSize + 1);
		//总页码少
        if (totalPage <= 10) {
            indexPage = 1;
            endPage = totalPage > 10 ? 10 : totalPage;
        // 总页码多
        } else {
            if (currentPage > 5) {
                int x = totalPage - currentPage + 1;
                if (x < 10) {
                    indexPage = totalPage - 9;
                } else {
                    indexPage = currentPage - 4;
                }
                endPage = (currentPage + 5) < totalPage ? currentPage + 5 : totalPage;
            } else {
                indexPage = 1;
                endPage = 10;
            }
        }
	}
	
	/**
	 * 当前页
	 * @return
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * 每页记录
	 * @return
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * 总记录
	 * @return
	 */
	public Integer getTotalRows() {
		return totalRows;
	}

	/**
	 * 总页数
	 * @return
	 */
	public Integer getTotalPage() {
		return totalPage;
	}

	/**
	 * 所以起始页
	 * @return
	 */
	public Integer getIndexPage() {
		return indexPage;
	}

	/**
	 * 索引结束页
	 * @return
	 */
	public Integer getEndPage() {
		return endPage;
	}
	

	/**
	 * 数据列表
	 * @return
	 */
    public List<T> getpList() {
        return pList;
    }

    @Override
    public String toString() {
        return "PageDataList [currentPage=" + currentPage + ", pageSize=" + pageSize + ", totalRows=" + totalRows
                + ", totalPage=" + totalPage + ", indexPage=" + indexPage + ", endPage=" + endPage
                + "]";
    }
    
}
