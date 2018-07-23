package com.github.tinosteinort.beanrepository;

public class DryRunAwareMock extends DryRunAware {

    public static final DryRunAware DRY_RUN = new DryRunAwareMock(true);
    public static final DryRunAware NO_DRY_RUN = new DryRunAwareMock(false);

    private final boolean isDryRun;

    public DryRunAwareMock(final boolean isDryRun) {
        this.isDryRun = isDryRun;
    }

    @Override boolean isDryRun() {
        return isDryRun;
    }
}
