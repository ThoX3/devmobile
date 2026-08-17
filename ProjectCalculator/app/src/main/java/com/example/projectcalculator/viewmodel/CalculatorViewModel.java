package com.example.projectcalculator.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projectcalculator.R;
import com.example.projectcalculator.data.APICallNutrition;
import com.example.projectcalculator.data.CalorieData;
import com.example.projectcalculator.data.CalorieDatabase;
import com.example.projectcalculator.model.CalculatorModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorViewModel extends ViewModel {
    private final MutableLiveData<String> currentExpression = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isCalorieMode = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isClassicMode = new MutableLiveData<>(false);
    private final MutableLiveData<String> calorieDataForItem = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<Boolean> isCalorieMode() {
        return isCalorieMode;
    }

    public LiveData<Boolean> isClassicMode() {
        return isClassicMode;
    }

    public void setCalorieMode(boolean isCalorie) {
        isCalorieMode.setValue(isCalorie);
    }

    public void setClassicMode(boolean isClassic) {
        isClassicMode.setValue(isClassic);
    }

    public LiveData<String> getCurrentExpression() {
        return currentExpression;
    }

    public LiveData<String> getCalorieDataForItem() {
        return calorieDataForItem;
    }

    public void processUserInput(int buttonId) {
        if (buttonId == R.id.buttonClear) {
            clearExpression();
        } else if (buttonId == R.id.buttonEqual) {
            if (currentExpression.getValue() == null || currentExpression.getValue().trim().isEmpty()) {
                return;
            }
            String result = calculateResult();
            clearExpression();
            addToExpression(result);
        } else if (buttonId == R.id.buttonDelete) {
            deleteLastCharacter();
        } else {
            String buttonText = getButtonTextById(buttonId);
            if (buttonText != null) {
                addToExpression(buttonText);
            }
        }
    }

    private String getButtonTextById(int buttonId) {
        if (buttonId == R.id.button0) return "0";
        if (buttonId == R.id.button1) return "1";
        if (buttonId == R.id.button2) return "2";
        if (buttonId == R.id.button3) return "3";
        if (buttonId == R.id.button4) return "4";
        if (buttonId == R.id.button5) return "5";
        if (buttonId == R.id.button6) return "6";
        if (buttonId == R.id.button7) return "7";
        if (buttonId == R.id.button8) return "8";
        if (buttonId == R.id.button9) return "9";
        if (buttonId == R.id.buttonPlus) return "+";
        if (buttonId == R.id.buttonMinus) return "-";
        if (buttonId == R.id.buttonMultiply) return "*";
        if (buttonId == R.id.buttonDivide) return "/";
        if (buttonId == R.id.buttonDot) return ".";
        return null;
    }

    private void addToExpression(String text) {
        StringBuilder expression = new StringBuilder(Objects.requireNonNull(currentExpression.getValue()));
        if (isOperator(text)) {
            if (expression.length() > 0 && isOperator(String.valueOf(expression.charAt(expression.length() - 1)))) {
                expression.deleteCharAt(expression.length() - 1);
            }
        }
        expression.append(text);
        currentExpression.setValue(expression.toString());
    }

    private boolean isOperator(String text) {
        return text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/");
    }

    private void clearExpression() {
        currentExpression.setValue("");
    }

    private void deleteLastCharacter() {
        String expression = currentExpression.getValue();
        if (expression != null && !expression.isEmpty()) {
            currentExpression.setValue(expression.substring(0, expression.length() - 1));
        }
    }

    @SuppressLint("DefaultLocale")
    private String calculateResult() {
        String expression = sanitizeExpression(Objects.requireNonNull(currentExpression.getValue()));
        try {
            double result = CalculatorModel.evaluateExpression(expression);
            if (Double.isNaN(result)) {
                return "Error";
            }
            if (expression.isEmpty())
                return "";
            return formatResult(result);
        } catch (Exception e) {
            return "Error";
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }

    @SuppressLint("DefaultLocale")
    private String formatResult(double result) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setExponentSeparator("E");

        DecimalFormat scientificFormat = new DecimalFormat("0.00E0", symbols);

        if (Math.abs(result) >= 1e10 || Math.abs(result) <= 1e-3 && result != 0.0) {
            if (result == (long) result) {
                return scientificFormat.format((long) result);
            }
            return scientificFormat.format(result);
        } else {
            if (result == (long) result) {
                return String.valueOf((long) result);
            }
            return String.format("%.2f", result).replace(',', '.');
        }
    }

    private String sanitizeExpression(String expression) {
        return expression.replaceAll("\\*10\\^", "E");
    }

    public void fetchCaloriesFromAPI(Context context) {
        APICallNutrition apiCall = new APICallNutrition();
        apiCall.fetchAndSaveCalories(context);
    }

    public void resetDatabase(Context context) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                CalorieDatabase db = CalorieDatabase.getDatabase(context);
                SupportSQLiteDatabase sqLiteDatabase = db.getOpenHelper().getWritableDatabase();

                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS calories_table");
                sqLiteDatabase.execSQL("CREATE TABLE calories_table (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, calories REAL)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void fetchCaloriesForItem(String itemName, Context context) {
        executor.execute(() -> {
            try {
                CalorieDatabase dbInstance = CalorieDatabase.getDatabase(context);
                CalorieData calorieData = dbInstance.caloriesDao().getCaloriesForItem(itemName);

                if (calorieData != null) {
                    String result = String.valueOf(calorieData.getCalories());
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> addToExpression(result));
                } else {
                    calorieDataForItem.postValue("No data available for " + itemName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                calorieDataForItem.postValue("Error fetching data: " + e.getMessage());
            }
        });
    }

}