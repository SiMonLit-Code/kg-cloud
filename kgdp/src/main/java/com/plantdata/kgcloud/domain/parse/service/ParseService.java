package com.plantdata.kgcloud.domain.parse.service;

import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.parse.req.NlpParseReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ParseService {
    ApiReturn<PdDocument> parseCountry(MultipartFile multiRequest);

    ApiReturn<List<PdDocument>> nlpParse(NlpParseReq nlpParseReq);
}
