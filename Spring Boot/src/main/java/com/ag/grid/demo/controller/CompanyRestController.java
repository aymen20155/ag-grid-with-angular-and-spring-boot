package com.ag.grid.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ag.grid.demo.model.CompanyResponse;
import com.ag.grid.demo.model.RequestWithFilterAndSort;
import com.ag.grid.demo.pojo.Company;
import com.ag.grid.demo.repository.CompanyRepositoryCustom;

@CrossOrigin("*")
@RestController
public class CompanyRestController {

	@Autowired
	CompanyRepositoryCustom companyRepositoryCustom;

	@PostMapping("/get-companies")
	public Page<CompanyResponse> register(@RequestParam("page") int page, @RequestParam("size") int size,
			@RequestBody RequestWithFilterAndSort requestWithFilterAndSort) {
		Pageable pageable = PageRequest.of(page, size);
		return companyRepositoryCustom.getCompanies(pageable, requestWithFilterAndSort);
	}

//	@RequestMapping(value = "/get-companies-v1", method = RequestMethod.GET)
	@GetMapping("/get-companies-v1")
	public List<Company> register(@RequestParam("start") int startRow, @RequestParam("end") int endRow) {
		return companyRepositoryCustom.getCompanies(startRow, endRow);
	}
}
