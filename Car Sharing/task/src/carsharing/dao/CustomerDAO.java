package carsharing.dao;

import carsharing.model.Car;
import carsharing.model.Customer;

import java.util.List;

public interface CustomerDAO {

    public List<Customer> getAll();
    public Customer getById(Integer id);
    public void create(String name);
    public List<Car> getCars();
    public void update(Customer customer);
}
