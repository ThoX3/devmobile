package com.example.projectcalculator;

import com.example.projectcalculator.viewmodel.CalculatorViewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorViewModelTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private CalculatorViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CalculatorViewModel();
    }

    @Test
    public void testInitialExpressionIsEmpty() {
        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("", currentExpression);
    }

    @Test
    public void testAddToExpression() {
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button3);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("5+3", currentExpression);
    }

    @Test
    public void testCalculateResult() {
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("8", currentExpression);
    }

    @Test
    public void testCalculateResultWithValidOperatorFirst() {
        viewModel.processUserInput(R.id.buttonMinus);
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("-2", currentExpression);
    }

    @Test
    public void testCalculateResultOfNothing() {
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("", currentExpression);
    }

    @Test
    public void testCalculateResultWithInvalidOperatorFirst() {
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("Error", currentExpression);
    }

    @Test
    public void testCalculateResultError() {
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("Error", currentExpression);
    }

    @Test
    public void testDeleteLastCharacter() {
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonDelete);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("5+", currentExpression);
    }

    @Test
    public void testDeleteLastCharacterOfNothing() {
        viewModel.processUserInput(R.id.buttonDelete);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("", currentExpression);

        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonDelete);

        currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("", currentExpression);
    }

    @Test
    public void testClearExpression() {
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonClear);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());

        assertEquals("", currentExpression);
    }

    @Test
    public void testModeFirstLaunchOfCalculator() {
        boolean isCalorie = LiveDataTestUtil.getOrAwaitValue(viewModel.isCalorieMode());
        boolean isClassic = LiveDataTestUtil.getOrAwaitValue(viewModel.isClassicMode());

        assertNotEquals(Boolean.TRUE, isCalorie);
        assertNotEquals(Boolean.TRUE, isClassic);
    }

    @Test
    public void testSwitchToCalorieMode() {
        viewModel.setCalorieMode(true);

        boolean isCalorie = LiveDataTestUtil.getOrAwaitValue(viewModel.isCalorieMode());
        boolean isClassic = LiveDataTestUtil.getOrAwaitValue(viewModel.isClassicMode());

        assertEquals(Boolean.TRUE, isCalorie);
        assertNotEquals(Boolean.TRUE, isClassic);
    }

    @Test
    public void testSwitchToClassicMode() {
        viewModel.setClassicMode(true);

        boolean isCalorie = LiveDataTestUtil.getOrAwaitValue(viewModel.isCalorieMode());
        boolean isClassic = LiveDataTestUtil.getOrAwaitValue(viewModel.isClassicMode());

        assertEquals(Boolean.TRUE, isClassic);
        assertNotEquals(Boolean.TRUE, isCalorie);
    }

    @Test
    public void testScientificNotationAddition() {
        viewModel.processUserInput(R.id.button9);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 12; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button9);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 12; i++) {
            viewModel.processUserInput(R.id.button0);
        }

        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("1.80E13", currentExpression);
    }

    @Test
    public void testScientificNotationSubtraction() {
        viewModel.processUserInput(R.id.button9);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 12; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);
        viewModel.processUserInput(R.id.buttonMinus);
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 12; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("4.00E12", currentExpression);
    }

    @Test
    public void testScientificNotationMultiplication() {
        viewModel.processUserInput(R.id.button9);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 12; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 10; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("4.50E23", currentExpression);
    }

    @Test
    public void testScientificNotationDivision() {
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 18; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);
        viewModel.processUserInput(R.id.buttonDivide);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 12; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("1000000", currentExpression);
    }

    @Test
    public void testNonScientificNotationWithTwoDecimalPlaces() {
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonDot);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.button4);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button2);
        viewModel.processUserInput(R.id.buttonDot);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("7.45", currentExpression);
    }

    @Test
    public void testNonScientificNotationSubtractionWithTwoDecimalPlaces() {
        viewModel.processUserInput(R.id.button8);
        viewModel.processUserInput(R.id.buttonDot);
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.button6);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMinus);
        viewModel.processUserInput(R.id.button3);
        viewModel.processUserInput(R.id.buttonDot);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button2);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("5.44", currentExpression);
    }

    @Test
    public void testOperationBetweenScientificAndNonScientificNotation() {
        viewModel.processUserInput(R.id.button9);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 10; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button5);
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("9.00E10", currentExpression);
    }

    @Test
    public void testAdditionWithLargeNumbers() {
        viewModel.processUserInput(R.id.button9);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonPlus);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("1.00E16", currentExpression);
    }

    @Test
    public void testSubtractionWithLargeNumbers() {
        viewModel.processUserInput(R.id.button2);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonMinus);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("1.00E15", currentExpression);
    }

    @Test
    public void testMultiplicationWithLargeNumbers() {
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        assertEquals("1.00E30", currentExpression);
    }

    @Test
    public void testDivisionWithLargeNumbers() {
        viewModel.processUserInput(R.id.button1);
        viewModel.processUserInput(R.id.buttonMultiply);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 15; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonDivide);
        viewModel.processUserInput(R.id.button1);
        for (int i = 0; i < 14; i++) {
            viewModel.processUserInput(R.id.button0);
        }
        viewModel.processUserInput(R.id.buttonEqual);

        String currentExpression = LiveDataTestUtil.getOrAwaitValue(viewModel.getCurrentExpression());
        System.out.println(currentExpression);
        assertEquals("10", currentExpression);
    }
}
