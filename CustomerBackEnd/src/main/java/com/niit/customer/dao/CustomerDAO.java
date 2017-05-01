package com.niit.customer.dao;

import java.util.List;

import com.niit.customer.model.Customer;

public interface CustomerDAO {
	public List<Customer> list();

	public boolean update(Customer customer);

	public boolean delete(Customer customer);

	public boolean save(Customer customer);
	public Customer isValidUser(String emailId, String dob);
	public Customer get(String emailId);

}
