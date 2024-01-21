package ru.org.sevn.mongo.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import ru.org.sevn.entity.DbEntity;

public abstract class BasicLogTimeMongoEventListener<E extends DbEntity> extends AbstractMongoEventListener<E> {

    protected BasicLogTimeMongoEventListener () {
    }

    @Override
    public void onBeforeConvert (BeforeConvertEvent<E> event) {
        super.onBeforeConvert (event);

        if (event.getSource ().getCreateTime () == null) {
            event.getSource ().setCreateTime (System.currentTimeMillis ());
        }

        event.getSource ().setUpdateTime (System.currentTimeMillis ());
    }
}
