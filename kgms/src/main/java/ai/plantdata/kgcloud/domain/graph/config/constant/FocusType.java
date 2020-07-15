package ai.plantdata.kgcloud.domain.graph.config.constant;

import ai.plantdata.cloud.exception.BizException;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.Set;

/**
 * @author jiangdeming
 * @date 2019/11/29
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FocusType {



    /**
     * 传输类型
     */
    graph("graph", "图探索", 1),
    path("path", "路径发现", 2),
    relation("relation", "关联分析", 2),
    timing("timing", "时序图探索", 1),
    pathtiming("pathtiming", "时序路径发现", 2),
    relationtiming("relationtiming", "时序关联关系", 2),
    explore("explore", "图谱分析Pro", 1);
    private static  final Set<FocusType> TWO_FOCUS = Sets.newHashSet(relation,path,pathtiming,relationtiming) ;
    private String code;
    private String desc;
    private int minSize;


    public static void check(int entitySize, String type) {
        Optional<FocusType> focusOpt = parseByName(type);
        if (!focusOpt.isPresent()) {
            throw new BizException(120624,"焦点不能为空");
        }
        FocusType focusType = focusOpt.get();
        boolean success = entitySize != 0 && TWO_FOCUS.contains(focusType) && entitySize < focusType.minSize;
        if (success) {
            throw new BizException(120625, focusType.desc + "至少设置" + focusType.minSize + "个节点数");
        }
    }


    public static Optional<FocusType> parseByName(String dataType) {
        for (FocusType value : FocusType.values()) {
            if (value.getCode().equals(dataType)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public static FocusType findType(String dataType) {
        for (FocusType value : FocusType.values()) {
            if (value.getCode().equals(dataType)) {
                return value;
            }
        }
        return FocusType.graph;
    }

    public static boolean contains(String dataType) {
        for (FocusType value : FocusType.values()) {
            if (value.name().equals(dataType)) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }
}