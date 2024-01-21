package ru.org.sevn.mydata.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.TagEntityRepository;

@Component
public class TagEntityComponent {

    @Autowired
    private TagEntityRepository tagEntityRepository;

    public Collection<TagEntity> getTags (String... tagNames) {
        return getTags (Arrays.stream (tagNames).toList ());
    }

    public Collection<TagEntity> getTags (Iterable<String> tagNames) {
        var newTags = new HashSet<String> ();
        var res = new HashSet<TagEntity> ();
        for (var tn : tagNames) {
            var hasElem = new AtomicBoolean (false);
            var tags = tagEntityRepository.findAll (QTagEntity.tagEntity.value.containsIgnoreCase (tn));
            tags.forEach (e -> {
                hasElem.set (true);
                res.add (e);
            });
            if (! hasElem.get ()) {
                newTags.add (tn);
            }
        }
        tagEntityRepository.saveAll (newTags.stream ().map (n -> new TagEntity ().value (n)).toList ())
                .forEach (e -> res.add (e));
        return res;
    }
}
