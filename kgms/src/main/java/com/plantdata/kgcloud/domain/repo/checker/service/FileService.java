package com.plantdata.kgcloud.domain.repo.checker.service;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/15  12:08
 */
public interface FileService extends Service {

    List<String> filePaths();
}
