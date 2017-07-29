package com.point.mongo;

import com.point.entity.UserInfoBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hadoop on 2017-7-20.
 */
@Repository
public interface UserRepository extends MongoRepository<UserInfoBean,String>{
}
