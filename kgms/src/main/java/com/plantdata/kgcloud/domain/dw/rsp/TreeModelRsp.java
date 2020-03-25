package com.plantdata.kgcloud.domain.dw.rsp;

import ai.plantdata.kg.common.bean.BasicInfo;
import com.plantdata.kgcloud.domain.dw.parser.ExcelParser;
import com.plantdata.kgcloud.domain.dw.util.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/8/9 13:08
 */
@Setter
@Getter
public class TreeModelRsp {

    private ExcelParser excelParser;
    private Row row;
    private BasicInfo parent;
    private BasicInfo son;

    public TreeModelRsp(BasicInfo parent, BasicInfo son, Row row, ExcelParser excelParser) {
        this.parent = parent;
        this.son = son;
        this.row = row;
        this.excelParser = excelParser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeModelRsp treeModel = (TreeModelRsp) o;
        return isSame(son, treeModel.son) || (isSame(parent, treeModel.parent) && son.getName().equals(treeModel.getSon().getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, son);
    }

    private boolean isSame(BasicInfo basicInfo, BasicInfo that){
        return basicInfo.getName().equals(that.getName()) && CommonUtil.equals(basicInfo.getMeaningTag(), that.getMeaningTag());
    }
}
