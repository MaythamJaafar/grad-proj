package com.example.application.views.sportSup;

import com.example.application.db.dbServices.DBServiceSportSupp;
import com.example.application.db.model.SportSupp;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.example.application.views.pcare.PCareForm;
import com.example.application.views.user.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import static com.example.application.util.Util.*;

@Route(value = "sportSuppGrid", layout = MainLayout.class)
@PageTitle("SportSupp Grid")
@PermitAll
public class SportSuppView extends VerticalLayout {
    public final DBServiceSportSupp dbServiceSportSupp ;
    private final Button addNewSportSUppBtn = new Button("Add New SportSupp");
    public final Grid<SportSupp> sportSuppGrid = new Grid<>();
    private final Dialog addSportSuppDialog = new Dialog();
    private GridListDataView<SportSupp> gridListDataView;
    private final User currentUser;

    public SportSuppView(DBServiceSportSupp dbServiceSportSupp, AuthenticatedUser authenticatedUser) {
        this.dbServiceSportSupp = dbServiceSportSupp;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        createGrid();
        setSizeFull();
        setFilters();
        addNewSportSUppBtn.addClickListener(buttonClickEvent ->  addSportSuppDialog.open());
        addSportSuppDialog.add(new SportSuppForm(dbServiceSportSupp,addSportSuppDialog,sportSuppGrid,gridListDataView));

    }

