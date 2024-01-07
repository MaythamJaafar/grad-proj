package com.example.application.views.baby;

import com.example.application.db.dbServices.DBServicesBaby;
import com.example.application.db.model.Baby;
import com.example.application.util.InputChecker;
import com.mongodb.lang.Nullable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.concurrent.atomic.AtomicBoolean;


public class BabyForm extends VerticalLayout {
    private DBServicesBaby dbServicesBaby;

    private final Button saveBabyBtn = new Button("Add Baby Item");
    private final TextField nameTxt = new TextField("Name");
    private final TextField categoryTxt = new TextField("Category");
    private final TextField batchNb = new TextField("BatchNb");
    private final TextField detailsTxt = new TextField("Details");
    private final TextField quantityNb = new TextField("Quantity");
    private final DatePicker expiryDateNb = new DatePicker("Expiry Date");
    private final TextField buyingPriceNb = new TextField("Buying Price");
    private final TextField sellingPriceNb = new TextField("Selling Price");
    private final Button cancelBtn = new Button("Cancel");
    private final Dialog dialog;
    private final Grid<Baby> babyGrid;
    private GridListDataView<Baby> gridListDataView;

    public BabyForm(DBServicesBaby dbServicesBaby, Dialog addBabyDialog, Grid<Baby> babyGrid, GridListDataView<Baby> gridListDataView) {
        this.dbServicesBaby = dbServicesBaby;
        this.dialog = addBabyDialog;
        this.babyGrid = babyGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> {
            addBabyDialog.close();
            clearFields();
        });
        addBabyDialog.setCloseOnOutsideClick(false);
        createForm();
        saveNewbaby(null);
        setPlaceHolders();
    }

    public BabyForm(DBServicesBaby dbServicesBaby, Dialog editDialog, Baby baby, Grid<Baby> babyGrid, GridListDataView<Baby> gridListDataView) {
        this.dbServicesBaby = dbServicesBaby;
        this.dialog = editDialog;
        this.babyGrid = babyGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> editDialog.close());
        editDialog.setCloseOnOutsideClick(false);
        createEditForm(baby);
        saveNewbaby(baby.get_id());
        saveBabyBtn.setText("Update");
    }

    private void clearFields() {
        categoryTxt.clear();
        nameTxt.clear();
        detailsTxt.clear();
        batchNb.clear();
        quantityNb.clear();
        expiryDateNb.clear();
        buyingPriceNb.clear();
        sellingPriceNb.clear();
    }

    private void createEditForm(Baby baby) {
        categoryTxt.setValue(baby.getCategory());
        nameTxt.setValue(baby.getName());
        detailsTxt.setValue(baby.getDetails());
        batchNb.setValue(baby.getBatchNo());
        quantityNb.setValue(baby.getQuantity());
        expiryDateNb.setValue(baby.getExpiryDate());
        buyingPriceNb.setValue(baby.getBuyingPrice());
        sellingPriceNb.setValue(baby.getSellingPrice());
        createForm();
    }

    public void createForm() {
        HorizontalLayout catSupHl = new HorizontalLayout(nameTxt, categoryTxt);
        HorizontalLayout batchDosHl = new HorizontalLayout(batchNb, quantityNb);
        HorizontalLayout forQuaHl = new HorizontalLayout(detailsTxt ,expiryDateNb);
        HorizontalLayout expiryBuySelHl = new HorizontalLayout(buyingPriceNb, sellingPriceNb);
        HorizontalLayout cancelSaveHl = new HorizontalLayout(saveBabyBtn, cancelBtn);

        add(catSupHl, batchDosHl, forQuaHl, batchDosHl, expiryBuySelHl, cancelSaveHl);
    }

public void saveNewbaby(@Nullable String id) {
        saveBabyBtn.addClickListener(buttonClickEvent -> {
            if (checkFields()) {
                Baby baby = new Baby();
                if (id != null)
                    baby.set_id(id);
                baby.setName(nameTxt.getValue());
                baby.setBatchNo(batchNb.getValue());
                baby.setCategory(categoryTxt.getValue());
                baby.setDetails(detailsTxt.getValue());
                baby.setQuantity(quantityNb.getValue());
                baby.setExpiryDate(expiryDateNb.getValue());
                baby.setBuyingPrice(buyingPriceNb.getValue());
                baby.setSellingPrice(sellingPriceNb.getValue());
                dbServicesBaby.saveBaby(baby);
                gridListDataView = babyGrid.setItems(dbServicesBaby.findAllBaby());
                gridListDataView.refreshAll();
                dialog.close();
            }
        });
    }

    private boolean checkFields() {
        AtomicBoolean continueFlag = new AtomicBoolean(true);
        InputChecker.checkTextField(nameTxt, continueFlag);
        InputChecker.checkTextField(batchNb, continueFlag);
        InputChecker.checkTextField(categoryTxt, continueFlag);
        InputChecker.checkTextField(detailsTxt, continueFlag);
        InputChecker.checkTextField(quantityNb, continueFlag);
        InputChecker.checkDatePicker(expiryDateNb, continueFlag);
        InputChecker.checkTextField(buyingPriceNb, continueFlag);
        InputChecker.checkTextField(sellingPriceNb, continueFlag);

        return continueFlag.get();
    }
    public void setPlaceHolders(){
        categoryTxt.setPlaceholder("Category");
        nameTxt.setPlaceholder("Name");
        detailsTxt.setPlaceholder("Details");
        batchNb.setPlaceholder("Batch Nb");
        quantityNb.setPlaceholder("Quantity");
        expiryDateNb.setPlaceholder("Expiry Date");
        buyingPriceNb.setPlaceholder("Buying Price");
        sellingPriceNb.setPlaceholder("Selling Price");
    }
}
