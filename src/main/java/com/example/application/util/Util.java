package com.example.application.util;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Util {

    public static Button createEditButton() {
        Button editBtn = new Button("Edit");
        setBtnStyles(editBtn, getSaveBtnColor());
        return editBtn;
    }

    public static Button createCancelButton() {
        Button cancelBtn = new Button("Cancel");
        setBtnStyles(cancelBtn, getSaveBtnColor());
        return cancelBtn;
    }

    public static Button createDeleteButton() {
        Button deleteBtn = new Button("Delete");
        setBtnStyles(deleteBtn, getCancelBtnColor());
        return deleteBtn;
    }

    public static void setBtnStyles(Button button, String color) {
        button.getStyle().set("background-color", color);
        button.getStyle().set("color", "white");
        button.getStyle().set("border", "5px");
        button.getStyle().set("border-radius", "5px");
    }

    public static void showSuccessNotification(String text) {
        Notification.show(text, 2000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    public static void showErrorNotification(String text) {
        Notification.show(text, 2000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static String getSaveBtnColor() {
        return "hsl(145, 72%, 30%)";
    }

    public static String getCancelBtnColor() {
        return "red";
    }

    public static VerticalLayout cardComponent(String question, String answer) {
        Accordion accordion = new Accordion();
        accordion.add(question, createAnswerLayout(answer));
        accordion.close();

        VerticalLayout card = new VerticalLayout();
        card.add(accordion);
        card.addClassName("card");
        return card;
    }

    private static VerticalLayout createAnswerLayout(String answer) {
        VerticalLayout answerLayout = new VerticalLayout();
        answerLayout.addClassName("faq-answer-layout");

        Div answerText = new Div();
        answerText.setText(answer);
        answerText.addClassName("faq-answer-text");

        answerLayout.add(answerText);

        return answerLayout;
    }
}
