package ai.plantdata.kgcloud.domain.menu.repository;

import ai.plantdata.kgcloud.domain.menu.entity.MenuFavor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-04 18:45
 **/
public interface MenuFavorRepository extends JpaRepository<MenuFavor, String> {
}
