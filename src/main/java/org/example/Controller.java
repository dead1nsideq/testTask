package org.example;

import java.sql.*;
import java.util.ArrayList;

/**
 * class which connects to database and provide some functional:
 * store correct root,equation and their connection
 * some methods to access data from database
 */
public class Controller {
    /**
     * The Connection object used to connect to the database.
     */
    private Connection conn;

    /**
     * Initializes a connection to the MySQL database.
     */
    public void init()  {
        try {
            String url = "jdbc:mysql://localhost:3306/equations";
            String user = "root";
            String password = "password";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inserts a root value into the 'roots' table of the database.
     * If a root value with the same value already exists, it is not inserted.
     * @param root root to insert into the 'roots' table
     */
    public void root(double root)  {
        try {
            String sql = "INSERT IGNORE INTO roots (root) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, root);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Populates the 'equation_roots' table of the database with the relationship between equations and roots.
     * If an equation has already been associated with a root, it is not added again.
     */
    public void equations_roots() {
        try {
            String sql = "SELECT * FROM equations";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String equation = rs.getString("equation");
                    String sql1 = "SELECT * FROM roots";
                    try (Statement stmt1 = conn.createStatement();
                         ResultSet rs1 = stmt1.executeQuery(sql1)) {
                        while (rs1.next()) {
                            double x = rs1.getDouble("root");
                            if (new EquationSolver(equation).findRoot(x)) {
                                String sql2 = "INSERT IGNORE INTO equation_roots (id_equation,id_root) VALUES (?,?)";
                                try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                                    stmt2.setString(1, rs.getString("id"));
                                    stmt2.setString(2, rs1.getString("id"));
                                    stmt2.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inserts an equation into the 'equations' table of the database.
     * If an equation with the same value already exists, it is not inserted.
     * @param equation the equation to insert into the 'equations' table
     */
    public void equation(String equation) {
        try {
            String sql = "INSERT IGNORE INTO equations (equation) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, equation);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints out all the equations and their associated roots in the 'equation_roots' table of the database.
     */
    public void showAllEquationRoots() {
        try {
            String sql = "SELECT equations.equation, roots.root " +
                    "FROM equations " +
                    "JOIN equation_roots ON equations.id = equation_roots.id_equation " +
                    "JOIN roots ON equation_roots.id_root = roots.id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String equation = rs.getString("equation");
                double root = rs.getDouble("root");
                System.out.println("Equation: " + equation + ", Root: " + root);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Get all equation from equation table
     * @return arraylist with all equations
     */
    public ArrayList<String> getAllEquations() {
        ArrayList<String> arrayList = new ArrayList<>();
        String query = "SELECT * FROM equations";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String equation = rs.getString("equation");
                arrayList.add(equation);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arrayList;
    }

    /**
     * find all equations which solves by provided root
     * @param x provided root
     */
    public void findByRoot(double x) {
        try {
            boolean flag = false;
            Statement stmt = conn.createStatement();
            String sql = "SELECT equations.equation FROM equations " +
                    "JOIN equation_roots ON equations.id = equation_roots.id_equation " +
                    "JOIN roots ON equation_roots.id_root = roots.id " +
                    "WHERE roots.root = " + x;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String equation = rs.getString("equation");
                System.out.println(equation);
                flag = true;
            }
            if (!flag) {
                System.out.println("No equations found with root = " + x);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * find all equations which have a certain number of roots
     * @param x how many roots in equation
     */
    public void rootCount(int x) {
        int count = 0;
        try {
            boolean flag = false;
            Statement stmt = conn.createStatement();
            String sql = "SELECT equations.equation " +
                    "FROM equations " +
                    "JOIN equation_roots ON equations.id = equation_roots.id_equation " +
                    "GROUP BY equations.id " +
                    "HAVING COUNT(*) = " + x;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String equation = rs.getString("equation");
                System.out.println(equation);
                count++;
                flag = true;
            }
            if (!flag) {
                System.out.println("No equations with this among of roots");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Equations with " + x + " root - " + count);
    }
}
