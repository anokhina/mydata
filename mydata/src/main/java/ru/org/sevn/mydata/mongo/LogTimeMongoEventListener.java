package ru.org.sevn.mydata.mongo;

import org.springframework.stereotype.Component;
import ru.org.sevn.entity.DbEntity;
import ru.org.sevn.mongo.listener.BasicLogTimeMongoEventListener;

@Component
public class LogTimeMongoEventListener extends BasicLogTimeMongoEventListener<DbEntity> {

}
