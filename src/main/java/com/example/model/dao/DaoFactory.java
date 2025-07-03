package com.example.model.dao;

import com.example.db.DB;
import com.example.model.dao.impl.DepartmentDaoJDBC;
import com.example.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }

}
