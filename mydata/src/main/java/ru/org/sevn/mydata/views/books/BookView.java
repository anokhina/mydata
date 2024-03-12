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
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.SettingsEntity;
import ru.org.sevn.mydata.repo.BookEntityRepository;
import ru.org.sevn.mydata.repo.SettingsEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.mydata.util.TagEntityComponent;
import ru.org.sevn.mydata.views.files.FileWalker;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.dialog.VaDialog;
import ru.org.sevn.va.textField.VaTextField;

@PageTitle ("Books")
@Route (value = "books")
@Log
public class BookView extends VerticalLayout {

    public static final String NAME_DIR_DATA = "ru.org.sevn.mydata.views.books.dirdata";

    private BookGrid grid;
    private Button addButton = new Button ("Add");
    private Button indexButton = new Button ("Index");
    private final HorizontalLayout buttonPanel = new HorizontalLayout ();

    private final VaTextField dataPath = new VaTextField (new VaTextField.Cfg ()
            .placeholder ("Ввести путь к директории с данными"));

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
        grid = new BookGrid (tagEntityRepository);

        buttonPanel.setWidthFull ();
        buttonPanel.add (addButton, indexButton);

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

        editorAdd = new AddDialog (new BookPanel ( () -> tagEntityRepository, QTagEntity.tagEntity.value));
        editorAdd.addButton ("Save", () -> {
            if (editorAdd.getMessage ().getBinder ().validate ().isOk ()) {
                var entity = modelBuilder.fromModel (editorAdd.getMessage ().getBinder ().getBean ());
                //bookEntityRepository.save(entity);
                dp.refreshAll ();
                return true;
            }
            else {
                return false;
            }
        });

        indexButton.addClickListener (evt -> {

            var op = settingsEntityRepository.findByName (NAME_DIR_DATA);
            if (op.isPresent ()) {
                var pathData = Path.of (op.get ().getValue (), "data");
                var fileData = pathData.toFile ();
                if (fileData.isDirectory () && fileData.exists ()) {

                    var fileProcessor = new BookFileProcessor (pathData, bookEntityRepository, tagEntityComponent);
                    final FileWalker fileWalker = new FileWalker (fileProcessor, ".exclude"/*, BookFileProcessor.FILE_NAME_INDEXED*/);

                    try {
                        Files.walkFileTree (pathData, fileWalker);
                    }
                    catch (IOException ex) {
                        log.log (Level.SEVERE, null, ex);
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
            editorAdd.getMessage ().getBinder ().setBean (new BookModel ());
            editorAdd.open ();
        });

        grid.setControlClick ( (lm, evt) -> {
            editorAdd.setTitle ("Редактирование");
            editorAdd.getMessage ().getBinder ().setBean (lm);
            editorAdd.open ();
        });
    }
}
