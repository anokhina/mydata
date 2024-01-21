package ru.org.sevn.mydata.views.links;

import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.combobox.ComboBox;
import ru.org.sevn.mydata.views.books.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import java.util.function.Supplier;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.LinkEntity;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.va.combo.VaEntityMultiselectCombobox;
import ru.org.sevn.va.img.ImgField;

public class LinkPanel extends FormLayout {
    private TextField title = new TextField ("title");
    private TextField author = new TextField ("author");
    private TextArea dsc = new TextArea ("dsc");
    private VaEntityMultiselectCombobox<TagEntity> tags;
    private ImgField img = new ImgField ("img");
    private TextField url = new TextField ("url");
    private TextField content = new TextField ("content");
    private ComboBox<IndexLanguageEnum> lang = new ComboBox<IndexLanguageEnum> ("lang", IndexLanguageEnum.values ());

    private BeanValidationBinder<LinkModel> binder = new BeanValidationBinder (LinkModel.class);

    public LinkPanel (Supplier<TagEntityRepository> repo, StringPath entityQpath) {
        tags = new VaEntityMultiselectCombobox (repo, entityQpath);
        tags.setItemLabelGenerator (i -> i.value ());
        tags.setLabel ("tags");
        tags.setNewObjectBuilder (s -> new TagEntity ().value (s), repo);
        add (lang, new Div (), title, url, tags, author, img, dsc);
        //binder.setValidatorsDisabled(false);
        dsc.setHeight ("30em");
        binder.bindInstanceFields (this);
    }

    public Binder<LinkModel> getBinder () {
        return binder;
    }

}
