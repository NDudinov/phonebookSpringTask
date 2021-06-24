package com.phonebook.tests;

import com.phonebook.spring.ApplicationConfig;
import com.phonebook.spring.PhoneBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.springframework.test.util.AssertionErrors.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class PhoneBookTest {

    @Autowired
    private PhoneBook phoneBook;

    // TODO: implement a couple of tests for PhoneBook.class
    @Test
    public void get_person_phone_numbers() {
        final Set<String> expected = new HashSet<>(asList("+79601232233"));
        assertEquals("phone numbers do not match",
                expected,
                phoneBook.findAll().get("Alex"));
    }

    @Test
    public void get_person_only_phone() {
        final Set<String> expected = new HashSet<>(asList("+79601232233"));
        assertEquals("phone numbers do not match",
                expected,
                phoneBook.findAllPhonesByName("Alex"));
    }

    @Test
    public void get_person_by_phone() {
        final String expected = "Alex";
        assertEquals("name does not match",
                expected,
                phoneBook.findNameByPhone("+79601232233"));
    }

    @Test
    public void add_person() {
        final String name_expected = "Joe";
        final Set<String> phone_expected = new HashSet<>(asList("+11111232233"));
        phoneBook.addPhone(name_expected, phone_expected.stream().collect(Collectors.toList()).get(0));
        assertEquals("phone numbers do not match",
                phone_expected,
                phoneBook.findAllPhonesByName("Joe"));
        assertEquals("name does not match",
                name_expected,
                phoneBook.findNameByPhone(phone_expected.stream().collect(Collectors.toList()).get(0)));
    }

    @Test
    public void remove_phone() {
        final String phone = "+79213215566";
        final String name = "Billy";
        phoneBook.removePhone(phone);
        assertFalse("Phone wasn't deleted", phoneBook.findAllPhonesByName(name).contains(phone));
    }

    @Test
    public void remove_last_phone() {
        final String phone = "+79601232233";
        final String name = "Alex";
        phoneBook.removePhone(phone);
        assertFalse("Record wasn't deleted", phoneBook.findAll().containsKey(name));
    }

    @Test
    public void remove_nonexistent_phone() {
        final String phone = "+111111111";
        phoneBook.removePhone(phone);
        assertTrue("Record wasn't deleted", true);
    }

}
