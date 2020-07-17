package ai.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.cloud.util.JacksonUtils;
import ai.plantdata.kgcloud.sdk.rsp.app.explore.ImageRsp;
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
    private static final String THUMB_NAIL = "thumbPath";

    public static Optional<ImageRsp> stringT0Image(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return Optional.empty();
        }
        try {
            Map<String, String> map = JacksonUtils.getInstance().readValue(imageUrl, Map.class);
            String href = map.get(HREF);
            String name = map.get(NAME);
            String thumbnail = map.get(THUMB_NAIL);
            if (!StringUtils.isAllEmpty(href, name, thumbnail)) {
                return Optional.of(new ImageRsp(name, href, thumbnail));
            }
        } catch (IOException e) {
            return Optional.of(new ImageRsp("image", imageUrl, imageUrl));
        }
        return Optional.empty();
    }
}
