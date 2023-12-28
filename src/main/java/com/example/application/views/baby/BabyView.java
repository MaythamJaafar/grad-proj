package com.example.application.views.baby;

import com.example.application.db.dbServices.DBServicesBaby;
import com.example.application.db.model.Baby;
import com.example.application.db.model.User;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import static com.example.application.util.Util.*;

@Route(value = "babyGrid", layout = MainLayout.class)
@PageTitle("Baby Grid")
@PermitAll

public class BabyView extends VerticalLayout {
    public final DBServicesBaby dbServicesBaby;
    private final Button addNewBabyBtn = new Button("Add Baby Item");
    public final Grid<Baby> babyGrid = new Grid<>();
    private final Dialog addBabyDialog = new Dialog();
    private final User currentUser;
    private GridListDataView<Baby> gridListDataView;

    public BabyView(DBServicesBaby dbServicesBaby, AuthenticatedUser authenticatedUser) {
        this.dbServicesBaby = dbServicesBaby;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        createGrid();
        setSizeFull();
        setFilters();
        addNewBabyBtn.addClickListener(buttonClickEvent -> addBabyDialog.open());
        addBabyDialog.add(new BabyForm(this.dbServicesBaby, addBabyDialog, babyGrid, gridListDataView));
    }

    private void setFilters() {
        HeaderRow filterRow = babyGrid.appendHeaderRow();

        TextField nameTxt = new TextField();
        filterRow.getCell(babyGrid.getColumnByKey("name")).setComponent(nameTxt);
        nameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        nameTxt.setClearButtonVisible(true);
        nameTxt.setWidthFull();
        nameTxt.setPlaceholder("Name");
        nameTxt.getElement().getStyle().set("font-size", "12px");

        TextField categoryTxt = new TextField();
        filterRow.getCell(babyGrid.getColumnByKey("category")).setComponent(categoryTxt);
        categoryTxt.setValueChangeMode(ValueChangeMode.EAGER);
        categoryTxt.setClearButtonVisible(true);
        categoryTxt.setWidthFull();
        categoryTxt.setPlaceholder("Category");
        categoryTxt.getElement().getStyle().set("font-size", "12px");

        TextField detailsTxt = new TextField();
        filterRow.getCell(babyGrid.getColumnByKey("details")).setComponent(detailsTxt);
        detailsTxt.setValueChangeMode(ValueChangeMode.EAGER);
        detailsTxt.setClearButtonVisible(true);
        detailsTxt.setWidthFull();
        detailsTxt.setPlaceholder("Details");
        detailsTxt.getElement().getStyle().set("font-size", "12px");

        TextField batchNoTxt = new TextField();
        filterRow.getCell(babyGrid.getColumnByKey("batchNb")).setComponent(batchNoTxt);
        batchNoTxt.setValueChangeMode(ValueChangeMode.EAGER);
        batchNoTxt.setClearButtonVisible(true);
        batchNoTxt.setWidthFull();
        batchNoTxt.setPlaceholder("Batch Nb");
        batchNoTxt.getElement().getStyle().set("font-size", "12px");

        TextField quantityTxt = new TextField();
        filterRow.getCell(babyGrid.getColumnByKey("quantity")).setComponent(quantityTxt);
        quantityTxt.setValueChangeMode(ValueChangeMode.EAGER);
        quantityTxt.setClearButtonVisible(true);
        quantityTxt.setWidthFull();
        quantityTxt.setPlaceholder("Quantity");
        quantityTxt.getElement().getStyle().set("font-size", "12px");

        DatePicker expiryDatePicker = new DatePicker();
        filterRow.getCell(babyGrid.getColumnByKey("expiryDate")).setComponent(expiryDatePicker);
        expiryDatePicker.setClearButtonVisible(true);
        expiryDatePicker.setWidthFull();
        expiryDatePicker.setPlaceholder("Expiry Date");
        expiryDatePicker.getElement().getStyle().set("font-size", "12px");

        TextField buyingPriceTxt = new TextField();
        if (!currentUser.getRole().equals(User.Role.CLIENT))
            filterRow.getCell(babyGrid.getColumnByKey("buyingPrice")).setComponent(buyingPriceTxt);
        buyingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        buyingPriceTxt.setClearButtonVisible(true);
        buyingPriceTxt.setWidthFull();
        buyingPriceTxt.setPlaceholder("Buying Price");
        buyingPriceTxt.getElement().getStyle().set("font-size", "12px");

        TextField sellingPriceTxt = new TextField();
        filterRow.getCell(babyGrid.getColumnByKey("sellingPrice")).setComponent(sellingPriceTxt);
        sellingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        sellingPriceTxt.setClearButtonVisible(true);
        sellingPriceTxt.setWidthFull();
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
        gridListDataView.setFilter(baby ->
                (nameTxt.getValue() == null || nameTxt.getValue().isEmpty() || baby.getName().contains(nameTxt.getValue()))
                        && (categoryTxt.getValue() == null || categoryTxt.getValue().isEmpty() || baby.getCategory().contains(categoryTxt.getValue()))
                        && (detailsTxt.getValue() == null || detailsTxt.getValue().isEmpty() || baby.getDetails().contains(detailsTxt.getValue()))
                        && (batchNoTxt.getValue() == null || batchNoTxt.getValue().isEmpty() || baby.getBatchNo().contains(batchNoTxt.getValue()))
                        && (quantityTxt.getValue() == null || quantityTxt.getValue().isEmpty() || baby.getQuantity().contains(quantityTxt.getValue()))
                        && (expiryDatePicker.getValue() == null || baby.getExpiryDate().equals(expiryDatePicker.getValue()))
                        && (buyingPriceTxt.getValue() == null || buyingPriceTxt.getValue().isEmpty() || baby.getBuyingPrice().contains(buyingPriceTxt.getValue()))
                        && (sellingPriceTxt.getValue() == null || sellingPriceTxt.getValue().isEmpty() || baby.getSellingPrice().contains(sellingPriceTxt.getValue())));
    }

