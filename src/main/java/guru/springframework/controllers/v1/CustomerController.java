package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.api.v1.model.CustomerListDTO;
import guru.springframework.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("api/v1/customers/")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public ResponseEntity<CustomerListDTO> getAllCustomers() {
        return new ResponseEntity<CustomerListDTO>(new CustomerListDTO(customerService.getAllCustomers()), HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return new ResponseEntity<CustomerDTO>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @GetMapping("firstName/{firstName}")
    public ResponseEntity<CustomerDTO> getCustomerByFistName(@PathVariable String firstName) {
        var key = searchKeyHelper(firstName);

        if (key == null)
            return new ResponseEntity<CustomerDTO>(new CustomerDTO(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<CustomerDTO>(customerService.getCustomerByFirstName(key), HttpStatus.OK);
    }

    @GetMapping("lastName/{lastName}")
    public ResponseEntity<CustomerDTO> getCustomerByLastName(@PathVariable String lastName) {
        var key = searchKeyHelper(lastName);

        if (key == null)
            return new ResponseEntity<CustomerDTO>(new CustomerDTO(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<CustomerDTO>(customerService.getCustomerByLastName(key), HttpStatus.OK);
    }

    private String searchKeyHelper(String input){
        var argument = input.split("_");

        if (argument.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < argument.length; i++) {
            stringBuffer.append(argument[i]);
            stringBuffer.append(" ");
        }

        var key = stringBuffer.toString();

        return key;
    }
}