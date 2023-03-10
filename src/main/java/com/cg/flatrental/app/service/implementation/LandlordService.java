package com.cg.flatrental.app.service.implementation;

/*
 * Author : HARSH VARDHAN PANCHOLI
 * Version : 1.0
 * Date : 19/May/2021
 * Description : Landlord Service Layer
*/


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.flatrental.app.entity.Landlord;
import com.cg.flatrental.app.exception.LandlordNotFoundException;
import com.cg.flatrental.app.repository.ILandlordRepository;
import com.cg.flatrental.app.service.ILandlordService;




@Service
//Indicates that an annotated class is a "Service"
public class LandlordService implements ILandlordService{
	
	@Autowired
	private ILandlordRepository repository;

	// Most logging operations, are done through this interface. 
	static final  Logger LOGGER = LogManager.getLogger(LandlordService.class);
	
	
	@Override
	public Landlord addLandlord(Landlord landlord) {
		Landlord ld =landlord;
		//validateLandlord(ld);
		LOGGER.info("Business method addLandlord executed");
		return repository.save(landlord);
	} 
	
	@Override
	public Landlord updateLandlord(Landlord landlord, int id) throws LandlordNotFoundException{
		LOGGER.info("Business method updateLandlord initiated");
		Landlord ld =landlord;
		validateLandlord(ld);
		LOGGER.info("Business method updateLandlord executed");
		Landlord value= repository.findById(id)
                .map(x -> {
                    x.setLandlordAge(landlord.getLandlordAge());
                    x.setLandlordName(landlord.getLandlordName());
                    return repository.save(x);
                }).orElseThrow(()->new LandlordNotFoundException("Landlord with id "+id+" does not exist."));
		return value;
	}

	@Override
	public Landlord deleteLandlord(int id) throws LandlordNotFoundException{
		LOGGER.info("Business method deleteLandlord initiated");
		Landlord value=repository.findById(id).orElseThrow(()->new LandlordNotFoundException("Landlord with id "+id+" does not exist."));
		LOGGER.info("Business method deleteLandlord executed");
		repository.delete(value);
		return value;
	}

	
	@Override	
	public Landlord viewLandlord(int id) throws LandlordNotFoundException{
		LOGGER.info("Business method viewLandlord initiated");
		Landlord value=repository.findById(id).orElseThrow(()->new LandlordNotFoundException("Landlord with id "+id+" does not exist."));
		LOGGER.info("Business method viewLandlord executed");
		return value;
	}

	
	@Override
	public List<Landlord> viewAllLandlord() {
		LOGGER.info("Business method viewAllLandlord executed");
		return repository.findAll();
	}
	
	
	static boolean validateLandlordName(String landlordName) throws LandlordNotFoundException {
		LOGGER.info("Landlord name validation started.");
		if (landlordName == "") {
			throw new LandlordNotFoundException("Landlord name cannot be empty.");
		} 
		else if (!landlordName.matches("^[A-Za-z][A-Za-z\\s]*$")) {
			if(landlordName.matches("^[\\s][A-Za-z\\s]*$")) {
				throw new LandlordNotFoundException("Landlord name cannot start with whitespace.");
			}
			throw new LandlordNotFoundException("Landlord name can only have alphabets.");
		} 
		else if (landlordName.length() < 3) {
			throw new LandlordNotFoundException("Landlord name at least have three alphabets");
		}
		else if (landlordName.length() > 40) {
			throw new LandlordNotFoundException("Landlord name can have at most forty alphabets");
		}
		LOGGER.info("Landlord name validate sucessfully.");
		return true;

	}

	
	static boolean validateLandlordAge(int landlordAge) throws LandlordNotFoundException {
		LOGGER.info("Landlord name validation started.");
		if (landlordAge <1) {
			throw new LandlordNotFoundException("Age cannot be 0 or negative.");
		}
		else if (landlordAge >100) {
			throw new LandlordNotFoundException("Age cannot be greater than 100.");
		}
		else if (landlordAge < 18) {
			throw new LandlordNotFoundException("Your age must be greater than or equal to 18 years.");
		} 
		LOGGER.info("Landlord age validate sucessfully.");
		return true;
	}
	
	
	static boolean validateLandlord(Landlord landlord)throws LandlordNotFoundException {
		LOGGER.info("Landlord "+landlord.getLandlordId()+" details validation started.");
		if (landlord.equals(null)) {
			throw new LandlordNotFoundException("Landlord details cannot be empty.");
		}
		else {
			validateLandlordAge(landlord.getLandlordAge());
			validateLandlordName(landlord.getLandlordName());
			/*for(Flat flat : landlord.getFlatList()) {
				FlatService.validateFlat(flat);
			}*/
			LOGGER.info("Validation Sucessfully done.");
		}
		return true;
	}

}