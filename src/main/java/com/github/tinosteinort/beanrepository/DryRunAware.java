package com.github.tinosteinort.beanrepository;

import java.util.concurrent.Callable;

class DryRunAware {

    private boolean dryRun = false;

    boolean isDryRun() {
        return dryRun;
    }

    void execute(final Runnable runnable) {
        final boolean dryRunBefore = dryRun;
        dryRun = true;
        try {
            runnable.run();
        }
        finally {
            dryRun = dryRunBefore;
        }
    }

    <T> T execute(final Callable<T> callable) {
        final boolean dryRunBefore = dryRun;
        dryRun = true;
        try {
            try {
                return callable.call();
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        finally {
            dryRun = dryRunBefore;
        }
    }
}
