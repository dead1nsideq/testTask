package org.example;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Class that allows you to check the correctness of the equation and find the root for it by number
 */

public class EquationSolver {
    /**
     * field equation which stores equation
     * field isValid is a flag that indicates the correctness of the equation in the field equation
     */
    private String equation;
    private boolean isValid;

    /**
     * Create new EquationSolver with provided equation
     * Object of EquationSolver wouldn't create if equation is not correct
     * @param equation Mathematics equation
     */
    public EquationSolver(String equation) {
        try {
            if (isValidEquation(equation)) {
                this.equation = equation;
                isValid = true;
            } else {
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
        }
    }

    /**
     * Method which checks if x is root of equation
     * @param x potential root
     * @return true if x is root of equation and false if it isn't
     */
    public boolean findRoot(double x) {
        double left,right;
        String[] parse = equation.split("=");
        Expression expressionRight = new ExpressionBuilder(parse[0]).variable("x").build().setVariable("x",x);
        right = expressionRight.evaluate();
        Expression expressionLeft = new ExpressionBuilder(parse[1]).variable("x").build().setVariable("x",x);
        left = expressionLeft.evaluate();
        return left == right;
    }

    /**
     * Method which checks if equation is correct
     * @param equation Mathematics equation
     * @return true if equation valid and vise versa
     */
    private boolean isValidEquation(String equation) {
        if (!equation.contains("x")) {
            return false;
        }
        String[] parse = equation.split("=");
        if (parse[0].equals(parse[1])) {
            System.out.println("Equation has infinity roots");
            return false;
        }
        return (new ExpressionBuilder(parse[0])
                .variable("x")
                .build()
                .setVariable("x",0)
                .validate()
                .isValid() &&
                new ExpressionBuilder(parse[1])
                        .variable("x")
                        .build()
                        .setVariable("x",0)
                        .validate()
                        .isValid());
    }

    /**
     * getter to field isValid
     * @return field isValid
     */
    public boolean isValid() {
        return isValid;
    }
}
