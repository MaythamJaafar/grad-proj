package com.example.application.views.expense;

import com.example.application.db.dbServices.DBServicesExpenses;
import com.example.application.db.model.Expense;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.application.util.Util.showErrorNotification;
import static com.example.application.util.Util.showSuccessNotification;

@Route(value = "expense", layout = MainLayout.class)
@PageTitle("Expense")
@RolesAllowed({"SUPER_ADMIN", "ADMIN"})
public class ExpenseView extends HorizontalLayout {
    public final DBServicesExpenses dbServicesExpenses;
    private final TextField titleTxt = new TextField("Title");
    private final TextField detailsTxt = new TextField("Details");
    private final TextField priceNb = new TextField("Price");
    private final Grid<Expense> expensesGrid = new Grid<>();
    private GridListDataView<Expense> gridListDataView;
    private final Button expenseBtn = new Button("Add Expense");
    private final Button clearBtn = new Button("Clear");
    private String id = null;
    private final User currentUser;


    public ExpenseView(DBServicesExpenses dbServicesExpenses, AuthenticatedUser authenticatedUser) {
        this.dbServicesExpenses = dbServicesExpenses;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        VerticalLayout section1 = new VerticalLayout();
        VerticalLayout section2 = new VerticalLayout();

        HorizontalLayout addClrHl = new HorizontalLayout(expenseBtn, clearBtn);

        setHeightFull();
        section1.setWidth("30%");
        section2.setWidth("70%");
        section1.setHeightFull();
        addClrHl.setSizeFull();
        expenseBtn.setWidth("80%");
        clearBtn.setWidth("20%");
        section1.add(titleTxt, detailsTxt, priceNb, addClrHl);
        section2.add(expensesGrid);
        createGrid();
        if (currentUser.getRole().equals(User.Role.ADMIN)) {
            section1.setEnabled(false);
            expenseBtn.getStyle().set("background-color", "grey");
        }

        setFilters();
        clearBtn.addClickListener(event -> clearFields());
        add(section1, section2);
        clearBtn.setVisible(false);
    }

    private void createGrid() {
        DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("M/d/yy H:mm");
        gridListDataView = expensesGrid.setItems(dbServicesExpenses.findAllExpenses());

        expensesGrid.addColumn(Expense::getTitle).setHeader("Title").setKey("title").setSortable(true);
        expensesGrid.addColumn(Expense::getDetails).setHeader("Details").setKey("details").setSortable(true);
        expensesGrid.addColumn(expense -> expense.getPrice() + "$").setHeader("Price").setKey("price").setSortable(true);
        expensesGrid.addColumn(expense -> expense.getDate().format(shortFormatter)).setHeader("Date").setWidth("30%").setKey("date").setSortable(true);
        if (currentUser.getRole().equals(User.Role.SUPER_ADMIN))
            expensesGrid.addComponentColumn(this::createEditBtn);
        titleTxt.getStyle().setWidth("100%");
        detailsTxt.getStyle().setWidth("100%");
        priceNb.getStyle().setWidth("100%");

        expenseBtn.getStyle().set("background-color", "green");
        expenseBtn.getStyle().set("color", "white");
        expenseBtn.getStyle().set("border", "none");
        expenseBtn.getStyle().set("border-radius", "5px");
        expensesGrid.setSizeFull();

        expenseBtn.addClickListener(buttonClickEvent -> {
            if (titleTxt.isEmpty() || detailsTxt.isEmpty() || priceNb.isEmpty()) {
                showErrorNotification("Insert All Fields");

                if (titleTxt.getValue() == null || titleTxt.getValue().isEmpty()) {
                    titleTxt.setInvalid(true);
                    titleTxt.setErrorMessage("Invalid input");
                } else {
                    titleTxt.setInvalid(false);
                    titleTxt.setErrorMessage(null);
                }
                if (detailsTxt.getValue() == null || detailsTxt.getValue().isEmpty()) {
                    detailsTxt.setInvalid(true);
                    detailsTxt.setErrorMessage("Invalid input");
                } else {
                    detailsTxt.setInvalid(false);
                    detailsTxt.setErrorMessage(null);
                }
                if (priceNb.getValue() == null || priceNb.getValue().isEmpty()) {
                    priceNb.setInvalid(true);
                    priceNb.setErrorMessage("Invalid input");
                } else {
                    priceNb.setInvalid(false);
                    priceNb.setErrorMessage(null);
                }
            } else {
                Expense newExpense = new Expense();
                if (id != null)
                    newExpense.set_id(id);
                newExpense.setTitle(titleTxt.getValue());
                newExpense.setDetails(detailsTxt.getValue());
                newExpense.setPrice(priceNb.getValue());
                newExpense.setDate(LocalDateTime.now());
                clearFields();
                showSuccessNotification("successfully added");

                dbServicesExpenses.saveExpense(newExpense);
                id = null;
                clearBtn.setVisible(false);
                expensesGrid.setItems(dbServicesExpenses.findAllExpenses());
                gridListDataView.refreshAll();
            }

        });

    }

