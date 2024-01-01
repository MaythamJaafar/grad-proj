package com.example.application.views.pcare;

import com.example.application.db.dbServices.DBServicePCare;
import com.example.application.db.model.PCare;
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


public class PCareForm extends VerticalLayout {
    private DBServicePCare dbServicePCare;

    private final Button savePCareBtn = new Button("Add PCare");
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
    private final Grid<PCare> pCareGrid;
    private GridListDataView<PCare> gridListDataView;

    public PCareForm(DBServicePCare dbServicePCare, Dialog addPCareDialog, Grid<PCare> pCareGrid, GridListDataView<PCare> gridListDataView) {
        this.dbServicePCare = dbServicePCare;
        this.dialog = addPCareDialog;
        this.pCareGrid = pCareGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> {
            addPCareDialog.close();
            clearFields();
        });
        addPCareDialog.setCloseOnOutsideClick(false);
        createForm();
        saveNewPCare(null);
    }

    public PCareForm(DBServicePCare dbServicePCare, Dialog editDialog, PCare pCare, Grid<PCare> pCareGrid, GridListDataView<PCare> gridListDataView) {
        this.dbServicePCare = dbServicePCare;
        this.dialog = editDialog;
        this.pCareGrid = pCareGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> editDialog.close());
        editDialog.setCloseOnOutsideClick(false);
        createEditForm(pCare);
        saveNewPCare(pCare.get_id());
        savePCareBtn.setText("Update");
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

    private void createEditForm(PCare pCare) {
        categoryTxt.setValue(pCare.getCategory());
        nameTxt.setValue(pCare.getName());
        detailsTxt.setValue(pCare.getDetails());
        batchNb.setValue(pCare.getBatchNo());
        quantityNb.setValue(pCare.getQuantity());
        expiryDateNb.setValue(pCare.getExpiryDate());
        buyingPriceNb.setValue(pCare.getBuyingPrice());
        sellingPriceNb.setValue(pCare.getSellingPrice());
        createForm();
    }

    public void createForm() {
        HorizontalLayout catSupHl = new HorizontalLayout(nameTxt, categoryTxt);
        HorizontalLayout batchDosHl = new HorizontalLayout(batchNb, quantityNb);
        HorizontalLayout forQuaHl = new HorizontalLayout(detailsTxt ,expiryDateNb);
        HorizontalLayout expiryBuySelHl = new HorizontalLayout(buyingPriceNb, sellingPriceNb);
        HorizontalLayout cancelSaveHl = new HorizontalLayout(savePCareBtn, cancelBtn);

        add(catSupHl, batchDosHl, forQuaHl, batchDosHl, expiryBuySelHl, cancelSaveHl);
    }

    public void saveNewPCare(@Nullable String id) {
        savePCareBtn.addClickListener(buttonClickEvent -> {
            if (checkFields()) {
                PCare pCare = new PCare();
                if (id != null)
                    pCare.set_id(id);
                pCare.setName(nameTxt.getValue());
                pCare.setBatchNo(batchNb.getValue());
                pCare.setCategory(categoryTxt.getValue());
                pCare.setDetails(detailsTxt.getValue());
                pCare.setQuantity(quantityNb.getValue());
                pCare.setExpiryDate(expiryDateNb.getValue());
                pCare.setBuyingPrice(buyingPriceNb.getValue());
                pCare.setSellingPrice(sellingPriceNb.getValue());
                dbServicePCare.savePCare(pCare);
                gridListDataView = pCareGrid.setItems(dbServicePCare.findAllPCare());
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

}
