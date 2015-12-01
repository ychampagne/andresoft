package org.andresoft.webui;

import org.andresoft.component.TableExample;
import org.andresoft.component.TreeExample;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path = "/table")
public class WebUIComponents extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // The root of the component hierarchy
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull(); // Use entire window
        setContent(content); // Attach to the UI

        // Add some component
        content.addComponent(new Label("Hello!"));

        // Layout inside layout
        HorizontalLayout hor = new HorizontalLayout();
        hor.setSizeFull(); // Use all available space

        // Couple of horizontally laid out components
        Tree tree = new Tree("My Tree", TreeExample.createTreeContent());
        hor.addComponent(tree);

        Table table = new Table("My Table", TableExample.generateContent());
        table.setSizeFull();
        hor.addComponent(table);
        hor.setExpandRatio(table, 1); // Expand to fill

        content.addComponent(hor);
        content.setExpandRatio(hor, 1); // Expand to fill
    }
}
