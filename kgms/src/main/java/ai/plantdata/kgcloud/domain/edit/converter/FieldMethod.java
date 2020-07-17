package ai.plantdata.kgcloud.domain.edit.converter;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * @Author: LinHo
 * @Date: 2019/11/11 13:58
 * @Description:
 */
@Setter
@Getter
public class FieldMethod {

    private String name;

    private Method method;

}
