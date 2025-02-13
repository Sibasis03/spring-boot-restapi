package com.flatmate.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flatmate.dtos.ForgotPasswordDTO;
import com.flatmate.dtos.LoginDTO;
import com.flatmate.dtos.OwnerDTO;
import com.flatmate.models.Customer;
import com.flatmate.models.Owner;
import com.flatmate.repository.OwnerRepository;
import com.flatmate.utils.StorageService;

@Service
public class OwnerService {

	@Autowired
	private OwnerRepository orepo;
	@Autowired
	private StorageService storageService;
	@Autowired
	private AdminService asrv;

	public void saveOwner(OwnerDTO dto) {
		Owner owner = new Owner();
		BeanUtils.copyProperties(dto, owner);
		String uidname = storageService.store(dto.getUidfile());
		String lightbillname = storageService.store(dto.getLightbillfile());
		owner.setLightbill(lightbillname);
		owner.setUidphoto(uidname);
		owner.setPwd(asrv.encrypt(dto.getPwd()));
		orepo.save(owner);
	}

	public boolean validate(ForgotPasswordDTO dto) {
		if (orepo.findByUserid(dto.getUserid()) != null) {
			Owner admin = orepo.findByUserid(dto.getUserid());
			if (admin.getQuestion().equals(dto.getQuestion()) && admin.getAnswer().equals(dto.getAnswer()))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	public void updatePassword(ForgotPasswordDTO dto) {
		Owner admin = orepo.findByUserid(dto.getUserid());
		admin.setPwd(asrv.encrypt(dto.getPwd()));
		orepo.save(admin);
	}

	public List<Owner> listAll() {
		return orepo.findAll();
	}

	public boolean checkExist(String userid) {
		return orepo.findByUserid(userid) != null;
	}

	public Owner findByUserId(String userid) {
		return orepo.findByUserid(userid);
	}

	public Owner findById(int id) {
		return orepo.getById(id);
	}

	public void updateStatus(int id) {
		Owner owner = orepo.getById(id);
		owner.setActive(!owner.isActive());
		orepo.save(owner);
	}

	public Owner validate(LoginDTO dto) {
		Owner cust = findByUserId(dto.getUserid());
		if (cust != null && cust.getPwd().equals(asrv.encrypt(dto.getPwd())) && cust.isActive()) {
			return cust;
		} else {
			return null;
		}
	}
}
