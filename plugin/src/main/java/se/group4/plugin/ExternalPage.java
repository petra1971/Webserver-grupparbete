package se.group4.plugin;

import se.group4.spi.Page;

public class ExternalPage implements Page {

    public ExternalPage() {
        System.out.println("Constructor for External Page");
    }

    @Override
    public void execute() {
        System.out.println("This is External Page");
    }
}
