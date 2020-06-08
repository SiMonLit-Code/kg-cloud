package com.plantdata.kgcloud.plantdata.req.app;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfoBoxMultiModalParameter {
    @NotBlank
    private String kgName;
    private Long id;
    private String kw;

}
