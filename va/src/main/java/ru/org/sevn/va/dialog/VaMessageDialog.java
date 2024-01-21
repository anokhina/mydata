package ru.org.sevn.va.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.function.Supplier;
import ru.org.sevn.util.ObjectUtil;
import ru.org.sevn.va.VaIcons;
import ru.org.sevn.va.VaUtil;

public class VaMessageDialog<MSGCMP extends Component & HasSize> extends Dialog {

    public interface UseScrollHorizontal extends UseScroll {
        @Override
        default Scroller.ScrollDirection useScrollDirection () {
            return Scroller.ScrollDirection.HORIZONTAL;
        }
    }

    public interface UseScrollVertical extends UseScroll {
        @Override
        default Scroller.ScrollDirection useScrollDirection () {
            return Scroller.ScrollDirection.VERTICAL;
        }
    }

    public interface UseScroll {
        default Scroller.ScrollDirection useScrollDirection () {
            return Scroller.ScrollDirection.NONE;
        }
    }

    private final H3 title = new H3 ();
    private final HorizontalLayout buttonsPanel = new HorizontalLayout ();
    private MSGCMP message;
    private final VerticalLayout content = new VerticalLayout ();
    private final Scroller messageContainer = new Scroller ();

    public VaMessageDialog (final String titleStr, MSGCMP msg) {
        VaUtil.addClassName (content, VaMessageDialog.class.getSimpleName ());

        VaUtil.idcmp (messageContainer);
        messageContainer.setScrollDirection (Scroller.ScrollDirection.NONE);
        messageContainer.setHeightFull ();
        messageContainer.setWidthFull ();
        add (messageContainer);

        content.setWidthFull ();
        content.setHeightFull ();
        add (content);
        content.add (title);

        message = initMessage (msg);
        content.add (messageContainer);
        messageContainer.setContent (message);

        setWidth (80, Unit.PERCENTAGE);
        setHeight (80, Unit.PERCENTAGE);
        content.add (buttonsPanel);
        buttonsPanel.setWidthFull ();
        buttonsPanel.addClassName ("buttonsPanel");

        setTitle (titleStr);
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

    public static VaMessageDialog newVaMessageDialog (final String titleStr) {
        return new VaMessageDialog (titleStr, new Div ());
    }

    public void setTitle (final String titleStr) {
        this.title.setText (titleStr);
    }

    public HorizontalLayout getButtonsPanel () {
        return buttonsPanel;
    }

    public Button addCancel (Supplier<Boolean> onCancel) {
        return addButton ("Отмена", onCancel);
    }

    public Button addButton (final String name, Supplier<Boolean> onClick) {
        final Button button = new Button (name, evt -> {
            if (onClick.get ()) {
                this.close ();
            }
        });
        buttonsPanel.add (button);
        return button;
    }

    public void setMessage (final MSGCMP message) {
        messageContainer.setContent (message);
        this.message = initMessage (message);
    }

    public Scroller getMessageContainer () {
        return messageContainer;
    }

    private MSGCMP initMessage (final MSGCMP message) {
        message.setWidthFull ();
        message.setHeight (null);
        VaUtil.idcmp (message);
        return message;
    }

    public MSGCMP getMessage () {
        return message;
    }

    public static <CMP extends Component & HasSize & HasText> VaMessageDialog getMessageDialog (final String title, final String msg, final CMP cmp) {
        return getMessageDialog (title, ObjectUtil.apply (cmp, div -> div.setText (msg)));
    }

    public static <CMP extends Component & HasSize> VaMessageDialog getMessageDialog (final String title, final CMP cmp) {
        final VaMessageDialog res = new VaMessageDialog (title, cmp);
        res.addCancel ( () -> true).setText ("Закрыть");
        res.setHeight (null);
        return res;
    }

    public static <CMP extends Component & HasSize> VaMessageDialog getMessageDialogYN (final String title, final String msg, Supplier<Boolean> onClickYes) {
        return getMessageDialogYN (title, ObjectUtil.apply (new Div (), div -> div.setText (msg)), onClickYes);
    }

    public static <CMP extends Component & HasSize> VaMessageDialog getMessageDialogYN (final String title, final CMP cmp, Supplier<Boolean> onClickYes) {
        final VaMessageDialog res = new VaMessageDialog (title, cmp);
        ObjectUtil.obj (res.addCancel ( () -> true))
                .apply (b -> b.setText ("Нет"))
                .apply (b -> b.setIcon (VaIcons.CANCEL.get ()));
        res.addButton ("Да", onClickYes).setIcon (VaIcons.YES.get ());
        res.setHeight (null);
        return res;
    }

    protected H3 getTitle () {
        return title;
    }

    @Override
    public void open () {
        if (messageContainer.getContent () instanceof UseScroll) {
            messageContainer.setScrollDirection ( ((UseScroll) messageContainer.getContent ()).useScrollDirection ());
        }
        else {
            messageContainer.setScrollDirection (Scroller.ScrollDirection.BOTH);
        }
        super.open ();
    }
}
