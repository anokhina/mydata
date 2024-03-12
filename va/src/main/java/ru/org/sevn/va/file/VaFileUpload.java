package ru.org.sevn.va.file;

import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import java.io.InputStream;
import lombok.Getter;

public class VaFileUpload extends Upload {

    @Getter
    private SucceededEvent succeededEvent;

    public VaFileUpload () {
        super (new MemoryBuffer ());

        addSucceededListener (evt -> {
            succeededEvent = evt;
        });
    }

    public MemoryBuffer getFileBuffer () {
        return (MemoryBuffer) getReceiver ();
    }

    public String getFileName () {
        if (succeededEvent != null) {
            return succeededEvent.getFileName ();
        }
        return null;
    }

    public InputStream getInputStream () {
        if (succeededEvent != null) {
            var buffer = getFileBuffer ();

            return buffer.getInputStream ();
        }
        return null;
    }
}
