package com.plantdata.kgcloud.service.impl;

import com.plantdata.kgcloud.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

//    @Autowired
//    private TaskTemplateMapper taskTemplateMapper;
//
//    @Value("${upload.basePath}")
//    private String basePath;
//
//    @Value("${model_path}")
//    private String path;
//
//    @Autowired
//    private FastFileStorageClient storageClient;
//
//    @Autowired
//    private MongoClient mongoClient;
//
//    @Autowired
//    private JwtToken jwtToken;
//
//    @Autowired
//    private FdfsWebServer fdfsWebServer;
//
//    @Override
//    public String upload(InputStream fileIn, FormDataContentDisposition fileInfo, String subPath) {
//        String target = new StringBuilder(basePath).append(subPath).append("/").append(McnUtils.simpleUUID())
//                .append(".").append(McnUtils.getExtName(fileInfo.getFileName())).toString();
//        try {
//            McnUtils.copyFile(fileIn, new File(target));
//        } catch (IOException e) {
//            throw ServiceException.newInstance(ErrorCodes.UPLOAD_ERROR);
//        }
//        return target;
//    }
//
//    @Override
//    public String upload(InputStream fileIn, FormDataContentDisposition fileInfo) {
//        String target = buildFilePath(String.valueOf(System.currentTimeMillis())) + fileInfo.getFileName();
//        try {
//            McnUtils.copyFile(fileIn, new File(target));
//        } catch (IOException e) {
//            throw ServiceException.newInstance(ErrorCodes.UPLOAD_ERROR);
//        }
//        return target;
//    }
//
//    @Override
//    public String uploadMongo(String type, InputStream fileIn, FormDataContentDisposition fileInfo, FormDataBodyPart formDataBodyPart) {
//        MongoDatabase database = mongoClient.getDatabase("files");
//        MongoCollection<Document> fileType = database.getCollection("file_map");
//        String path = uploadFastDfs(fileIn, fileInfo, formDataBodyPart);
//        fileType.updateOne(new Document("type", type), new Document("$set", new Document("file_id", path)), new UpdateOptions().upsert(true));
//        return path;
//    }
//
//
//    @Override
//    public String uploadFastDfs(InputStream fileIn, FormDataContentDisposition fileInfo, FormDataBodyPart formDataBodyPart) {
//        String fileName = fileInfo.getFileName();
//        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
//        long length = formDataBodyPart.getEntityAs(File.class).length();
//        StorePath storePath = storageClient.uploadFile(fileIn, length, extName, null);
//        return storePath.getFullPath();
//    }
//
//
//    @Override
//    public Response download(String type) {
//        MongoDatabase database = mongoClient.getDatabase("files");
//        MongoCollection<Document> fileType = database.getCollection("file_map");
//        Document first = fileType.find(new Document("type", type)).first();
//        Object filePath = first.get("file_id");
//        if (filePath != null) {
//            if(filePath instanceof String) {
//                String build = UriBuilder.fromPath(fdfsWebServer.getWebServerUrl()).path(first.getString("file_id")).build().toString();
//                try {
//                    URL url = new URL(build);
//                    URLConnection con = url.openConnection();
//                    InputStream fis = con.getInputStream();
//                    StreamingOutput fileStream = output -> IOUtils.copy(fis, output);
//                    return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM).build();
//                } catch (Exception e) {
//                    return Response.status(404).build();
//                }
//            }
//            if(filePath instanceof ObjectId){
//                GridFSBucket gridFSBucket = GridFSBuckets.create(database);
//                GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(first.getObjectId("file_id"));
//                StreamingOutput fileStream = output -> IOUtils.copy(gridFSDownloadStream, output);
//                return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM).build();
//            }
//            return Response.status(404).build();
//        } else {
//            return Response.status(404).build();
//        }
//    }
//
//    @Override
//    public String multiUpload(FormDataMultiPart form, String dir) {
//        List<FormDataBodyPart> list = form.getFields("file");
//        if (Objects.nonNull(list) && !list.isEmpty()) {
//            String basePath = buildFilePath(dir);
//            for (FormDataBodyPart formDataBodyPart : list) {
//                InputStream fileIn = formDataBodyPart.getValueAs(InputStream.class);
//                FormDataContentDisposition detail = formDataBodyPart.getFormDataContentDisposition();
//                String fileName = detail.getFileName();
//                try {
//                    McnUtils.copyFile(fileIn, new File(basePath + "/" + fileName));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return basePath;
//        }
//        return "";
//    }
//
//    private String buildFilePath(String dir) {
//        StringBuilder sb = new StringBuilder(path);
//        return sb.append(jwtToken.getUserIdAsString()).append("/").append(StringUtils.isBlank(dir) ? McnUtils.simpleUUID() : dir).append("_upload").append("/").toString();
//    }
//
//    @Override
//    public List<Map<String, Object>> nameConversion(String skillConfig) {
//        try {
//            JSONArray config = JSON.parseArray(skillConfig);
//            List<Map<String, Object>> list = new ArrayList<>();
//
//            for (int i = 0; i < config.size(); i++) {
//                JSONObject jo = config.getJSONObject(i);
//                Map<String, Object> map = new HashMap<>();
//                map.put("id", jo.get("id"));
//                map.put("mapperName", PinyinHelper.convertToPinyinString(jo.getString("mapperName"), "", PinyinFormat.WITHOUT_TONE) + "_" + jo.getString("id"));
//                list.add(map);
//            }
//            return list;
//        } catch (Exception e) {
//            throw ServiceException.newInstance(ErrorCodes.JSON_PARSE_ERROR);
//        }
//    }
//
//    @Override
//    public List<TaskTemplateRepository> listTaskTemplate() {
//        return taskTemplateMapper.getTemplate();
//    }

}
