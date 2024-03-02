package com.example.application.views.home;

import com.example.application.util.Util;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.ui.Transport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.scheduling.annotation.Scheduled;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout {
    private final VerticalLayout branchDetailsVl = new VerticalLayout();
    private final Tab airportTab = new Tab("Airport Highway Branch");
    private final Tab hamraTab = new Tab("Hamra Branch");
    private final Tabs tabs = new Tabs(hamraTab, airportTab);
    private final TextField phoneNbLbl = new TextField("Phone");
    private final TextField addressLbl = new TextField("Address");
    private final TextField workTimeLbl = new TextField("Work Time");
    private final TextField categoriesLbl = new TextField("Categories");
    private final TextField emailLbl = new TextField("Email");
    private final H3 customNotificationH3 = new H3("Coming Soon!!");

    public HomeView() {
        setSizeFull();

        setBranchDetails("+9615-999887", "Hamra Street, Front American University of Beirut", "24/7", "Medicines", "hamra@mj.com", false);
        tabs.getElement().getStyle().set("align-self", "center");
        tabs.addSelectedChangeListener(tab -> {
            if (tab.getSelectedTab().equals(airportTab)) {
                setBranchDetails("+9615-999888", "Airport Highway, Beside Golden Plaza Hotel", "24/7", "Medicines/Beauty", "airport@mj.com", true);
            } else {
                setBranchDetails("+9615-999887", "Hamra Street, Front American University of Beirut", "24/7", "Medicines", "hamra@mj.com", false);
            }
        });

        branchDetailsVl.setSizeFull();
        branchDetailsVl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        H2 visitH2 = new H2("M/J's Pharmacies");
        H1 findH1 = new H1("You'll find whatever you need");
        H4 expertH4 = new H4("Expert Pharmacists to help you.");
        branchDetailsVl.add(visitH2, findH1, expertH4, customNotificationH3);

        phoneNbLbl.addClassName("rounded");
        addressLbl.addClassName("rounded");
        workTimeLbl.addClassName("rounded");
        categoriesLbl.addClassName("rounded");
        workTimeLbl.addClassName("rounded");
        emailLbl.addClassName("rounded");

        phoneNbLbl.setReadOnly(true);
        addressLbl.setReadOnly(true);
        categoriesLbl.setReadOnly(true);
        workTimeLbl.setReadOnly(true);
        emailLbl.setReadOnly(true);

        HorizontalLayout categoriesWorkHl = new HorizontalLayout(categoriesLbl, workTimeLbl);
        HorizontalLayout btnHl = new HorizontalLayout(addGoogleMapsButton(), addFbButton(), addTwButton());
        btnHl.setWidthFull();

        VerticalLayout vl = new VerticalLayout();
        vl.add(addressLbl, categoriesWorkHl, phoneNbLbl, emailLbl, btnHl);
        vl.setAlignItems(Alignment.CENTER);
        vl.setWidth("40%");

        phoneNbLbl.setWidthFull();
        emailLbl.setWidthFull();

        categoriesLbl.setWidth("70%");
        workTimeLbl.setWidth("30%");
        categoriesWorkHl.setWidthFull();

        addressLbl.setWidthFull();
        workTimeLbl.setWidthFull();

        branchDetailsVl.add(vl);

        tabs.setSelectedTab(hamraTab);

        if (VaadinSession.getCurrent().getAttribute("firstLogin") != null)
            add(tabs, branchDetailsVl);
        else
            createComingSoonDialog();
        customNotificationH3.getElement().getStyle().set("color", "red");
    }

    public void createComingSoonDialog() {
        VaadinSession.getCurrent().setAttribute("firstLogin","1");
        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        dialog.setModal(true);
        dialog.setWidth("70%");

        H2 hamraH2 = new H2("Hamra Branch");
        H2 airportH2 = new H2("Airport Highway Branch");
        H4 comingSoonH4 = new H4("Coming Soon!!");
        comingSoonH4.getElement().getStyle().set("color", "red");

        Button seeDetailsBtn = Util.createEditButton();
        seeDetailsBtn.setText("See Details");
        seeDetailsBtn.addClickListener(clickEvent -> {
            add(tabs, branchDetailsVl);
            dialog.close();
        });

        VerticalLayout hamraBranchCard = new VerticalLayout(hamraH2, seeDetailsBtn);
        VerticalLayout airportBranchCard = new VerticalLayout(airportH2, comingSoonH4);
        hamraBranchCard.getElement().getStyle().set("align-items", "center");
        airportBranchCard.getElement().getStyle().set("align-items", "center");
        hamraBranchCard.addClassName("card");
        airportBranchCard.addClassName("card");
        hamraBranchCard.setWidthFull();
        airportBranchCard.setWidthFull();

        HorizontalLayout dialogHl = new HorizontalLayout(hamraBranchCard, airportBranchCard);
        dialog.getElement().getStyle().set("background-image", "url(https://www.transparenttextures.com/patterns/brick-wall.png)");
        dialog.add(dialogHl);
        dialog.open();
    }

    public void setBranchDetails(String phone, String address, String workTime, String categories, String email, boolean comingSoon) {
        phoneNbLbl.setValue(phone);
        addressLbl.setValue(address);
        workTimeLbl.setValue(workTime);
        categoriesLbl.setValue(categories);
        emailLbl.setValue(email);
        customNotificationH3.setVisible(comingSoon);
    }

    private Button addGoogleMapsButton() {
        double latitude = 37.7749;
        double longitude = -122.4194;

        String url = "https://www.google.com/maps?q=" + latitude + "," + longitude;

        Button googleMapsButton = new Button("Open in Google Maps", new Icon(VaadinIcon.MAP_MARKER));
        googleMapsButton.addClickListener(event -> getUI().ifPresent(ui -> ui.getPage().executeJs("window.open('" + url + "', '_blank')")));

        return googleMapsButton;
    }

    private Button addFbButton() {
        String url = "https://www.facebook.com/test";

        Button googleMapsButton = new Button("Facebook Page", new Icon(VaadinIcon.FACEBOOK));
        googleMapsButton.addClickListener(event -> getUI().ifPresent(ui -> ui.getPage().executeJs("window.open('" + url + "', '_blank')")));

        return googleMapsButton;
    }

    private Button addTwButton() {
        String url = "https://www.twitter.com/test";

        Button googleMapsButton = new Button("X Page", new Icon(VaadinIcon.TWITTER));
        googleMapsButton.addClickListener(event -> getUI().ifPresent(ui -> ui.getPage().executeJs("window.open('" + url + "', '_blank')")));

        return googleMapsButton;
    }

}
