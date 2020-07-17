package ai.plantdata.kgcloud.domain.dictionary.service;


import ai.plantdata.cloud.bean.BaseReq;
import ai.plantdata.kgcloud.sdk.req.WordReq;
import ai.plantdata.kgcloud.sdk.rsp.WordRsp;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 14:59
 **/
public interface WordService {
    /**
     * 词条全查找
     *
     * @param dictId
     * @return
     */
    List<WordRsp> findAll(String userId, Long dictId);

    /**
     * 词条分页查找
     *
     * @param dictId
     * @return
     */
    Page<WordRsp> findAll(String userId, Long dictId, BaseReq baseReq);

    /**
     * 词条按照id查找
     *
     * @param dictId
     * @param id
     * @return
     */
    WordRsp findById(String userId, Long dictId, String id);

    /**
     * 词条删除
     *
     * @param dictId
     * @param id
     * @return
     */
    void delete(String userId, Long dictId, String id);

    /**
     * 词条新增
     *
     * @param r
     * @return
     */
    WordRsp insert(String userId, Long dictId, WordReq r);

    /**
     * 词条更新
     *
     * @param dictId
     * @param id
     * @param r
     * @return
     */
    WordRsp update(String userId, Long dictId, String id, WordReq r);

    /**
     * 导出词典
     *
     * @param dictId
     * @param response
     */
    void exportWord(String userId, Long dictId, HttpServletResponse response);

    /**
     * 词典导入
     *
     * @param userId
     * @param dictId
     * @param file
     * @throws Exception
     */
    void importWord(String userId, Long dictId, MultipartFile file) throws Exception;
}
