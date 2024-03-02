package com.example.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;

@AnonymousAllowed
@PageTitle("Sign In")
@Slf4j
@Route(value = "signin")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm loginForm = new LoginForm();
    private final RouterLink signupLink = new RouterLink();

    //test
    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n loginI18n = new LoginI18n();
        LoginI18n.Form form = new LoginI18n.Form();
        form.setTitle("Sign-In");
        form.setSubmit("Sign In");
        form.setUsername("Username");
        form.setPassword("Password");
        loginI18n.setForm(form);
        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Error in credentials");
        errorMessage.setMessage("Error Username or Password , Please check and try again");
        loginI18n.setErrorMessage(errorMessage);
        loginForm.setI18n(loginI18n);
        getElement().getStyle().set("box-radius", "10px");
        getElement().getStyle().set("background-image", "url(https://www.transparenttextures.com/patterns/brick-wall.png)");
        getElement().getStyle().set("background-color", "#ffffff");

        Image signInWithUaePassImg = new Image();
        signInWithUaePassImg.setSrc("./images/uaePass.png");
        signInWithUaePassImg.setWidth("26px");
        signInWithUaePassImg.setHeight("23px");
        signInWithUaePassImg.getElement().getStyle().set("margin", "-13px");
        signInWithUaePassImg.addClassName("pointer");

        HorizontalLayout signInWithUaePassBtn = new HorizontalLayout();
        NativeLabel signInWithUaePassLabel = new NativeLabel("Sign in with UAE PASS");
        signInWithUaePassLabel.getElement().getStyle().set("font-weight", "bold");
        signInWithUaePassLabel.addClassName("pointer");
        signInWithUaePassBtn.add(signInWithUaePassImg, signInWithUaePassLabel);
        signInWithUaePassBtn.setWidthFull();
        signInWithUaePassBtn.setMinWidth("300px");
        signInWithUaePassBtn.setMinHeight("44px");
        signInWithUaePassBtn.getElement().getStyle().set("padding-top", "10px");
        signInWithUaePassBtn.getElement().getStyle().set("padding-right", "30px");
        signInWithUaePassBtn.getElement().getStyle().set("padding-bottom", "10px");
        signInWithUaePassBtn.getElement().getStyle().set("padding-left", "30px");
        signInWithUaePassBtn.getElement().getStyle().set("border-style", "solid");
        signInWithUaePassBtn.getElement().getStyle().set("border-radius", "10px");
        signInWithUaePassBtn.getElement().getStyle().set("border-width", "thin");
        signInWithUaePassBtn.getElement().getStyle().set("justify-content", "center");
        signInWithUaePassBtn.setAlignItems(Alignment.CENTER);

        signInWithUaePassBtn.addClassName("pointer");

        signupLink.setText("Don't have an account? Sign-Up here");
        signupLink.getElement().getStyle().set("font-size", "16px");
        signupLink.getElement().getStyle().set("font-weight", "bold");
        signupLink.setRoute(SignUpView.class);

        add(loginForm, signupLink/*, reCaptcha*/);
        addClassName("login-rich-content");

        loginForm.setAction("signin");

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }

}