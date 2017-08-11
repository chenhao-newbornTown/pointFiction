package com.point.mongo;

import com.point.entity.PicBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hadoop on 2017-8-7.
 */
@Repository
public interface PicRepostitory extends MongoRepository<PicBean,String> {
}
