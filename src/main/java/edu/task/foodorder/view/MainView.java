package edu.task.foodorder.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import edu.task.foodorder.backend.domain.Request;
import edu.task.foodorder.backend.domain.Role;
import edu.task.foodorder.backend.domain.Status;
import edu.task.foodorder.backend.domain.User;
import edu.task.foodorder.backend.service.RequestService;
import edu.task.foodorder.backend.service.UserService;
import edu.task.foodorder.components.ClientForm;
import edu.task.foodorder.components.OperatorForm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
public class MainView extends VerticalLayout {

    private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    private final Grid<Request> grid = new Grid<>(Request.class);
    private final RequestService requestService;
    private final UserService userService;
    private final ClientForm clientForm;
    private final OperatorForm operatorForm;
    private final Boolean isClient = auth.getAuthorities().contains(Role.CLIENT);


    public MainView(RequestService requestService, UserService userService) {
        this.requestService = requestService;
        this.userService = userService;

        User currentUser = userService.findByUsername(auth.getName());

        setSizeFull();
        H1 header = new H1((isClient ? "Your orders" : "All orders"));
        Anchor logout = new Anchor("/logout", "Log out");
        HorizontalLayout layout = new HorizontalLayout(header, logout);
        layout.setWidth("100%");
        layout.expand(header);
        add(layout);

        if (isClient) {
            add(getNewOrderBtn());
        }
        configureOrderTable();



        clientForm = new ClientForm(requestService.getRequestRepo(), currentUser);
        operatorForm = new OperatorForm(requestService.getRequestRepo(), currentUser);
        Div content = new Div(operatorForm, clientForm, grid);
        content.setSizeFull();
        add(content);
        updateOrderTable();

        if (isClient) {
            grid.asSingleSelect().addValueChangeListener(e -> clickOnRequest(e.getValue()));
        } else {
            grid.asSingleSelect().addValueChangeListener(e -> opClickOnRequest(e.getValue()));
        }
        operatorForm.setChangeHandler(() -> {
            operatorForm.setVisible(false);
            updateOrderTable();
        });
        clientForm.setChangeHandler(() -> {
            clientForm.setVisible(false);
            updateOrderTable();
        });

        closeEditor();
    }

    private Button getNewOrderBtn() {
        Button newBtn = new Button("New order!", click -> addOrder());
        newBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return newBtn;
    }

    private void addOrder() {
        grid.asSingleSelect().clear();
        clientForm.fixRequest(new Request(Status.NEW));
    }

    private void opClickOnRequest(Request request) {
        if(request == null){
            closeEditor();
        } else {
            if (request.getStatus().equals(Status.NEW)
                    || request.getStatus().equals(Status.FIXED)) {
                operatorForm.acceptRequest(request);
            } else {
                Notification.show("Status of this order is: " + request.getStatus().name());
            }
        }
    }

    private void clickOnRequest(Request request) {
        if(request == null){
            closeEditor();
        } else {
            if (request.getStatus().equals(Status.ERROR)) {
                clientForm.fixRequest(request);
            } else {
                Notification.show("Status of your order is: " + request.getStatus().name());
            }
        }
    }

    private void closeEditor(){
        clientForm.fixRequest(null);
        operatorForm.acceptRequest(null);
    }

    private void configureOrderTable() {
        grid.setSizeFull();
        grid.removeColumnByKey("client_id");
        grid.removeColumnByKey("operator_id");

        grid.setColumns("id", "status",
                "order", "created_at", "comment", "updated_at");
        grid.addColumn(request -> {
            User client = request.getClient_id();
            return client.getUsername();
        }).setHeader("client");

        grid.addColumn(r -> {
            User op = r.getOperator_id();
            if (op == null){
                return "";
            } else{
                return op.getUsername();
            }
        }).setHeader("operator");

    }

    private void updateOrderTable() {
        if (!isClient) {
            grid.setItems(requestService.findAll());
        } else {

            grid.setItems(requestService.findAll(userService.findByUsername(auth.getName())));
        }
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }
}
