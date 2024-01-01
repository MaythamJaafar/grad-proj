package com.example.application.views.sportSup;

import com.example.application.db.dbServices.DBServiceSportSupp;
import com.example.application.db.model.SportSupp;
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


public class SportSuppForm extends VerticalLayout {
    private final DBServiceSportSupp dbServiceSportSupp;
    private final Button saveSportSuppBtn = new Button("Add sportSupp");
    private final TextField nameTxt = new TextField("Name");
    private final TextField categoryTxt = new TextField("Category");
    private final TextField batchNb = new TextField("Batch #");
    private final TextField detailsTxt = new TextField("Details");
    private final TextField quantityNb = new TextField("Quantity");
    private final DatePicker expiryDateNb = new DatePicker("Expiry date");
    private final TextField buyingPriceNb = new TextField("Buying Price");
    private final TextField sellingPriceNb = new TextField("Selling Price");
    private final Button cancelBtn = new Button("Cancel");
    private final Dialog dialog;
    private final Grid<SportSupp> sportSuppGrid;
    private GridListDataView<SportSupp> gridListDataView;

    public SportSuppForm(DBServiceSportSupp dbServiceSportSupp, Dialog addSportSuppDialog, Grid<SportSupp> sportSuppGrid,GridListDataView<SportSupp> gridListDataView) {
        this.dbServiceSportSupp = dbServiceSportSupp;
        this.dialog = addSportSuppDialog;
        this.sportSuppGrid = sportSuppGrid;
        cancelBtn.addClickListener(buttonClickEvent -> {
            addSportSuppDialog.close();
            clearFields();
        });
        addSportSuppDialog.setCloseOnOutsideClick(false);
        createForm();
        saveNewSportSupp(null);
    }

    public SportSuppForm(DBServiceSportSupp dbServiceSportSupp, Dialog editDialog, SportSupp sportSupp, Grid<SportSupp> sportSuppGrid, GridListDataView<SportSupp> gridListDataView) {
        this.dbServiceSportSupp = dbServiceSportSupp;
        this.dialog = editDialog;
        this.sportSuppGrid = sportSuppGrid;
        cancelBtn.addClickListener(buttonClickEvent -> editDialog.close());
        editDialog.setCloseOnOutsideClick(false);
        createEditForm(sportSupp);
        saveNewSportSupp(sportSupp.get_id());
        saveSportSuppBtn.setText("Update");
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

    private void createEditForm(SportSupp sportSupp) {
        categoryTxt.setValue(sportSupp.getCategory());
        nameTxt.setValue(sportSupp.getName());
        detailsTxt.setValue(sportSupp.getDetails());
        batchNb.setValue(sportSupp.getBatchNo());
        quantityNb.setValue(sportSupp.getQuantity());
        expiryDateNb.setValue(sportSupp.getExpiryDate());
        buyingPriceNb.setValue(sportSupp.getBuyingPrice());
        sellingPriceNb.setValue(sportSupp.getSellingPrice());
        createForm();
    }

    public void createForm() {
        HorizontalLayout catSupHl = new HorizontalLayout(nameTxt,categoryTxt);
        HorizontalLayout batchDosHl = new HorizontalLayout(batchNb, quantityNb);
        HorizontalLayout forQuaHl = new HorizontalLayout(detailsTxt ,expiryDateNb );
        HorizontalLayout expiryBuySelHl = new HorizontalLayout(buyingPriceNb, sellingPriceNb);
        HorizontalLayout cancelSaveHl = new HorizontalLayout(saveSportSuppBtn, cancelBtn);

        add(catSupHl, batchDosHl, forQuaHl, batchDosHl, expiryBuySelHl, cancelSaveHl);
    }

    public void saveNewSportSupp(@Nullable String id) {
        saveSportSuppBtn.addClickListener(buttonClickEvent -> {
            if (checkFields()) {
                SportSupp newSportSupp = new SportSupp();
                if (id != null)
                    newSportSupp.set_id(id);
                newSportSupp.setName(nameTxt.getValue());
                newSportSupp.setBatchNo(batchNb.getValue());
                newSportSupp.setCategory(categoryTxt.getValue());
                newSportSupp.setDetails(detailsTxt.getValue());
                newSportSupp.setQuantity(quantityNb.getValue());
                newSportSupp.setExpiryDate(expiryDateNb.getValue());
                newSportSupp.setBuyingPrice(buyingPriceNb.getValue());
                newSportSupp.setSellingPrice(sellingPriceNb.getValue());
                dbServiceSportSupp.saveSportSupp(newSportSupp);
                gridListDataView = sportSuppGrid.setItems(dbServiceSportSupp.findAllSportSUpp());
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
