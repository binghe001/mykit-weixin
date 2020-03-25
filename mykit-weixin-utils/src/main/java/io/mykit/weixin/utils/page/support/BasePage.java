package io.mykit.weixin.utils.page.support;

import io.mykit.weixin.utils.page.BasePageBean;

import java.util.List;


/**
 * 封装的分页数据
 * @author liuyazhuang
 *
 * @param <T>
 */
public class BasePage<T> extends BasePageBean {
	private static final long serialVersionUID = -3040595767538501230L;
	private List<T> list;
	
	public BasePage() {
		super();
	}

	public BasePage(int allRow, int totalPage, int currentPage, int pageSize){
		super(allRow, totalPage, currentPage, pageSize);
		this.init();
	}

	public BasePage(int allRow, int totalPage, int currentPage, int pageSize, List<T> list) {
		super(allRow, totalPage, currentPage, pageSize);
		this.list = list;
		this.init();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
