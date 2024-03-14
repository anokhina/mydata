package ru.org.sevn.mydata.views.books;

import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
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
import ru.org.sevn.mydata.views.FileUtil;
import ru.org.sevn.va.combo.VaEntityMultiselectCombobox;

public class BookPanel extends VerticalLayout {

    private FormLayout form = new FormLayout ();

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
        form.add (lang, title, author, img, new Div (), url, content, dsc, tags);
        //binder.setValidatorsDisabled(false);
        content.setHeight ("30em");
        dsc.setHeight ("30em");
        binder.bindInstanceFields (this);

        var generateContent = new Button ("Generate content", evt -> {
            //TODO path exception
            var dirPath = Path.of (ctx.supplierDataDir.get ().toString (), binder.getBean ().pathId ());
            var fileNames = dirPath.toFile ().list ( (dir, name) -> {
                if (StringUtils.equalsAny (name, "index.md", "indexed.md", "img.png")) {
                    return false;
                }
                return true;
            });
            var val = Stream.of (fileNames).collect (Collectors.joining ("\n"));
            content.setValue (val);
        });
        var openDir = new Button ("Open dir", evt -> {
            FileUtil.open ( () -> {
                var dirPath = Path.of (ctx.supplierDataDir.get ().toString (), binder.getBean ().pathId ());
                return dirPath.toString ();
            });
        });

        var buttons = new HorizontalLayout ();
        add (buttons);

        buttons.add (generateContent, openDir);
    }

    public Binder<BookModel> getBinder () {
        return binder;
    }

}
