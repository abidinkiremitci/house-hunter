package me.oblomov.househunter.db.dao;

import me.oblomov.househunter.db.model.HouseFilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseFilterDAO extends JpaRepository<HouseFilterEntity, Long>, JpaSpecificationExecutor<HouseFilterEntity> {
}
