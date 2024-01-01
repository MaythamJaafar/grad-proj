package com.example.application.views.eye;

import com.example.application.db.dbServices.DBServicesEyeSupp;
import com.example.application.db.model.EyeSupp;
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
import org.hibernate.boot.jaxb.hbm.internal.CacheAccessTypeConverter;

import java.util.concurrent.atomic.AtomicBoolean;

public class EyeSuppForm extends VerticalLayout {
    private final DBServicesEyeSupp dbServicesEyeSupp;
    private final Button saveEyeSuppBtn = new Button("Add Eye Item");
    private final TextField nameTxt = new TextField("Name");
    private final TextField categoryTxt = new TextField("Category");
    private final TextField batchNb = new TextField("Batch #");
    private final TextField detailsTxt = new TextField("Details");
    private final TextField quantityNb = new TextField("Quantity");
//    private final DatePicker expiryDateNb = new DatePicker("Expiry date");
    private final TextField buyingPriceNb = new TextField("Buying Price");
    private final TextField sellingPriceNb = new TextField("Selling Price");
    private final Button cancelBtn = new Button("Cancel");
    private final Dialog dialog;
    private final Grid<EyeSupp> eyeSuppGrid;
    private GridListDataView<EyeSupp> gridListDataView;

    public EyeSuppForm(DBServicesEyeSupp dbServicesEyeSupp, Dialog addEyeSuppDialog, Grid<EyeSupp> eyeSuppGrid, GridListDataView<EyeSupp> gridListDataView) {
        this.dbServicesEyeSupp = dbServicesEyeSupp;
        this.dialog = addEyeSuppDialog;
        this.eyeSuppGrid = eyeSuppGrid;
        cancelBtn.addClickListener(buttonClickEvent -> {
            addEyeSuppDialog.close();
            clearFields();
        });
        addEyeSuppDialog.setCloseOnOutsideClick(false);
        createForm();
        saveNewEyeSupp(null);
    }

    public EyeSuppForm(DBServicesEyeSupp dbServicesEyeSupp, Dialog editDialog, EyeSupp eyeSupp, Grid<EyeSupp> eyeSuppGrid, GridListDataView<EyeSupp> gridListDataView) {
        this.dbServicesEyeSupp = dbServicesEyeSupp;
        this.dialog = editDialog;
        this.eyeSuppGrid = eyeSuppGrid;
        cancelBtn.addClickListener(buttonClickEvent -> editDialog.close());
        editDialog.setCloseOnOutsideClick(false);
        createEditForm(eyeSupp);
        saveNewEyeSupp(eyeSupp.get_id());
        saveEyeSuppBtn.setText("Update");
    }

    private void clearFields() {
        categoryTxt.clear();
        nameTxt.clear();
        detailsTxt.clear();
        batchNb.clear();
        quantityNb.clear();
//        expiryDateNb.clear();
        buyingPriceNb.clear();
        sellingPriceNb.clear();
    }

    private void createEditForm(EyeSupp eyeSupp) {
        categoryTxt.setValue(eyeSupp.getCategory());
        nameTxt.setValue(eyeSupp.getName());
        detailsTxt.setValue(eyeSupp.getDetails());
        batchNb.setValue(eyeSupp.getBatchNo());
        quantityNb.setValue(eyeSupp.getQuantity());
//        expiryDateNb.setValue(eyeSupp.getExpiryDate());
        buyingPriceNb.setValue(eyeSupp.getBuyingPrice());
        sellingPriceNb.setValue(eyeSupp.getSellingPrice());
        createForm();
    }

    public void createForm() {
        HorizontalLayout catSupHl = new HorizontalLayout(nameTxt);
        HorizontalLayout batchDosHl = new HorizontalLayout(categoryTxt, detailsTxt);
        HorizontalLayout forQuaHl = new HorizontalLayout(batchNb,quantityNb );
        HorizontalLayout BuySelHl = new HorizontalLayout(buyingPriceNb, sellingPriceNb);
        HorizontalLayout cancelSaveHl = new HorizontalLayout(saveEyeSuppBtn, cancelBtn);
        catSupHl.getElement().getStyle().set("align-self", "center");

        add(catSupHl, batchDosHl, forQuaHl, BuySelHl, cancelSaveHl);
    }

    public void saveNewEyeSupp(@Nullable String id) {
        saveEyeSuppBtn.addClickListener(buttonClickEvent -> {
            if (checkFields()) {
                EyeSupp newEyeSupp = new EyeSupp();
                if (id != null)
                    newEyeSupp.set_id(id);
                newEyeSupp.setName(nameTxt.getValue());
                newEyeSupp.setBatchNo(batchNb.getValue());
                newEyeSupp.setCategory(categoryTxt.getValue());
                newEyeSupp.setDetails(detailsTxt.getValue());
                newEyeSupp.setQuantity(quantityNb.getValue());
//                newEyeSupp.setExpiryDate(expiryDateNb.getValue());
                newEyeSupp.setBuyingPrice(buyingPriceNb.getValue());
                newEyeSupp.setSellingPrice(sellingPriceNb.getValue());
                dbServicesEyeSupp.saveEyeSupp(newEyeSupp);
                gridListDataView = eyeSuppGrid.setItems(dbServicesEyeSupp.findAllEyeSupp());
                gridListDataView.refreshAll();
                dialog.close();
                clearFields();
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
//        InputChecker.checkDatePicker(expiryDateNb, continueFlag);
        InputChecker.checkTextField(buyingPriceNb, continueFlag);
        InputChecker.checkTextField(sellingPriceNb, continueFlag);

        return continueFlag.get();
    }
}
