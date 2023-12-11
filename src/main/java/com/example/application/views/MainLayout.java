package com.example.application.views;

import com.example.application.db.dbServices.DBServiceEntityUser;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.about.AboutView;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.datagrid.DataGridView;
import com.example.application.views.feed.FeedView;
import com.example.application.views.gridwithfilters.GridwithFiltersView;
import com.example.application.views.helloworld.HelloWorldView;
import com.example.application.views.masterdetail.MasterDetailView;
import com.example.application.views.personform.PersonFormView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private final User currentUser;
    private final DBServiceEntityUser dbServiceUser;
    private final AuthenticatedUser authenticatedUser;

    public MainLayout(AuthenticatedUser authenticatedUser, DBServiceEntityUser dbServiceUser) {
        this.authenticatedUser = authenticatedUser;
        this.dbServiceUser = dbServiceUser;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.setWidth("100%");
        header.setHeight("100%");
        header.getElement().getStyle().set("justify-content", "space-between");
        header.addClassName("header-shadow");
        header.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Width.MEDIUM);

        MenuBar userMenu = new MenuBar();
        userMenu.addClassName("pointer");
        userMenu.setThemeName("tertiary-inline contrast");

        Div layout = new Div();
        layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.MEDIUM);
        layout.getElement().getStyle().set("justify-content", "space-between");


        Avatar avatar = new Avatar(currentUser.getFullName());
        avatar.setImage("./images/pp.png");
        avatar.setThemeName("xsmall");
        avatar.getElement().setAttribute("tabindex", "-1");

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

        MenuItem usernameMenuItem = userMenu.addItem(div);
        usernameMenuItem.addClassName("pointer");
        usernameMenuItem.getSubMenu().addItem("Change password", e -> changePassword(currentUser));
        usernameMenuItem.getSubMenu().addItem("Sign out", e -> authenticatedUser.logout());

        layout.add(userMenu);
        header.add(layout);
        return header;
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
        addToNavbar(true, createHeaderContent());

    }

    private void addDrawerContent() {
        H1 appName = new H1("My App");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
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
            confirmNewPasswordTxt.clear();
            confirmNewPasswordTxt.setInvalid(false);
        });
        confirmNewPasswordTxt.setPlaceholder("Confirm New Password");
        confirmNewPasswordTxt.addValueChangeListener(event -> {
            confirmNewPasswordTxt.setInvalid(false);
            changePasswordButton.setEnabled(event.getValue() != null && event.getValue().equals(newPasswordTxt.getValue()));
            if (event.getValue() != null && !event.getValue().equals(newPasswordTxt.getValue())) {
                confirmNewPasswordTxt.setInvalid(true);
                confirmNewPasswordTxt.setErrorMessage("Password doesnt match ");

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

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Hello World", HelloWorldView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.FILE.create()));
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.CHART_AREA_SOLID.create()));
        nav.addItem(new SideNavItem("Data Grid", DataGridView.class, LineAwesomeIcon.TH_SOLID.create()));
        nav.addItem(new SideNavItem("Master-Detail", MasterDetailView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
        nav.addItem(new SideNavItem("Person Form", PersonFormView.class, LineAwesomeIcon.USER.create()));
        nav.addItem(new SideNavItem("Grid with Filters", GridwithFiltersView.class, LineAwesomeIcon.FILTER_SOLID.create()));
        nav.addItem(new SideNavItem("Feed", FeedView.class, LineAwesomeIcon.LIST_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
