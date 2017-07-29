package com.point.mongo;

import com.point.entity.UserFictionBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hadoop on 2017-7-27.
 */
@Repository
public interface UserFictionRepository extends MongoRepository<UserFictionBean,String> {
}
