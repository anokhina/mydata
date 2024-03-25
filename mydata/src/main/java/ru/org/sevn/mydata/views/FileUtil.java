package ru.org.sevn.mydata.views;

import com.vaadin.flow.component.notification.Notification;
import lombok.extern.java.Log;
import ru.org.sevn.function.ThrowableSupplier;
import ru.org.sevn.log.LogUtil;
import ru.org.sevn.mydata.sys.FileOpener;

@Log
public class FileUtil {

    public static void open (String path) {
        open ( () -> path);
    }

    public static void open (ThrowableSupplier<Throwable, String> pathSupp) {
        open (new FileOpener (), pathSupp);
    }

    public static void open (FileOpener fo, ThrowableSupplier<Throwable, String> pathSupp) {
        String path = null;
        try {
            path = pathSupp.get ();

            var err = fo.dir (path);
            if (err != null) {
                Notification.show ("Error: can't open " + path + ". " + err);
            }
        }
        catch (Throwable ex) {
            Notification.show ("Error: can't open " + path);
            LogUtil.error (log, ex);
        }
    }
}
