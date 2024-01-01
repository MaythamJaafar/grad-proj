package com.example.application.views;

import com.example.application.db.dbServices.DBServiceEntityUser;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.home.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@org.springframework.stereotype.Component
@UIScope
public class test extends AppLayout {
    private final DBServiceEntityUser dbServiceUser;
    private final AuthenticatedUser authenticatedUser;
    private final UI ui = UI.getCurrent();
    private final User currentUser;
    private final AtomicInteger dashboardTabClick = new AtomicInteger(0);
    private String tabSelectedName = "";
    private Tabs tabs;
    private Tab dashboardTab;
    private H2 viewTitle;

    public test(AuthenticatedUser authenticatedUser, DBServiceEntityUser dbServiceUser) {
        this.authenticatedUser = authenticatedUser;
        this.dbServiceUser = dbServiceUser;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        tabs = getTabs();
        addToNavbar(true, createHeaderContent());
        Scroller tabsScroller = new Scroller(tabs);
        tabsScroller.setHeight("100%");
        tabsScroller.setWidth("100%");
        VerticalLayout verticalLayout = new VerticalLayout(tabsScroller);
        verticalLayout.getElement().getStyle().set("padding", "0");
        verticalLayout.getElement().getStyle().set("margin", "0");
        verticalLayout.setHeight("100%");
        verticalLayout.getElement().getStyle().set("justify-content", "space-between");
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addToDrawer(verticalLayout);
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.setWidth("100%");
        header.setHeight("100%");
        header.getElement().getStyle().set("background-color", "royalblue");
        header.getElement().getStyle().set("justify-content", "space-between");
        header.addClassName("header-shadow");
        header.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Width.MEDIUM);

        Div layout = new Div();
        layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.MEDIUM);
        layout.getElement().getStyle().set("justify-content", "space-between");

        H4 appName = new H4("My App");
        appName.addClassNames(LumoUtility.Margin.Vertical.MEDIUM, LumoUtility.Margin.End.AUTO, LumoUtility.FontSize.MEDIUM);
        Image image = new Image("./images/logo.png", "Logo");
        image.getElement().getStyle().set("margin-left", "-20px");
        image.setWidth("160px");
        image.setHeight("60px");
        image.addClassName("pointer");
        image.addClickListener(imageClickEvent -> {
            RouterLink routerLink = new RouterLink();
            routerLink.setRoute(HomeView.class);
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout(image);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(horizontalLayout);

        Avatar avatar = new Avatar(currentUser.getFullName());
        avatar.setImage("./images/pp.png");
        avatar.setThemeName("xsmall");
        avatar.getElement().setAttribute("tabindex", "-1");

        MenuBar userMenu = new MenuBar();
        userMenu.addClassName("pointer");
        userMenu.setThemeName("tertiary-inline contrast");

        Div div = new Div();
        div.add(avatar);
        NativeLabel text = new NativeLabel(currentUser.getFullName().split("\s")[0]);
        text.getElement().getStyle().set("color", "white");
        div.add(text);
        Icon dropDownIcon = new Icon("lumo", "dropdown");
        dropDownIcon.setColor("white");
        div.add(dropDownIcon);
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("align-items", "center");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        div.addClassName("pointer");

        MenuItem userName = userMenu.addItem(div);
        userName.addClassName("pointer");
        userName.getSubMenu().addItem("Change password", e -> changePassword(currentUser));
        userName.getSubMenu().addItem("Sign out", e -> authenticatedUser.logout());

        layout.add(userMenu);
        header.add(layout);
        return header;
    }

    private void addDrawerContent() {
        H1 appName = new H1("My App");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        addToDrawer(header, createFooter());
    }

    private void changePassword(User entityUser) {
        Dialog dialog = new Dialog();
        PasswordField newPasswordTxt = new PasswordField();
        newPasswordTxt.setWidth("70%");
        PasswordField confirmNewPasswordTxt = new PasswordField();
        confirmNewPasswordTxt.setWidth("70%");
        VerticalLayout verticalLayout = new VerticalLayout(newPasswordTxt, confirmNewPasswordTxt);
        verticalLayout.getElement().getStyle().set("padding", "0");
        verticalLayout.getElement().getStyle().set("margin", "0");
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dialog.add(verticalLayout);
        dialog.getElement().getStyle().set("align-item", "center");
        Button changePasswordButton = new Button("Save", (e) -> {
            if (newPasswordTxt.getValue().equals(confirmNewPasswordTxt.getValue())) {
                dialog.close();
                entityUser.setPassword(new BCryptPasswordEncoder().encode(confirmNewPasswordTxt.getValue()));
                dbServiceUser.saveUser(entityUser);
            }
        });
        changePasswordButton.setEnabled(false);

        dialog.setHeaderTitle(
                String.format("Change password of \"%s\"?", entityUser.get_id()));
        newPasswordTxt.setPlaceholder("New Password");
        newPasswordTxt.addValueChangeListener(event -> {
            ui.access(() -> {
                confirmNewPasswordTxt.clear();
                confirmNewPasswordTxt.setInvalid(false);
            });
        });
        confirmNewPasswordTxt.setPlaceholder("Confirm New Password");
        confirmNewPasswordTxt.addValueChangeListener(event -> {
            confirmNewPasswordTxt.setInvalid(false);
            changePasswordButton.setEnabled(event.getValue() != null && event.getValue().equals(newPasswordTxt.getValue()));
            if (event.getValue() != null && !event.getValue().equals(newPasswordTxt.getValue())) {
                ui.access(() -> {
                    confirmNewPasswordTxt.setInvalid(true);
                    confirmNewPasswordTxt.setErrorMessage("Password doesnt match ");
                });

            }
        });

        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePasswordButton.getStyle().set("margin-right", "auto");
        dialog.getFooter().add(changePasswordButton);

        Button cancelButton = new Button("Cancel", (e) -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);
        dialog.open();
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.setWidthFull();
        dashboardTab = createTab(VaadinIcon.DASHBOARD, dashboardTabClick, "Dashboard", HomeView.class);
        tabs.add(dashboardTab);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        return tabs;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    private Tab createTab(VaadinIcon viewIcon, AtomicInteger click, String viewName, Class<? extends Component> navigationTarget) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        Span span = new Span(viewName);
        span.getElement().getStyle().set("margin-top", "-10px");
        span.getElement().getStyle().set("font-size", "small");
        span.getElement().getStyle().set("text-align", "center");
        VerticalLayout verticalLayout = new VerticalLayout(icon, span);
        verticalLayout.getElement().getStyle().set("padding", "0");
        verticalLayout.getElement().getStyle().set("margin", "5px");
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        link.add(verticalLayout);
        link.setTabIndex(-1);
        link.setRoute(navigationTarget);
        Tab tab = new Tab(link);
        tab.addClassName("pointer");
        tab.getElement().getStyle().set("padding", "0");
        tab.getElement().addEventListener("click", event -> {
            String oldTabSelectedName = tabSelectedName;
            tabSelectedName = viewName;
            if (click.get() > 0 && oldTabSelectedName.equals(tabSelectedName)) {
                ui.getPage().reload();
            }
            click.getAndIncrement();
        });
        tab.getElement().getStyle().set("font-size", "smaller");
        return tab;
    }

    private void setClicksZero() {
        dashboardTabClick.set(0);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        if (tabs != null)
            ui.access(() -> {
                setClicksZero();
                switch (getCurrentPageTitle()) {
                    case "Dashboard" -> tabs.setSelectedTab(dashboardTab);
                    default -> tabs.setSelectedTab(null);
                }
            });
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}