package de.tse.beanrepository.example.services;

import de.tse.beanrepository.BeanRepository;

public class MailService {

    private final PrintService printService;

    public MailService(final BeanRepository repo) {
        this.printService = repo.get(PrintService.class);
    }

    public void sendMail(final String to, final String text) {
        printService.print("Message for " + to + ": " + text);
    }
}
