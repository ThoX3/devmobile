package com.example.projectcalculator;

import com.example.projectcalculator.model.CalculatorModel;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorModelTest {
    @Test
    public void testSimpleAdditionInteger() {
        String expression = "2 + 3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testSimpleSubtractionInteger() {
        String expression = "10 - 4";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(6.0, result, 0.001);
    }

    @Test
    public void testMultiplicationInteger() {
        String expression = "5 * 3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(15.0, result, 0.001);
    }

    @Test
    public void testDivisionInteger() {
        String expression = "20 / 4";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(5.0, result, 0.001);
    }

    @Test
    public void testDivisionByZero() {
        String expression = "10 / 0";
        double result = CalculatorModel.evaluateExpression(expression);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testParentheses() {
        String expression = "(2 + 3) * 4";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(20.0, result, 0.001);
    }

    @Test
    public void testParentheses2() {
        String expression = "(2 + 3) 4";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(20.0, result, 0.001);
    }

    @Test
    public void testSimpleAdditionDecimal() {
        String expression = "5.5 + 4.3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(9.8, result, 0.001);
    }

    @Test
    public void testSimpleSubtractionDecimal() {
        String expression = "55.5 - 2.3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(53.2, result, 0.001);
    }

    @Test
    public void testMultiplicationDecimal() {
        String expression = "15.7 * 8.3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(130.31, result, 0.001);
    }

    @Test
    public void testDivisionDecimal() {
        String expression = "13.9 / 6.3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertEquals(2.206, result, 0.001);
    }

    @Test
    public void testInvalidExpression() {
        String expression = "2 + * 3";
        double result = CalculatorModel.evaluateExpression(expression);
        assertTrue(Double.isNaN(result));
    }
}
