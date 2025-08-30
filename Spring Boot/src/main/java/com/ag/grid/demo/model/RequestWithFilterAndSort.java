package com.ag.grid.demo.model;

import java.io.Serializable;
import java.util.Map;

public class RequestWithFilterAndSort implements Serializable {

	private Map<String, FilterModel> filterModel;
	private String colId;
	private String sort;

	public RequestWithFilterAndSort() {
	}

	public RequestWithFilterAndSort(Map<String, FilterModel> filterModel, String colId, String sort) {
		this.filterModel = filterModel;
		this.colId = colId;
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "RequestWithFilterAndSort{" + "filterModel=" + filterModel + ", colId='" + colId + '\'' + ", sort='"
				+ sort + '\'' + '}';
	}

	public Map<String, FilterModel> getFilterModel() {
		return filterModel;
	}

	public void setFilterModel(Map<String, FilterModel> filterModel) {
		this.filterModel = filterModel;
	}

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
