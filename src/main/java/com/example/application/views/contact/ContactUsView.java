package com.example.application.views.contact;

//import com.example.application.db.dbServices.DBServiceContactUS;

import com.example.application.db.dbServices.DBServiceContactUs;
import com.example.application.db.model.ContactUs;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.util.InputChecker;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

@Route(value = "contact", layout = MainLayout.class)
@PageTitle("Contact")
@PermitAll
public class ContactUsView extends VerticalLayout {
    private final DBServiceContactUs dbServiceContactUs;
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("last Name");
    private final EmailField emailField = new EmailField("Email");
    private final ComboBox<User.Gender> genderCombo = new ComboBox<>("Gender", User.Gender.values());
    private final NumberField ageField = new NumberField("Age");
    private final TextArea messageField = new TextArea("Message");
    private final User currentUser;
    private final Tab createMessageTab = new Tab("Message");
    private final Tab myMessagesTab = new Tab("My Messages");
    private final Tabs tabs = new Tabs(createMessageTab, myMessagesTab);
    private final VerticalLayout createMessageVl = new VerticalLayout();
    private final VerticalLayout messagesVl = new VerticalLayout();

    private ContactUsView(DBServiceContactUs dbServiceContactUs, AuthenticatedUser authenticatedUser) {
        this.dbServiceContactUs = dbServiceContactUs;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        setSendMessageVl();
        messagesVl.setVisible(false);

        tabs.getElement().getStyle().set("align-self", "center");
        createMessageTab.getElement().getStyle().set("font-size", "x-large");
        myMessagesTab.getElement().getStyle().set("font-size", "x-large");

        tabs.addSelectedChangeListener(tab -> {
            if (tab.getSelectedTab().equals(createMessageTab)) {
                createMessageVl.setVisible(true);
                messagesVl.setVisible(false);
            } else {
                createMessageVl.setVisible(false);
                setMessageGridVl();
            }
        });
        tabs.setSelectedTab(createMessageTab);

        add(tabs, createMessageVl, messagesVl);
    }

    private void setMessageGridVl() {
        messagesVl.removeAll();
        messagesVl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        messagesVl.setVisible(true);
        messagesVl.setSizeFull();
        Grid<ContactUs> messagesGrid = new Grid<>();
        if (!currentUser.getRole().equals(User.Role.CLIENT)) {
            messagesGrid.addColumn(ContactUs::getFirstName).setHeader("First Name").setKey("firstName").setSortable(true);
            messagesGrid.addColumn(ContactUs::getLastName).setHeader("Last Name").setKey("lastName").setSortable(true);
            messagesGrid.addColumn(ContactUs::getEmail).setHeader("Email").setKey("email").setSortable(true);
            messagesGrid.addColumn(ContactUs::getMessageOwner).setHeader("Message Owner").setKey("messageOwner").setSortable(true);
            messagesGrid.addColumn(ContactUs::getGender).setHeader("Gender").setKey("gender").setSortable(true);
            messagesGrid.addColumn(ContactUs::getAge).setHeader("Age").setKey("age").setSortable(true);
            messagesGrid.setItems(dbServiceContactUs.findAllFeedbacks());
        } else
            messagesGrid.setItems(dbServiceContactUs.findAllFeedbacksByUsername(currentUser.getUsername()));

        messagesGrid.addColumn(ContactUs::getMessage).setHeader("Message").setKey("message").setSortable(true);
        messagesGrid.addColumn(ContactUs::getCreatedAt).setHeader("Created At").setKey("createdAt").setSortable(true);

        messagesGrid.addItemClickListener(contactUsItemClickEvent -> {
            NativeLabel messageSenderLbl = new NativeLabel("Sent by " + contactUsItemClickEvent.getItem().getMessageOwner());
            NativeLabel messageSentAtLbl = new NativeLabel(   contactUsItemClickEvent.getItem().getCreatedAt().format( DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm a")));
            NativeLabel messageLbl = new NativeLabel(contactUsItemClickEvent.getItem().getMessage());
            VerticalLayout messageDetailsVl = new VerticalLayout(messageSenderLbl, messageSentAtLbl);
            messageLbl.getElement().getStyle().set("font-size", "larger");
            messageLbl.getElement().getStyle().set("font-weight", "bolder");
            messageLbl.getElement().getStyle().set("align-self", "center");
            messageSenderLbl.getElement().getStyle().set("font-weight", "bolder");
            messageDetailsVl.getElement().getStyle().set("align-items", "self-end");

            VerticalLayout messageVl = new VerticalLayout(messageLbl, messageDetailsVl);
            Dialog messageDialog = new Dialog(messageVl);
            messageDialog.setMaxWidth("60%");
            messageDialog.setMinWidth("20%");
            messageDialog.open();
        });
        messagesVl.add(messagesGrid);
    }

    public void setSendMessageVl() {
        Button submitButton = new Button("Submit");
        createMessageVl.setSizeFull();
        createMessageVl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        H2 contactUsH2 = new H2("Contact Us");
        H1 letTalkH1 = new H1("Let's Talk about your Project");
        H4 dropLineH4 = new H4("Drop us a line through the form below and we'll get back to you.");
        createMessageVl.add(contactUsH2, letTalkH1, dropLineH4);

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

        createMessageVl.add(vl);
        submitButton.addClickListener(buttonClickEvent -> saveComment());
    }

    public void saveComment() {
        if (checkFields()) {
            ContactUs newContactUs = new ContactUs();
            newContactUs.setFirstName(firstNameField.getValue());
            newContactUs.setLastName(lastNameField.getValue());
            newContactUs.setEmail(emailField.getValue());
            newContactUs.setGender(String.valueOf(genderCombo.getValue()));
            newContactUs.setAge(String.valueOf(ageField.getValue()));
            newContactUs.setMessage(messageField.getValue());
            newContactUs.setCreatedAt(LocalDateTime.now());
            newContactUs.setMessageOwner(currentUser.getUsername());
            dbServiceContactUs.saveContact(newContactUs);
            clearFields();
            Notification.show("Thank you for your message! We will get in touch with you soon.");
        }
    }

    private boolean checkFields() {
        AtomicBoolean continueFlag = new AtomicBoolean(true);
        InputChecker.checkTextField(firstNameField, continueFlag);
        InputChecker.checkTextField(lastNameField, continueFlag);
        InputChecker.checkEmailField(emailField, continueFlag);
        InputChecker.checkCombo(genderCombo, continueFlag);
        InputChecker.checkNumberField(ageField, continueFlag);
        InputChecker.checkTextAreaField(messageField, continueFlag);

        return continueFlag.get();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        genderCombo.clear();
        ageField.clear();
        messageField.clear();
    }
}

