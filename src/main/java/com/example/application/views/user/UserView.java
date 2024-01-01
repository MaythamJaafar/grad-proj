package com.example.application.views.user;

import com.example.application.db.dbServices.DBServiceEntityUser;
import com.example.application.db.model.User;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.List;

import static com.example.application.util.Util.*;

@Route(value = "userGrid", layout = MainLayout.class)
@PageTitle("User Grid")
@RolesAllowed({"SUPER_ADMIN"})
public class UserView extends VerticalLayout {
    public final DBServiceEntityUser dbServiceEntityUser;
    private final Button addNewUserBtn = new Button("Add User");
    public final Grid<User> userGrid = new Grid<>();
    private final Dialog addUserDialog = new Dialog();
    private GridListDataView<User> gridListDataView;

    public UserView(DBServiceEntityUser dbServiceEntityUser) {
        this.dbServiceEntityUser = dbServiceEntityUser;
        createGrid();
        setSizeFull();
        setFilters();
        addNewUserBtn.addClickListener(buttonClickEvent -> addUserDialog.open());
        addUserDialog.add(new UserForm(dbServiceEntityUser, addUserDialog, userGrid, gridListDataView));
    }

    private void setFilters() {
        HeaderRow filterRow = userGrid.appendHeaderRow();

        TextField fullNameTxt = new TextField("");
        fullNameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        fullNameTxt.setClearButtonVisible(true);
        filterRow.getCell(userGrid.getColumnByKey("fullName")).setComponent(fullNameTxt);

        TextField usernameTxt = new TextField("");
        usernameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        usernameTxt.setClearButtonVisible(true);
        filterRow.getCell(userGrid.getColumnByKey("username")).setComponent(usernameTxt);

        ComboBox<User.Gender> genderCombo = new ComboBox<>("");
        genderCombo.setItems(User.Gender.values());
        genderCombo.setClearButtonVisible(true);
        filterRow.getCell(userGrid.getColumnByKey("gender")).setComponent(genderCombo);

        ComboBox<User.Role> roleCombo = new ComboBox<>("");
        List<User.Role> roleList = Arrays.asList(User.Role.values());
        roleCombo.setItems(roleList);
        roleCombo.setClearButtonVisible(true);
        filterRow.getCell(userGrid.getColumnByKey("role")).setComponent(roleCombo);

        DatePicker dobDatePicker = new DatePicker("");
        dobDatePicker.setClearButtonVisible(true);
        filterRow.getCell(userGrid.getColumnByKey("dob")).setComponent(dobDatePicker);

        fullNameTxt.addValueChangeListener(event -> applyFilter(fullNameTxt, usernameTxt, genderCombo, roleCombo, dobDatePicker));
        usernameTxt.addValueChangeListener(event -> applyFilter(fullNameTxt, usernameTxt, genderCombo, roleCombo, dobDatePicker));
        genderCombo.addValueChangeListener(event -> applyFilter(fullNameTxt, usernameTxt, genderCombo, roleCombo, dobDatePicker));
        roleCombo.addValueChangeListener(event -> applyFilter(fullNameTxt, usernameTxt, genderCombo, roleCombo, dobDatePicker));
        dobDatePicker.addValueChangeListener(event -> applyFilter(fullNameTxt, usernameTxt, genderCombo, roleCombo, dobDatePicker));
    }

    private void applyFilter(TextField fullNameTxt, TextField usernameTxt, ComboBox<User.Gender> genderCombo, ComboBox<User.Role> roleCombo, DatePicker dobDatePicker) {
        gridListDataView.setFilter(user -> (
                (fullNameTxt.getValue() == null || fullNameTxt.getValue().isEmpty() || user.getFullName().contains(fullNameTxt.getValue()))
                && (usernameTxt.getValue() == null || usernameTxt.getValue().isEmpty() || user.getUsername().contains(usernameTxt.getValue()))
                && (genderCombo.getValue() == null || user.getGender().equals(genderCombo.getValue()))
                && (roleCombo.getValue() == null || user.getRole().equals(roleCombo.getValue()))
                && (dobDatePicker.getValue() == null || user.getDateOfBirth().equals(dobDatePicker.getValue()))
        ));
    }

    private void createGrid() {
        gridListDataView = userGrid.setItems(dbServiceEntityUser.findAllUser());
        userGrid.addColumn(User::getFullName).setHeader("Full Name").setKey("fullName").setSortable(true);
        userGrid.addColumn(User::getRole).setHeader("Role").setKey("role").setSortable(true);
        userGrid.addColumn(User::getGender).setHeader("Gender").setKey("gender").setSortable(true);
        userGrid.addColumn(User::getUsername).setHeader("Username").setKey("username").setSortable(true);
        userGrid.addColumn(User::getDateOfBirth).setHeader("DOB").setKey("dob").setSortable(true);
        Style edit = userGrid.addComponentColumn(user -> {
            Button editBtn = createEditButton();
            Button deleteBtn = createDeleteButton();

            editBtn.addClickListener(clickEvent -> {
                Dialog editDialog = new Dialog();
                editDialog.add(new UserForm(dbServiceEntityUser, editDialog, user, userGrid, gridListDataView));
                editDialog.open();
            });
            deleteBtn.addClickListener(clickEvent -> {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                Button confirmDeleteBtn = createDeleteButton();
                confirmDialog.setConfirmButton(confirmDeleteBtn);
                confirmDialog.setHeader("Delete Confirmation");
                confirmDialog.setText("Are you sure you want to delete " + user.getUsername(    ));
                confirmDialog.setCancelable(true);
                    confirmDeleteBtn.addClickListener(clickEvent1 -> {
                    dbServiceEntityUser.deleteById(user.get_id());
                    gridListDataView = userGrid.setItems(dbServiceEntityUser.findAllUser());
                    gridListDataView.refreshAll();
                    showSuccessNotification("Item deleted successfully");
                });
                confirmDialog.setCancelButton(new Button("Cancel", clickEvent1 -> confirmDialog.close()));
                confirmDialog.setConfirmButton(confirmDeleteBtn);
                confirmDialog.open();
            });

                return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Edit").setFooter(addNewUserBtn).getStyle().setMargin("margin-left");

        addNewUserBtn.getStyle().set("background-color", "green");
        addNewUserBtn.getStyle().set("color", "white");
        addNewUserBtn.getStyle().set("border", "none");
        addNewUserBtn.getStyle().set("border-radius", "5px");
        userGrid.setSizeFull();
        add(userGrid);
    }
}


