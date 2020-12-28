package guru.springframework.services;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public List<VendorDTO> findAllVendors() {
        return vendorRepository.findAll().stream()
                .map(vendor -> {
                    var vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setUrl(getVendorUrl(vendorDTO.getId()));
                    return vendorDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public VendorDTO findVendorById(Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    var vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setUrl(getVendorUrl(id));
                    return vendorDTO;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDTO findVendorByName(String name) {

        var vendor = vendorRepository.findByName(name);

        errorHelperMethod(vendor, name);

        var returnValue = vendorMapper.vendorToVendorDTO(vendor);
        returnValue.setUrl(getVendorUrl(vendor.getId()));

        return returnValue;
    }

    @Override
    public VendorDTO saveNewVendor(VendorDTO vendorDTO) {
        return saveAndReturnDTO(vendorMapper.vendorDTOtoVendor(vendorDTO));
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {

        var vendor = vendorMapper.vendorDTOtoVendor(vendorDTO);
        vendor.setId(id);

        vendorRepository.save(vendor);

        return saveAndReturnDTO(vendor);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return vendorRepository.findById(id).map(vendor -> {

            if (vendorDTO.getName() != null)
                vendor.setName(vendorDTO.getName());

            vendorRepository.save(vendor);

            var returnDto = vendorMapper.vendorToVendorDTO(vendor);
            returnDto.setUrl(getVendorUrl(id));

            return returnDto;
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }

    //HELPER METHODS HERE
    private VendorDTO saveAndReturnDTO(Vendor vendor) {

        var savedVendor = vendorRepository.save(vendor);

        var returnDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        returnDTO.setUrl(getVendorUrl(savedVendor.getId()));

        return returnDTO;
    }

    private void errorHelperMethod(Vendor ArgumentObject, String ArgumentField) {
        if (ArgumentObject == null) {
            log.error("Vendor For " + ArgumentField + " Not Found");
            throw new ResourceNotFoundException();
        }
    }

    private String getVendorUrl(Long id) {
        return VendorController.BASE_URL + "/id/" + id;
    }

}
