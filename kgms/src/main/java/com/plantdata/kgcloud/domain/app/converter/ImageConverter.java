package com.plantdata.kgcloud.domain.app.converter;

import com.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/28 10:40
 */
public class ImageConverter {

    private static final String HREF = "href";
    private static final String NAME = "name";

    public static Optional<ImageRsp> stringT0Image(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return Optional.empty();
        }
        try {
            Map<String, String> map = JacksonUtils.getInstance().readValue(imageUrl, Map.class);
            String href = map.get(HREF);
            String name = map.get(NAME);
            if (!StringUtils.isAnyEmpty(href, name)) {
                return Optional.of(new ImageRsp(name, href));
            }
        } catch (IOException e) {
            return Optional.of(new ImageRsp("image", imageUrl));
        }
        return Optional.empty();
    }
}
