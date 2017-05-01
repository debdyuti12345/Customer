package com.niit.customer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.customer.dao.CustomerDAO;
import com.niit.customer.model.Customer;

@RestController
public class CustomerController {
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	CustomerDAO customerDao;
	
	@Autowired 
	Customer customer;
	
	@RequestMapping(value="/getAll", method=RequestMethod.GET)
	public ResponseEntity<List<Customer>> getAllUser(){
		log.debug("staring get All");
		List customers =customerDao.list();
		if(customers.isEmpty()){
			
			customer.setErrorcode("100");
			customer.setErrorMessage("no customer are available");
		      customers.add(customer);
		return new ResponseEntity<List<Customer>>(customers,HttpStatus.OK);
		}
		customer.setErrorcode("301");
		customer.setErrorcode("successfully feched from database");
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
		
	}
	
	@PostMapping("/createUser/")
	public ResponseEntity<Customer> createUser(@RequestBody Customer customer){
		log.debug("starting method createcustomer");
		if (customerDao.get(customer.getEmailId()) == null) {
			log.debug("customer is going to create with id:" + customer.getEmailId());
			
			  if (customerDao.save(customer) ==true)
			  {
				  customer.setErrorcode("200");
					customer.setErrorMessage("Thank you  for registration. You have successfully registered as " + customer.getEmailId());
			  }
			  else
			  {
				  customer.setErrorcode("404");
					customer.setErrorMessage("Could not complete the operatin please contact Admin");
		
				  
			  }
			
			return new ResponseEntity<Customer>(customer, HttpStatus.OK);
		}
		log.debug("User already exist with id " + customer.getEmailId());
		customer.setErrorcode("404");
		customer.setErrorMessage("User already exist with id : " + customer.getEmailId());
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	@DeleteMapping("/deleteUser/{emailId}")
	public ResponseEntity<Customer> deleteUser(@PathVariable("emaiId")String emailId){
		Customer customer=customerDao.get(emailId);
		if(customer!=null){
			customerDao.delete(customer);
			customer.setErrorcode("200");
			customer.setErrorMessage("user deleted successfully");
		}
		else{
			customer.setErrorcode("301");
			customer.setErrorMessage("already esist"+customer.getEmailId());
		}
		return new ResponseEntity<Customer>(customer,HttpStatus.CREATED);
			
	}
	@PutMapping("/updateUser")
	public ResponseEntity<Customer> updateUser(@RequestBody Customer customer) {
		log.debug("calling method updateUser");
		if (customerDao.get(customer.getEmailId()) == null) {
			log.debug("User does not exist with id " + customer.getEmailId());
			customer = new Customer(); // ?
			customer.setErrorcode("404");
			customer.setErrorMessage("User does not exist with id " + customer.getEmailId());
			return new ResponseEntity<Customer>(customer, HttpStatus.OK);
		}

		customerDao.update(customer);
		log.debug("Customer updated successfully");
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	@PostMapping("/login")
	public ResponseEntity<Customer> login(@RequestBody Customer customer, HttpSession session) {
		log.debug("calling method authenticate");
		customer= customerDao.isValidUser(customer.getEmailId(), customer.getDob());
		if (customer == null) {
			customer = new Customer(); // Do wee need to create new user?
			customer.setErrorcode("404");
			customer.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
			log.debug("In Valid Credentials");

		} else

		{
			customer.setErrorcode("200");
			customer.setErrorMessage("You have successfully logged in.");
			
			log.debug("Valid Credentials");
			/*session.setAttribute("loggedInUser", user);*/
			session.setAttribute("loggedInUserID", customer.getEmailId());
			session.setAttribute("loggedInUserRole", customer.getDob());
			
			log.debug("You are loggin with the role : " +session.getAttribute("loggedInUserRole"));

			
		}

		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	

}
