package com.example.application.views.pcare;

import com.example.application.db.dbServices.DBServicePCare;
import com.example.application.db.model.PCare;
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

@Route(value = "pCareGrid", layout = MainLayout.class)
@PageTitle("PCare Items")
@PermitAll

public class PCareView extends VerticalLayout {
    public final DBServicePCare dbServicePCare;
    private final Button addNewPCareBtn = new Button("Add");
    public final Grid<PCare> pCareGrid = new Grid<>();
    private final Dialog addPCareDialog = new Dialog();
    private final User currentUser;
    private GridListDataView<PCare> gridListDataView;

    public PCareView(DBServicePCare dbServicePCare, AuthenticatedUser authenticatedUser) {
        this.dbServicePCare = dbServicePCare;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        createGrid();
        setSizeFull();
        setFilters();
        addNewPCareBtn.addClickListener(buttonClickEvent -> addPCareDialog.open());
        addPCareDialog.add(new PCareForm(this.dbServicePCare, addPCareDialog, pCareGrid, gridListDataView));
    }

    private void setFilters() {
        HeaderRow filterRow = pCareGrid.appendHeaderRow();

        TextField nameTxt = new TextField();
        filterRow.getCell(pCareGrid.getColumnByKey("name")).setComponent(nameTxt);
        nameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        nameTxt.setClearButtonVisible(true);
        nameTxt.setWidthFull();
        nameTxt.setPlaceholder("Name");
        nameTxt.getElement().getStyle().set("font-size", "12px");

        TextField categoryTxt = new TextField();
        filterRow.getCell(pCareGrid.getColumnByKey("category")).setComponent(categoryTxt);
        categoryTxt.setValueChangeMode(ValueChangeMode.EAGER);
        categoryTxt.setClearButtonVisible(true);
        categoryTxt.setWidthFull();
        categoryTxt.setPlaceholder("Category");
        categoryTxt.getElement().getStyle().set("font-size", "12px");

        TextField detailsTxt = new TextField();
        filterRow.getCell(pCareGrid.getColumnByKey("details")).setComponent(detailsTxt);
        detailsTxt.setValueChangeMode(ValueChangeMode.EAGER);
        detailsTxt.setClearButtonVisible(true);
        detailsTxt.setWidthFull();
        detailsTxt.setPlaceholder("Details");
        detailsTxt.getElement().getStyle().set("font-size", "12px");

        TextField batchNoTxt = new TextField();
        filterRow.getCell(pCareGrid.getColumnByKey("batchNb")).setComponent(batchNoTxt);
        batchNoTxt.setValueChangeMode(ValueChangeMode.EAGER);
        batchNoTxt.setClearButtonVisible(true);
        batchNoTxt.setWidthFull();
        batchNoTxt.setPlaceholder("Batch Nb");
        batchNoTxt.getElement().getStyle().set("font-size", "12px");

        TextField quantityTxt = new TextField();
        filterRow.getCell(pCareGrid.getColumnByKey("quantity")).setComponent(quantityTxt);
        quantityTxt.setValueChangeMode(ValueChangeMode.EAGER);
        quantityTxt.setClearButtonVisible(true);
        quantityTxt.setWidthFull();
        quantityTxt.setPlaceholder("Quantity");
        quantityTxt.getElement().getStyle().set("font-size", "12px");

        DatePicker expiryDatePicker = new DatePicker();
        filterRow.getCell(pCareGrid.getColumnByKey("expiryDate")).setComponent(expiryDatePicker);
        expiryDatePicker.setClearButtonVisible(true);
        expiryDatePicker.setWidthFull();
        expiryDatePicker.setPlaceholder("Expiry Date");
        expiryDatePicker.getElement().getStyle().set("font-size", "12px");

        TextField buyingPriceTxt = new TextField();
        if (!currentUser.getRole().equals(User.Role.CLIENT))
            filterRow.getCell(pCareGrid.getColumnByKey("buyingPrice")).setComponent(buyingPriceTxt);
        buyingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        buyingPriceTxt.setClearButtonVisible(true);
        buyingPriceTxt.setWidthFull();
        buyingPriceTxt.setPlaceholder("Buying Price");
        buyingPriceTxt.getElement().getStyle().set("font-size", "12px");

        TextField sellingPriceTxt = new TextField();
        filterRow.getCell(pCareGrid.getColumnByKey("sellingPrice")).setComponent(sellingPriceTxt);
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
        gridListDataView.setFilter(pCare ->
                (nameTxt.getValue() == null || nameTxt.getValue().isEmpty() || pCare.getName().contains(nameTxt.getValue()))
                        && (categoryTxt.getValue() == null || categoryTxt.getValue().isEmpty() || pCare.getCategory().contains(categoryTxt.getValue()))
                        && (detailsTxt.getValue() == null || detailsTxt.getValue().isEmpty() || pCare.getDetails().contains(detailsTxt.getValue()))
                        && (batchNoTxt.getValue() == null || batchNoTxt.getValue().isEmpty() || pCare.getBatchNo().contains(batchNoTxt.getValue()))
                        && (quantityTxt.getValue() == null || quantityTxt.getValue().isEmpty() || pCare.getQuantity().contains(quantityTxt.getValue()))
                        && (expiryDatePicker.getValue() == null || pCare.getExpiryDate().equals(expiryDatePicker.getValue()))
                        && (buyingPriceTxt.getValue() == null || buyingPriceTxt.getValue().isEmpty() || pCare.getBuyingPrice().contains(buyingPriceTxt.getValue()))
                        && (sellingPriceTxt.getValue() == null || sellingPriceTxt.getValue().isEmpty() || pCare.getSellingPrice().contains(sellingPriceTxt.getValue())));
    }

