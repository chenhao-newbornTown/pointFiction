package com.point.mongo;

import com.point.entity.FictionDetailBean;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hadoop on 2017-7-27.
 */
public interface FictionDeatilRepository extends MongoRepository<FictionDetailBean,String> {
}
