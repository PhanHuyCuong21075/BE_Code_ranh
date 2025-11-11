package com.cuongph.be_code.repo.custom.query;

public class UserRepositoryQuery {

    protected String sqlSuggestedFriends() {
        return """
        SELECT u.*
        FROM users u
        WHERE u.id <> :userId
          AND u.id NOT IN (
              SELECT CASE 
                        WHEN f.requester_id = :userId THEN f.receiver_id
                        ELSE f.requester_id
                     END
              FROM friends f
              WHERE (f.requester_id = :userId OR f.receiver_id = :userId)
                AND f.status IN ('PENDING','ACCEPTED')
          )
    """;
    }

}
