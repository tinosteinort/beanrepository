package com.github.tinosteinort.beanrepository;

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
}
