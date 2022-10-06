package carsharing.dao;

import carsharing.model.Car;

import java.sql.SQLException;
import java.util.List;

public interface CarDAO {

    public List<Car> getAllCar() throws SQLException;
    public List<Car> getCarsByCompany(int id);
    public Car getCar(int id);
    public void createCar(String name, int company_id);
    List<Car> executeQuery(String sql);
}
