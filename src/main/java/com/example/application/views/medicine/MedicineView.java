package com.example.application.views.medicine;

import com.example.application.db.dbServices.DBServiceMedicine;
import com.example.application.db.model.Medicine;
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

@Route(value = "medicinesGrid", layout = MainLayout.class)
@PageTitle("Medicines Grid")
@PermitAll

public class MedicineView extends VerticalLayout {
    public final DBServiceMedicine dbServiceMedicine;
    private final Button addNewMedicineBtn = new Button("Add Medicine");
    public final Grid<Medicine> medicineGrid = new Grid<>();
    private final Dialog addMedicineDialog = new Dialog();
    private GridListDataView<Medicine> gridListDataView;
    private final User currentUser;

    public MedicineView(DBServiceMedicine dbServiceMedicine, AuthenticatedUser authenticatedUser) {
        this.dbServiceMedicine = dbServiceMedicine;
        currentUser = authenticatedUser.get().orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        createGrid();
        setSizeFull();
        setFilters();
        addNewMedicineBtn.addClickListener(buttonClickEvent -> addMedicineDialog.open());
        addMedicineDialog.add(new MedicineForm(dbServiceMedicine, addMedicineDialog, medicineGrid, gridListDataView));

    }

    private void setFilters() {
        HeaderRow filterRow = medicineGrid.appendHeaderRow();

        TextField nameTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("name")).setComponent(nameTxt);
        nameTxt.setValueChangeMode(ValueChangeMode.EAGER);
        nameTxt.setClearButtonVisible(true);
        nameTxt.setWidthFull();
        nameTxt.setPlaceholder("Name");
        nameTxt.getElement().getStyle().set("font-size", "12px");

