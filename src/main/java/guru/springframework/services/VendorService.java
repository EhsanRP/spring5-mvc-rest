package guru.springframework.services;

import guru.springframework.api.v1.model.VendorDTO;

import java.util.List;

public interface VendorService {

    List<VendorDTO> findAllVendors();

    VendorDTO findVendorById(Long id);

    VendorDTO findVendorByName(String name);

    VendorDTO saveNewVendor(VendorDTO vendorDTO);

    VendorDTO updateVendor(Long id, VendorDTO vendorDTO);

    VendorDTO patchVendor(Long id, VendorDTO vendorDTO);

    void deleteVendor(Long id);

}
