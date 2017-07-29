package com.point.config;

import java.lang.reflect.Field;
import com.point.entity.AutoIncKey;
import com.point.entity.SequenceId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Created by hadoop on 2017-7-20.
 */

@Component
public class SaveMongoEventListener extends AbstractMongoEventListener<Object>{

    protected static Logger logger = LoggerFactory.getLogger(SaveMongoEventListener.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(final Object source) {
        logger.info("run onBeforeConvert--->"+source.getClass().getSimpleName());
        if (source != null) {
            ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    // 如果字段添加了我们自定义的AutoIncKey注解
                    if (field.isAnnotationPresent(AutoIncKey.class)) {
                        // 设置自增ID
                        field.set(source, getNextId(source.getClass().getSimpleName()));
                    }
                }
            });
        }
    }

        /**
         * 获取下一个自增ID
         * @param collName  集合名
         * @return
         */
    public Long getNextId(String collName) {

        logger.info("run getNextId method --->"+collName);
        Query query = new Query(Criteria.where("coll_name").is(collName));
        Update update = new Update();
        update.inc("seq_id", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        return seqId.getSeq_id();
    }


}
