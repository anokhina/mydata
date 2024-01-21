package ru.org.sevn.va.upload;

import com.vaadin.flow.component.upload.UploadI18N;
import java.util.Arrays;

public class VaUploadI18N extends UploadI18N {
    public VaUploadI18N () {
        setDropFiles (new DropFiles ().setOne ("Перетащите файл сюда")
                .setMany ("Перетащите файлы сюда"));
        setAddFiles (new AddFiles ().setOne ("Загрузить файл...")
                .setMany ("Загрузить файлы..."));
        setError (new Error ().setTooManyFiles ("Слишком много файлов.")
                .setFileIsTooBig ("Слишком большой файл.")
                .setIncorrectFileType ("Неверный тип файла."));
        setUploading (new Uploading ()
                .setStatus (new Uploading.Status ().setConnecting ("Соединение...")
                        .setStalled ("Приостановлено")
                        .setProcessing ("Обработка файла...").setHeld ("В очереди"))
                .setRemainingTime (new Uploading.RemainingTime ()
                        .setPrefix ("оценочное время загрузки: ")
                        .setUnknown ("невозможно оценить оставшееся время"))
                .setError (new Uploading.Error ()
                        .setServerUnavailable (
                                "Не удалось загрузить файл, попробуйте позже")
                        .setUnexpectedServerError (
                                "Не удалось загрузить из-за ошибки сервера")
                        .setForbidden ("Загрузка завершена")));
        setUnits (new Units ().setSize (Arrays.asList ("B", "kB", "MB", "GB", "TB",
                "PB", "EB", "ZB", "YB")));
    }
}
/*
        setDropFiles(new DropFiles().setOne("Drop file here")
                .setMany("Drop files here"));
        setAddFiles(new AddFiles().setOne("Upload File...")
                .setMany("Upload Files..."));
        setCancel("Cancel");
        setError(new Error().setTooManyFiles("Too Many Files.")
                .setFileIsTooBig("File is Too Big.")
                .setIncorrectFileType("Incorrect File Type."));
        setUploading(new Uploading()
                .setStatus(new Uploading.Status().setConnecting("Connecting...")
                        .setStalled("Stalled")
                        .setProcessing("Processing File...").setHeld("Queued"))
                .setRemainingTime(new Uploading.RemainingTime()
                        .setPrefix("remaining time: ")
                        .setUnknown("unknown remaining time"))
                .setError(new Uploading.Error()
                        .setServerUnavailable(
                                "Upload failed, please try again later")
                        .setUnexpectedServerError(
                                "Upload failed due to server error")
                        .setForbidden("Upload forbidden")));
        setUnits(new Units().setSize(Arrays.asList("B", "kB", "MB", "GB", "TB",
                "PB", "EB", "ZB", "YB")));
*/