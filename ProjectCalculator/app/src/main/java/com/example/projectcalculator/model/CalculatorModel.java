package com.example.projectcalculator.model;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class CalculatorModel {
    public static double evaluateExpression(String expression) {
        try {
            return evaluateWithExp4j(expression);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private static double evaluateWithExp4j(String expression) {
        expression = expression.replaceAll("\\s", "");

        Expression exp = new ExpressionBuilder(expression)
                .operator(new net.objecthunter.exp4j.operator.Operator("^", 2, true, Operator.PRECEDENCE_POWER) {
                    @Override
                    public double apply(double[] args) {
                        return Math.pow(args[0], args[1]);
                    }
                })
                .build();

        return exp.evaluate();
    }
}
