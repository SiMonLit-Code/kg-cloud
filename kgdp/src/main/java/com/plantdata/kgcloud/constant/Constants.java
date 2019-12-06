package com.plantdata.kgcloud.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Constants {

    @Value("${document_path}")
    public String documentPath;

    @Value("${kgdp_url}")
    public String kgdpUrl;

}
