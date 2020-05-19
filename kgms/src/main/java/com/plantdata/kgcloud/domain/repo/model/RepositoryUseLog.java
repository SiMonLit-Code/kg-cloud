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
@Table(name = "repository_use_log")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class RepositoryUseLog {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "repositoryId")
    private int repositoryId;
    @Column(name = "menuId")
    private int menuId;
    @Column(name = "userId")
    private String userId;

    public RepositoryUseLog(int repositoryId, int menuId, String userId) {
        this.repositoryId = repositoryId;
        this.menuId = menuId;
        this.userId = userId;
    }
}
