package com.encentral.ems.impl;

import com.encentral.ems.api.IEmployee;
import com.google.inject.AbstractModule;

public class EmployeeModule  extends AbstractModule {
    @Override
    public void configure() {
        bind(IEmployee.class).to(DefaultEmployeeImpl.class);
    }
}
