package com.example;

import com.example.model.Department;
import com.example.model.dao.DaoFactory;
import com.example.model.dao.DepartmentDao;

import java.util.List;

public class Main2 {

    public static void main(String[] args) {
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Department - insert ===");
        Department dep = new Department(null, "Gelados");
        departmentDao.insert(dep);

        System.out.println();

        System.out.println("=== Department - update ===");
        departmentDao.update(new Department(5, "Quentes"));

        System.out.println();

        System.out.println("=== Department - delete ===");
        departmentDao.deleteById(6);

        System.out.println();

        System.out.println("=== Department - findById ==");
        Department department = departmentDao.findById(5);
        System.out.println(department);

        System.out.println();

        System.out.println("=== Department - findAll ==");
        List<Department> departments = departmentDao.findAll();
        departments.forEach(System.out::println);
    }

}
