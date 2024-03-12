package ru.org.sevn.va.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.Scroller;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import ru.org.sevn.obj.ConfigurableObject;
import ru.org.sevn.va.VaUtil;
import ru.org.sevn.va.scroll.UseScroll;

public class VaDialog<MSGCMP extends Component & HasSize> extends Dialog implements ConfigurableObject<VaDialog<MSGCMP>> {
    public static final String ATTENTION = "Внимание";

    public static final String BUTTON_OK = "OK";

    @Getter
    @Setter
    private MSGCMP message;

    private final Scroller messageContainer = new Scroller ();

    public VaDialog (String title, MSGCMP msg) {
        setHeaderTitle (title);

        VaUtil.idcmp (messageContainer);
        messageContainer.setScrollDirection (Scroller.ScrollDirection.NONE);
        messageContainer.setHeightFull ();
        messageContainer.setWidthFull ();
        add (messageContainer);

        message = initMessage (msg);
        messageContainer.setContent (message);

        //setWidth (80, Unit.PERCENTAGE);
        //setHeight (80, Unit.PERCENTAGE);

        addOpenedChangeListener (evt -> {
            if (evt.isOpened ()) {
                VaUtil.getClientHeight (message.getId ().get (), wmessage -> {
                    VaUtil.getClientHeight (messageContainer.getId ().get (), wmessageContainer -> {
                        if (wmessage > wmessageContainer) {
                            messageContainer.setScrollDirection (Scroller.ScrollDirection.VERTICAL);
                        }
                    });
                });
            }
        });

        setCloseOnEsc (false);
        setCloseOnOutsideClick (false);
    }

    public Button addCancel (Supplier<Boolean> onCancel) {
        return addButton ("Отмена", onCancel);
    }

    public Button addButtonOk (Supplier<Boolean> onClick) {
        return addButton (BUTTON_OK, onClick);
    }

    public Button addButton (final String name, Supplier<Boolean> onClick) {
        if (StringUtils.isNotBlank (name)) {
            return addButtonAny (name, onClick);
        }
        return null;
    }

    public Button addButtonAny (final String name, Supplier<Boolean> onClick) {
        final Button button = new Button (name, evt -> {
            if (onClick.get ()) {
                this.close ();
            }
        });
        getFooter ().add (button);
        return button;
    }

    private MSGCMP initMessage (final MSGCMP message) {
        message.setWidthFull ();
        message.setHeight (null);
        VaUtil.idcmp (message);
        return message;
    }

    public void setTitle (final String titleStr) {
        setHeaderTitle (titleStr);
    }

    @Override
    public void open () {
        UseScroll.setScrollDirection (messageContainer);
        super.open ();
    }

}
