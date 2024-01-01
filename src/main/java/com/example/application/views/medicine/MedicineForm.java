package com.example.application.views.medicine;

import com.example.application.db.dbServices.DBServiceMedicine;
import com.example.application.db.model.Medicine;
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


public class MedicineForm extends VerticalLayout {
    private DBServiceMedicine dbServiceMedicine;

    private final Button saveMedicineBtn = new Button("Add Medicine Item");
    private final TextField nameTxt = new TextField("Name");
    private final TextField categoryTxt = new TextField("Category");
    private final TextField batchNb = new TextField("Batch #");
    private final TextField dosageTxt = new TextField("Dosage");
    private final TextField formulaTxt = new TextField("Formula");
    private final TextField detailsTxt = new TextField("Details");
    private final TextField quantityNb = new TextField("Quantity");
    private final TextField sideEffect = new TextField("Side Effect");
    private final DatePicker expiryDateNb = new DatePicker("Expiry Date");
    private final TextField buyingPriceNb = new TextField("Buying Price");
    private final TextField sellingPriceNb = new TextField("Selling Price");
    private final Button cancelBtn = new Button("Cancel");
    private final Dialog dialog;
    private final Grid<Medicine> medicineGrid;
    private GridListDataView<Medicine> gridListDataView;


    public MedicineForm(DBServiceMedicine dbServiceMedicine, Dialog addMedicineDialog, Grid<Medicine> medicineGrid, GridListDataView<Medicine> gridListDataView) {
        this.dbServiceMedicine = dbServiceMedicine;
        this.dialog = addMedicineDialog;
        this.medicineGrid = medicineGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> {
            addMedicineDialog.close();
            clearFields();
        });
        addMedicineDialog.setCloseOnOutsideClick(false);
        createForm();
        saveNewMedicine(null);
    }

    public MedicineForm(DBServiceMedicine dbServiceMedicine, Dialog editDialog, Medicine medicine, Grid<Medicine> medicineGrid, GridListDataView<Medicine> gridListDataView) {
        this.dbServiceMedicine = dbServiceMedicine;
        this.dialog = editDialog;
        this.medicineGrid = medicineGrid;
        this.gridListDataView = gridListDataView;
        cancelBtn.addClickListener(buttonClickEvent -> editDialog.close());
        editDialog.setCloseOnOutsideClick(false);
        createEditForm(medicine);
        saveNewMedicine(medicine.get_id());
        saveMedicineBtn.setText("update");
    }

    private void clearFields() {
        categoryTxt.clear();
        nameTxt.clear();
        formulaTxt.clear();
        detailsTxt.clear();
        batchNb.clear();
        dosageTxt.clear();
        quantityNb.clear();
        sideEffect.clear();
        expiryDateNb.clear();
        buyingPriceNb.clear();
        sellingPriceNb.clear();
    }

    private void createEditForm(Medicine medicine) {
        categoryTxt.setValue(medicine.getCategory());
        nameTxt.setValue(medicine.getName());
        formulaTxt.setValue(medicine.getFormula());
        detailsTxt.setValue(medicine.getDetails());
        batchNb.setValue(medicine.getBatchNo());
        dosageTxt.setValue(medicine.getDosage());
        quantityNb.setValue(medicine.getQuantity());
        sideEffect.setValue(medicine.getSideEffect());
        expiryDateNb.setValue(medicine.getExpiryDate());
        buyingPriceNb.setValue(medicine.getBuyingPrice());
        sellingPriceNb.setValue(medicine.getSellingPrice());
        createForm();
    }

    public void createForm() {
        HorizontalLayout catSupHl = new HorizontalLayout(nameTxt,categoryTxt);
        HorizontalLayout batchDosHl = new HorizontalLayout(batchNb, dosageTxt,formulaTxt);
        HorizontalLayout forQuaHl = new HorizontalLayout(quantityNb,sideEffect, detailsTxt);
        HorizontalLayout expiryBuySelHl = new HorizontalLayout(expiryDateNb, buyingPriceNb, sellingPriceNb);
        HorizontalLayout cancelSaveHl = new HorizontalLayout(saveMedicineBtn, cancelBtn);

        add(catSupHl, batchDosHl, forQuaHl, batchDosHl, expiryBuySelHl, cancelSaveHl);
    }

    public void saveNewMedicine(@Nullable String id) {
        saveMedicineBtn.addClickListener(buttonClickEvent -> {
            if (checkFields()) {
                Medicine newMedicine = new Medicine();
                if (id != null)
                    newMedicine.set_id(id);
                newMedicine.setName(nameTxt.getValue());
                newMedicine.setBatchNo(batchNb.getValue());
                newMedicine.setCategory(categoryTxt.getValue());
                newMedicine.setDosage(dosageTxt.getValue());
                newMedicine.setFormula(formulaTxt.getValue());
                newMedicine.setDetails(detailsTxt.getValue());
                newMedicine.setQuantity(quantityNb.getValue());
                newMedicine.setSideEffect(sideEffect.getValue());
                newMedicine.setExpiryDate(expiryDateNb.getValue());
                newMedicine.setBuyingPrice(buyingPriceNb.getValue());
                newMedicine.setSellingPrice(sellingPriceNb.getValue());
                dbServiceMedicine.saveMedicine(newMedicine);
                gridListDataView = medicineGrid.setItems(dbServiceMedicine.findAllMedicines());
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
        InputChecker.checkTextField(dosageTxt, continueFlag);
        InputChecker.checkTextField(formulaTxt, continueFlag);
        InputChecker.checkTextField(detailsTxt, continueFlag);
        InputChecker.checkTextField(quantityNb, continueFlag);
        InputChecker.checkTextField(sideEffect, continueFlag);
        InputChecker.checkDatePicker(expiryDateNb, continueFlag);
        InputChecker.checkTextField(buyingPriceNb, continueFlag);

        return continueFlag.get();
    }

}
