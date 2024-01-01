package com.example.application.views.eye;

import com.example.application.db.dbServices.DBServicesEyeSupp;
import com.example.application.db.model.EyeSupp;
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

@Route(value = "eyeGrid", layout = MainLayout.class)
@PageTitle("Eye Items")
@PermitAll

public class EyeSuppView extends VerticalLayout {
    public final DBServicesEyeSupp dbServicesEyeSupp ;
    private final Button addNewEyeSuppBtn = new Button("Add");
public final Grid<EyeSupp> eyeSuppGrid = new Grid<>();
    private final Dialog addEyeSuppDialog = new Dialog();
    private GridListDataView<EyeSupp> gridListDataView;
    private final User currentUser;

    public EyeSuppView(DBServicesEyeSupp dbServicesEyeSupp, AuthenticatedUser authenticatedUser) {
        this.dbServicesEyeSupp = dbServicesEyeSupp;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        createGrid();
        setSizeFull();
        setFilters();
        addNewEyeSuppBtn.addClickListener(buttonClickEvent ->  addEyeSuppDialog.open());
        addEyeSuppDialog.add(new EyeSuppForm(dbServicesEyeSupp,addEyeSuppDialog,eyeSuppGrid,gridListDataView));

    }

    private void setFilters() {
        HeaderRow filterRow = eyeSuppGrid.appendHeaderRow();

        TextField nameTxt = new TextField();
        filterRow.getCell(eyeSuppGrid.getColumnByKey("name")).setComponent(nameTxt);
        nameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        nameTxt.setClearButtonVisible(true);
        nameTxt.setWidthFull();
        nameTxt.setPlaceholder("Name");
        nameTxt.getElement().getStyle().set("font-size", "12px");

        TextField categoryTxt = new TextField();
        filterRow.getCell(eyeSuppGrid.getColumnByKey("category")).setComponent(categoryTxt);
        categoryTxt.setValueChangeMode(ValueChangeMode.EAGER);
        categoryTxt.setClearButtonVisible(true);
        categoryTxt.setPlaceholder("Category");
        categoryTxt.getElement().getStyle().set("font-size", "12px");


        TextField detailsTxt = new TextField();
        filterRow.getCell(eyeSuppGrid.getColumnByKey("details")).setComponent(detailsTxt);
        detailsTxt.setValueChangeMode(ValueChangeMode.EAGER);
        detailsTxt.setClearButtonVisible(true);
        detailsTxt.setPlaceholder("Details");
        detailsTxt.getElement().getStyle().set("font-size", "12px");

        TextField batchNoTxt = new TextField();
        filterRow.getCell(eyeSuppGrid.getColumnByKey("batchNb")).setComponent(batchNoTxt);
        batchNoTxt.setValueChangeMode(ValueChangeMode.EAGER);
        batchNoTxt.setClearButtonVisible(true);batchNoTxt.setPlaceholder("Batch Nb");
        batchNoTxt.getElement().getStyle().set("font-size", "12px");


        TextField quantityTxt = new TextField();
        filterRow.getCell(eyeSuppGrid.getColumnByKey("quantity")).setComponent(quantityTxt);
        quantityTxt.setValueChangeMode(ValueChangeMode.EAGER);
        quantityTxt.setClearButtonVisible(true);
        quantityTxt.setPlaceholder("Quantity");
        quantityTxt.getElement().getStyle().set("font-size", "12px");

//        DatePicker expiryDatePicker = new DatePicker();
//        filterRow.getCell(eyeSuppGrid.getColumnByKey("expiryDate")).setComponent(expiryDatePicker);
//        expiryDatePicker.setClearButtonVisible(true);
//        expiryDatePicker.setPlaceholder("Expiry Date");
//        expiryDatePicker.getElement().getStyle().set("font-size", "12px");


        TextField buyingPriceTxt = new TextField();
        if(!currentUser.getRole().equals(User.Role.CLIENT))
            filterRow.getCell(eyeSuppGrid.getColumnByKey("buyingPrice")).setComponent(buyingPriceTxt);
        buyingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        buyingPriceTxt.setClearButtonVisible(true);
        buyingPriceTxt.setPlaceholder("Buying Price");
        buyingPriceTxt.getElement().getStyle().set("font-size", "12px");

        TextField sellingPriceTxt = new TextField();
        filterRow.getCell(eyeSuppGrid.getColumnByKey("sellingPrice")).setComponent(sellingPriceTxt);
        sellingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        sellingPriceTxt.setClearButtonVisible(true);
        sellingPriceTxt.setPlaceholder("selling Price");
        sellingPriceTxt.getElement().getStyle().set("font-size", "12px");

        nameTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));
        categoryTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));
        detailsTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));
        batchNoTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));
        quantityTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));
        buyingPriceTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));
        sellingPriceTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, detailsTxt, batchNoTxt, quantityTxt, buyingPriceTxt, sellingPriceTxt));


    }

    private void applyFilter(TextField nameTxt, TextField categoryTxt, TextField detailsTxt, TextField batchNoTxt, TextField quantityTxt, TextField buyingPriceTxt, TextField sellingPriceTxt) {
        gridListDataView.setFilter(EyeSupp ->
                (nameTxt.getValue() == null || nameTxt.getValue().isEmpty() || EyeSupp.getName().contains(nameTxt.getValue()))
                        && (categoryTxt.getValue() == null || categoryTxt.getValue().isEmpty() || EyeSupp.getCategory().contains(categoryTxt.getValue()))
                        && (detailsTxt.getValue() == null || detailsTxt.getValue().isEmpty() || EyeSupp.getDetails().contains(detailsTxt.getValue()))
                        && (batchNoTxt.getValue() == null || batchNoTxt.getValue().isEmpty() || EyeSupp.getBatchNo().contains(batchNoTxt.getValue()))
                        && (quantityTxt.getValue() == null || quantityTxt.getValue().isEmpty() || EyeSupp.getQuantity().contains(quantityTxt.getValue()))
                        && (buyingPriceTxt.getValue() == null || buyingPriceTxt.getValue().isEmpty() || EyeSupp.getBuyingPrice().contains(buyingPriceTxt.getValue()))
                        && (sellingPriceTxt.getValue() == null || sellingPriceTxt.getValue().isEmpty() || EyeSupp.getSellingPrice().contains(sellingPriceTxt.getValue())));
    }


    private void createGrid() {
        gridListDataView = eyeSuppGrid.setItems(dbServicesEyeSupp.findAllEyeSupp());
        eyeSuppGrid.addComponentColumn(eyeSupp -> {
            NativeLabel nameLbl = new NativeLabel(eyeSupp.getName());
            nameLbl.getElement().setProperty("title", eyeSupp.getName());
            return nameLbl;
        }).setHeader("Name").setKey("name").setSortable(true).setResizable(true);

        eyeSuppGrid.addComponentColumn(eyeSupp -> {
            NativeLabel detailsLbl = new NativeLabel(eyeSupp.getDetails());
            detailsLbl.getElement().setProperty("title", eyeSupp.getDetails());
            return detailsLbl;
        }).setHeader("Details").setKey("details").setSortable(true).setResizable(true);


        eyeSuppGrid.addComponentColumn(eyeSupp -> {
            NativeLabel categoryLbl = new NativeLabel(eyeSupp.getCategory());
            categoryLbl.getElement().setProperty("title", eyeSupp.getCategory());
            return categoryLbl;
        }).setHeader("Category").setKey("category").setSortable(true).setResizable(true);

        eyeSuppGrid.addComponentColumn(eyeSupp -> {
            NativeLabel batchNbLbl = new NativeLabel(eyeSupp.getBatchNo());
            batchNbLbl.getElement().setProperty("title", eyeSupp.getBatchNo());
            return batchNbLbl;
        }).setHeader("Batch Nb").setKey("batchNb").setSortable(true).setResizable(true);

        eyeSuppGrid.addComponentColumn(eyeSupp -> {
            NativeLabel quantityLbl = new NativeLabel(eyeSupp.getQuantity());
            quantityLbl.getElement().setProperty("title", eyeSupp.getQuantity());
            return quantityLbl;
        }).setHeader("Quantity").setKey("quantity").setSortable(true).setResizable(true);

//        eyeSuppGrid.addComponentColumn(eyeSupp -> {
//            NativeLabel expDateLbl = new NativeLabel(eyeSupp.getExpiryDate().toString());
//            expDateLbl.getElement().setProperty("title", eyeSupp.getExpiryDate().toString());
//            return expDateLbl;
//        }).setHeader("Exp Date").setKey("expiryDate").setSortable(true).setResizable(true);

        if(!currentUser.getRole().equals(User.Role.CLIENT))
            eyeSuppGrid.addComponentColumn(eyeSupp -> {
                NativeLabel buyingPriceLbl = new NativeLabel(eyeSupp.getBuyingPrice());
                buyingPriceLbl.getElement().setProperty("title", eyeSupp.getBuyingPrice());
                return buyingPriceLbl;
            }).setHeader("Buying Price").setKey("buyingPrice").setSortable(true).setResizable(true);

        eyeSuppGrid.addComponentColumn(eyeSupp -> {
            NativeLabel sellingPriceLbl = new NativeLabel(eyeSupp.getSellingPrice());
            sellingPriceLbl.getElement().setProperty("title", eyeSupp.getSellingPrice());
            return sellingPriceLbl;
        }).setHeader("Selling Price").setKey("sellingPrice").setSortable(true).setResizable(true);

        if(!currentUser.getRole().equals(User.Role.CLIENT))
            eyeSuppGrid.addComponentColumn(eyeSupp -> {
                Button editBtn = createEditButton();
                Button deleteBtn = createDeleteButton();

                deleteBtn.addClickListener(clickEvent -> {
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Confirmation");
                    confirmDialog.setText("Are you sure you want to delete "+ eyeSupp.getName());

                    Button confirmDeleteBtn = createDeleteButton();
                    confirmDialog.setConfirmButton(confirmDeleteBtn);
                    confirmDeleteBtn.addClickListener(clickEvent1 -> {
                        dbServicesEyeSupp.deleteById(eyeSupp.get_id());
                        gridListDataView = eyeSuppGrid.setItems(dbServicesEyeSupp.findAllEyeSupp());
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
                    editDialog.add(new EyeSuppForm(dbServicesEyeSupp, editDialog, eyeSupp, eyeSuppGrid, gridListDataView));
                    editDialog.open();
                });
                return new HorizontalLayout(editBtn,deleteBtn);
            }).setHeader("Edit").setFooter(addNewEyeSuppBtn).setWidth("10%");
        addNewEyeSuppBtn.getStyle().set("background-color", getSaveBtnColor());
        addNewEyeSuppBtn.getStyle().set("color", "white");
        addNewEyeSuppBtn.getStyle().set("border", "none");
        addNewEyeSuppBtn.getStyle().set("border-radius", "5px");
        eyeSuppGrid.setHeightFull();
        add(eyeSuppGrid);
    }
}
