package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanAccessor;

public class MailService {

    private final PrintService printService;

    public MailService(final BeanAccessor beans) {
        this.printService = beans.getBean(PrintService.class);
    }

    public void sendMail(final String to, final String text) {
        printService.print("Message for " + to + ": " + text);
    }
}
