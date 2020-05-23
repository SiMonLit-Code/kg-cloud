package com.plantdata.kgcloud.domain.repo.entity;

import com.plantdata.kgcloud.domain.repo.converter.RepositoryLogTypeConverter;
import com.plantdata.kgcloud.domain.repo.enums.RepositoryLogEnum;
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
@Table(name = "repo_use_log")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class RepoUseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "click_id")
    private Integer clickId;
    @Column(name = "type")
    @Convert(converter = RepositoryLogTypeConverter.class)
    private RepositoryLogEnum logType;
    @Column(name = "user_id")
    private String userId;

    public RepoUseLog(Integer clickId, RepositoryLogEnum logType, String userId) {
        this.clickId = clickId;
        this.userId = userId;
        this.logType = logType;
    }
}
