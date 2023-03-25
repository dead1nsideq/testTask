package org.example;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    private static int getIntFromConsole() {
        int x;
        while (true) {
            try {
                x = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please re-enter");
                scanner.next();
            }
        }
        return x;
    }

    private static double getDoubleFromConsole() {
        double x;
        while (true) {
            try {
                x = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please re-enter");
                scanner.next();
            }
        }
        return x;
    }

    private static void printMenu() {
        System.out.println("Select option:\n" +
                "1. Add equations to database\n" +
                "2. Add roots to database\n" +
                "3. Get all equations with a certain root\n" +
                "4. Search by volume\n" +
                "5. Show all equation and their roots\n" +
                "6. Exit");
    }

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.init();

        EquationSolver program;
        int option;

        while (true) {
            printMenu();

            option = getIntFromConsole();

            switch (option) {
                case 1:
                    System.out.println("Please enter correct equation: ");
                    scanner.next();
                    String equation = scanner.nextLine();
                    program = new EquationSolver(equation);

                    while (!program.isValid()) {
                        System.out.println("Invalid equation,please re-enter equation");
                        equation = scanner.nextLine();
                        program = new EquationSolver(equation);
                    }

                    controller.equation(equation);
                    break;
                case 2:
                    ArrayList<String> equations = controller.getAllEquations();

                    if (equations.size() != 0) {
                        System.out.println("Enter root: ");
                        boolean flag = false;
                        double x = getDoubleFromConsole();
                        for (String str : equations) {
                            program = new EquationSolver(str);

                            if (program.findRoot(x)) {
                                flag = true;
                                controller.root(x);
                                controller.equations_roots();
                            }
                        }
                        if (flag) {
                            System.out.println("Root " + x + " has been added");
                        } else {
                            System.out.println("Root" + x + " does not match any of the stored equations");
                        }
                    } else {
                        System.out.println("There are no equations in the database. Please enter some equations first.");
                    }
                    break;
                case 3:
                    System.out.println("Enter root: ");
                    double root = getDoubleFromConsole();
                    controller.findByRoot(root);
                    break;
                case 4:
                    System.out.println("Enter volume: ");
                    int volume = getIntFromConsole();
                    controller.rootCount(volume);
                    break;
                case 5:
                    System.out.println("Show all equation and their roots: ");
                    controller.showAllEquationRoots();
                    break;
                case 6:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please select a valid option.");
                    break;
            }
        }
    }
}

