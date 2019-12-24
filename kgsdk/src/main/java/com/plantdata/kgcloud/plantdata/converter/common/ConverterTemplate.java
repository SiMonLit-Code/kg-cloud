package com.plantdata.kgcloud.plantdata.converter.common;

import com.plantdata.kgcloud.bean.ApiReturn;

import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/24 17:02
 */
public class ConverterTemplate<NT, NR, OT, OR> {

    private Function<OT, NT> paramFunction;
    private Function<NT, ApiReturn<NR>> returnFunction;
    private Function<NR, OR> rspFunction;

    public static <NT, NR, OT, OR> ConverterTemplate<NT, NR, OT, OR> factory(Function<OT, NT> paramFunction, Function<NT, ApiReturn<NR>> returnFunction, Function<NR, OR> rspFunction) {
        ConverterTemplate<NT, NR, OT, OR> template = new ConverterTemplate<>();
        template.paramFunction = paramFunction;
        template.returnFunction = returnFunction;
        template.rspFunction = rspFunction;
        return template;
    }

    public OR execute(OT param) {
        return returnFunction
                .compose(paramFunction)
                .andThen(a -> BasicConverter.convert(a, rspFunction))
                .apply(param);
    }
}
