package me.oblomov.househunter.db.dao;

import me.oblomov.househunter.db.model.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseDAO extends JpaRepository<HouseEntity, Long>, JpaSpecificationExecutor<HouseEntity> {
}
