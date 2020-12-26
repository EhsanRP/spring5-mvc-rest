package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.api.v1.model.CustomerListDTO;
import guru.springframework.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerListDTO> getAllCustomers() {
        return new ResponseEntity<CustomerListDTO>(new CustomerListDTO(customerService.getAllCustomers()), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return new ResponseEntity<CustomerDTO>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @GetMapping("/firstName/{firstName}")
    public ResponseEntity<CustomerDTO> getCustomerByFistName(@PathVariable String firstName) {
        var key = searchKeyHelper(firstName);

        if (key == null)
            return new ResponseEntity<CustomerDTO>(new CustomerDTO(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<CustomerDTO>(customerService.getCustomerByFirstName(key), HttpStatus.OK);
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<CustomerDTO> getCustomerByLastName(@PathVariable String lastName) {
        var key = searchKeyHelper(lastName);

        if (key == null)
            return new ResponseEntity<CustomerDTO>(new CustomerDTO(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<CustomerDTO>(customerService.getCustomerByLastName(key), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createNewCustomer(@RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<CustomerDTO>(customerService.createNewCustomer(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<CustomerDTO>(customerService.saveCustomerByDTO(id, customerDTO), HttpStatus.OK);
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<CustomerDTO> patchCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<CustomerDTO>(customerService.patchCustomer(id, customerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //HELPER METHODS
    private String searchKeyHelper(String input) {
        var argument = input.split("_");

        if (argument.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < argument.length; i++) {
            stringBuffer.append(argument[i]);
            stringBuffer.append(" ");
        }

        if (stringBuffer.length() == 0)
            return null;

        var key = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();

        return key;
    }
}
