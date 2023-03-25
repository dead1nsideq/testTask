package org.example;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for EquationSolver.
 */
public class EquationSolverTest
{
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(new EquationSolver("x+5=10").findRoot(5));
        assertTrue(new EquationSolver("2*x=10").findRoot(5));
        assertTrue(new EquationSolver("x*x=25").findRoot(5));
        assertTrue(new EquationSolver("x*x=25").findRoot(-5));
        assertTrue(new EquationSolver("(x+2)/2=10").findRoot(18));
        assertTrue(new EquationSolver("(x+10)(5+5)=100").findRoot(0));
        assertTrue(new EquationSolver("10/x=10").findRoot(1));
        assertTrue(new EquationSolver("10/x=10").isValid());
    }
    @Test
    public void shouldAnswerWithFalse() {
        assertFalse(new EquationSolver("x+5=10").findRoot(6));
        assertFalse(new EquationSolver("x=10").findRoot(9));
        assertFalse(new EquationSolver("x*x=10").findRoot(10));
        assertFalse(new EquationSolver("not a statement").isValid());
        assertFalse(new EquationSolver("x=x").isValid());
        assertFalse(new EquationSolver("10=11").isValid());
        assertFalse(new EquationSolver("x-10").isValid());
    }
}
