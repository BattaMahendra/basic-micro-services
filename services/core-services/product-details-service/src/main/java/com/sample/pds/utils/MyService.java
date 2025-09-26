package com.sample.pds.utils;

import java.util.UUID;

public class MyService {
    private final String id;

    public MyService(String scope) {
        // generate unique ID for each instance
        this.id = scope + "-" + UUID.randomUUID().toString();
        System.out.println("Creating instance: " + id);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {

        return "MyService{id='" + id + "'}";
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
