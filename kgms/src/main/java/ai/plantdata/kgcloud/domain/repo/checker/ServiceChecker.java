package ai.plantdata.kgcloud.domain.repo.checker;


/**
 * @author cjw
 * @date 2020/5/15  11:09
 */
public interface ServiceChecker {



    /**
     * 检测插件服务/数据库/模型文件是否存在
     */
    boolean check();
}
