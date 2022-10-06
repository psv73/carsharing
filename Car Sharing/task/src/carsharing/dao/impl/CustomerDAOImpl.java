package carsharing.dao.impl;

import carsharing.DatabaseAccess;
import carsharing.dao.CustomerDAO;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private static final DatabaseAccess DATABASE_ACCESS = new DatabaseAccess();

    @Override
    public List<Customer> getAll() {
        String sql = """
                SELECT * FROM Customer;
                """;
        return executeQuery(sql);
    }

    @Override
    public Customer getById(Integer id) {
        String sql = String.format("""
                SELECT * FROM Customer WHERE id = %d;
                """, id);
        return executeQuery(sql).get(0);
    }

    @Override
    public void create(String name) {
        String sql = String.format("""
                INSERT INTO Customer (name) VALUES ('%s');
                """, name);
        DATABASE_ACCESS.executeUpdate(sql);
    }

    @Override
    public List<Car> getCars() {
        String sql = String.format("""
                SELECT * FROM
                """);
        return null;
    }

    @Override
    public void update(Customer customer) {
        String sql = String.format("""
                UPDATE Customer SET
                    name = '%s', 
                    rented_car_id = %d 
                WHERE id = %d 
                """, customer.getName(), customer.getRented_car_id(), customer.getId());
        DATABASE_ACCESS.executeUpdate(sql);
    }

    private List<Customer> executeQuery(String sql) {
        List<Customer> customers = new ArrayList<>();
        DATABASE_ACCESS.createConnection();

        ResultSet rs = DATABASE_ACCESS.executeQuery(sql);

        try {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("rented_car_id")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DATABASE_ACCESS.closeQuery();
        }
        return customers;
    }
}
