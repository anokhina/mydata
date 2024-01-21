package ru.org.sevn.mydata.views.links;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.org.sevn.mydata.entity.LinkEntity;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.repo.LinkEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.dialog.VaMessageDialog;
import ru.org.sevn.va.grid.TextFieldFilter;

@PageTitle ("Links")
@Route (value = "links")
public class LinkView extends VerticalLayout {
    private LinkGrid grid;
    private Button addButton = new Button ("Add");
    private TextField ftsFilter = new TextField ();

    static class AddDialog extends VaMessageDialog<LinkPanel> {
        public AddDialog (LinkPanel linkPanel) {
            super ("Link", linkPanel);
            addCancel ( () -> true);
            setSizeFull ();
        }
    }

    private AddDialog editorAdd;

    @Autowired
    public LinkView (
            MongoTemplate mongoTemplate,
            LinkEntityRepository linkEntityRepository,
            TagEntityRepository tagEntityRepository) {
        grid = new LinkGrid (tagEntityRepository);
        ftsFilter.setClearButtonVisible (true);
        ftsFilter.setWidthFull ();
        var controls = new HorizontalLayout (addButton, ftsFilter);
        controls.setDefaultVerticalComponentAlignment (Alignment.CENTER);
        controls.setWidthFull ();
        add (controls, grid);

        var modelBuilder = new LinkLinkModelBuilder ();

        var dp = new ModelRepositoryDataProvider<LinkEntity, LinkModel, LinkEntityRepository, LinkEntityQdslFilter> (
                "linkEntity",
                LinkEntity.class,
                mongoTemplate,
                linkEntityRepository, modelBuilder);
        grid.setItems (dp);
        ftsFilter.addValueChangeListener (evt -> {
            grid.getFilter ().setFts (evt.getValue ());
        });

        //grid.setSizeFull();
        grid.setHeight ("90vh");

        editorAdd = new AddDialog (new LinkPanel ( () -> tagEntityRepository, QTagEntity.tagEntity.value));
        editorAdd.addButton ("Save", () -> {
            if (editorAdd.getMessage ().getBinder ().validate ().isOk ()) {
                var entity = modelBuilder.fromModel (editorAdd.getMessage ().getBinder ().getBean ());
                linkEntityRepository.save (entity);
                dp.refreshAll ();
                return true;
            }
            else {
                return false;
            }
        });
        addButton.addClickListener (evt -> {
            editorAdd.setTitle ("Новый");
            editorAdd.getMessage ().getBinder ().setBean (new LinkModel ());
            editorAdd.open ();
        });

        grid.setControlClick ( (lm, evt) -> {
            editorAdd.setTitle ("Редактирование");
            editorAdd.getMessage ().getBinder ().setBean (lm);
            editorAdd.open ();
        });
    }
}
