package carsharing;

import carsharing.dao.impl.CarDAOImpl;
import carsharing.dao.impl.CompanyDaoImpl;
import carsharing.dao.impl.CustomerDAOImpl;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {

    static Scanner scanner = null;
    static final CompanyDaoImpl COMPANY_DAO = new CompanyDaoImpl();
    static final CarDAOImpl CAR_DAO = new CarDAOImpl();
    static final CustomerDAOImpl CUSTOMER_DAO = new CustomerDAOImpl();

    public Menu(Scanner scanner) {
        Menu.scanner = scanner;
    }

    public void startMenu() {

        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Login as customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            int manuID = scanner.nextInt();
            System.out.println();

            switch (manuID) {
                case 1 -> managerMenu();
                case 2 -> chooseCustomer();
                case 3 -> createCustomer();
                case 0 -> {
                    return;
                }
                case 100 -> {
                    DatabaseAccess da = new DatabaseAccess();
                    da.clearDatabase();
                }
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");

        scanner = new Scanner(System.in);

        String customer = scanner.nextLine();
        CUSTOMER_DAO.create(customer);

        System.out.println("The customer was created!");
        System.out.println();
    }

    private void customerMenu(int customerId) {

        if (customerId == 0) {
            return;
        }

        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            int menuId = scanner.nextInt();
            System.out.println();

            switch (menuId) {
                case 1 -> rentCar(customerId);
                case 2 -> returnCar(customerId);
                case 3 -> showRentedCar(customerId);
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void showRentedCar(int customerId) {
        Customer customer = CUSTOMER_DAO.getById(customerId);

        if (customer.getRented_car_id() == null ||
                customer.getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!");
            System.out.println();
        } else {
            Car car = CAR_DAO.getCar(customer.getRented_car_id());
            Company company = COMPANY_DAO.getById(car.getCompany_id());
            System.out.println("You rented car:");
            System.out.println(car.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
            System.out.println();
        }
    }

    private void returnCar(int customerId) {
        Customer customer = CUSTOMER_DAO.getById(customerId);

        if (customer.getRented_car_id() == null ||
                customer.getRented_car_id() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            customer.setRented_car_id(null);
            CUSTOMER_DAO.update(customer);
            System.out.println("You've returned a rented car!");
        }

        System.out.println();
    }

    private void rentCar(int customerId) {

        if (CUSTOMER_DAO.getById(customerId).getRented_car_id() != null &&
                CUSTOMER_DAO.getById(customerId).getRented_car_id() != 0) {
            System.out.println("You've already rented a car!");
            System.out.println();
            return;
        }

        List<Company> companies = COMPANY_DAO.getAll();

        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        while (true) {
            System.out.println("Choose a company:");
            companies.forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
            System.out.println("0. Back");

            int companyId = scanner.nextInt();

            System.out.println();

            if (companyId == 0) {
                return;
            }

            if (CAR_DAO.getCarsByCompany(companyId).isEmpty()) {
                System.out.printf("No available cars in the '%s' company",
                        COMPANY_DAO.getById(companyId).getName());
                System.out.println();
                continue;
            }

            int carId = chooseCar(companyId);

            if (carId != 0) {
                Customer customer = CUSTOMER_DAO.getById(customerId);
                customer.setRented_car_id(carId);
                CUSTOMER_DAO.update(customer);
                System.out.printf("Your rented '%s'\n", CAR_DAO.getCar(carId).getName());
            }

            System.out.println();
            return;
        }
    }

    private int chooseCar(int companyId) {
        List<Car> cars = CAR_DAO.getCarsByCompany(companyId);
        AtomicInteger i = new AtomicInteger(0);

        System.out.println("Choose a car:");
        cars.forEach(c -> System.out.println(i.incrementAndGet() +
                ". " + c.getName()));
        System.out.println("0. Back");
        int chooseId = scanner.nextInt();
        System.out.println();

        return chooseId > 0 ? cars.get(chooseId - 1).getId() : 0;
    }

    private boolean showCustomerList() {
        List<Customer> customers = CUSTOMER_DAO.getAll();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty");
            System.out.println();
            return false;
        } else {
            System.out.println("Choose a customer:");
            customers.forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
            System.out.println("0. Back");
            return true;
        }
    }

    private void chooseCustomer() {
        if (showCustomerList()) {
            int customerId = scanner.nextInt();
            System.out.println();
            customerMenu(customerId);
        }
    }

    private void managerMenu() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            int menuId = scanner.nextInt();

            System.out.println();

            switch (menuId) {
                case 1 -> chooseCompany();
                case 2 -> createCompany();
                case 0 -> {
                    return;
                }
            }
        }
    }

    private void chooseCompany() {
        if (showCompanyList()) {
            int company_id = scanner.nextInt();
            System.out.println();
            companyMenu(company_id);
        }
    }

    private void companyMenu(int company_id) {

        if (company_id == 0) {
            return;
        }

        System.out.printf("'%s' company:\n", COMPANY_DAO.getById(company_id).getName());

        while (true) {
            System.out.println("1. Car list");
            System.out.println("2. Create car");
            System.out.println("0. Back");

            int menuId = scanner.nextInt();
            System.out.println();

            switch (menuId) {
                case 0 -> {
                    return;
                }
                case 1 -> showCarsOfCompany(company_id);
                case 2 -> createCar(company_id);
            }
        }
    }

    private void createCar(int company_id) {
        System.out.println("Enter the car name:");

        scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        CAR_DAO.createCar(name, company_id);

        System.out.println("The car was added!");
        System.out.println();
    }

    private void showCarsOfCompany(int company_id) {
        List<Car> cars = CAR_DAO.getCarsByCompany(company_id);

        if (cars.isEmpty()) {
            System.out.println("The car list is empty");
            System.out.println();
        } else {
//            System.out.printf("'%s' cars:\n", COMPANY_DAO.getCompany(company_id).getName());
            AtomicInteger i = new AtomicInteger(1);
            System.out.println("Car list:");
            cars.forEach(c -> System.out.println(i.getAndIncrement() + ". " + c.getName()));
            System.out.println();
        }
    }

    private void createCompany() {
        System.out.println("Enter the company name:");

        scanner = new Scanner(System.in);

        String company = scanner.nextLine();
        COMPANY_DAO.create(company);

        System.out.println("The company was created!");
        System.out.println();
    }

    private boolean showCompanyList() {
        List<Company> companies = COMPANY_DAO.getAll();

        if (companies.isEmpty()) {
            System.out.println("The company list is empty");
            System.out.println();
            return false;
        } else {
            AtomicInteger i = new AtomicInteger(1);
            System.out.println("Choose a company:");
            companies.forEach(c -> System.out.println(i.getAndIncrement() + ". " + c.getName()));
            System.out.println("0. Back");
            return true;
        }
    }
}
