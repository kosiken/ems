package com.encentral.ems.impl;

import com.encentral.ems.models.IEmsModel;
import com.encentral.entities.JPAEmployee;
import com.encentral.entities.QJPAEmployee;
import com.google.common.base.Preconditions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class AbstractImpl {
    protected JPAQueryFactory queryFactory = null;
    protected EntityManager entityManager = null;

    public AbstractImpl() {

    }

    protected void reinitialize(EntityManager entityManager) {
        if(this.entityManager == null) {
            queryFactory = new JPAQueryFactory(entityManager);
            this.entityManager = entityManager;
            return;
        }
        // We have to create new entity managers if the current one is closed
        if(!this.entityManager.isOpen()) {
            this.entityManager = entityManager;
            queryFactory = new JPAQueryFactory(entityManager);
            return;
        }
    }

    protected void close(String entity) {
//        App.logger.info("Closing " + entity);
        this.entityManager.close();
    }

    protected EntityTransaction createTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        return transaction;
    }

    protected void setModelId(IEmsModel model) {
        Preconditions.checkNotNull(model);
        model.setId(UUID.randomUUID().toString());
    }

    protected JPAEmployee getEmployeeEmail(String value) {
        Preconditions.checkNotNull(value, "Email cannot be null");

        QJPAEmployee employee = QJPAEmployee.jPAEmployee;
        JPAEmployee jpaEmployee= null;



        jpaEmployee = queryFactory.selectFrom(employee)
                .where(employee.email.eq(value))
                .fetchOne();



        return jpaEmployee;
    }

    protected JPAEmployee getEmployeeId(String value) {
        Preconditions.checkNotNull(value, "Email cannot be null");

        QJPAEmployee employee = QJPAEmployee.jPAEmployee;
        JPAEmployee jpaEmployee= null;


        jpaEmployee = queryFactory.selectFrom(employee)
                .where(employee.id.eq(value))
                .fetchOne();



        return jpaEmployee;
    }
}
