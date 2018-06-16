package com.github.tinosteinort.beanrepository.application.env;

import java.util.function.Supplier;

public class ArgsProvider implements Supplier<String[]> {

    private final String[] args;

    public ArgsProvider(final String[] args) {
        this.args = args;
    }

    @Override public String[] get() {
        return args;
    }
}