    private void createGrid() {
        gridListDataView = babyGrid.setItems(dbServicesBaby.findAllBaby());
        babyGrid.addComponentColumn(baby -> {
            NativeLabel nameLbl = new NativeLabel(baby.getName());
            nameLbl.getElement().setProperty("title", baby.getName());
            return nameLbl;
        }).setHeader("Name").setKey("name").setSortable(true).setResizable(true);

        babyGrid.addComponentColumn(baby -> {
            NativeLabel detailsLbl = new NativeLabel(baby.getDetails());
            detailsLbl.getElement().setProperty("title", baby.getDetails());
            return detailsLbl;
        }).setHeader("Details").setKey("details").setSortable(true).setResizable(true);

        babyGrid.addComponentColumn(baby -> {
            NativeLabel categoryLbl = new NativeLabel(baby.getCategory());
            categoryLbl.getElement().setProperty("title", baby.getCategory());
            return categoryLbl;
        }).setHeader("Category").setKey("category").setSortable(true).setResizable(true);

        babyGrid.addComponentColumn(baby -> {
            NativeLabel batchNbLbl = new NativeLabel(baby.getBatchNo());
            batchNbLbl.getElement().setProperty("title", baby.getBatchNo());
            return batchNbLbl;
        }).setHeader("Batch Nb").setKey("batchNb").setSortable(true).setResizable(true);

        babyGrid.addComponentColumn(baby -> {
            NativeLabel quantityLbl = new NativeLabel(baby.getQuantity());
            quantityLbl.getElement().setProperty("title", baby.getQuantity());
            return quantityLbl;
        }).setHeader("Quantity").setKey("quantity").setSortable(true).setResizable(true);

        babyGrid.addComponentColumn(baby -> {
            NativeLabel expDateLbl = new NativeLabel(baby.getExpiryDate().toString());
            expDateLbl.getElement().setProperty("title", baby.getExpiryDate().toString());
            return expDateLbl;
        }).setHeader("Exp Date").setKey("expiryDate").setSortable(true).setResizable(true);
        if (!currentUser.getRole().equals(User.Role.CLIENT))
            babyGrid.addComponentColumn(baby -> {
                NativeLabel buyingPriceLbl = new NativeLabel(baby.getBuyingPrice());
                buyingPriceLbl.getElement().setProperty("title", baby.getBuyingPrice());
                return buyingPriceLbl;
            }).setHeader("Buying Price").setKey("buyingPrice").setSortable(true).setResizable(true);
        babyGrid.addComponentColumn(baby -> {
            NativeLabel sellingPriceLbl = new NativeLabel(baby.getSellingPrice());
            sellingPriceLbl.getElement().setProperty("title", baby.getSellingPrice());
            return sellingPriceLbl;
        }).setHeader("Selling Price").setKey("sellingPrice").setSortable(true).setResizable(true);

        if (!currentUser.getRole().equals(User.Role.CLIENT))
            babyGrid.addComponentColumn(baby -> {
                Button editBtn = createEditButton();
                Button deleteBtn = createDeleteButton();

                editBtn.addClickListener(clickEvent -> {
                    Dialog editDialog = new Dialog();
                    editDialog.add(new BabyForm(dbServicesBaby, editDialog, baby, babyGrid, gridListDataView));
                    editDialog.open();
                });
                deleteBtn.addClickListener(clickEvent -> {
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    Button confirmDeleteBtn = createDeleteButton();
                    confirmDialog.setConfirmButton(confirmDeleteBtn);
                    confirmDialog.setHeader("Delete Confirmation");
                    confirmDialog.setText("Are you sure you want to delete " + baby.getName());
                    confirmDeleteBtn.addClickListener(clickEvent1 -> {
                        dbServicesBaby.deleteById(baby.get_id());
                        gridListDataView = babyGrid.setItems(dbServicesBaby.findAllBaby());
                        gridListDataView.refreshAll();
                        showSuccessNotification("Item deleted successfully");
                    });
                    Button cancelBtn = createCancelButton();
                    cancelBtn.addClickListener(clickEvent1 -> {
                        confirmDialog.close();
                    });
                    confirmDialog.setCancelButton(cancelBtn);
                    confirmDialog.setCancelable(true);
                    confirmDialog.open();
                });
                HorizontalLayout editDltBtn = new HorizontalLayout();
                editDltBtn.add(editBtn, deleteBtn);
                return editDltBtn;
            }).setHeader("Edit").setFooter(addNewBabyBtn).setWidth("10%");

        addNewBabyBtn.getStyle().set("background-color", getSaveBtnColor());
        addNewBabyBtn.getStyle().set("color", "white");
        addNewBabyBtn.getStyle().set("border", "none");
        addNewBabyBtn.getStyle().set("border-radius", "5px");
        babyGrid.setHeightFull();
        add(babyGrid);
    }

}