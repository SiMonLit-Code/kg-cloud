package com.plantdata.kgcloud.domain.menu.repository;

import com.plantdata.kgcloud.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface MenuRepository extends JpaRepository<Menu, String> {
}
