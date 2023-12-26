package com.example.application.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "contact", layout = MainLayout.class)
@PageTitle("Contact")
@PermitAll
public class Contact extends VerticalLayout {

}
