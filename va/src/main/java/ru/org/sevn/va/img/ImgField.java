package ru.org.sevn.va.img;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImgField extends CustomField<String> {

    private Image previewImg = new Image ();
    private TextArea textField = new TextArea ();

    public ImgField (String label) {
        textField.setWidthFull ();
        this.add (textField);
        textField.setHeight ("5em");
        textField.setReadOnly (true);

        String pasteFunction = "for(const item of event.clipboardData.items) {"
                + "  if(item.type.startsWith(\"image/\")) {"
                + "    var blob = item.getAsFile();"
                + "    var reader = new FileReader();"
                + "    reader.onload = function(onloadevent) {$1.$server.upload(onloadevent.target.result);};"
                + "    reader.readAsDataURL(blob);"
                + "  }"
                + "}";
        this.getElement ().executeJs ("$0.addEventListener(\"paste\", event => {" + pasteFunction + "})", textField.getElement (), this);

        previewImg.setWidthFull ();
        previewImg.setHeight ("50vh");
        add (previewImg);

        setLabel (label);
    }

    @Override
    protected String generateModelValue () {
        return textField.getValue ();
    }

    @Override
    protected void setPresentationValue (String t) {
        setImgString (t);
    }

    @ClientCallable
    private void upload (String dataUrl) {
        System.out.println ("DataUrl: " + dataUrl);
        setImgString (dataUrl);
        updateValue ();
    }

    private void setImgString (String dataUrl) {
        if (dataUrl != null && dataUrl.startsWith ("data:")) {
            textField.setValue (dataUrl);

            byte [] imgBytes = Base64.getDecoder ().decode (dataUrl.substring (dataUrl.indexOf (',') + 1));
            previewImg.setSrc (new StreamResource ("preview.png", () -> new ByteArrayInputStream (imgBytes)));
        }
        else {
            textField.setValue ("");
            byte [] imgBytes = new byte [0];
            previewImg.setSrc (new StreamResource ("preview.png", () -> new ByteArrayInputStream (imgBytes)));
        }
    }

}
