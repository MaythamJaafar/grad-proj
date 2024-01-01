package com.example.application.util;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.application.util.Util.showErrorNotification;

@Slf4j
public class InputChecker {
    private static NumberField numberField;
    private static AtomicBoolean continueFlag;

    public static boolean checkDates(DatePicker sinceDatePicker, DatePicker untilDatePicker, LocalDate date) {
        if (date == null)
            return false;
        else if (sinceDatePicker.getValue() == null && untilDatePicker.getValue() == null)
            return true;
        else if (sinceDatePicker.getValue() != null && untilDatePicker.getValue() != null)
            return !date.isBefore(sinceDatePicker.getValue()) && !date.isAfter(untilDatePicker.getValue());
        else if (sinceDatePicker.getValue() != null)
            return !date.isBefore(sinceDatePicker.getValue());
        else
            return untilDatePicker.getValue() != null && !date.isAfter(untilDatePicker.getValue());
    }

    public static boolean checkDates(DatePicker sinceDatePicker, DatePicker untilDatePicker, LocalDateTime dateTime) {
        if (sinceDatePicker.getValue() == null && untilDatePicker.getValue() == null)
            return true;
        else if (dateTime == null)
            return false;
        else if (sinceDatePicker.getValue() != null && untilDatePicker.getValue() != null)
            return !dateTime.toLocalDate().isBefore(sinceDatePicker.getValue()) && !dateTime.toLocalDate().isAfter(untilDatePicker.getValue());
        else if (sinceDatePicker.getValue() != null)
            return !dateTime.toLocalDate().isBefore(sinceDatePicker.getValue());
        else
            return untilDatePicker.getValue() != null && !dateTime.toLocalDate().isAfter(untilDatePicker.getValue());
    }

    public static boolean checkIntegers(IntegerField fromIntegerField, IntegerField toIntegerField, int intToCompare) {
        if (fromIntegerField.getValue() == null && toIntegerField.getValue() == null)
            return true;
        else if (fromIntegerField.getValue() != null && toIntegerField.getValue() != null)
            return intToCompare > fromIntegerField.getValue() && intToCompare < toIntegerField.getValue();
        else if (fromIntegerField.getValue() != null)
            return intToCompare > fromIntegerField.getValue();
        else
            return toIntegerField.getValue() != null && intToCompare < toIntegerField.getValue();
    }

