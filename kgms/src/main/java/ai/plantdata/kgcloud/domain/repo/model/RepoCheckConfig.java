package ai.plantdata.kgcloud.domain.repo.model;

import ai.plantdata.kgcloud.domain.repo.enums.RepoCheckType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/18  16:20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepoCheckConfig  {

    private RepoCheckType checkType;
    private String content;
}
