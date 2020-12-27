package guru.springframework.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorDTO {

    private Long id;
    private String name;
    private String url;

    public VendorDTO(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
