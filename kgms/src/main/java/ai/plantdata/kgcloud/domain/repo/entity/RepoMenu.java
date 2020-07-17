package ai.plantdata.kgcloud.domain.repo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author cjw
 * @date 2020/5/21  18:49
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "repo_menu")
public class RepoMenu {

    @Id
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "repository_id")
    private Integer repositoryId;

    public RepoMenu(Integer menuId, Integer repositoryId) {
        this.menuId = menuId;
        this.repositoryId = repositoryId;
    }
}
