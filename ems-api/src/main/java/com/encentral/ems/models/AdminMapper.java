package com.encentral.ems.models;

import com.encentral.entities.JPAAdmin;
 public class AdminMapper {
    public static JPAAdmin adminToJPAAdmin(Admin admin) {
        JPAAdmin jpaAdmin = new JPAAdmin();
        jpaAdmin.setEmail(admin.getEmail());
        jpaAdmin.setId(admin.getId());
        jpaAdmin.setName(admin.getName());
        jpaAdmin.setPassword(admin.getPassword());
        jpaAdmin.setToken(admin.getToken());
        return jpaAdmin;
    }

    public static  Admin jpaAdmintoAdmin(JPAAdmin jpaAdmin) {
        Admin admin = new Admin();
        admin.setEmail(jpaAdmin.getEmail());
      if(jpaAdmin.getId() != null)  admin.setId(jpaAdmin.getId().toString());
        admin.setName(jpaAdmin.getName());
        admin.setPassword(jpaAdmin.getPassword());
        admin.setToken(jpaAdmin.getToken());
        return admin;
    }
}
