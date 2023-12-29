package com.example.application.views.contact;

import com.example.application.db.model.User;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "contact", layout = MainLayout.class)
@PageTitle("Contact")
@PermitAll
public class ContactUs extends VerticalLayout {
    public ContactUs() {
        initUI();
    }


    private void initUI() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        contentLayout.setSizeFull();

        H2 contactUsH2 = new H2("Contact Us");
        H1 letTalkH1 = new H1("Let's Talk about your Project");
        H4 dropLineH4 = new H4("Drop us a line through the form below and we'll get back to you.");
        contentLayout.add(contactUsH2, letTalkH1, dropLineH4);

        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("last Name");
        EmailField emailField = new EmailField("Email");
        ComboBox<User.Gender> genderCombo = new ComboBox<>("Gender", User.Gender.values());
        NumberField ageField = new NumberField("Age");
        TextArea messageField = new TextArea("Message");

        ageField.setMin(18);
        ageField.setMax(100);
        ageField.setStep(1);
        ageField.setStepButtonsVisible(true);

        firstNameField.addClassName("rounded");
        lastNameField.addClassName("rounded");
        emailField.addClassName("rounded");
        ageField.addClassName("rounded");
        genderCombo.addClassName("rounded");
        messageField.addClassName("rounded");

        Button submitButton = new Button("Submit", event -> showSubmissionNotification());

        HorizontalLayout nameHl = new HorizontalLayout(firstNameField, lastNameField);
        HorizontalLayout ageGenderHl = new HorizontalLayout(genderCombo, ageField);

        VerticalLayout vl = new VerticalLayout();
        vl.add(nameHl, emailField, ageGenderHl, messageField, submitButton);
        vl.setAlignItems(Alignment.CENTER);
        vl.setWidth("40%");

        firstNameField.setPlaceholder("First Name");
        lastNameField.setPlaceholder("Last Name");
        emailField.setPlaceholder("Email");
        genderCombo.setPlaceholder("Gender");
        ageField.setPlaceholder("Age");
        messageField.setPlaceholder("Message");

        firstNameField.setClearButtonVisible(true);
        lastNameField.setClearButtonVisible(true);
        emailField.setClearButtonVisible(true);
        genderCombo.setClearButtonVisible(true);
        ageField.setClearButtonVisible(true);
        messageField.setClearButtonVisible(true);

        firstNameField.setWidth("50%");
        lastNameField.setWidth("50%");
        nameHl.setWidthFull();

        genderCombo.setWidth("50%");
        ageField.setWidth("50%");
        ageGenderHl.setWidthFull();

        emailField.setWidthFull();
        messageField.setWidthFull();


        submitButton.getElement().getThemeList().add("primary");

        contentLayout.add(vl);

        add(contentLayout);
    }

    private void showSubmissionNotification() {
        Notification.show("Thank you for your message! We will get in touch with you soon.");
    }
}
