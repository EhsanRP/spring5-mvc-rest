package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;


    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading Data");

        System.out.println("Loading Categories - Supposed to be 5 of them");
        loadCategories();
        System.out.println("Number of Categories: " + categoryRepository.count());

        System.out.println("Loading Customers - Supposed to be 5 of them");
        loadCustomers();
        System.out.println("Number of Categories: " + categoryRepository.count());

    }

    private void loadCustomers() {
        customerRepository.save(new Customer("Negar","Omrani Khoo"));
        customerRepository.save(new Customer("Ehsan","Rostami Pour"));
        customerRepository.save(new Customer("Uncle","J.T"));
        customerRepository.save(new Customer("Arian"," Dashti"));
        customerRepository.save(new Customer("Tomas","Roddson"));
    }

    private void loadCategories() {
        categoryRepository.save(new Category("Fruits"));
        categoryRepository.save(new Category("Dried"));
        categoryRepository.save(new Category("Fresh"));
        categoryRepository.save(new Category("Exotic"));
        categoryRepository.save(new Category("Nuts"));
    }
}
