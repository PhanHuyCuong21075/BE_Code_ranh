package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.repo.custom.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long>, FriendRepositoryCustom {
}