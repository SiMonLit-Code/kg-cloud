package com.plantdata.kgcloud.domain.repo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author cjw
 * @date 2020/5/18  14:49
 */
@Getter
@Setter
@Entity
@Table(name = "repo_repository_use_log")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class RepositoryUseLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "repository_id")
    private int repositoryId;
    @Column(name = "menu_id")
    private int menuId;
    @Column(name = "user_id")
    private String userId;

    public RepositoryUseLog(int repositoryId, int menuId, String userId) {
        this.repositoryId = repositoryId;
        this.menuId = menuId;
        this.userId = userId;
    }
}
