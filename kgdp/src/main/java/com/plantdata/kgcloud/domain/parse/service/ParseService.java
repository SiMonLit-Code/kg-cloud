package com.plantdata.kgcloud.domain.parse.service;

import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import org.springframework.web.multipart.MultipartFile;

public interface ParseService {
    ApiReturn<PdDocument> parseCountry(MultipartFile multiRequest);
}
