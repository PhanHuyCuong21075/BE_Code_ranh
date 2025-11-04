package com.cuongph.be_code.repo.custom;

import com.cuongph.be_code.entity.PostEntity;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostEntity> findPostsByUserIds(List<Long> userIds, Long userId);

    List<PostEntity> findAllPublicPostsAndUser(Long userId);
}