package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.api.v1.model.VendorListDTO;
import guru.springframework.services.ResourceNotFoundException;
import guru.springframework.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "api/v1/vendors";

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public VendorListDTO getAllVendors() {
        return new VendorListDTO(vendorService.findAllVendors());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/name/{name}")
    public VendorDTO getVendorByName(@PathVariable String name) {
        var key = searchKeyHelper(name);

        if (key == null)
            throw new ResourceNotFoundException();

        return vendorService.findVendorByName(key);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/id/{id}")
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findVendorById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.saveNewVendor(vendorDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/id/{id}")
    public VendorDTO updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.updateVendor(id, vendorDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/id/{id}")
    public VendorDTO patchVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.patchVendor(id, vendorDTO);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void patchVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
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
