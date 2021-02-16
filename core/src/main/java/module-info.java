module se.group4.core {
    //requires se.group4.spi;
    requires se.group4.fileutils;

    //For hibernate
    requires java.persistence;
    requires java.sql;
    requires net.bytebuddy;
    requires com.fasterxml.classmate;
    requires java.xml.bind;
    requires com.google.gson;
    requires jdk.httpserver;

    opens se.group4.core to org.hibernate.orm.core;
//    requires static lombok;
    //Vart är lombok?
}