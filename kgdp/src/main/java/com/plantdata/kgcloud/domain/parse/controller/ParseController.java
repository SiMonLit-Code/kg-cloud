package com.plantdata.kgcloud.domain.parse.controller;

import com.hiekn.pddocument.bean.PdDocument;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.parse.service.ParseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "parse")
@RestController
@RequestMapping("/parse")
public class ParseController {


    @Autowired
    private ParseService parseService;


    @ApiOperation("国家概况解析")
    @PostMapping(value = "country", consumes = "multipart/form-data")
    public ApiReturn<PdDocument> multiUpload(MultipartFile multiRequest) {
        return parseService.parseCountry(multiRequest);
    }


    @ApiOperation("nlp解析")
    @PostMapping("nlp")
    public ApiReturn<PdDocument> nlp(String url, @RequestBody List<String> texts) {
        return parseService.nlpParse(url,texts);
    }
}
