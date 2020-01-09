package com.plantdata.kgcloud.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * describe about this class
 *
 * @author: DingHao
 * @date: 2019/7/2 13:50
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class SwaggerHeaderPlugin implements OperationBuilderPlugin {

    private TypeResolver resolver;

    public SwaggerHeaderPlugin(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    @Override
    public void apply(OperationContext operationContext) {
        Optional<NoHeader> requestParam = operationContext.findAnnotation(NoHeader.class);
        if (!requestParam.isPresent()) {
            Parameter param = new ParameterBuilder()
                    .parameterType("header")
                    .name("APK")
                    .modelRef(new ModelRef("string"))
                    .type(resolver.resolve(String.class))
                    .build();
            operationContext.operationBuilder().parameters(Lists.newArrayList(param));
        }
    }
}
