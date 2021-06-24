package com.phonebook.main;

import com.phonebook.spring.ApplicationConfig;
import com.phonebook.spring.PhoneBook;
import com.phonebook.spring.PhoneBookFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

/**
 * PhoneBook entry point
 */
public class PhoneBookMain {

    public static void main(String[] args) {
        ApplicationContext context = newApplicationContext(args);

        Scanner sc = new Scanner(System.in);
        sc.useDelimiter(System.getProperty("line.separator"));

        PhoneBook phoneBook = context.getBean("phoneBook", PhoneBook.class);
        PhoneBookFormatter renderer = (PhoneBookFormatter) context.getBean("phoneBookFormatter");

        renderer.info("type 'ADD' 'name' 'phone' to add a phone to an existing or new user");
        renderer.info("type 'SHOW' to show contents of a phonebook");
        renderer.info("type 'REMOVE_PHONE' to remove a phone number");
        renderer.info("type 'exit' to quit.");
        while (sc.hasNext()) {
            String line = sc.nextLine().trim();
            if (line.equals("exit")) {
                renderer.info("Have a good day...");
                break;
            }
            try {
                if (line.contains("ADD")) {
                    String name = line.substring(4,line.lastIndexOf(" "));
                    String phone = line.substring(line.indexOf("+"));
                    phoneBook.addPhone(name, phone);
                    renderer.info(String.format("Adding a new phone %s to a user %s",phone,name));
                }
                if (line.contains("SHOW")) {
                    renderer.info("Showing content of a phone book");
                    renderer.show(phoneBook.findAll());
                }
                if (line.contains("REMOVE_PHONE")) {
                    String phone = line.substring(line.indexOf("+"));
                    renderer.info(String.format("Phone number %s is removed",phone));
                    phoneBook.removePhone(phone);
                }
            } catch (Exception e) {
                renderer.error(e);
                break;
            }
        }
    }

    static ApplicationContext newApplicationContext(String... args) {
        return args.length > 0 && args[0].equals("classPath")
                ? new ClassPathXmlApplicationContext("application-config.xml")
                : new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }

}
