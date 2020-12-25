package guru.springframework.repositories;

import guru.springframework.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByFirstName(String name);

    Customer findByLastName(String lastName);

    Optional<Customer> findById(Long id);
}
