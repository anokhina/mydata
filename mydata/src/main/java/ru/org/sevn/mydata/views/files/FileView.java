package ru.org.sevn.mydata.views.files;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.org.sevn.mydata.entity.FileEntity;
import ru.org.sevn.mydata.repo.FileEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.va.VaUtil;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.data.NoModelConverter;
import ru.org.sevn.va.dialog.VaMessageDialog;

@PageTitle ("Files")
@Route (value = "files")
@Log
public class FileView extends VerticalLayout implements HasUrlParameter<String> {
    private FileGrid grid;
    private Button addButton = new Button ("Add");
    private Button statButton = new Button ("Status");
    private Button toggleButton = new Button ("Done");
    private Button selectButton = new Button ("SEL");
    private TextField ftsFilter = new TextField ();
    private CompletableFuture<Void> walkResult;

    public static class MyFileProcessor implements FileProcessor {

        private final FileEntityRepository fileEntityRepository;

        MyFileProcessor (final FileEntityRepository fileEntityRepository) {
            this.fileEntityRepository = fileEntityRepository;
        }

        @Override
        public FileVisitResult processFile (Path filePath, BasicFileAttributes attrs) throws Exception {
            if (! attrs.isDirectory ()) {

                var p = filePath.toString ();

                //fileEntityRepository.findByPath(p);

                var fe = new FileEntity ();

                var file = filePath.toFile ();
                var ext = FilenameUtils.getExtension (file.getName ());
                fe.path (p)
                        .name (file.getName ())
                        .size (attrs.size ())
                        .ext (ext);
                var words = new ArrayList<String> ();
                var nc = filePath.getNameCount ();
                for (var i = 0; i < nc; i++) {
                    var w = filePath.getName (i).toString ();
                    var e = FilenameUtils.getExtension (w);
                    if (e.length () < 6) {
                        w = FilenameUtils.getBaseName (w);
                    }

                    var arr = w.split ("[_,\\;\\.\\-]");
                    for (var s : arr) {
                        words.add (s);
                    }
                }

                fe.dsc (words.stream ().collect (Collectors.joining (" ")));
                System.out.println ("++++++++++++++++++++++++++" + filePath);
                System.out.println ("+++++++++++++++++++++++++>" + fe);
                try {
                    fileEntityRepository.save (fe);
                }
                catch (Exception ex) {
                    ex.printStackTrace ();
                }

            }
            return java.nio.file.FileVisitResult.CONTINUE;
        }
    }

    private final FileEntityRepository fileEntityRepository;

    @Autowired
    public FileView (
            MongoTemplate mongoTemplate,
            FileEntityRepository fileEntityRepository,
            TagEntityRepository tagEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
        grid = new FileGrid (tagEntityRepository);
        ftsFilter.setClearButtonVisible (true);
        ftsFilter.setWidthFull ();
        var controls = new HorizontalLayout (addButton, statButton, toggleButton, selectButton, ftsFilter);
        controls.setDefaultVerticalComponentAlignment (Alignment.CENTER);
        controls.setWidthFull ();
        add (controls, grid);

        var modelBuilder = new NoModelConverter<FileEntity> ();

        var dp = new ModelRepositoryDataProvider<FileEntity, FileEntity, FileEntityRepository, FileEntityQdslFilter> (
                "fileEntity",
                FileEntity.class,
                mongoTemplate,
                fileEntityRepository, modelBuilder);
        grid.getDataCommunicator ().setDefinedSize (false);
        grid.setItems (dp);
        grid.getDataCommunicator ().setDefinedSize (false);
        ftsFilter.addValueChangeListener (evt -> {
            grid.getFilter ().setFts (evt.getValue ());
        });
        grid.setSelectionMode (Grid.SelectionMode.MULTI);
        var model = (com.vaadin.flow.component.grid.AbstractGridMultiSelectionModel<?>) grid.getSelectionModel ();
        model.setSelectionColumnFrozen (true);
        model.setSelectAllCheckboxVisibility (GridMultiSelectionModel.SelectAllCheckboxVisibility.VISIBLE);
        toggleButton.addClickListener (evt -> {
            var set = grid.getSelectedItems ();
            set.forEach (e -> e.sortedOut (! e.sortedOut ()));
            fileEntityRepository.saveAll (set);
            grid.getSelectionModel ().deselectAll ();
            dp.refreshAll ();
        });
        selectButton.addClickListener (evt -> {
            var limit = 3000;
            var dc = grid.getDataCommunicator ();
            var ai = new AtomicInteger (0);
            var ssize = dp.size (new Query<> (grid.getFilter ()));
            if (ssize > limit) {
                dp.fetch (new Query<> (0, limit, Collections.emptyList (), null, grid.getFilter ())).forEach (el -> {
                    var f = new File (el.path ());
                    if (! f.exists () || evt.isCtrlKey ()) {
                        ai.incrementAndGet ();
                        grid.select (el);
                    }
                });
                Notification.show ("Total: " + ssize + " - " + ai.get ());

            }
            else {
                dp.fetch (new Query<> (grid.getFilter ())).forEach (el -> {
                    var f = new File (el.path ());
                    if (! f.exists () || evt.isCtrlKey ()) {
                        ai.incrementAndGet ();
                        grid.select (el);
                    }
                });
                Notification.show ("Total: " + ai.get ());
            }
        });

        //grid.setSizeFull();
        grid.setHeight ("90vh");

        grid.setControlClick ( (lm, evt) -> {
            try {
                var err = new FileOpener ().dir (lm.path ());
                if (err != null) {
                    Notification.show ("Error: can't open " + lm.path () + ". " + err);
                }
            }
            catch (Exception ex) {
                Notification.show ("Error: can't open " + lm.path ());
                Logger.getLogger (FileView.class.getName ()).log (Level.SEVERE, null, ex);
            }
        });
        grid.setDelClick ( (lm, evt) -> {
            var f = new File (lm.path ());
            if (f.exists ()) {
                VaMessageDialog.getMessageDialogYN ("Attension", "Delete file " + lm.path () + "?", () -> {
                    f.delete ();
                    grid.select (lm);
                    Notification.show ("Delete " + lm.path ());
                    return true;
                }).open ();
            }
            else {
                grid.select (lm);
                Notification.show ("Not found " + lm.path ());
            }
        });

        statButton.addClickListener (evt -> {
            Notification.show ("Status: " + (walkResult == null ? "not started" : (walkResult.isDone () ? "done" : "processed")));
        });
        addButton.addClickListener (evt -> {
            if (walkResult == null || walkResult.isDone ()) {
                walkResult = CompletableFuture.runAsync ( () -> {
                    var fldr = "/media/sevn/Elements/MUSIC";
                    final Path ppath = new File (fldr).toPath ();
                    final MyFileProcessor fileProcessor = new MyFileProcessor (fileEntityRepository);
                    final FileWalker fileWalker = new FileWalker (fileProcessor, ".exclude");
                    try {
                        Files.walkFileTree (ppath, fileWalker);
                    }
                    catch (IOException ex) {
                        log.log (Level.SEVERE, null, ex);
                    }
                });
            }
        });
    }

    @Override
    public void setParameter (BeforeEvent event, @OptionalParameter String parameter) {
        var location = event.getLocation ();
        var queryParameters = location.getQueryParameters ();

        var parametersMap = queryParameters.getParameters ();

        if (! parametersMap.isEmpty ()) {
            parametersMap.keySet ().forEach (e -> {
                grid.setFilterValue (e, parametersMap.get (e).get (0));
            });
            grid.getDataProvider ().refreshAll ();
        }
    }

}
