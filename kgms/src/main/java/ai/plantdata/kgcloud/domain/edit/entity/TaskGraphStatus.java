package ai.plantdata.kgcloud.domain.edit.entity;

import ai.plantdata.kgcloud.domain.edit.converter.JpaMapConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 17:39
 * @Description:
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="task_graph_status")
@EntityListeners(AuditingEntityListener.class)
public class TaskGraphStatus {

    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "kg_name")
    private String kgName;


    @Column(name = "params")
    @Convert(converter = JpaMapConverter.class)
    private Map<String,Object> params;

    @Basic
    @Column(name = "task_type")
    private String type;

    @Basic
    @Column(name = "task_status")
    private String status;

    @Basic
    @CreatedDate
    @Column(name = "create_at")
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;
}
