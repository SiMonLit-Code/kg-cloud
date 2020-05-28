package com.plantdata.kgcloud.domain.repo.entity;

import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;

/**
 * @author cjw
 * @date 2020/5/15  11:15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repo_handler")
public class RepoHandler {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "interface_id")
    private int interfaceId;


    @Column(name = "repo_id")
    private int repoId;

    @Column(name = "rank")
    private int rank;

    @Column(name = "handle_condition")
    private String handleCondition;

    @Column(name = "handle_type")
    private String handleType;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_server_name")
    private String requestServerName;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "remark")
    private String remark;


}
