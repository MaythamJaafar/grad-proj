package com.example.application.views.contact;

import com.example.application.db.model.User;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
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
    private final RadioButtonGroup<User.Gender> genderRd = new RadioButtonGroup<>("Gender", User.Gender.values());

    private void initUI() {
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        contentLayout.setSizeFull();

        H1 header = new H1("Contact Us");
        contentLayout.add(header);

        FormLayout formLayout = new FormLayout();

        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("last Name");
        EmailField emailField = new EmailField("Email");
//        GenderRd ageField = new EmailField("Age");
        TextField ageField = new TextField("Age");
        TextArea messageField = new TextArea("Message");

        firstNameField.addClassName("rounded");
        lastNameField.addClassName("rounded");
        emailField.addClassName("rounded");
        ageField.addClassName("rounded");
        genderRd.addClassName("rounded");
        messageField.addClassName("rounded");

        Button submitButton = new Button("Submit", event -> {
            showSubmissionNotification();
        });
        HorizontalLayout nameHl = new HorizontalLayout(firstNameField,lastNameField);
        HorizontalLayout emailHl = new HorizontalLayout(emailField);
        HorizontalLayout ageGenderHl = new HorizontalLayout(genderRd,ageField);
        HorizontalLayout messageHl = new HorizontalLayout(messageField);
        HorizontalLayout submitHl = new HorizontalLayout(submitButton);

        formLayout.add(nameHl,emailHl,ageGenderHl,messageHl,submitHl);
        emailHl.setWidthFull();
        formLayout.setMaxWidth("400px");
        formLayout.getStyle().set("margin", "auto");

        submitButton.getElement().getThemeList().add("primary");

        contentLayout.add(formLayout);

        add(contentLayout);
    }

    private void showSubmissionNotification() {
        Notification.show("Thank you for your message! We will get in touch with you soon.");
    }
}
