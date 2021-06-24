package com.phonebook.spring;

import com.phonebook.main.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.asList;

/**
 * Keeps phoneBook data in memory in ordered in accordance to addition.
 */
@Repository
public class InMemoryRepositoryIml implements InMemoryRepository {

    private Map<String, Set<String>> data;

    private final static String NO_NAME = "No name with this phone";

    @Autowired
    PhoneBookFormatter formatter;

    /**
     * no args constructor
     */
    public InMemoryRepositoryIml() {
        // LinkedHashMap is chosen because usually iteration order matters
        this(new LinkedHashMap<>());
    }

    /**
     * this constructor allows to inject initial data to the repository
     *
     * @param data
     */
    public InMemoryRepositoryIml(Map<String, Set<String>> data) {
        this.data = new LinkedHashMap<>(data);
    }

    @Override
    public Map<String, Set<String>> findAll() {
        return new LinkedHashMap<>(this.data);
    }

    @Override
    public Set<String> findAllPhonesByName(String name) {
        Set<String> result = new HashSet<>();
        data.forEach((key, value) -> {
            if (key.contains(name)) {
                result.addAll(value);
            }
        });
        return result;
    }

    @Override
    public String findNameByPhone(String phone) {
        String result = NO_NAME;
        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            if (entry.getValue().contains(phone)) {
                result = entry.getKey();
            }
        }
        return result;
    }

    @Override
    public void addPhone(String name, String phone) {
        if (data.containsKey(name)) {
            data.get(name).add(phone);
        } else {
            Set<String> phone_set = new HashSet<>(asList(phone));
            data.put(name, phone_set);
        }
    }

    @Override
    public void removePhone(String phone) throws IllegalArgumentException {
        String key = findNameByPhone(phone);
        if (key.equals(NO_NAME)) {
            formatter.error(NO_NAME);
            formatter.error(new IllegalArgumentException());
        } else{
            if (findAll().get(key).size() > 1) {
                data.get(key).remove(phone);
            } else data.remove(key);
        }

    }
}
