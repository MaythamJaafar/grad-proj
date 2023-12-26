package com.example.application.views.user;

import com.example.application.db.dbServices.DBServiceEntityUser;
import com.example.application.db.model.User;
import com.example.application.util.InputChecker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.application.util.Util.*;

public class UserForm extends VerticalLayout {
    private final Button saveUserBtn = new Button("Add User");
    private final TextField fullNameTxt = new TextField("Full Name");
    private final RadioButtonGroup<User.Gender> genderRd = new RadioButtonGroup<>("Gender", User.Gender.values());
    private final ComboBox<User.Role> roleCom = new ComboBox<>("Role", User.Role.values());
    private final TextField usernameTxt = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final DatePicker dateOfBirth = new DatePicker("DOB");
    private final Button cancelBtn = new Button("Cancel");
    private final Dialog dialog;
    private User user;
    private final DBServiceEntityUser dbServiceEntityUser;
    private final Grid<User> userGrid;
    private GridListDataView<User> gridListDataView;


    public UserForm(DBServiceEntityUser dbServiceEntityUser, Dialog dialog, Grid<User> userGrid, GridListDataView<User> gridListDataView) {
        this.dbServiceEntityUser = dbServiceEntityUser;
        this.dialog = dialog;
        this.userGrid = userGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> dialog.close());
        dialog.setCloseOnOutsideClick(false);
        createDesign();
        saveNewUser();
    }


    public UserForm(DBServiceEntityUser dbServiceEntityUser, Dialog dialog, User user, Grid<User> userGrid, GridListDataView<User> gridListDataView) {
        this.dbServiceEntityUser = dbServiceEntityUser;
        this.dialog = dialog;
        this.user = user;
        this.userGrid = userGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> dialog.close());
        saveUserBtn.setText("Save User");
        createDesign();
        saveNewUser();
        setComponentsValues(user);
        usernameTxt.setEnabled(false);
    }

    private void setComponentsValues(User user) {
        if (user.getFullName() != null)
            fullNameTxt.setValue(user.getFullName());
        if (user.getDateOfBirth() != null)
            dateOfBirth.setValue(user.getDateOfBirth());
        if (user.getRole() != null)
            roleCom.setValue(user.getRole());
        if (user.getGender() != null)
            genderRd.setValue(user.getGender());
        if (user.getUsername() != null)
            usernameTxt.setValue(user.getUsername());
        if (user.getPassword() != null)
            password.setValue(user.getPassword());
    }

    public void createDesign() {
        HorizontalLayout nameHl = new HorizontalLayout(fullNameTxt, usernameTxt);
        HorizontalLayout phnDobHl = new HorizontalLayout(roleCom, dateOfBirth);
        HorizontalLayout roleGnHL = new HorizontalLayout(genderRd, password);
        HorizontalLayout saveCancelHl = new HorizontalLayout(saveUserBtn, cancelBtn);
        saveCancelHl.setSizeFull();
        saveUserBtn.setWidth("50%");
        cancelBtn.setWidth("50%");
        saveUserBtn.getStyle().set("color", "white");
        saveUserBtn.getStyle().set("background-color", getSaveBtnColor());
        cancelBtn.getStyle().set("color", "white");
        cancelBtn.getStyle().set("background-color", getCancelBtnColor());
        add(nameHl, phnDobHl, roleGnHL, saveCancelHl);
    }


    public void saveNewUser() {
        saveUserBtn.addClickListener(buttonClickEvent -> {
            if (checkFields()) {
                User newUser = user == null ? new User() : user;
                newUser.setFullName(fullNameTxt.getValue());
                newUser.setDateOfBirth(dateOfBirth.getValue());
                newUser.setUsername(usernameTxt.getValue());
                newUser.set_id(usernameTxt.getValue());
                if (newUser.getPassword() == null || !newUser.getPassword().equals(password.getValue())) {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    newUser.setPassword(passwordEncoder.encode(password.getValue()));
                }
                newUser.setRole(roleCom.getValue());
                newUser.setGender(genderRd.getValue());
                if (user == null && dbServiceEntityUser.findUserByUsername(usernameTxt.getValue()) != null)
                    showErrorNotification("Username Already Exists");
                else {
                    dbServiceEntityUser.saveUser(newUser);
                    dialog.close();
                    gridListDataView = userGrid.setItems(dbServiceEntityUser.findAllUser());
                    gridListDataView.refreshAll();
                }
            }
        });
    }

    private boolean checkFields() {
        AtomicBoolean continueFlag = new AtomicBoolean(true);
        InputChecker.checkTextField(fullNameTxt, continueFlag);
        InputChecker.checkDatePicker(dateOfBirth, continueFlag);
        InputChecker.checkTextField(usernameTxt, continueFlag);
        InputChecker.checkPasswordField(password, continueFlag);
        InputChecker.checkCombo(roleCom, continueFlag);
        InputChecker.checkRdGrp(genderRd, continueFlag);
        return continueFlag.get();
    }


}
