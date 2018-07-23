package com.github.tinosteinort.beanrepository;

import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DryRunAwareTest {

    @Test public void simpleDryRunExecuteRunnable() {

        DryRunAware dryRun = new DryRunAware();
        assertFalse(dryRun.isDryRun());

        dryRun.execute(() -> {
            assertTrue(dryRun.isDryRun());
        });

        assertFalse(dryRun.isDryRun());
    }

    @Test public void simpleDryRunExecuteCallable() {

        DryRunAware dryRun = new DryRunAware();
        assertFalse(dryRun.isDryRun());

        dryRun.execute((Callable<Void>) () -> {
            assertTrue(dryRun.isDryRun());
            return null;
        });

        assertFalse(dryRun.isDryRun());
    }

    @Test public void nestedDryRunExecuteRunnable() {

        DryRunAware dryRun = new DryRunAware();
        assertFalse(dryRun.isDryRun());

        dryRun.execute(() -> {
            assertTrue(dryRun.isDryRun());

            dryRun.execute(() -> {
                assertTrue(dryRun.isDryRun());
            });

            assertTrue(dryRun.isDryRun());
        });

        assertFalse(dryRun.isDryRun());
    }

    @Test public void nestedDryRunExecuteCallable() {

        DryRunAware dryRun = new DryRunAware();
        assertFalse(dryRun.isDryRun());

        dryRun.execute((Callable<Void>) () -> {
            assertTrue(dryRun.isDryRun());

            dryRun.execute((Callable<Void>) () -> {
                assertTrue(dryRun.isDryRun());
                return null;
            });

            assertTrue(dryRun.isDryRun());
            return null;
        });

        assertFalse(dryRun.isDryRun());
    }
}