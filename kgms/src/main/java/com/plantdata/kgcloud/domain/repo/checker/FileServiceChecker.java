package com.plantdata.kgcloud.domain.repo.checker;

import com.plantdata.kgcloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/15  11:11
 */
@Slf4j
public class FileServiceChecker implements ServiceChecker {

    private List<String> filePaths;

    public FileServiceChecker(List<String> filePaths) {
        this.filePaths = filePaths;
    }


    @Override
    public void check() {
        List<String> noExist = filePaths.stream().filter(a -> Files.notExists(Paths.get(a))).collect(Collectors.toList());
        if (noExist.size() > 0) {
            log.error("文件不存在:" + noExist.stream().reduce((a, b) -> a + "," + b).orElse(StringUtils.EMPTY));
            throw new BizException("文件不存在");
        }
    }
}
