/**
 * 
 */
package com.mahelinc.servicegenie.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mahelinc.servicegenie.entity.Garage;
import com.mahelinc.servicegenie.model.GarageDetails;
import com.mahelinc.servicegenie.service.GarageJobsService;
import com.mahelinc.servicegenie.service.GarageService;

import io.swagger.annotations.Api;

/**
 * The Class ServiceGenieApiServiceController.
 *
 * @author surendrane
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "/api/v1")
public class ServiceGenieApiServiceController {

	/** The garage service. */
	@Autowired
	GarageService garageService;

	/** The garage jobs service. */
	@Autowired
	GarageJobsService garageJobsService;

	/**
	 * Gets the garages on location.
	 *
	 * @param location the location
	 * @return the garages on location
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getGaragesOnLocation")
	public ResponseEntity<List<Garage>> getGaragesOnLocation(@RequestParam("location") String location) {
		List<Garage> garages = garageService.findAllGaragesInSpecifiedLocation(location);
		return new ResponseEntity<List<Garage>>(garages, HttpStatus.OK);
	}

	/**
	 * Gets the garage details by name.
	 *
	 * @param name the name
	 * @return the garage details by name
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getGarageDetailsByName")
	public ResponseEntity<GarageDetails> getGarageDetailsByName(@RequestParam("garageName") String name) {
		GarageDetails garageDetails = garageService.findGarageDetails(name);
		return new ResponseEntity<GarageDetails>(garageDetails, HttpStatus.OK);
	}

	/**
	 * Creates the garage.
	 *
	 * @param garageDetails the garage details
	 * @return the response entity
	 */
	@CrossOrigin(origins = "*")
	@PostMapping("/createGarage")
	public ResponseEntity<String> createGarage(@ModelAttribute GarageDetails garageDetails) {
		garageService.createGarageWithServices(garageDetails);
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	/**
	 * Gets the garages on lat and long.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param distance the distance
	 * @return the garages on lat and long
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getGaragesByLatAndLong")
	public ResponseEntity<List<GarageDetails>> getGaragesOnLatAndLong(@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude, @RequestParam("distanceInKms") double distance) {
		List<GarageDetails> listOfGarages = new ArrayList<GarageDetails>();
		List<Garage> garages = garageService.findAllNearestGaragesWithIndistance(latitude, longitude, distance);
		for (Garage garage : garages) {
			listOfGarages.add(garageJobsService.getGarageDetails(garage));
		}
		return new ResponseEntity<List<GarageDetails>>(listOfGarages, HttpStatus.OK);
	}

	/**
	 * Gets the all unique locations.
	 *
	 * @return the all unique locations
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getAllUniqueGarageLocations")
	public ResponseEntity<List<String>> getAllUniqueLocations() {
		List<String> uniqueGarageLocations = garageService.getUniqueGarageLocations();
		return new ResponseEntity<List<String>>(uniqueGarageLocations, HttpStatus.OK);
	}

	/**
	 * Gets the all garages.
	 *
	 * @return the all garages
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getAllGarages")
	public ResponseEntity<List<Garage>> getAllGarages() {
		return new ResponseEntity<List<Garage>>(garageService.findAllGarages(), HttpStatus.OK);
	}

	/**
	 * Gets the all garages with regex name.
	 *
	 * @param regexName the regex name
	 * @return the all garages with regex name
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getAllGaragesUsingRegex")
	public ResponseEntity<List<GarageDetails>> getAllGaragesWithRegexName(
			@RequestParam("garageNameContaining") String regexName) {
		return new ResponseEntity<List<GarageDetails>>(garageService.findAllGaragesWithName(regexName), HttpStatus.OK);
	}

	/**
	 * Gets the all garages with service.
	 *
	 * @param serviceID the service ID
	 * @return the all garages with service
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getAllGaragesWithService")
	public ResponseEntity<List<Garage>> getAllGaragesWithService(@RequestParam("service") String serviceID) {
		return new ResponseEntity<List<Garage>>(garageJobsService.getAllGaragesWithJob(serviceID), HttpStatus.OK);
	}

	/**
	 * Bulk upload.
	 *
	 * @param multiPartFile the multi part file
	 * @return the response entity
	 */
	@CrossOrigin(origins = "*")
	@PostMapping("/bulkUpload")
	public ResponseEntity<String> bulkUpload(@RequestPart(value = "file") MultipartFile multiPartFile) {
		try {
			garageService.bulkUploadOfGarages(multiPartFile);
		} catch (Exception e) {
			return new ResponseEntity<String>("Upload Failed", HttpStatus.NOT_MODIFIED);
		}
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}

	/**
	 * Gets the all garages with name on location.
	 *
	 * @param garageName the garage name
	 * @param location the location
	 * @return the all garages with name on location
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/getAllGaragesWithNameOnLocation")
	public ResponseEntity<List<Garage>> getAllGaragesWithNameOnLocation(@RequestParam("garageName") String garageName,
			@RequestParam("location") String location) {
		return new ResponseEntity<List<Garage>>(garageService.findAllGaragesWithNameAndLocation(garageName, location),
				HttpStatus.OK);
	}
}