    private void setFilters() {
        HeaderRow filterRow = sportSuppGrid.appendHeaderRow();

        TextField nameTxt = new TextField();
        filterRow.getCell(sportSuppGrid.getColumnByKey("name")).setComponent(nameTxt);
        nameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        nameTxt.setClearButtonVisible(true);
        nameTxt.setWidthFull();
        nameTxt.setPlaceholder("Name");
        nameTxt.getElement().getStyle().set("font-size", "12px");

        TextField categoryTxt = new TextField();
        filterRow.getCell(sportSuppGrid.getColumnByKey("category")).setComponent(categoryTxt);
        categoryTxt.setValueChangeMode(ValueChangeMode.EAGER);
        categoryTxt.setClearButtonVisible(true);
        categoryTxt.setPlaceholder("Category");
        categoryTxt.getElement().getStyle().set("font-size", "12px");


        TextField detailsTxt = new TextField();
        filterRow.getCell(sportSuppGrid.getColumnByKey("details")).setComponent(detailsTxt);
        detailsTxt.setValueChangeMode(ValueChangeMode.EAGER);
        detailsTxt.setClearButtonVisible(true);
        detailsTxt.setPlaceholder("Details");
        detailsTxt.getElement().getStyle().set("font-size", "12px");

        TextField batchNoTxt = new TextField();
        filterRow.getCell(sportSuppGrid.getColumnByKey("batchNb")).setComponent(batchNoTxt);
        batchNoTxt.setValueChangeMode(ValueChangeMode.EAGER);
        batchNoTxt.setClearButtonVisible(true);batchNoTxt.setPlaceholder("Batch Nb");
        batchNoTxt.getElement().getStyle().set("font-size", "12px");


        TextField quantityTxt = new TextField();
        filterRow.getCell(sportSuppGrid.getColumnByKey("quantity")).setComponent(quantityTxt);
        quantityTxt.setValueChangeMode(ValueChangeMode.EAGER);
        quantityTxt.setClearButtonVisible(true);
        quantityTxt.setPlaceholder("Quantity");
        quantityTxt.getElement().getStyle().set("font-size", "12px");

        DatePicker expiryDatePicker = new DatePicker();
        filterRow.getCell(sportSuppGrid.getColumnByKey("expiryDate")).setComponent(expiryDatePicker);
        expiryDatePicker.setClearButtonVisible(true);
        expiryDatePicker.setPlaceholder("Expiry Date");
        expiryDatePicker.getElement().getStyle().set("font-size", "12px");


        TextField buyingPriceTxt = new TextField();
        if(!currentUser.getRole().equals(User.Role.CLIENT))
            filterRow.getCell(sportSuppGrid.getColumnByKey("buyingPrice")).setComponent(buyingPriceTxt);
        buyingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        buyingPriceTxt.setClearButtonVisible(true);
        buyingPriceTxt.setPlaceholder("Buying Price");
        buyingPriceTxt.getElement().getStyle().set("font-size", "12px");

        TextField sellingPriceTxt = new TextField();
        filterRow.getCell(sportSuppGrid.getColumnByKey("sellingPrice")).setComponent(sellingPriceTxt);
        sellingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        sellingPriceTxt.setClearButtonVisible(true);
        sellingPriceTxt.setPlaceholder("selling Price");
        sellingPriceTxt.getElement().getStyle().set("font-size", "12px");

        nameTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        categoryTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        detailsTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        batchNoTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        quantityTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        expiryDatePicker.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        buyingPriceTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));
        sellingPriceTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt));


    }

    private void applyFilter(TextField nameTxt, TextField categoryTxt, TextField detailsTxt, TextField batchNoTxt, TextField quantityTxt, DatePicker expiryDatePicker, TextField buyingPriceTxt, TextField sellingPriceTxt) {
        gridListDataView.setFilter(sportSupp ->
                (nameTxt.getValue() == null || nameTxt.getValue().isEmpty() || sportSupp.getName().contains(nameTxt.getValue()))
                        && (categoryTxt.getValue() == null || categoryTxt.getValue().isEmpty() || sportSupp.getCategory().contains(categoryTxt.getValue()))
                        && (detailsTxt.getValue() == null || detailsTxt.getValue().isEmpty() || sportSupp.getDetails().contains(detailsTxt.getValue()))
                        && (batchNoTxt.getValue() == null || batchNoTxt.getValue().isEmpty() || sportSupp.getBatchNo().contains(batchNoTxt.getValue()))
                        && (quantityTxt.getValue() == null || quantityTxt.getValue().isEmpty() || sportSupp.getQuantity().contains(quantityTxt.getValue()))
                        && (expiryDatePicker.getValue() == null || sportSupp.getExpiryDate().equals(expiryDatePicker.getValue()))
                        && (buyingPriceTxt.getValue() == null || buyingPriceTxt.getValue().isEmpty() || sportSupp.getBuyingPrice().contains(buyingPriceTxt.getValue()))
                        && (sellingPriceTxt.getValue() == null || sellingPriceTxt.getValue().isEmpty() || sportSupp.getSellingPrice().contains(sellingPriceTxt.getValue())));
    }


    private void createGrid() {
        gridListDataView = sportSuppGrid.setItems(dbServiceSportSupp.findAllSportSUpp());
        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel nameLbl = new NativeLabel(sportSupp.getName());
            nameLbl.getElement().setProperty("title", sportSupp.getName());
            return nameLbl;
        }).setHeader("Name").setKey("name").setSortable(true).setResizable(true);

        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel detailsLbl = new NativeLabel(sportSupp.getDetails());
            detailsLbl.getElement().setProperty("title", sportSupp.getDetails());
            return detailsLbl;
        }).setHeader("Details").setKey("details").setSortable(true).setResizable(true);


        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel categoryLbl = new NativeLabel(sportSupp.getCategory());
            categoryLbl.getElement().setProperty("title", sportSupp.getCategory());
            return categoryLbl;
        }).setHeader("Category").setKey("category").setSortable(true).setResizable(true);

        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel batchNbLbl = new NativeLabel(sportSupp.getBatchNo());
            batchNbLbl.getElement().setProperty("title", sportSupp.getBatchNo());
            return batchNbLbl;
        }).setHeader("Batch Nb").setKey("batchNb").setSortable(true).setResizable(true);

        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel quantityLbl = new NativeLabel(sportSupp.getQuantity());
            quantityLbl.getElement().setProperty("title", sportSupp.getQuantity());
            return quantityLbl;
        }).setHeader("Quantity").setKey("quantity").setSortable(true).setResizable(true);

        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel expDateLbl = new NativeLabel(sportSupp.getExpiryDate().toString());
            expDateLbl.getElement().setProperty("title", sportSupp.getExpiryDate().toString());
            return expDateLbl;
        }).setHeader("Exp Date").setKey("expiryDate").setSortable(true).setResizable(true);

        if(!currentUser.getRole().equals(User.Role.CLIENT))
            sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel buyingPriceLbl = new NativeLabel(sportSupp.getBuyingPrice());
            buyingPriceLbl.getElement().setProperty("title", sportSupp.getBuyingPrice());
            return buyingPriceLbl;
        }).setHeader("Buying Price").setKey("buyingPrice").setSortable(true).setResizable(true);

        sportSuppGrid.addComponentColumn(sportSupp -> {
            NativeLabel sellingPriceLbl = new NativeLabel(sportSupp.getSellingPrice());
            sellingPriceLbl.getElement().setProperty("title", sportSupp.getSellingPrice());
            return sellingPriceLbl;
        }).setHeader("Selling Price").setKey("sellingPrice").setSortable(true).setResizable(true);

        if(!currentUser.getRole().equals(User.Role.CLIENT))
        sportSuppGrid.addComponentColumn(sportSupp -> {
            Button editBtn = createEditButton();
            Button deleteBtn = createDeleteButton();

            deleteBtn.addClickListener(clickEvent -> {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Delete Confirmation");
                confirmDialog.setText("Are you sure you want to delete "+ sportSupp.getName());

                Button confirmDeleteBtn = createDeleteButton();
                confirmDialog.setConfirmButton(confirmDeleteBtn);
                confirmDeleteBtn.addClickListener(clickEvent1 -> {
                    dbServiceSportSupp.deleteById(sportSupp.get_id());
                    gridListDataView = sportSuppGrid.setItems(dbServiceSportSupp.findAllSportSUpp());
                    gridListDataView.refreshAll();
                    showSuccessNotification("Item deleted successfully");
                    confirmDialog.close();
                });
                Button cancelBtn = createCancelButton();
                confirmDialog.setCancelButton(cancelBtn);
                confirmDialog.setCancelable(true);
                cancelBtn.addClickListener(clickEvent1 -> {
                   confirmDialog.close();
                });
                confirmDialog.open();
            });

            editBtn.addClickListener(clickEvent -> {
                Dialog editDialog = new Dialog();
                editDialog.add(new SportSuppForm(dbServiceSportSupp, editDialog, sportSupp, sportSuppGrid, gridListDataView));
                editDialog.open();
            });
            return new HorizontalLayout(editBtn,deleteBtn);
        }).setHeader("Edit").setFooter(addNewSportSUppBtn).setWidth("10%");
        addNewSportSUppBtn.getStyle().set("background-color", getSaveBtnColor());
        addNewSportSUppBtn.getStyle().set("color", "white");
        addNewSportSUppBtn.getStyle().set("border", "none");
        addNewSportSUppBtn.getStyle().set("border-radius", "5px");
        sportSuppGrid.setHeightFull();
        add(sportSuppGrid);
    }

}
