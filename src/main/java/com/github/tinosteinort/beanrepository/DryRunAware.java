package com.github.tinosteinort.beanrepository;

import java.util.concurrent.Callable;

class DryRunAware {

    private boolean dryRun = false;

    boolean isDryRun() {
        return dryRun;
    }

    void execute(final Runnable runnable) {
        dryRun = true;
        try {
            runnable.run();
        }
        finally {
            dryRun = false;
        }
    }

    <T> T execute(final Callable<T> callable) throws Exception {
        dryRun = true;
        try {
            return callable.call();
        }
        finally {
            dryRun = false;
        }
    }
}
