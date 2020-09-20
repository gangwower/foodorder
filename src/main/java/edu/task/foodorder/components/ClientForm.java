package edu.task.foodorder.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

public class ClientForm extends VerticalLayout {
    private Request request;
    private final RequestRepo requestRepo;
    private final User currentUser;

    TextField order = new TextField("Order");
    TextField comment = new TextField("Comment");
    Button saveBtn = new Button("Save");
    Button cancelBtn = new Button("Cancel");

    Binder<Request> clientBinder = new Binder<>(Request.class);
    private ChangeHandler changeHandler;
    private boolean isJustCreated;

    public ClientForm(RequestRepo requestRepo, User user){
        this.requestRepo = requestRepo;
        currentUser = user;
        clientBinder.bindInstanceFields(this);


        order.setSizeFull();
        comment.setSizeFull();
        comment.setReadOnly(true);
        add(order, comment, createButtonsLayout());
    }

    public void fixRequest(Request r){
        if (r == null){
            setVisible(false);
            return;
        }

        if (r.getId() != null){
            request = requestRepo.findById(r.getId()).orElse(r);
        } else {
            request = r;
        }
        clientBinder.setBean(request);

        isJustCreated = request.getOrder().isEmpty();
        setVisible(true);
        if(isJustCreated){
            comment.setVisible(false);
            request.setClient_id(currentUser);

        }

        order.focus();
    }

    public void setChangeHandler(ChangeHandler h) {

        changeHandler = h;
    }

    private Component createButtonsLayout() {
        saveBtn.getElement().getThemeList().add("primary");
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveBtn.addClickShortcut(Key.ENTER);
        cancelBtn.addClickShortcut(Key.ESCAPE);

        saveBtn.addClickListener(click -> {
            if (!isJustCreated){
                request.setStatus(Status.FIXED);
                request.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            }
            isJustCreated = false;
            requestRepo.save(request);
            changeHandler.onChange();
        });
        cancelBtn.addClickListener(click -> {
            fixRequest(request);
            changeHandler.onChange();
        });


        order.setValueChangeMode(ValueChangeMode.EAGER);
        clientBinder.addStatusChangeListener(e -> saveBtn.setEnabled(!order.isEmpty()));

        return new HorizontalLayout(saveBtn, cancelBtn);
    }
}

