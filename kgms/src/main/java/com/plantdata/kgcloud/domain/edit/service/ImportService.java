package com.plantdata.kgcloud.domain.edit.service;

import com.plantdata.kgcloud.domain.edit.req.upload.ImportTemplateReq;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LinHo
 * @Date: 2019/12/2 10:02
 * @Description:
 */
public interface ImportService {

    /**
     * 下载导入模板
     *
     * @param kgName
     * @param importTemplateReq
     * @param response
     */
    void getImportTemplate(String kgName, ImportTemplateReq importTemplateReq, HttpServletResponse response);

    /**
     * 导入概念
     *
     * @param kgName
     * @param file
     * @return
     */
    String importConcepts(String kgName, MultipartFile file);

    /**
     * 导入实体
     *
     * @param kgName
     * @param conceptId
     * @param file
     * @return
     */
    String importEntities(String kgName, Long conceptId, MultipartFile file);

    /**
     * 导入同义词
     *
     * @param kgName
     * @param file
     * @return
     */
    String importSynonyms(String kgName, MultipartFile file);

    /**
     * 导入属性定义
     *
     * @param kgName
     * @param type   0:数值,1:对象
     * @param file
     * @return
     */
    String importAttrDefinition(String kgName, Integer type, MultipartFile file);

    /**
     * 导入领域词
     *
     * @param kgName
     * @param conceptId
     * @param file
     * @return
     */
    String importDomain(String kgName, Long conceptId, MultipartFile file);

    /**
     * 导入关系
     *
     * @param kgName
     * @param mode   0:忽略不存在的实例,1:实体不存在则新增
     * @param file
     * @return
     */
    String importRelation(String kgName, Integer mode, MultipartFile file);

    /**
     * 导入特定关系
     *
     * @param kgName
     * @param attrId 属性id
     * @param mode
     * @param file
     * @return
     */
    String importRelation(String kgName, Integer attrId, Integer mode, MultipartFile file);

    /**
     * rdf导入
     *
     * @param kgName
     * @param file
     * @param format
     * @return
     */
    String importRdf(String kgName, MultipartFile file, String format);

    /**
     * rdf导出
     *
     * @param kgName
     * @param format
     * @param scope
     * @return
     */
    String exportRdf(String kgName, String format, Integer scope);

    /**
     * 实体概念模型导出
     * @param kgName
     * @param response
     */
    void exportEntity(String kgName, HttpServletResponse response);

}
