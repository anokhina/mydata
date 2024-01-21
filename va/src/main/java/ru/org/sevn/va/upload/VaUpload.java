package ru.org.sevn.va.upload;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;

public class VaUpload extends Upload {

    public static final int KB = 1024;
    public static final int MB = 1024 * 1024;
    public static final int GB = 1024 * 1024 * 1024;

    @Data
    @AllArgsConstructor
    public static class FileLoaded<T extends Receiver> {
        private T receiver;
        private SucceededEvent event;
    }

    public static class OneFileLoaded extends FileLoaded<MemoryBuffer> {

        public OneFileLoaded (MemoryBuffer receiver, SucceededEvent event) {
            super (receiver, event);
        }
    }

    public VaUpload () {
        super ();
    }

    public static final <T extends Upload> T getOneFileUpload (T upload, Integer maxFileSizeInBytes, Consumer<OneFileLoaded> onLoad) {
        MemoryBuffer buffer = new MemoryBuffer ();
        upload.setReceiver (buffer);
        System.out.println ("------------------------" + maxFileSizeInBytes);
        if (maxFileSizeInBytes != null) {
            upload.setMaxFileSize (maxFileSizeInBytes);
        }
        var i18N = new VaUploadI18N ();
        upload.setI18n (i18N);
        upload.setDropAllowed (true);
        upload.setAutoUpload (false);

        upload.addFileRejectedListener (event -> {
            String errorMessage = event.getErrorMessage ();

            Notification notification = Notification.show (errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants (NotificationVariant.LUMO_ERROR);
        });

        upload.addSucceededListener (event -> {
            // Get information about the uploaded file
            /*
            InputStream fileData = buffer.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();
            */

            // Do something with the file data
            onLoad.accept (new OneFileLoaded (buffer, event));
        });

        return upload;
    }
}
