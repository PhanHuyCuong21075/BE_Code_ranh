package com.cuongph.be_code.repo;

import com.cuongph.be_code.dto.response.CommentsResponse;
import com.cuongph.be_code.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("""
                SELECT new com.cuongph.be_code.dto.response.CommentsResponse(
                    c.id,
                    u.username,
                    c.content,
                    c.imageUrl,
                    c.createdAt
            
                )
                FROM CommentEntity c
                JOIN UserEntity u ON u.id = c.userId
                WHERE c.postId = :postId
                ORDER BY c.createdAt DESC
            """)
    List<CommentsResponse> findCommentsByPostId(
            @Param("postId") Long postId
    );


}