        TextField categoryTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("category")).setComponent(categoryTxt);
        categoryTxt.setValueChangeMode(ValueChangeMode.EAGER);
        categoryTxt.setWidthFull();
        categoryTxt.setClearButtonVisible(true);
        categoryTxt.setPlaceholder("Category");
        categoryTxt.getElement().getStyle().set("font-size", "12px");

        TextField dosageTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("dosage")).setComponent(dosageTxt);
        dosageTxt.setValueChangeMode(ValueChangeMode.EAGER);
        dosageTxt.setClearButtonVisible(true);
        dosageTxt.setWidthFull();
        dosageTxt.setPlaceholder("Dosage");
        dosageTxt.getElement().getStyle().set("font-size", "12px");

        TextField detailsTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("details")).setComponent(detailsTxt);
        detailsTxt.setValueChangeMode(ValueChangeMode.EAGER);
        detailsTxt.setClearButtonVisible(true);
        detailsTxt.setWidthFull();
        detailsTxt.setPlaceholder("Details");
        detailsTxt.getElement().getStyle().set("font-size", "12px");

        TextField batchNoTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("batchNb")).setComponent(batchNoTxt);
        batchNoTxt.setValueChangeMode(ValueChangeMode.EAGER);
        batchNoTxt.setClearButtonVisible(true);
        batchNoTxt.setWidthFull();
        batchNoTxt.setPlaceholder("Batch Nb");
        batchNoTxt.getElement().getStyle().set("font-size", "12px");

        TextField quantityTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("quantity")).setComponent(quantityTxt);
        quantityTxt.setValueChangeMode(ValueChangeMode.EAGER);
        quantityTxt.setClearButtonVisible(true);
        quantityTxt.setWidthFull();
        quantityTxt.setPlaceholder("Quantity");
        quantityTxt.getElement().getStyle().set("font-size", "12px");

        DatePicker expiryDatePicker = new DatePicker("");
        filterRow.getCell(medicineGrid.getColumnByKey("expiryDate")).setComponent(expiryDatePicker);
        expiryDatePicker.setClearButtonVisible(true);
        expiryDatePicker.setWidthFull();
        expiryDatePicker.setPlaceholder("Expiry Date");
        expiryDatePicker.getElement().getStyle().set("font-size", "12px");


        TextField buyingPriceTxt = new TextField("");
        if(!currentUser.getRole().equals(User.Role.CLIENT))
            filterRow.getCell(medicineGrid.getColumnByKey("buyingPrice")).setComponent(buyingPriceTxt);
        buyingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        buyingPriceTxt.setClearButtonVisible(true);
        buyingPriceTxt.setWidthFull();
        buyingPriceTxt.setPlaceholder("Buying Price");
        buyingPriceTxt.getElement().getStyle().set("font-size", "12px");

        TextField sellingPriceTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("sellingPrice")).setComponent(sellingPriceTxt);
        sellingPriceTxt.setValueChangeMode(ValueChangeMode.EAGER);
        sellingPriceTxt.setClearButtonVisible(true);
        sellingPriceTxt.setWidthFull();
        sellingPriceTxt.setPlaceholder("Selling Price");
        sellingPriceTxt.getElement().getStyle().set("font-size", "12px");

        TextField formulaTxt = new TextField("");
        filterRow.getCell(medicineGrid.getColumnByKey("formula")).setComponent(formulaTxt);
        formulaTxt.setValueChangeMode(ValueChangeMode.EAGER);
        formulaTxt.setClearButtonVisible(true);
        formulaTxt.setWidthFull();
        formulaTxt.setPlaceholder("Formula");
        formulaTxt.getElement().getStyle().set("font-size", "12px");

        nameTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        categoryTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        dosageTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        detailsTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        batchNoTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        quantityTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        expiryDatePicker.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        buyingPriceTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        sellingPriceTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
        formulaTxt.addValueChangeListener(event -> applyFilter(nameTxt, categoryTxt, dosageTxt, detailsTxt, batchNoTxt, quantityTxt, expiryDatePicker, buyingPriceTxt, sellingPriceTxt, formulaTxt));
    }

    private void applyFilter(TextField nameTxt, TextField categoryTxt, TextField dosageTxt, TextField detailsTxt, TextField batchNoTxt, TextField quantityTxt, DatePicker expiryDatePicker, TextField buyingPriceTxt, TextField sellingPriceTxt, TextField formulaTxt) {
        gridListDataView.setFilter(medicine ->
                (nameTxt.getValue() == null || nameTxt.getValue().isEmpty() || medicine.getName().contains(nameTxt.getValue()))
                        && (categoryTxt.getValue() == null || categoryTxt.getValue().isEmpty() || medicine.getCategory().contains(categoryTxt.getValue()))
                        && (dosageTxt.getValue() == null || dosageTxt.getValue().isEmpty() || medicine.getDosage().contains(dosageTxt.getValue()))
                        && (detailsTxt.getValue() == null || detailsTxt.getValue().isEmpty() || medicine.getDetails().contains(detailsTxt.getValue()))
                        && (batchNoTxt.getValue() == null || batchNoTxt.getValue().isEmpty() || medicine.getBatchNo().contains(batchNoTxt.getValue()))
                        && (quantityTxt.getValue() == null || quantityTxt.getValue().isEmpty() || medicine.getQuantity().contains(quantityTxt.getValue()))
                        && (expiryDatePicker.getValue() == null || medicine.getExpiryDate().equals(expiryDatePicker.getValue()))
                        && (buyingPriceTxt.getValue() == null || buyingPriceTxt.getValue().isEmpty() || medicine.getBuyingPrice().contains(buyingPriceTxt.getValue()))
                        && (sellingPriceTxt.getValue() == null || sellingPriceTxt.getValue().isEmpty() || medicine.getSellingPrice().contains(sellingPriceTxt.getValue()))
                        && (formulaTxt.getValue() == null || formulaTxt.getValue().isEmpty() || medicine.getFormula().contains(formulaTxt.getValue())));
    }


    private void createGrid() {
        gridListDataView = medicineGrid.setItems(dbServiceMedicine.findAllMedicines());
        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel nameLbl = new NativeLabel(medicine.getName());
            nameLbl.getElement().setProperty("title", medicine.getName());
            return nameLbl;
        }).setHeader("Name").setKey("name").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel detailsLbl = new NativeLabel(medicine.getDetails());
            detailsLbl.getElement().setProperty("title", medicine.getDetails());
            return detailsLbl;
        }).setHeader("Details").setKey("details").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel dosageLbl = new NativeLabel(medicine.getDosage());
            dosageLbl.getElement().setProperty("title", medicine.getDosage());
            return dosageLbl;
        }).setHeader("Dosage").setKey("dosage").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel categoryLbl = new NativeLabel(medicine.getCategory());
            categoryLbl.getElement().setProperty("title", medicine.getCategory());
            return categoryLbl;
        }).setHeader("Category").setKey("category").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel batchNbLbl = new NativeLabel(medicine.getBatchNo());
            batchNbLbl.getElement().setProperty("title", medicine.getBatchNo());
            return batchNbLbl;
        }).setHeader("Batch Nb").setKey("batchNb").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel quantityLbl = new NativeLabel(medicine.getQuantity());
            quantityLbl.getElement().setProperty("title", medicine.getQuantity());
            return quantityLbl;
        }).setHeader("Quantity").setKey("quantity").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel formulaLbl = new NativeLabel(medicine.getFormula());
            formulaLbl.getElement().setProperty("title", medicine.getFormula());
            return formulaLbl;
        }).setHeader("Formula").setKey("formula").setSortable(true).setResizable(true);

        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel expDateLbl = new NativeLabel(medicine.getExpiryDate().toString());
            expDateLbl.getElement().setProperty("title", medicine.getExpiryDate().toString());
            return expDateLbl;
        }).setHeader("Exp Date").setKey("expiryDate").setSortable(true).setResizable(true);
            if(!currentUser.getRole().equals(User.Role.CLIENT)) {
                medicineGrid.addComponentColumn(medicine -> {
                    NativeLabel buyingPriceLbl = new NativeLabel(medicine.getBuyingPrice());
                    buyingPriceLbl.getElement().setProperty("title", medicine.getBuyingPrice());
                    return buyingPriceLbl;
                }).setHeader("Buying Price").setKey("buyingPrice").setSortable(true).setResizable(true);
            }
        medicineGrid.addComponentColumn(medicine -> {
            NativeLabel sellingPriceLbl = new NativeLabel(medicine.getSellingPrice());
            sellingPriceLbl.getElement().setProperty("title", medicine.getSellingPrice());
            return sellingPriceLbl;
        }).setHeader("Selling Price").setKey("sellingPrice").setSortable(true).setResizable(true);


        if(!currentUser.getRole().equals(User.Role.CLIENT))
        medicineGrid.addComponentColumn(medicine -> {
            Button editBtn = createEditButton();
            editBtn.addClickListener(clickEvent -> {
                Dialog editDialog = new Dialog();
                editDialog.add(new MedicineForm(dbServiceMedicine, editDialog, medicine, medicineGrid, gridListDataView));
                editDialog.open();
            });
            Button deleteBtn = createDeleteButton();
            deleteBtn.addClickListener(buttonClickEvent -> {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setText("Are you sure you want to delete " + medicine.getName());
                confirmDialog.setHeader("Delete");
                confirmDialog.setCancelable(true);
                Button confirmDeleteBtn = createDeleteButton();
                confirmDeleteBtn.addClickListener(clickEvent -> {
                    dbServiceMedicine.deleteMedicine(medicine.get_id());
                    gridListDataView = medicineGrid.setItems(dbServiceMedicine.findAllMedicines());
                    gridListDataView.refreshAll();
                    confirmDialog.close();
                });
                confirmDialog.setCancelButton(new Button("Cancel", clickEvent -> confirmDialog.close()));
                confirmDialog.setConfirmButton(confirmDeleteBtn);
                confirmDialog.open();
            });
            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Edit").setFooter(addNewMedicineBtn).setWidth("10%");
        addNewMedicineBtn.getStyle().set("background-color", getSaveBtnColor());
        addNewMedicineBtn.getStyle().set("color", "white");
        addNewMedicineBtn.getStyle().set("border", "none");
        addNewMedicineBtn.getStyle().set("border-radius", "5px");
        medicineGrid.setHeightFull();
        add(medicineGrid);
    }
}




