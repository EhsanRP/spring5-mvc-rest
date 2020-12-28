package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.controllers.v1.CustomerController;
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
                    customerDTO.setCustomerURL(getCustomerUrl(customerDTO.getId()));

                    return customerDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    var customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerURL(getCustomerUrl(customerDTO.getId()));
                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerByFirstName(String name) {
        var customer = customerRepository.findByFirstName(name);

        errorHelperMethod(customer, name);

        var returnValue = customerMapper.customerToCustomerDTO(customer);
        returnValue.setCustomerURL(getCustomerUrl(returnValue.getId()));
        return returnValue;
    }

    @Override
    public CustomerDTO getCustomerByLastName(String lastName) {
        var customer = customerRepository.findByLastName(lastName);

        errorHelperMethod(customer, lastName);

        var returnValue = customerMapper.customerToCustomerDTO(customer);
        returnValue.setCustomerURL(getCustomerUrl(returnValue.getId()));
        return returnValue;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        return saveAndReturnDTO(customerMapper.CustomerDTOtoCustomer(customerDTO));
    }

    private CustomerDTO saveAndReturnDTO(Customer customer) {

        var savedCustomer = customerRepository.save(customer);

        var returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        returnDTO.setCustomerURL(getCustomerUrl(savedCustomer.getId()));
        return returnDTO;
    }

    @Override
    public CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO) {
        var customer = customerMapper.CustomerDTOtoCustomer(customerDTO);
        customer.setId(id);

        return saveAndReturnDTO(customer);
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {

            if (customerDTO.getFirstName() != null)
                customer.setFirstName(customerDTO.getFirstName());

            if (customerDTO.getLastName() != null)
                customer.setLastName(customerDTO.getLastName());

            var returnDTO = customerMapper.customerToCustomerDTO(customerRepository.save(customer));
            returnDTO.setCustomerURL(getCustomerUrl(id));

            return returnDTO;
        }).orElseThrow(RuntimeException::new);

        //TODO BETTER IMPLEMENT FOR EXCEPTION HANDLING
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }


    //HELPER METHODS HERE
    private void errorHelperMethod(Customer ArgumentObject, String ArgumentField) {
        if (ArgumentObject == null) {
            log.error("Customer For " + ArgumentField + " Not Found");
            throw new ResourceNotFoundException();
        }
    }

    private String getCustomerUrl(Long id){
        return CustomerController.BASE_URL + "/id/" + id;
    }
}
