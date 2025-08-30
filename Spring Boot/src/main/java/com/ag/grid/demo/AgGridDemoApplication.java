package com.ag.grid.demo;

import com.ag.grid.demo.pojo.Company;
import com.ag.grid.demo.repository.CompanyRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.opencsv.CSVReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class AgGridDemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(AgGridDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init (CompanyRepository companyRepository){
		return args -> {

			try (CSVReader reader = new CSVReader(new FileReader(ResourceUtils.getFile("classpath:data/data.csv")))) {
				reader.skip(1);
				List<String[]> r = reader.readAll();
				List<Company> companyList=r.stream().map(row->{
					return companyRepository.save(new Company(row[1],row[2],row[3],Integer.parseInt(row[4])));
				}).collect(Collectors.toList());
				System.out.println(companyList);
			}

		};
	}


}
