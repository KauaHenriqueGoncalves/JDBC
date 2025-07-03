package com.example.model.dao.impl;

import com.example.db.DB;
import com.example.db.DbException;
import com.example.model.Department;
import com.example.model.Seller;
import com.example.model.dao.SellerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) "
                     + "VALUES "
                     + "(?,?,?,?,?);";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getEmail());
            pstmt.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            pstmt.setDouble(4, obj.getBaseSalary());
            pstmt.setInt(5, obj.getDepartment().getId());
            int rowAffected = pstmt.executeUpdate();
            if (rowAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer id = rs.getInt(1);
                    obj.setId(id);

                    System.out.println("Row Affected : " + rowAffected);
                    System.out.println("Done! Create id=" + id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
        }
    }

    @Override
    public void update(Seller obj) {
        String sql = "UPDATE seller "
                     + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                     + "WHERE Id = ?;";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getName());
            pstmt.setString(2, obj.getEmail());
            pstmt.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            pstmt.setDouble(4, obj.getBaseSalary());
            pstmt.setInt(5, obj.getDepartment().getId());
            pstmt.setInt(6, obj.getId());
            int rowAffected = pstmt.executeUpdate();
            if (rowAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                System.out.println("Row Affected : " + rowAffected);
                while (rs.next()) {
                    Integer id = rs.getInt(1);
                    obj.setId(id);
                    System.out.println("Done! Update id=" + id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM seller "
                     + "WHERE Id = ?;";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, id);
            int rowAffected = pstmt.executeUpdate();
            if (rowAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                System.out.println("Row Affected : " + rowAffected);
                System.out.println("Done! Delete id=" + id);
            }
            else {
                System.out.println("No rows affected!");
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
        }
    }

    @Override
    public Seller findById(Integer id) {
        String sql = "SELECT seller.*, department.Name As DepName "
                     + "FROM seller INNER JOIN department "
                     + "ON seller.DepartmentId = department.Id "
                     + "WHERE seller.Id = ?;";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Department department = this.instantiateDepartment(rs);
                Seller seller = this.instantiateSeller(rs, department);
                return seller;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        String sql = "SELECT seller.*, department.Name As DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "WHERE seller.DepartmentId = ? "
                + "ORDER BY seller.Name ASC;";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, department.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Seller seller = this.instantiateSeller(rs, department);
                sellers.add(seller);
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
            DB.closeResultSet(rs);
        }

        return sellers;
    }

    @Override
    public List<Seller> findAll() {
        String sql = "SELECT seller.*, department.Name As DepName "
                + "FROM seller INNER JOIN department "
                + "ON seller.DepartmentId = department.Id "
                + "ORDER BY seller.Name ASC;";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Department department = this.instantiateDepartment(rs);
                Seller seller = this.instantiateSeller(rs, department);
                sellers.add(seller);
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
            DB.closeResultSet(rs);
        }

        return sellers;
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

}
