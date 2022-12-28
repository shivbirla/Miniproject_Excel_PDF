package com.nt.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.nt.entity.EligibilityDetails;
import com.nt.repo.EligibilityDetailsRepo;

@Component
public class AapRunner implements ApplicationRunner {

	@Autowired
	private EligibilityDetailsRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		EligibilityDetails entity1 = new EligibilityDetails();
		entity1.setEligId(01);
		entity1.setName("Shiv");
		entity1.setMobile(70495149l);
		entity1.setGender('M');
		entity1.setSsn(23456l);
		entity1.setPlanName("SNAP");
		entity1.setPlanStatus("Approved");

		repo.save(entity1);

		EligibilityDetails entity2 = new EligibilityDetails();
		entity2.setEligId(02);
		entity2.setName("Nirmal");
		entity2.setMobile(7745422l);
		entity2.setGender('M');
		entity2.setSsn(567654l);
		entity2.setPlanName("CCAP");
		entity2.setPlanStatus("Denied");

		repo.save(entity2);

		EligibilityDetails entity3 = new EligibilityDetails();
		entity3.setEligId(03);
		entity3.setName("Akshay");
		entity3.setMobile(9672553l);
		entity3.setGender('M');
		entity3.setSsn(123456l);
		entity3.setPlanName("Medicaid");
		entity3.setPlanStatus("Closed");

		repo.save(entity3);
	}

}
