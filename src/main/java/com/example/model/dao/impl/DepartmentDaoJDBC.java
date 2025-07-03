package com.example.model.dao.impl;

import com.example.db.DB;
import com.example.db.DbException;
import com.example.model.Department;
import com.example.model.dao.DepartmentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        String sql = "INSERT INTO department (Name) "
                     + "VALUES (?)";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, obj.getName());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                    System.out.println("Rows affected: " + rowsAffected);
                    System.out.println("Done! Create Id=" + obj.getId());
                }
                DB.closeResultSet(rs);
            }
            else {
                System.out.println("No rows affected");
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
    public void update(Department obj) {
        String sql = "UPDATE department "
                + "SET Name=? "
                + "WHERE id=?;";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, obj.getName());
            pstmt.setInt(2, obj.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rows affected: " + rowsAffected);
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
        String sql = "DELETE FROM department "
                + "WHERE id=?;";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Rows affected: " + rowsAffected);
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
    public Department findById(Integer id) {
        String sql = "SELECT * FROM department "
                     + "WHERE id=?;";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("Name"));
                return obj;
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
            DB.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public List<Department> findAll() {
        String sql = "SELECT * FROM department;";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Department> departments = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                departments.add(obj);
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(pstmt);
            DB.closeResultSet(rs);
        }

        return departments;
    }

}
