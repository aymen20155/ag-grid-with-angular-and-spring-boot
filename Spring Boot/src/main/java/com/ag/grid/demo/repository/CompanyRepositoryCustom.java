package com.ag.grid.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ag.grid.demo.model.CompanyResponse;
import com.ag.grid.demo.model.FilterModel;
import com.ag.grid.demo.model.RequestWithFilterAndSort;
import com.ag.grid.demo.pojo.Company;

@Service
public class CompanyRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	public List<CompanyResponse> getCompanies(int start, int end) {
		int limit = end - start + 1;
		int offset = start;
		String query = String.format("SELECT * FROM COMPANY LIMIT %d OFFSET %d", limit, offset);
		return entityManager.createNativeQuery(query, CompanyResponse.class).getResultList();
	}

	public Page<CompanyResponse> getCompanies(Pageable pageable, RequestWithFilterAndSort requestWithFilterAndSort) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CompanyResponse> companyResponseCriteriaQuery = criteriaBuilder
				.createQuery(CompanyResponse.class);
		Root<Company> companyRoot = companyResponseCriteriaQuery.from(Company.class);

		companyResponseCriteriaQuery.multiselect(companyRoot.get("id"), companyRoot.get("companyName"),
				companyRoot.get("employeeName"), companyRoot.get("description"), companyRoot.get("leave"));

		if (requestWithFilterAndSort.getColId() != null && requestWithFilterAndSort.getSort().equals("desc")) {
			companyResponseCriteriaQuery
					.orderBy(criteriaBuilder.desc(companyRoot.get(requestWithFilterAndSort.getColId())));
		} else if (requestWithFilterAndSort.getColId() != null && requestWithFilterAndSort.getSort().equals("asc")) {
			companyResponseCriteriaQuery
					.orderBy(criteriaBuilder.asc(companyRoot.get(requestWithFilterAndSort.getColId())));
		}

		List<Predicate> predicateList = new ArrayList<>();
		predicateList.addAll(getFilter(criteriaBuilder, predicateList, companyRoot, requestWithFilterAndSort));
		Predicate predicateArray[] = new Predicate[predicateList.size()];

		companyResponseCriteriaQuery.where(criteriaBuilder.and(predicateList.toArray(predicateArray)));

		List<CompanyResponse> companyResponseList = entityManager.createQuery(companyResponseCriteriaQuery)
				.getResultList();
		List<CompanyResponse> companyResponses = entityManager.createQuery(companyResponseCriteriaQuery)
				.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
		return new PageImpl<>(companyResponses, pageable, companyResponseList.size());
	}

	private List<Predicate> getFilter(CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,
			Root<Company> companyRoot, RequestWithFilterAndSort requestWithFilterAndSort) {
		Optional.ofNullable(requestWithFilterAndSort.getFilterModel()).ifPresent(filterModelMap -> {
			for (Map.Entry<String, FilterModel> filterModelEntry : filterModelMap.entrySet()) {

				String colName = filterModelEntry.getKey();
				FilterModel filterModel = filterModelEntry.getValue();
				System.out.println("colName in filter : " + colName);
				/*
				 * if(filterModel.getType().equalsIgnoreCase("NUMBER") &&
				 * filterModel.getFilterType().equalsIgnoreCase("GREATETHEN")){
				 * predicateList.add(criteriaBuilder.ge(companyRoot.get(colName),Long.parseLong(
				 * filterModel.getFilter()))); } else
				 * if(filterModel.getType().equalsIgnoreCase("NUMBER") &&
				 * filterModel.getFilterType().equalsIgnoreCase("LESSTHEN")){
				 * predicateList.add(criteriaBuilder.lessThan(companyRoot.get(colName),Long.
				 * parseLong(filterModel.getFilter())));
				 * 
				 * }
				 */
				if (filterModel.getType().equalsIgnoreCase("equals")) {
					predicateList.add(criteriaBuilder.equal(companyRoot.get(colName), filterModel.getFilter()));
				} else if (filterModel.getType().equalsIgnoreCase("notEqual")) {
					predicateList.add(criteriaBuilder.notEqual(companyRoot.get(colName), filterModel.getFilter()));

				} else if (filterModel.getType().equalsIgnoreCase("contains")) {
					predicateList.add(criteriaBuilder.like(companyRoot.get(colName), filterModel.getFilter()));

				} else if (filterModel.getType().equalsIgnoreCase("notContains")) {
					predicateList.add(criteriaBuilder.notLike(companyRoot.get(colName), filterModel.getFilter()));

				} else if (filterModel.getType().equalsIgnoreCase("startsWith")) {
					predicateList.add(criteriaBuilder.like(companyRoot.get(colName), "%" + filterModel.getFilter()));

				} else if (filterModel.getType().equalsIgnoreCase("endsWith")) {
					predicateList.add(criteriaBuilder.like(companyRoot.get(colName), filterModel.getFilter() + "%"));
				} else if (filterModel.getType().equalsIgnoreCase("blank")) {
					predicateList.add(criteriaBuilder.isNull(companyRoot.get(colName)));
				} else if (filterModel.getType().equalsIgnoreCase("notBlank")) {
					predicateList.add(criteriaBuilder.isNotNull(companyRoot.get(colName)));
				}
			}
		});
		return predicateList;
	}

}
