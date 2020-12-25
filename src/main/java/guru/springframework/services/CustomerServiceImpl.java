package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    var customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerURL("/api/customers/id/"+customerDTO.getId());

                    return customerDTO;
                })
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    var customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerURL("/api/customers/id/"+customerDTO.getId());
                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerByFirstName(String name) {
        var customer = customerRepository.findByFirstName(name);

        errorHelperMethod(customer, name);

        var returnValue = customerMapper.customerToCustomerDTO(customer);
        returnValue.setCustomerURL("/api/customers/id/"+ returnValue.getId());
        return returnValue;
    }

    @Override
    public CustomerDTO getCustomerByLastName(String lastName) {
        var customer = customerRepository.findByLastName(lastName);

        errorHelperMethod(customer, lastName);

        var returnValue = customerMapper.customerToCustomerDTO(customer);
        returnValue.setCustomerURL("/api/customers/id/"+ returnValue.getId());
        return returnValue;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        var customer = customerMapper.CustomerDTOtoCustomer(customerDTO);
        var savedCustomer = customerRepository.save(customer);
        var returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        returnDTO.setCustomerURL("/api/customers/id/"+ returnDTO.getId());
        return returnDTO;
    }

    private void errorHelperMethod(Customer ArgumentObject, String ArgumentField) {
        if (ArgumentObject == null) {
            log.error("Customer For " + ArgumentField + " Not Found");
            throw new RuntimeException();
        }
    }
}
