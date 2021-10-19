package com.encentral.ems.impl;

import com.encentral.ems.api.IAdmin;
import com.google.inject.AbstractModule;

public class AdminModule extends AbstractModule {
    @Override
    protected void configure() {
            super.configure();
        bind(IAdmin.class).to(DefaultAdminImpl.class);

    }
}
