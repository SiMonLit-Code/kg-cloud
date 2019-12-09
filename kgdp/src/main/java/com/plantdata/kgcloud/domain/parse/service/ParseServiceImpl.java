package com.plantdata.kgcloud.domain.parse.service;

import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.parse.util.ParseCountryUtils;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ParseServiceImpl implements ParseService {

    @Override
    public ApiReturn<PdDocument> parseCountry(MultipartFile multiRequest) {

        try {
            String fileName = multiRequest.getOriginalFilename();
            InputStream in = multiRequest.getInputStream();
            return ApiReturn.success(ParseCountryUtils.getReplaceElementsInWord(fileName,in));
        }catch (IOException e){
            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_PARSE_ERROR);
        }
    }

    @Override
    public ApiReturn<PdDocument> nlpParse(String url, List<String> texts) {
        return null;
    }
}
