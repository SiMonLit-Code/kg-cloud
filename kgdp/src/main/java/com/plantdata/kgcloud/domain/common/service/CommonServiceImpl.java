package com.plantdata.kgcloud.domain.common.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.model.UpdateOptions;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.constant.Constants;
import com.plantdata.kgcloud.constant.ConvertConstent;
import com.plantdata.kgcloud.constant.KgDocumentErrorCodes;
import com.plantdata.kgcloud.domain.common.entity.TitleBean;
import com.plantdata.kgcloud.domain.common.entity.WordContent;
import com.plantdata.kgcloud.domain.common.rsp.FileRsp;
import com.plantdata.kgcloud.domain.common.util.BasicImageURIResolver;
import com.plantdata.kgcloud.domain.common.util.ConvertUtil;
import com.plantdata.kgcloud.domain.common.util.KgDocumentImageExtractor;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.UUIDUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {


    private static final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private Constants constants;

    @Resource
    private MongoClient mongoClient;


    @Override
    public ResponseEntity<byte[]> resourceDownLoad(String name){

        MongoDatabase database = mongoClient.getDatabase(ConvertConstent.databases);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        MongoCollection<Document> fileType = database.getCollection("file_map");
        Document first = fileType.find(new Document("source", name)).first();
        ObjectId fileId = first.getObjectId("file_id");
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fileId);

             ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {

            IOUtils.copy(gridFSDownloadStream, output);
        }catch (Exception e){
            log.error(e.getMessage());
            throw BizException.of(KgDocumentErrorCodes.RESOURCE_DOWNLOAD_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",first.getString("name"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(output.toByteArray(),headers, HttpStatus.OK);
    }

    @Override
    public String upload(InputStream fileIn,String name) {

        String fname = constants.getDocumentPath() + "/" + UUIDUtils.getShortString()+name;
        File file = new File(fname);
        try {
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }


            FileUtils.copyInputStreamToFile(fileIn, file);
        } catch (IOException ex) {
            throw BizException.of(KgDocumentErrorCodes.RESOURCE_UPLOAD_ERROR);
        }
        return fname;
    }

    @Override
    public ApiReturn<String> uploadImage(String name, byte[] content) {


//        MultipartFile file = new MockMultipartFile(name,name,null,content);
//        FastdfsPathDto fastdfsPathDto = fastdfsTemplate.uploadFile(file);
//        String fileOrgSource = fastdfsTemplate.getFileDownloadUrl(fastdfsPathDto.getFullPath());
        MongoDatabase database = mongoClient.getDatabase(ConvertConstent.databases);
        MongoCollection<Document> fileCollection = database.getCollection("file_map");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        String f = charsetTransform(name, "ISO_8859_1", "UTF-8");

        String fileOrgName = UUIDUtils.getShortString() +name;
        String fileOrgSource = constants.getKgdpUrl()+"/common/resource/download?name="+fileOrgName;
        try {
            ObjectId objectId = gridFSBucket.uploadFromStream(f, new ByteArrayInputStream(content));
            Document filter =  new Document("name", name);
            filter.put("source",fileOrgName);
            fileCollection.updateOne(filter, new Document("$set", new Document("file_id", objectId)), new UpdateOptions().upsert(true));
        }catch (Exception e){
            e.printStackTrace();
            throw BizException.of(KgDocumentErrorCodes.RESOURCE_UPLOAD_ERROR);
        }
        return ApiReturn.success(fileOrgSource);
    }

    @Override
    public List<WordContent> convertDocToHtml(String path, Boolean structureDismantling) {

        File file = new File(path);

        String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
        String imagePathStr = file.getParentFile().getAbsolutePath();
        String targetFileName = imagePathStr+"/"+fileName+".html";

        if(!file.exists()){
            throw BizException.of(KgDocumentErrorCodes.DOCUMENT_NOT_EXISTS);
        }

        List<WordContent> pElements = null;
        Integer structure = 0;
        try {

            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(file));
            org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            //保存图片，并返回图片的相对路径
            wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {

                if(name.endsWith(ConvertConstent.EMF)){
                    content = ConvertUtil.emfToPng(content);
                    name += ConvertConstent.PNG;
                }
                if(name.endsWith(ConvertConstent.WMF)){
                    content = ConvertUtil.wmfToPng(content,constants.getDocumentPath());
                    name += ConvertConstent.PNG;
                }
                ApiReturn<String> success = uploadImage(name,content);
                return success.getData();
            });

            TitleBean titleBean = ConvertUtil.getTitle(wordDocument);
            structure = titleBean.getStructure();

            wordToHtmlConverter.processDocument(wordDocument);
            org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(new FileOutputStream(targetFileName));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);

            if(structureDismantling){
                pElements = ConvertUtil.setTitleAtt(targetFileName,titleBean);
            }else{
                pElements = ConvertUtil.setTitleAtt(targetFileName);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw BizException.of(KgDocumentErrorCodes.WORD_CONVERT_HTML_ERROR);
        }


        return pElements;
    }

    @Override
    public List<WordContent> convertDocxToHtml(String path, Boolean structureDismantling) {
        File file = new File(path);

        String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
        String imagePathStr = file.getParentFile().getAbsolutePath();
        String targetFileName = imagePathStr+"/"+fileName+".html";

        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }

        List<WordContent> pElements = null;
        Integer structure = 0;
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(file));

            TitleBean titleBean = ConvertUtil.getTitle(document);
            structure = titleBean.getStructure();
            XHTMLOptions options = XHTMLOptions.create();
            Map<String, String> imageMapMap = Maps.newHashMap();
            // 存放图片的文件夹
            options.setExtractor(new KgDocumentImageExtractor(this,imageMapMap,constants.getDocumentPath()));
            // html中图片的路径
            options.URIResolver(new BasicImageURIResolver(imageMapMap));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);

            if(structureDismantling){
                pElements = ConvertUtil.setTitleAtt(targetFileName,titleBean);
            }else{
                pElements = ConvertUtil.setTitleAtt(targetFileName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                }catch (Exception e){}
            }
        }
        return pElements;
    }

    @Override
    public ApiReturn<List<FileRsp>> multiUpload(MultipartFile[] multiRequest) {
        if(multiRequest == null || multiRequest.length == 0){
            return ApiReturn.success();
        }else{

            List<FileRsp> successList = Lists.newArrayList();
            for(int i=0; i<multiRequest.length; i++){
                MultipartFile file = multiRequest[i];
                try {
//                    FastdfsPathDto fastdfsPathDto = fastdfsTemplate.uploadFile(file);
//                    String source = this.fastdfsTemplate.getFileDownloadUrl(fastdfsPathDto.getFullPath());
                    String source = upload(file.getInputStream(),file.getOriginalFilename());
                    successList.add(FileRsp.builder().name(file.getOriginalFilename())
                            .size(file.getSize())
                            .source(source)
                            .type(file.getContentType()).build());
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("文档上传失败:"+file.getOriginalFilename());
                }
            }

            return ApiReturn.success(successList);
        }
    }


    private String charsetTransform(String text, String fromCharset, String toCharset) {
        try {
            return new String(text.getBytes(fromCharset), toCharset);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
