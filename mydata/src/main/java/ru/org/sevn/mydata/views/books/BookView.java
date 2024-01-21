package ru.org.sevn.mydata.views.books;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.repo.BookEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.mydata.views.files.FileProcessor;
import ru.org.sevn.mydata.views.files.FileWalker;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.dialog.VaMessageDialog;

@PageTitle ("Books")
@Route (value = "books")
@Log
public class BookView extends VerticalLayout {
    private BookGrid grid;
    private Button addButton = new Button ("Add");
    private Button indexButton = new Button ("Index");

    static class AddDialog extends VaMessageDialog<BookPanel> {
        public AddDialog (BookPanel bp) {
            super ("Book", bp);
            addCancel ( () -> true);
            setSizeFull ();
        }
    }

    private AddDialog editorAdd;

    @RequiredArgsConstructor
    public static class MyFileProcessor implements FileProcessor {
        private final BookEntityRepository bookEntityRepository;

        @Override
        public FileVisitResult processFile (Path filePath, BasicFileAttributes attrs) throws Exception {
            if (! attrs.isDirectory ()) {
                var file = filePath.toFile ();
                if (file.getName ().equalsIgnoreCase ("index.md")) {
                    ///////////////////////////////////
                }
            }
            return java.nio.file.FileVisitResult.CONTINUE;
        }
    }

    @Autowired
    public BookView (
            MongoTemplate mongoTemplate,
            BookEntityRepository bookEntityRepository,
            TagEntityRepository tagEntityRepository) {
        grid = new BookGrid (tagEntityRepository);
        add (addButton, indexButton, grid);

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
            var fileProcessor = new MyFileProcessor (bookEntityRepository);
            final FileWalker fileWalker = new FileWalker (fileProcessor, ".exclude");
            /*
            try {
                Files.walkFileTree (ppath, fileWalker);
            }
            catch (IOException ex) {
                log.log (Level.SEVERE, null, ex);
            }
            */
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