    private void setFilters() {
        HeaderRow filterRow = expensesGrid.appendHeaderRow();

        TextField titleTxt = new TextField(" ");
        titleTxt.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(expensesGrid.getColumnByKey("title")).setComponent(titleTxt);
        titleTxt.setWidthFull();
        titleTxt.setPlaceholder("");
        titleTxt.getElement().getStyle().set("font-size", "12px");

        TextField detailTxt = new TextField("");
        titleTxt.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(expensesGrid.getColumnByKey("details")).setComponent(detailTxt);
        detailTxt.setWidthFull();
        detailTxt.setPlaceholder("Details");
        detailTxt.getElement().getStyle().set("font-size", "12px");

        TextField priceTxt = new TextField("");
        titleTxt.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(expensesGrid.getColumnByKey("price")).setComponent(priceTxt);
        priceTxt.setWidthFull();
        priceTxt.setPlaceholder("Price");
        priceTxt.getElement().getStyle().set("font-size", "12px");

        DateTimePicker datePicker = new DateTimePicker("");
        datePicker.getElement().getThemeList().add("date-picker-width");
        datePicker.getElement().getThemeList().add("time-picker-width");
        filterRow.getCell(expensesGrid.getColumnByKey("date")).setComponent(datePicker);

        titleTxt.addValueChangeListener(event -> applyFilter(titleTxt.getValue(), detailTxt.getValue(), priceTxt.getValue(), datePicker.getValue()));
        detailTxt.addValueChangeListener(event -> applyFilter(titleTxt.getValue(), detailTxt.getValue(), priceTxt.getValue(), datePicker.getValue()));
        priceTxt.addValueChangeListener(event -> applyFilter(titleTxt.getValue(), detailTxt.getValue(), priceTxt.getValue(), datePicker.getValue()));
        datePicker.addValueChangeListener(event -> applyFilter(titleTxt.getValue(), detailTxt.getValue(), priceTxt.getValue(), datePicker.getValue()));
    }

    private void applyFilter(String title, String details, String price, LocalDateTime date) {
        System.out.println(date);
        gridListDataView.setFilter(expense ->
                (title == null || expense.getTitle().contains(title))
                        && (details == null || expense.getDetails().contains(details))
                        && (price == null || expense.getPrice().contains(price))
                        && (date == null || expense.getDate().isAfter(date))
        );
    }

    private Button createEditBtn(Expense expense) {
        Button editBtn = new Button("edit");
        editBtn.addClickListener(clickEvent -> {
            titleTxt.setValue(expense.getTitle());
            detailsTxt.setValue(expense.getDetails());
            priceNb.setValue(expense.getPrice());
            clearBtn.setVisible(true);
            id = expense.get_id();
            expenseBtn.setText("Update Expense");
        });
        editBtn.getStyle().set("background-color", "blue");
        editBtn.getStyle().set("color", "white");
        editBtn.getStyle().set("border", "5px");
        editBtn.getStyle().set("border-radius", "5px");

        return editBtn;
    }

    private void clearFields() {
        titleTxt.clear();
        priceNb.clear();
        detailsTxt.clear();
        id = null;
        clearBtn.setVisible(false);
        expenseBtn.setText("Add Expense");
    }
}
