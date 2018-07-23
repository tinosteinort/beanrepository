package com.github.tinosteinort.beanrepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HitCounter {

    private final List<String> hits = new ArrayList<>();

    public int hits() {
        return hits.size();
    }

    public void hit() {
        hits.add("<anonymous>");
    }

    public void reset() {
        hits.clear();
    }

    public void hit(final String name) {
        Objects.requireNonNull(name, "name of hit must not be null");
        hits.add(name);
    }

    public boolean hasHit(final String name) {
        return hits.contains(name);
    }
}
