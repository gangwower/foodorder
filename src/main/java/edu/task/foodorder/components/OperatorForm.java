package edu.task.foodorder.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import edu.task.foodorder.backend.domain.Request;
import edu.task.foodorder.backend.domain.Status;
import edu.task.foodorder.backend.domain.User;
import edu.task.foodorder.backend.repos.RequestRepo;

import java.sql.Timestamp;

public class OperatorForm extends VerticalLayout {
    private Request request;

    TextField comment = new TextField("Comment");
    Button acceptBtn = new Button("Accept", VaadinIcon.CHECK.create());
    Button denyBtn = new Button("Deny", VaadinIcon.CLOSE.create());
    Button cancelBtn = new Button("Cancel");

    Binder<Request> operatorBinder = new Binder<>(Request.class);
    private ChangeHandler changeHandler;
    private final RequestRepo requestRepo;
    private final User currentUser;

    public OperatorForm(RequestRepo requestRepo, User user) {
        this.requestRepo = requestRepo;
        currentUser = user;

        operatorBinder.bindInstanceFields(this);
        comment.setSizeFull();
        add(comment, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        acceptBtn.getElement().getThemeList().add("primary");
        denyBtn.getElement().getThemeList().add("error");
        acceptBtn.addClickShortcut(Key.ENTER);

        acceptBtn.addClickListener(click -> {

            request.setStatus(Status.DONE);
            request.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            requestRepo.save(request);
            changeHandler.onChange();
        });
        denyBtn.addClickListener(click -> {

            request.setStatus(Status.ERROR);
            request.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            requestRepo.save(request);
            changeHandler.onChange();
        });
        cancelBtn.addClickListener(click -> {
            acceptRequest(request);
            changeHandler.onChange();
        });

        comment.setValueChangeMode(ValueChangeMode.EAGER);
        operatorBinder.addStatusChangeListener(e -> denyBtn.setEnabled(!comment.isEmpty()));

        return new HorizontalLayout(acceptBtn, denyBtn, cancelBtn);
    }

    public void acceptRequest(Request r) {
        if (r == null){
            setVisible(false);
            return;
        }
        if (r.getId() != null){
            request = requestRepo.findById(r.getId()).orElse(r);
        } else {
            request = r;
        }
        operatorBinder.setBean(request);

        setVisible(true);
        request.setOperator_id(currentUser);

        comment.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
