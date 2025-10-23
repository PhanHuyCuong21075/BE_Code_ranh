package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
    SELECT p FROM Post p
    WHERE (p.user.id IN :userIds AND p.isPublic = 1)
       OR (p.user.id = :userId)
    ORDER BY p.createdAt DESC
    """)
    List<Post> findPostsByUserIds(@Param("userIds") List<Long> userIds, @Param("userId") Long userId);


    @Query("""
    SELECT p FROM Post p
    WHERE p.isPublic = 1 OR p.user.id = :userId
    ORDER BY p.createdAt DESC
    """)
    List<Post> findAllPublicPostsAndUser(@Param("userId") Long userId);

}
