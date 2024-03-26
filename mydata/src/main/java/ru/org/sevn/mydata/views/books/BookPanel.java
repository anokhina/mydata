package ru.org.sevn.mydata.views.books;

import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.mydata.sys.FileOpener;
import ru.org.sevn.mydata.views.FileUtil;
import ru.org.sevn.va.combo.VaEntityMultiselectCombobox;
import ru.org.sevn.va.dialog.VaTextDialog;

public class BookPanel extends VerticalLayout {

    private FormLayout form = new FormLayout ();

    private TextField titleShort = new TextField ("titleShort");
    private TextField title = new TextField ("title");
    private TextField author = new TextField ("author");
    private TextArea dsc = new TextArea ("dsc");
    private VaEntityMultiselectCombobox<TagEntity> tags;
    private TextField img = new TextField ("img");
    private TextField url = new TextField ("url");
    private TextArea content = new TextArea ("content");
    private ComboBox<IndexLanguageEnum> lang = new ComboBox<IndexLanguageEnum> ("lang", IndexLanguageEnum.values ());

    private BeanValidationBinder<BookModel> binder = new BeanValidationBinder (BookModel.class);

    @Setter
    @Accessors (fluent = true, chain = true)
    public static class Ctx {
        Supplier<TagEntityRepository> tagEntityRepository;
        StringPath entityQpath;
        Supplier<Path> supplierDataDir;
    }

    public BookPanel (Ctx ctx) {
        tags = new VaEntityMultiselectCombobox (ctx.tagEntityRepository, ctx.entityQpath);
        tags.setItemLabelGenerator (i -> i.value ());
        tags.setLabel ("tags");
        tags.setNewObjectBuilder (s -> new TagEntity ().value (s), ctx.tagEntityRepository);

        form.setSizeFull ();
        add (form);
        form.add (lang, title, author, img, titleShort, url, content, dsc, tags);
        //binder.setValidatorsDisabled(false);
        content.setHeight ("30em");
        dsc.setHeight ("30em");
        binder.bindInstanceFields (this);

        var generateContent = new Button ("Generate content", evt -> {
            var dirPath = dirPath (ctx);
            generateContent (dirPath);
        });
        var openDir = new Button ("Open dir", evt -> {
            if (evt.isCtrlKey ()) {
                FileUtil.open (new FileOpener (FileOpener.XDG_OPEN), () -> {
                    return dirPath (ctx).toString ();
                });

            }
            else {
                FileUtil.open ( () -> {
                    return dirPath (ctx).toString ();
                });
            }
        });
        var mkDir = new Button ("Mkdir", evt -> {
            if (binder.getBean ().getEntity () == null || binder.getBean ().getEntity ().getId () == null) {
                var dirPath = dirPath (ctx);
                new VaTextDialog ("Создать " + dirPath + "?", VaTextDialog.BUTTON_Cancel)
                        .configure (d -> {
                            d.addButtonOk ( () -> {
                                dirPath.toFile ().mkdirs ();
                                Notification.show ("Dir is created: " + dirPath);
                                return true;
                            });
                        }).open ();
            }
            else {
                Notification.show ("Mkdir is not supported");
            }
        });
        var load = new Button ("Load", evt -> {
            var prefix = titleShort.getValue ();
            if ( (binder.getBean ().getEntity () == null || binder.getBean ().getEntity ().getId () == null) && StringUtils.isNotBlank (prefix)) {

                var dirPath = Path.of (ctx.supplierDataDir.get ().toString (), prefix);
                new VaTextDialog ("Загрузить " + dirPath + "?", VaTextDialog.BUTTON_Cancel)
                        .configure (d -> {
                            d.addButtonOk ( () -> {
                                if (! dirPath.toFile ().exists ()) {
                                    Notification.show ("Dir doesn't exist: " + dirPath);
                                }
                                else {

                                    binder.getBean ().pathId (prefix);
                                    var fileNames = contentFiles (dirPath);
                                    if (fileNames.length > 0) {
                                        title.setValue (fileNames [0]);
                                        author.setValue (fileNames [0]);
                                    }
                                    generateContent (fileNames);

                                    Notification.show ("Dir is loaded: " + dirPath);
                                }
                                return true;
                            });
                        }).open ();
            }
            else {
                Notification.show ("Load is not supported");
            }
        });
        var buttons = new HorizontalLayout ();
        add (buttons);

        buttons.add (generateContent, openDir, mkDir, load);
    }

    private String [] contentFiles (Path dirPath) {
        //TODO path exception
        var fileNames = dirPath.toFile ().list ( (dir, name) -> {
            if (StringUtils.equalsAny (name, "index.md", "indexed.md", "img.png")) {
                return false;
            }
            return true;
        });
        return fileNames;
    }

    private void generateContent (Path dirPath) {
        var fileNames = contentFiles (dirPath);
        generateContent (fileNames);
    }

    private void generateContent (String [] fileNames) {
        var val = Stream.of (fileNames).collect (Collectors.joining ("\n"));
        content.setValue (val);
    }

    private Path dirPath (Ctx ctx) {
        var dirPath = Path.of (ctx.supplierDataDir.get ().toString (), binder.getBean ().pathId ());
        var prefix = titleShort.getValue ();
        if ( (binder.getBean ().getEntity () == null || binder.getBean ().getEntity ().getId () == null) && ! binder.getBean ().pathId ().equals (prefix)) {
            dirPath = Path.of (ctx.supplierDataDir.get ().toString (), binder.getBean ().pathId () + "_" + prefix);
        }
        return dirPath;
    }

    public Binder<BookModel> getBinder () {
        return binder;
    }

}
