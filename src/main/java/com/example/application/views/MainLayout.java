package com.example.application.views;

import com.example.application.db.dbServices.DBServiceEntityUser;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.util.InputChecker;
import com.example.application.views.baby.BabyView;
import com.example.application.views.contact.ContactUs;
import com.example.application.views.dashboard.DashboardView;
import com.example.application.views.expense.ExpenseView;
import com.example.application.views.eye.EyeSuppView;
import com.example.application.views.faq.FaqView;
import com.example.application.views.medicine.MedicineView;
import com.example.application.views.pcare.PCareView;
import com.example.application.views.sportSup.SportSuppView;
import com.example.application.views.user.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
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
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.application.util.Util.showErrorNotification;

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
        getElement().getStyle().set("background-image", "url(https://www.transparenttextures.com/patterns/brick-wall.png)");
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.setHeight("100%");
        header.addClassName("header-shadow");
        header.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Width.MEDIUM);

        MenuBar userMenu = new MenuBar();
        userMenu.addClassName("pointer");
        userMenu.setThemeName("tertiary-inline contrast");

        Div layout = new Div();
        layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.MEDIUM);

        Avatar avatar = new Avatar(currentUser.getFullName());
        avatar.setImage("./images/pp.png");
        avatar.setThemeName("xsmall");
        avatar.getElement().setAttribute("tabindex", "-1");

        Div div = new Div();
        div.add(avatar);
        NativeLabel text = new NativeLabel(currentUser.getFullName().split(" ")[0]);
        text.getElement().getStyle().set("color", "grey");
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

        Button themeBtn = new Button();
        themeBtn.setWidth("20px");
        themeBtn.setHeight("20px");

        Icon nightIcon = VaadinIcon.MOON_O.create();
        Icon dayIcon = VaadinIcon.SUN_O.create();

        themeBtn.setIcon(nightIcon);
        themeBtn.addClickListener(buttonClickEvent -> {
            UI.getCurrent().getElement().getThemeList().clear();
            if (themeBtn.getIcon().equals(nightIcon)) {
                getElement().getThemeList().add("dark-background");
                getElement().getStyle().set("background-color", "#333");
                getElement().getStyle().set("color", "#fff");
                themeBtn.setIcon(dayIcon);
            } else {
                getElement().getThemeList().add("light-background");
                getElement().getStyle().set("background-color", "#f0f0f0");
                getElement().getStyle().set("color", "#333");
                themeBtn.setIcon(nightIcon);
            }
        });

        layout.add(userMenu, themeBtn);
        layout.getElement().getStyle().set("margin-top", "12px");

        header.add(layout);

        layout.add(userMenu);
        header.add(layout);
        header.getElement().getStyle().set("margin-right", "100px");
        return header;
    }

    private void addHeaderContent() {
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        viewTitle.getElement().getStyle().set("align-self", "center");
        viewTitle.getElement().getStyle().set("margin-left", "20px");
        viewTitle.getElement().getStyle().set("margin-top", "12px");

        HorizontalLayout headerHl = new HorizontalLayout(viewTitle, createHeaderContent());
        headerHl.getElement().getStyle().set("justify-content", "space-between");
        headerHl.setWidthFull();
        headerHl.setHeight("50px");
        headerHl.getElement().getStyle().set("background-image", "url(https://www.transparenttextures.com/patterns/brick-wall.png)");
        addToNavbar(true, headerHl);

    }

    private void addDrawerContent() {
        H1 appName = new H1("M/J");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        appName.getElement().getStyle().set("align-self", "center");
        Header header = new Header(appName);
        header.getElement().getStyle().set("text-align", "center");

        Scroller scroller = new Scroller(createNavigation());
        NativeLabel label = new NativeLabel();
        label.add(header, scroller, createFooter());
        label.addClassNames("drawer-section", "drawerLayout");
        addToDrawer(label);
    }

    private void changePassword(User entityUser) {
        Dialog dialog = new Dialog();
        PasswordField newPasswordTxt = new PasswordField();
        newPasswordTxt.setWidth("70%");

        PasswordField confirmNewPasswordTxt = new PasswordField();
        confirmNewPasswordTxt.setWidth("70%");

        NativeLabel titleLbl = new NativeLabel("Change password of " + entityUser.get_id() + " (" + entityUser.getRole() + ") ?");
        titleLbl.getElement().getStyle().set("color", "grey");
        titleLbl.getElement().getStyle().set("font-size", "larger");

        VerticalLayout verticalLayout = new VerticalLayout(titleLbl, newPasswordTxt, confirmNewPasswordTxt);
        verticalLayout.getElement().getStyle().set("padding", "0");
        verticalLayout.getElement().getStyle().set("margin", "0");
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
//        dialog.getElement().getStyle().set("align-item", "center");
        dialog.add(verticalLayout);

        Button changePasswordButton = new Button("Save", (e) -> {
            AtomicBoolean continueFlag = new AtomicBoolean(true);
            InputChecker.checkPasswordField(newPasswordTxt, continueFlag);
            InputChecker.checkPasswordField(confirmNewPasswordTxt, continueFlag);

            if (continueFlag.get())
                if (newPasswordTxt.getValue().equals(confirmNewPasswordTxt.getValue())) {
                    dialog.close();
                    entityUser.setPassword(new BCryptPasswordEncoder().encode(confirmNewPasswordTxt.getValue()));
                    dbServiceUser.saveUser(entityUser);
                } else
                    showErrorNotification("Passwords do not match");
        });


        newPasswordTxt.setPlaceholder("New Password");
        newPasswordTxt.addValueChangeListener(event -> {
            confirmNewPasswordTxt.clear();
            confirmNewPasswordTxt.setInvalid(false);
        });
        confirmNewPasswordTxt.setPlaceholder("Confirm New Password");

        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePasswordButton.getStyle().set("margin-right", "auto");
        dialog.setWidth("30%");

        Button cancelButton = new Button("Cancel", (e) -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.getFooter().add(changePasswordButton, cancelButton);
        dialog.open();
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, LineAwesomeIcon.DATABASE_SOLID.create()));
        nav.addItem(new SideNavItem("MedicineGrid", MedicineView.class, LineAwesomeIcon.HEART.create()));
        if (currentUser.getRole().equals(User.Role.SUPER_ADMIN))
            nav.addItem(new SideNavItem("UserGrid", UserView.class, LineAwesomeIcon.USER.create()));
        if (currentUser.getRole().equals(User.Role.SUPER_ADMIN) || currentUser.getRole().equals(User.Role.ADMIN))
            nav.addItem(new SideNavItem("Expenses", ExpenseView.class, LineAwesomeIcon.WALLET_SOLID.create()));
        nav.addItem(new SideNavItem("SportSupp", SportSuppView.class, LineAwesomeIcon.DUMBBELL_SOLID.create()));
        nav.addItem(new SideNavItem("Contact", ContactUs.class, LineAwesomeIcon.PHONE_SOLID.create()));
        nav.addItem(new SideNavItem("PCare", PCareView.class, LineAwesomeIcon.PERSON_BOOTH_SOLID.create()));
        nav.addItem(new SideNavItem("FAQ", FaqView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
        nav.addItem(new SideNavItem("Eye", EyeSuppView.class, LineAwesomeIcon.EYE.create()));
        nav.addItem(new SideNavItem("Baby", BabyView.class, LineAwesomeIcon.BABY_SOLID.create()));
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        NativeLabel text = new NativeLabel("Powered By @Maytham Jaafar");
        text.getElement().getStyle().set("color", "grey");
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
