package com.example.application.views;

import com.example.application.db.model.User;
import com.example.application.db.repo.RepoUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.application.util.InputChecker.*;

@AnonymousAllowed
@PageTitle("SignUp")
@Slf4j
@Route(value = "signup")
public class SignUpView extends VerticalLayout {
    private final TextField fullNameTxt = new TextField("Full Name");
    private final TextField usernameTxt = new TextField("Username");
    private final EmailField emailField = new EmailField("Email");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField passwordConfirm = new PasswordField("Confirm Password");

    public SignUpView(RepoUser repoUser) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getElement().getStyle().set("box-radius", "10px");
        getElement().getStyle().set("background-image", "url(https://www.transparenttextures.com/patterns/brick-wall.png)");
        getElement().getStyle().set("background-color", "#ffffff");

        fullNameTxt.setWidth("100%");
        usernameTxt.setWidth("100%");
        emailField.setWidth("100%");
        password.setWidth("100%");
        passwordConfirm.setWidth("100%");
        fullNameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        usernameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        emailField.setValueChangeMode(ValueChangeMode.EAGER);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        passwordConfirm.setValueChangeMode(ValueChangeMode.EAGER);
        fullNameTxt.addValueChangeListener(event -> fullNameTxt.setInvalid(false));
        usernameTxt.addValueChangeListener(event -> usernameTxt.setInvalid(false));
        emailField.addValueChangeListener(event -> emailField.setInvalid(false));
        emailField.setPlaceholder("username@example.com");

        fullNameTxt.setRequired(true);
        usernameTxt.setRequired(true);
        emailField.setRequired(true);
        password.setRequired(true);
        passwordConfirm.setRequired(true);

        User newUser = new User();
        newUser.setRole(User.Role.CLIENT);

        password.addValueChangeListener(event -> password.setInvalid(false));
        passwordConfirm.addValueChangeListener(event -> passwordConfirm.setInvalid(false));

        Button signUpBtn = new Button("Sign Up");
        signUpBtn.addClassName("pointer");
        signUpBtn.setWidth("100%");

        H3 signUpTitle = new H3("Sign-Up");

        RouterLink signInLink = new RouterLink();
        signInLink.setText("Already have an account? Sign-In here");

        signInLink.getElement().getStyle().set("font-size", "16px");
        signInLink.getElement().getStyle().set("font-weight", "bold");
        signInLink.setRoute(LoginView.class);

        VerticalLayout signUpVl = new VerticalLayout();
        signUpVl.add(signUpTitle, fullNameTxt, usernameTxt, emailField, password, passwordConfirm, signUpBtn);
        signUpVl.setAlignItems(Alignment.CENTER);
        signUpVl.getElement().getStyle().set("background", "#FFFFFF");
        signUpVl.setWidth("350px");
        signUpBtn.addClickListener(event -> {
            if (!checkFields())
                return;
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = new User();
            user.set_id(usernameTxt.getValue());
            user.setRole(User.Role.CLIENT);
            user.setFullName(fullNameTxt.getValue());
            user.setUsername(usernameTxt.getValue());
            user.setJoinedAt(LocalDate.now());
            user.setPassword(passwordEncoder.encode(password.getValue()));
            repoUser.save(user);
        });
        signUpVl.getElement().getStyle().set("display", "table-header-group");
        add(signUpVl, signInLink);

    }

    private boolean checkFields() {
        AtomicBoolean continueSignUp = new AtomicBoolean(true);
        checkTextField(fullNameTxt, continueSignUp);
        checkTextField(usernameTxt, continueSignUp);
        checkEmailField(emailField, continueSignUp);
        checkPasswordField(password, continueSignUp);
        checkPasswordField(passwordConfirm, continueSignUp);
        if (password.getValue() != null && passwordConfirm.getValue() != null && !password.getValue().equals(passwordConfirm.getValue())) {
            passwordConfirm.setInvalid(true);
            passwordConfirm.setErrorMessage("Passwords do not match");
            continueSignUp.set(false);
        }
        return continueSignUp.get();
    }
}