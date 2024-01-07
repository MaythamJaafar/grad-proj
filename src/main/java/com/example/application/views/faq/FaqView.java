package com.example.application.views.faq;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

import static com.example.application.util.Util.cardComponent;

@PageTitle("FAQ")
@Route(value = "faq", layout = MainLayout.class)
@PermitAll
public class FaqView extends VerticalLayout {

    public FaqView() {
        createFaqHeader();
        createFAQAccordion();
        getElement().getStyle().set("padding", "0");
        setWidthFull();
    }

    private void createFaqHeader() {
        Div backgroundDiv = new Div();
        backgroundDiv.getStyle().set("background-image", "url(./images/faq.jpg)");
        backgroundDiv.getStyle().set("background-size", "cover");
        backgroundDiv.getStyle().set("background-position", "center");
        backgroundDiv.setWidthFull();
        HorizontalLayout headerHl = new HorizontalLayout();
        headerHl.setHeight("200px");
        backgroundDiv.add(headerHl);
        add(backgroundDiv);
    }

    private void createFAQAccordion() {

        HorizontalLayout accordionCardsHl = new HorizontalLayout();
        accordionCardsHl.setWidth("90%");

        List<String> questionList = new ArrayList<>();
        List<String> answerList = new ArrayList<>();

        questionList.add("How do I refill my prescription online in data?");
        answerList.add("To refill your prescription online, log in to your account, go to the prescription section, and follow the prompts to submit your refill request. You can also use our mobile app for quick and easy refills.");

        questionList.add("Can I transfer my prescription to another pharmacy?");
        answerList.add("Yes, you can transfer your prescription. Simply provide us with the details, and we'll take care of the rest. You can initiate the transfer online or in-store.");

        questionList.add("What is the expiration date policy for medications?");
        answerList.add("We adhere to strict guidelines regarding medication expiration dates. All medications shipped from our pharmacy have a sufficient shelf life, and we recommend checking the expiration date before use.");

        questionList.add("What payment methods are accepted for online orders?");
        answerList.add("We accept major credit cards, debit cards, and some insurance plans for online orders. Check our website for the complete list of accepted payment methods.");

        questionList.add("How can I track the status of my prescription order?");
        answerList.add("Log in to your account and navigate to the order status section to track the progress of your prescription. You can also contact our customer service for assistance.");

        questionList.add("Do you offer automatic prescription refills?");
        answerList.add("Yes, we offer automatic refills for maintenance medications. Enroll in our AutoRefill program, and we'll ensure you receive your medications on time without the hassle of manual refills.");

        questionList.add("What is your prescription delivery service?");
        answerList.add("We offer prescription delivery within [service area]. Delivery is free for orders over [amount], and you can expect your medications within [timeframe].");

        questionList.add("How can I request a generic alternative for my medication?");
        answerList.add("If you're interested in a generic alternative, please speak with your pharmacist during your next visit or contact us. Our team will work with your healthcare provider to explore suitable options.");

        questionList.add("What should I do if I experience side effects from my medication?");
        answerList.add("If you experience any side effects, discontinue the medication and contact your healthcare provider immediately. You can also reach out to our pharmacist for guidance.");

        questionList.add("Can I purchase over-the-counter (OTC) medications online?");
        answerList.add("Yes, you can purchase a wide range of OTC medications on our website. Simply add the products to your cart, proceed to checkout, and we'll deliver them to your doorstep.");

        questionList.add("How do I update my personal information in my account?");
        answerList.add("To update your personal information, log in to your account and navigate to the profile settings. You can modify your details such as address, phone number, and email.");

        questionList.add("Is my personal and health information secure on website?");
        answerList.add("Yes, we take the security and privacy of your information seriously. Our website uses advanced encryption and follows industry standards to protect your personal and health data.");

        questionList.add("What do I do if I forget my account password?");
        answerList.add("If you forget your password, click on the 'Forgot Password' link on the login page. Follow the instructions to reset your password via the email associated with your account.");

        questionList.add("Are there any discounts or loyalty programs for frequent customers?");
        answerList.add("Yes, we offer discounts and loyalty programs for our frequent customers. Check our promotions page and subscribe to our newsletter to stay informed about special offers.");

        questionList.add("Can I order medications for someone else?");
        answerList.add("Yes, you can order medications for someone else. During the checkout process, provide the recipient's information, and make sure to enter their prescription details accurately.");

        questionList.add("What do I do if I receive the wrong medication or dosage?");
        answerList.add("If you receive the wrong medication or dosage, contact our customer service immediately. We will guide you on the steps to return the incorrect items and receive the correct prescription.");

        questionList.add("How can I transfer my prescription history to a new healthcare provider?");
        answerList.add("To transfer your prescription history to a new healthcare provider, contact our pharmacy, and we will assist you in providing the necessary information to your new healthcare professional.");

        questionList.add("What information do I need for online prescription refills?");
        answerList.add("To refill your prescription, you'll need the prescription number, your name, and the medication details. You can find this information on your prescription label.");

        questionList.add("Do you offer consultations with pharmacists for medication-related ?");
        answerList.add("Yes, we provide consultations with our pharmacists for any medication-related queries. Feel free to call or visit our pharmacy, or use our online chat service for quick assistance.");

        questionList.add("Can I request a refill for a medication that's not on my prescription list?");
        answerList.add("Refills are generally limited to medications on your prescription list. If you need a refill for a new medication, please consult with your healthcare provider for a new prescription.");

        int nbOfElementsInHl = 4;
        VerticalLayout vl = new VerticalLayout();
        vl.setHeight("100%");
        int x = questionList.size() / 4;

        for (int i = 0; i < questionList.size(); i++) {
            if (i != 0 && i % x == 0) {
                accordionCardsHl.add(vl);
                vl = new VerticalLayout();
                vl.setHeight("100%");
            }
            vl.add(cardComponent(questionList.get(i), answerList.get(i)));
            if (i != 0 && i == questionList.size() - 1 && i % nbOfElementsInHl != 0)
                accordionCardsHl.add(vl);
        }
        add(accordionCardsHl);
    }

}