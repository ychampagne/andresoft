package org.andresoft.webui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path = "/basic")
public class WebUIBasic extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Layout myview = new MyView();
        setContent(myview);
    }


    class MyView extends VerticalLayout {
        TextField entry = new TextField("Enter this");
        Label display = new Label("See this");
        Button click = new Button("Click This");

        public MyView() {
            addComponent(entry);
            addComponent(display);
            addComponent(click);

            // Configure it a bit
            setSizeFull();
            addStyleName("myview");
        }
    }
}
