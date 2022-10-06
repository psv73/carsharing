package carsharing.dao.impl;

import carsharing.DatabaseAccess;
import carsharing.dao.CompanyDAO;
import carsharing.model.Company;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDAO {

    static final DatabaseAccess DATABASE_ACCESS = new DatabaseAccess();

    @Override
    public List<Company> getAll() {
        String sql = """
                SELECT * FROM Company;
                """;
        return executeQuery(sql);
    }

    @Override
    public Company getById(int id) {
        String sql = String.format("""
                SELECT * FROM Company 
                 WHERE id = %d;
                """, id);
        return executeQuery(sql).get(0);
    }

    @Override
    public void delete(Company company) {

    }

    @Override
    public void update(Company company) {

    }

    @Override
    public void create(String name) {
        String sql = String.format("INSERT INTO Company (name) VALUES ('%s')", name);
        DATABASE_ACCESS.executeUpdate(sql);
    }

    public List<Company> executeQuery(String sql) {
        List<Company> companies = new ArrayList<>();
        DATABASE_ACCESS.createConnection();

        ResultSet rs = DATABASE_ACCESS.executeQuery(sql);

        try {
            while (rs.next()) {
                companies.add(new Company(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DATABASE_ACCESS.closeQuery();
        }

        return companies;
    }
}
