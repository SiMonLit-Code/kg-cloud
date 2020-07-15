package ai.plantdata.kgcloud.domain.repo.enums;

/**
 * 组件检查类型
 */
public enum RepoCheckType {

    /**
     * 通过consul检查
     */
    CONSUL,

    /**
     * 通过mongo数据库 检查
     */
    MONGO,

    /**
     * 通过文件检查
     */
    FILE,
    ;
}