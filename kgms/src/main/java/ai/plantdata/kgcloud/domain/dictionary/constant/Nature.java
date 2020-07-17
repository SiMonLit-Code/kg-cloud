package ai.plantdata.kgcloud.domain.dictionary.constant;

import java.util.Objects;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-05 18:07
 **/
public enum Nature {
    /**
     *
     */
    NOUN("n", "名词"),
    VERB("v", "动词"),
    ADJECTIVE("a", "形容词"),
    ADVERB("d", "副词"),
    PREPOSITION("p", "介词"),
    EXCLAMATION("e", "叹词"),
    TIMEWORD("t", "时间词");


    private String type;
    private String show;

    Nature(String type, String show) {
        this.type = type;
        this.show = show;
    }

    public static String toType(String s) {
        for (Nature e : Nature.values()) {
            if (Objects.equals(s, e.type()) || Objects.equals(s, e.show())) {
                return e.type();
            }
        }
        return s;
    }

    public static String toShow(String s) {
        for (Nature e : Nature.values()) {
            if (Objects.equals(s, e.type()) || Objects.equals(s, e.show())) {
                return e.show();
            }
        }
        return s;
    }

    public String type() {
        return type;
    }

    public String show() {
        return show;
    }
}
