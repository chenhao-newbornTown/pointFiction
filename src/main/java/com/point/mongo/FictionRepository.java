package com.point.mongo;

import com.point.entity.FictionBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hadoop on 2017-7-26.
 */
@Repository
public interface FictionRepository extends MongoRepository<FictionBean,String>{
}
