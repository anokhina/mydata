package ru.org.sevn.mydata.views.books;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.org.sevn.mddata.FileIndexer;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.SettingsEntity;
import ru.org.sevn.mydata.entity.StringLongObject;
import ru.org.sevn.mydata.repo.BookEntityRepository;
import ru.org.sevn.mydata.repo.SettingsEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.mydata.util.TagEntityComponent;
import ru.org.sevn.mydata.views.files.FileWalker;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.dialog.VaDialog;
import ru.org.sevn.va.dialog.VaTextDialog;
import ru.org.sevn.va.textField.VaTextField;
import static ru.org.sevn.log.LogUtil.*;

@PageTitle ("Books")
@Route (value = "books")
@Log
public class BookView extends VerticalLayout {

    public static final String NAME_DIR_DATA = "ru.org.sevn.mydata.views.books.dirdata";

    private BookGrid grid;
    private Button addButton = new Button ("Add");
    private Button indexButton = new Button ("Index");
    private Button indexAllButton = new Button ("IndexAll");
    private final HorizontalLayout buttonPanel = new HorizontalLayout ();

    private final VaTextField dataPath = new VaTextField (new VaTextField.Cfg ()
            .placeholder ("Ввести путь к директории с данными"));

    private String generatePathId () {
        var sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
        return sdf.format (new Date ());
    }

    static class AddDialog extends VaDialog<BookPanel> {
        public AddDialog (BookPanel bp) {
            super ("Book", bp);
            addCancel ( () -> true);
            setSizeFull ();
        }
    }

    private AddDialog editorAdd;

