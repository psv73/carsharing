package carsharing.dao.impl;

import carsharing.DatabaseAccess;
import carsharing.dao.CarDAO;
import carsharing.model.Car;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImpl implements CarDAO {

    static final DatabaseAccess DATABASE_ACCESS = new DatabaseAccess();

    @Override
    public List<Car> getAllCar() {
        String sql = """
                SELECT * FROM Car 
                """;
        return executeQuery(sql);
    }

    @Override
    public List<Car> getCarsByCompany(int company_id) {
        String sql = String.format("""
                SELECT * FROM Car 
                WHERE company_id = %d
                """, company_id);
        return executeQuery(sql);
    }

    @Override
    public Car getCar(int id) {
        String sql = String.format("""
                SELECT * FROM Car 
                WHERE id = %d
                """, id);
        return executeQuery(sql).get(0);
    }

    @Override
    public void createCar(String name, int company_id) {
        String sql = String.format("""
                INSERT INTO Car (name, company_id) 
                VALUES ('%s', %d)
                """, name, company_id);
        DATABASE_ACCESS.executeUpdate(sql);
    }

    @Override
    public List<Car> executeQuery(String sql) {
        List<Car> cars = new ArrayList<>();
        DATABASE_ACCESS.createConnection();

        ResultSet rs = DATABASE_ACCESS.executeQuery(sql);

        try {
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("company_id")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DATABASE_ACCESS.closeQuery();
        }

        return cars;
    }
}
