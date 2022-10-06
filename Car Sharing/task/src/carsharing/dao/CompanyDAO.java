package carsharing.dao;

import carsharing.model.Company;

import java.util.List;

public interface CompanyDAO {

//    public List<Company> resultSetToCompany(ResultSet resultSet);
    public List<Company> getAll();
    public Company getById(int id);
    public void delete(Company company);
    public void update(Company company);
    public void create(String name);
}