    public static void checkCombo(ComboBox comboBox, AtomicBoolean continueFlag) {
        if (comboBox.getValue() == null || comboBox.getValue().toString().isEmpty()) {
            comboBox.setInvalid(true);
            comboBox.setErrorMessage("Fill Empty Fields");
            comboBox.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkSelect(Select select, AtomicBoolean continueFlag) {
        if (select.getValue() == null) {
            select.setInvalid(true);
            select.setErrorMessage("Fill Empty Fields");
            select.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkCheckBoxGroup(CheckboxGroup checkbox, int countOfItems, AtomicBoolean continueFlag) {
        if (checkbox.getValue() == null || countOfItems == 0) {
            checkbox.setInvalid(true);
            checkbox.setErrorMessage("Fill Empty Fields");
            checkbox.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkCheckBox(Checkbox checkbox, AtomicBoolean continueFlag, UI ui) {
        if (checkbox.getValue() == null || !checkbox.getValue()) {
            checkbox.scrollIntoView();
            continueFlag.set(false);
            if (checkbox.getLabel().contains("ADX")) {
                ui.access(() -> showErrorNotification("Please accept to digitally sign ADX form."));
            }
            if (checkbox.getLabel().contains("DFM")) {
                ui.access(() -> showErrorNotification("Please accept to digitally sign DFM form."));
            }
        }
    }

    public static boolean containsArabicCharacters(String text) {
        Pattern pattern = Pattern.compile("[\\p{InArabic}]");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static void checkRdGrp(RadioButtonGroup rdGrp, AtomicBoolean continueFlag) {
        if (rdGrp.getValue() == null || rdGrp.getValue().toString().isEmpty()) {
            rdGrp.setInvalid(true);
            rdGrp.setErrorMessage("Fill Empty Fields");
            rdGrp.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkNumberField(NumberField numberField, AtomicBoolean continueFlag) {
        InputChecker.numberField = numberField;
        InputChecker.continueFlag = continueFlag;
        if (numberField.getValue() == null) {
            numberField.setInvalid(true);
            numberField.setErrorMessage("Fill Empty Fields");
            numberField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkTextField(TextField textField, AtomicBoolean continueFlag) {
        if (textField.getValue() == null || textField.getValue().isEmpty()) {
            textField.setInvalid(true);
            textField.setErrorMessage("Fill Empty Fields");
            textField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkIban(TextField ibanTxt, int limitation, AtomicBoolean continueFlag) {
        if (ibanTxt.getValue() != null && ibanTxt.getValue().length() > 0) {
            ibanTxt.setValue(ibanTxt.getValue().replaceAll(" ", ""));
            if (limitation > 0 && ibanTxt.getValue().length() != limitation) {
                ibanTxt.setInvalid(true);
                ibanTxt.setErrorMessage("Iban Number should be " + limitation + " character");
                if (ibanTxt.getValue().length() > limitation) {
                    String splitValue = ibanTxt.getValue().substring(0, limitation);
                    ibanTxt.setValue(splitValue);
                }
                continueFlag.set(false);
            }
        } else {
            ibanTxt.setInvalid(true);
            ibanTxt.setErrorMessage("Fill Empty Fields");
            ibanTxt.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkTextAreaField(TextArea textArea, AtomicBoolean continueFlag) {
        if (textArea.getValue() == null || textArea.getValue().isEmpty()) {
            textArea.setInvalid(true);
            textArea.setErrorMessage("Fill Empty Fields");
            textArea.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkZeros(TextField textField, AtomicBoolean continueFlag) {
        String value = textField.getValue() == null ? "" : textField.getValue();
        if (value.startsWith("0")) {
            textField.setInvalid(true);
            textField.setErrorMessage("Should not start with zero");
            textField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkArabicTextField(TextField textField, AtomicBoolean continueFlag) {
        if (textField.getValue() != null && !textField.getValue().isEmpty() && textField.isEnabled()) {
            if (!textField.getValue().matches("[\\p{IsArabic}\\s]+")) {
                textField.setInvalid(true);
                textField.setErrorMessage("Only Arabic characters are allowed");
                textField.scrollIntoView();
                continueFlag.set(false);
            } else textField.setInvalid(false);
        } else {
            textField.setInvalid(true);
            textField.setErrorMessage("Fill Empty Fields");
            textField.scrollIntoView();
            continueFlag.set(false);
        }

    }

    public static void checkEnglishTextField(TextField textField, AtomicBoolean continueFlag) {
        if (!textField.getValue().matches("[a-zA-Z\\s]+")) {
            textField.setInvalid(true);
            textField.setErrorMessage("Only English characters are allowed");
            textField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkEnglishAndNumbersTextField(TextField textField, AtomicBoolean continueFlag) {
        if (!textField.getValue().matches("[a-zA-Z0-9\\s]+")) {
            textField.setInvalid(true);
            textField.setErrorMessage("Only English characters and numbers are allowed");
            textField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkEnglishAlphaNumericTextField(TextField textField, AtomicBoolean continueFlag) {
        if (!textField.getValue().matches("[a-zA-Z0-9\\s]+")) {
            textField.setInvalid(true);
            textField.setErrorMessage("Only English character or Numbers are allowed");
            textField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void setEnglishAlphaNumericAllowedCheckListener(TextField textField, UI ui) {
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> ui.access(() -> checkEnglishAlphaNumericTextField(textField, new AtomicBoolean())));
        textField.addBlurListener(event -> ui.access(() -> checkEnglishAlphaNumericTextField(textField, new AtomicBoolean())));
    }

    public static void setEnglishAllowedCheckListener(TextField textField, UI ui) {
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> ui.access(() -> checkEnglishTextField(textField, new AtomicBoolean())));
        textField.addBlurListener(event -> ui.access(() -> checkEnglishTextField(textField, new AtomicBoolean())));
    }

    public static void setEnglishAllowedCheckAndLimitedListener(TextField textField, UI ui) {
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> ui.access(() -> checkEnglishTextField(textField, new AtomicBoolean())));
        textField.addBlurListener(event -> ui.access(() -> checkEnglishTextField(textField, new AtomicBoolean())));
    }

    public static void setEnglishAndNumbersAllowedCheckListener(TextField textField, UI ui) {
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> ui.access(() -> checkEnglishAndNumbersTextField(textField, new AtomicBoolean())));
        textField.addBlurListener(event -> ui.access(() -> checkEnglishAndNumbersTextField(textField, new AtomicBoolean())));
    }

    public static void setArabicAllowedCheckListener(TextField textField, UI ui) {
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.addValueChangeListener(event -> ui.access(() -> checkArabicTextField(textField, new AtomicBoolean())));
        textField.addBlurListener(event -> ui.access(() -> checkArabicTextField(textField, new AtomicBoolean())));
    }

    public static void checkPasswordField(PasswordField passwordField, AtomicBoolean continueFlag) {
        if (passwordField.getValue() == null || passwordField.getValue().isEmpty()) {
            passwordField.setInvalid(true);
            passwordField.setErrorMessage("Fill Empty Fields");
            passwordField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkEmailField(EmailField emailField, AtomicBoolean continueFlag) {
        if (emailField.getValue() == null || emailField.getValue().isEmpty()) {
            emailField.setInvalid(true);
            emailField.setErrorMessage("Fill Empty Fields");
            emailField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkIntegerField(IntegerField integerField, AtomicBoolean continueFlag) {
        if (integerField.getValue() == null) {
            integerField.setInvalid(true);
            integerField.setErrorMessage("Fill Empty Fields");
            integerField.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkDatePicker(DatePicker datePicker, AtomicBoolean continueFlag) {
        if (datePicker.getValue() == null) {
            datePicker.setInvalid(true);
            datePicker.setErrorMessage("Fill Empty Fields");
            datePicker.scrollIntoView();
            continueFlag.set(false);
        }
    }

    public static void checkMaxLimitations(AbstractField.ComponentValueChangeEvent<TextField, String> event, TextField textField, int limit, String type, UI ui) {
        if (event.getValue() != null && event.isFromClient())
            ui.access(() -> {
                if (event.getValue().length() > limit) {
                    String splitValue = event.getValue().substring(0, limit);
                    textField.setValue(splitValue);
                    if (textField.getValue().length() == limit) {
                        textField.setInvalid(true);
                        textField.setErrorMessage(type + " max Characters allowed is " + limit + " character");
                    }
                } else textField.setInvalid(false);
            });
    }
}