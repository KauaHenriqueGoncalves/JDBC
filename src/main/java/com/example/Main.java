package com.example;

import com.example.model.Department;
import com.example.model.Seller;
import com.example.model.dao.DaoFactory;
import com.example.model.dao.SellerDao;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== Test Sellers - findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println();

        System.out.println("=== Test Sellers - findByDepartmentId ===");
        Department department = new Department(2, null);
        List<Seller> sellers = sellerDao.findByDepartment(department);
        sellers.forEach(System.out::println);

        System.out.println();

        System.out.println("=== Test Sellers - findAll ===");
        List<Seller> sellersAll = sellerDao.findAll();
        sellersAll.forEach(System.out::println);

        System.out.println();

        System.out.println("=== Test Sellers - insert ===");
        Seller newSeller = new Seller(null, "Kauã Henrique Gonçalves", "kauahenriquegoncalves00@gmail.com", new Date(), 2500.00, department);
        sellerDao.insert(newSeller);

        System.out.println();

        System.out.println("=== Test Sellers - update ===");
        sellerDao.update(new Seller(7, "John Mark", "johnyBravo@gmail.com", new Date(), 3000.00, department));

        System.out.println();

        System.out.println("=== Test Sellers - deleteById ===");
        sellerDao.deleteById(20);
    }

}