    @Autowired
    public BookView (
            MongoTemplate mongoTemplate,
            BookEntityRepository bookEntityRepository,
            SettingsEntityRepository settingsEntityRepository,
            TagEntityRepository tagEntityRepository,
            TagEntityComponent tagEntityComponent) {
        grid = new BookGrid (new BookGrid.Ctx ()
                .tagEntityRepository (tagEntityRepository)
                .supplierDataDir ( () -> pathData (settingsEntityRepository))
        //
        );

        buttonPanel.setWidthFull ();
        buttonPanel.add (addButton, indexButton, indexAllButton);

        add (dataPath, buttonPanel, grid);

        {
            {
                var op = settingsEntityRepository.findByName (NAME_DIR_DATA);
                if (op.isPresent ()) {
                    dataPath.setValue (op.get ().getValue ());
                }
            }
            dataPath.getControlButtons ().add (new Button ("Save", evt -> {
                var filePath = Path.of (dataPath.getValue ());
                var file = filePath.toFile ();
                if (file.exists () && file.isDirectory ()) {
                    var ent = settingsEntityRepository.findByName (NAME_DIR_DATA).orElseGet ( () -> new SettingsEntity ().name (NAME_DIR_DATA));
                    ent.setValue (dataPath.getValue ());
                    settingsEntityRepository.save (ent);
                    Notification.show ("dir: " + filePath);
                }
                else {
                    Notification.show ("Can't find dir: " + filePath);
                }
            }));
        }

        var modelBuilder = new BookBookModelBuilder ();

        var dp = new ModelRepositoryDataProvider<BookEntity, BookModel, BookEntityRepository, BookEntityQdslFilter> (
                "bookEntity",
                BookEntity.class,
                mongoTemplate,
                bookEntityRepository, modelBuilder);
        grid.setItems (dp);
        //grid.setSizeFull();
        grid.setHeight ("90vh");

        editorAdd = new AddDialog (new BookPanel (new BookPanel.Ctx ()
                .tagEntityRepository ( () -> tagEntityRepository)
                .entityQpath (QTagEntity.tagEntity.value)
                .supplierDataDir ( () -> pathData (settingsEntityRepository))
        //
        ));
        editorAdd.addButton ("Save", () -> {
            if (editorAdd.getMessage ().getBinder ().validate ().isOk ()) {
                var pathData = pathData (settingsEntityRepository);
                if (pathData != null && dirExists (pathData)) {
                    var entity = modelBuilder.fromModel (editorAdd.getMessage ().getBinder ().getBean ());
                    var isNew = (entity.getId () == null);

                    try {
                        var entityDirPath = Path.of (pathData.toString (), entity.pathId ());
                        if (isNew) {
                            BookFileProcessor.writeIndexed (pathData, entity, "index.md");
                            new VaTextDialog ("Создано описание в " + entityDirPath).open ();
                        }
                        else {
                            var mapcs = FileIndexer.getContentSizes (entityDirPath, entity.getContent ().split ("\n"));
                            entity.getContentSize ().clear ();
                            mapcs.forEach ( (k, v) -> {
                                entity.getContentSize ().add (new StringLongObject (k, v));
                            });
                            bookEntityRepository.save (entity);
                            BookFileProcessor.writeIndexed (pathData, entity);
                        }
                    }
                    catch (IOException ex) {
                        error (log, null, ex);
                        Notification.show ("Can't write indexed data for: " + entity.getPathId ());
                    }
                }
                else {
                    Notification.show ("Can't find settings: " + NAME_DIR_DATA);
                }

                dp.refreshAll ();
                return true;
            }
            else {
                return false;
            }
        });

        indexButton.addClickListener (evt -> {
            var tf = new VaTextField (new VaTextField.Cfg ().placeholder ("имя-путь директории, которую надо индексировать"));
            new VaDialog<VaTextField> ("Проиндексировать", tf)
                    .configure (d -> {

                        d.addCancel ( () -> true);
                        d.addButtonOk ( () -> {
                            try {
                                var dirName = tf.getValue ().replace ("\\", "");
                                var dirPath = Path.of (dirName);

                                var pathData = pathData (settingsEntityRepository);
                                var fileProcessor = new BookFileProcessor (pathData, bookEntityRepository, tagEntityComponent);
                                fileProcessor.setDirExact (dirPath);
                                final FileWalker fileWalker = new FileWalker (fileProcessor);

                                Files.walkFileTree (dirPath, fileWalker);

                                return true;
                            }
                            catch (Exception ex) {
                                Notification.show ("Can't index: " + tf.getValue ());
                                error (log, null, ex);
                                return false;
                            }
                        });
                    }).open ();
        });

        indexAllButton.addClickListener (evt -> {

            var pathData = pathData (settingsEntityRepository);
            if (pathData != null) {
                if (dirExists (pathData)) {

                    var fileProcessor = new BookFileProcessor (pathData, bookEntityRepository, tagEntityComponent);
                    final FileWalker fileWalker = new FileWalker (fileProcessor, ".exclude", BookFileProcessor.FILE_NAME_INDEXED);

                    try {
                        Files.walkFileTree (pathData, fileWalker);
                    }
                    catch (IOException ex) {
                        error (log, null, ex);
                    }
                }
                else {
                    Notification.show ("Can't index data in dir: " + pathData + ". Doesn't exist.");
                }
            }
            else {
                Notification.show ("Can't find settings: " + NAME_DIR_DATA);
            }

        });

        addButton.addClickListener (evt -> {
            editorAdd.setTitle ("Новый");
            editorAdd.getMessage ().getBinder ().setBean (
                    new BookModel ()
                            .pathId (generatePathId ())
                            .img ("img.png")
            //
            );
            editorAdd.open ();
        });

        grid.setControlClick ( (lm, evt) -> {
            editorAdd.setTitle ("Редактирование");
            editorAdd.getMessage ().getBinder ().setBean (lm);
            editorAdd.open ();
        });
    }

    private static boolean dirExists (Path pathData) {
        var fileData = pathData.toFile ();
        return (fileData.isDirectory () && fileData.exists ());
    }

    private Path pathData (SettingsEntityRepository settingsEntityRepository) {
        var op = settingsEntityRepository.findByName (NAME_DIR_DATA);
        if (op.isPresent ()) {
            var pathData = Path.of (op.get ().getValue (), "data");
            return pathData;
        }
        else {
            return null;
        }
    }
}
