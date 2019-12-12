package com.plantdata.kgcloud.domain.menu.entity;

import com.plantdata.kgcloud.domain.common.converter.IntegerListConverter;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/

@Data
@Entity
@Table(name = "menu_favor")
@EntityListeners(AuditingEntityListener.class)
public class MenuFavor {

    @Id
    @Column(name = "user_id")
    @CreatedBy
    private String userId;

    @Basic
    @Column(name = "menu_id")
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> menuId;

    @Basic
    @Column(name = "create_at", updatable = false)
    @CreatedDate
    private Date createAt;

    @Basic
    @Column(name = "update_at")
    @LastModifiedDate
    private Date updateAt;

}
