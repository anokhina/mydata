package ru.org.sevn.mydata.util;

import org.springframework.stereotype.Component;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import java.util.UUID;

@Component
public class UUIDComponent {
    private final TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator ();

    public UUID getTimeBased () {
        return timeBasedGenerator.generate ();
    }
}