    private void createGrid() {
        gridListDataView = pCareGrid.setItems(dbServicePCare.findAllPCare());
        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel nameLbl = new NativeLabel(pCare.getName());
            nameLbl.getElement().setProperty("title", pCare.getName());
            return nameLbl;
        }).setHeader("Name").setKey("name").setSortable(true).setResizable(true);

        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel detailsLbl = new NativeLabel(pCare.getDetails());
            detailsLbl.getElement().setProperty("title", pCare.getDetails());
            return detailsLbl;
        }).setHeader("Details").setKey("details").setSortable(true).setResizable(true);

        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel categoryLbl = new NativeLabel(pCare.getCategory());
            categoryLbl.getElement().setProperty("title", pCare.getCategory());
            return categoryLbl;
        }).setHeader("Category").setKey("category").setSortable(true).setResizable(true);

        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel batchNbLbl = new NativeLabel(pCare.getBatchNo());
            batchNbLbl.getElement().setProperty("title", pCare.getBatchNo());
            return batchNbLbl;
        }).setHeader("Batch Nb").setKey("batchNb").setSortable(true).setResizable(true);

        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel quantityLbl = new NativeLabel(pCare.getQuantity());
            quantityLbl.getElement().setProperty("title", pCare.getQuantity());
            return quantityLbl;
        }).setHeader("Quantity").setKey("quantity").setSortable(true).setResizable(true);

        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel expDateLbl = new NativeLabel(pCare.getExpiryDate().toString());
            expDateLbl.getElement().setProperty("title", pCare.getExpiryDate().toString());
            return expDateLbl;
        }).setHeader("Exp Date").setKey("expiryDate").setSortable(true).setResizable(true);
        if (!currentUser.getRole().equals(User.Role.CLIENT))
            pCareGrid.addComponentColumn(pCare -> {
                NativeLabel buyingPriceLbl = new NativeLabel(pCare.getBuyingPrice());
                buyingPriceLbl.getElement().setProperty("title", pCare.getBuyingPrice());
                return buyingPriceLbl;
            }).setHeader("Buying Price").setKey("buyingPrice").setSortable(true).setResizable(true);
        pCareGrid.addComponentColumn(pCare -> {
            NativeLabel sellingPriceLbl = new NativeLabel(pCare.getSellingPrice());
            sellingPriceLbl.getElement().setProperty("title", pCare.getSellingPrice());
            return sellingPriceLbl;
        }).setHeader("Selling Price").setKey("sellingPrice").setSortable(true).setResizable(true);

        if (!currentUser.getRole().equals(User.Role.CLIENT))
            pCareGrid.addComponentColumn(pCare -> {
                Button editBtn = createEditButton();
                Button deleteBtn = createDeleteButton();

                editBtn.addClickListener(clickEvent -> {
                    Dialog editDialog = new Dialog();
                    editDialog.add(new PCareForm(dbServicePCare, editDialog, pCare, pCareGrid, gridListDataView));
                    editDialog.open();
                });
                deleteBtn.addClickListener(clickEvent -> {
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    Button confirmDeleteBtn = createDeleteButton();
                    confirmDialog.setConfirmButton(confirmDeleteBtn);
                    confirmDialog.setHeader("Delete Confirmation");
                    confirmDialog.setText("Are you sure you want to delete " + pCare.getName());
                    confirmDeleteBtn.addClickListener(clickEvent1 -> {
                        dbServicePCare.deleteById(pCare.get_id());
                        gridListDataView = pCareGrid.setItems(dbServicePCare.findAllPCare());
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
            }).setHeader("Edit").setFooter(addNewPCareBtn).setWidth("10%");

        addNewPCareBtn.getStyle().set("background-color", getSaveBtnColor());
        addNewPCareBtn.getStyle().set("color", "white");
        addNewPCareBtn.getStyle().set("border", "none");
        addNewPCareBtn.getStyle().set("border-radius", "5px");
        pCareGrid.setHeightFull();
        add(pCareGrid);
    }

}