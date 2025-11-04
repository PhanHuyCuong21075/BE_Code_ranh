package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.PostEntity;
import com.cuongph.be_code.repo.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {
}