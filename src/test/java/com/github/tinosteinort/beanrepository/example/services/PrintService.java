package com.github.tinosteinort.beanrepository.example.services;

public class PrintService implements WriterService {

    @Override public void print(final String value) {
        System.out.println(value);
    }
}
