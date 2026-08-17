package com.example.projectcalculator.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

import com.example.projectcalculator.R;
import com.example.projectcalculator.viewmodel.CalculatorViewModel;

public class MainActivity extends AppCompatActivity {
    private LinearLayout classicLayout;
    private LinearLayout calorieLayout;
    private LinearLayout languageSelectionLayout;
    private CalculatorViewInterface view;
    private CalculatorViewModel viewModel;
    private Button classicButton;
    private Button calorieButton;
    private View indicatorBar;
    private String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewAndViewModel(false);
        viewModel.resetDatabase(this);
        viewModel.fetchCaloriesFromAPI(this);

        enableFullScreenMode();

        if (savedInstanceState != null) {
            boolean isCalorieMode = savedInstanceState.getBoolean("isCalorieMode", false);
            boolean isClassicMode = savedInstanceState.getBoolean("isClassicMode", false);
            float savedPosition = savedInstanceState.getFloat("indicatorBarPosition", 0);
            String currentLanguage = savedInstanceState.getString("currentLanguage", "");

            if (isCalorieMode) {
                activateCalorieMode();
            }

            if (isClassicMode) {
                activateClassicMode();
            }

            indicatorBar.setTranslationX(savedPosition);

            if (view != null) {
                view.updateTextView(savedInstanceState.getString("currentExpression"));
            }

            if (!currentLanguage.isEmpty()) {
                setLocale(currentLanguage);
                currentLang = currentLanguage;
                initializeViewAndViewModel(true);
                switchToCalculator();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentLanguage", currentLang);
        outState.putFloat("indicatorBarPosition", indicatorBar.getTranslationX());
        outState.putBoolean("isCalorieMode", Boolean.TRUE.equals(viewModel.isCalorieMode().getValue()));
        outState.putBoolean("isClassicMode", Boolean.TRUE.equals(viewModel.isClassicMode().getValue()));
    }

    private void initializeViewAndViewModel(boolean isLangSelected) {
        setContentView(R.layout.activity_main);

        languageSelectionLayout = findViewById(R.id.language_selection_layout);
        classicLayout = findViewById(R.id.classic_layout);
        calorieLayout = findViewById(R.id.calorie_layout);
        classicButton = findViewById(R.id.classic_button);
        calorieButton = findViewById(R.id.calorie_button);
        indicatorBar = findViewById(R.id.indicator_bar);

        if (!isLangSelected) {
            setButtonsEnabled(false);

            Button englishButton = findViewById(R.id.english);
            Button frenchButton = findViewById(R.id.french);
            Button vietnameseButton = findViewById(R.id.vietnamese);

            englishButton.setOnClickListener(v -> {
                setLocale("en");
                currentLang = "en";
                initializeViewAndViewModel(true);
                switchToCalculator();
            });

            frenchButton.setOnClickListener(v -> {
                setLocale("fr");
                currentLang = "fr";
                initializeViewAndViewModel(true);
                switchToCalculator();
            });

            vietnameseButton.setOnClickListener(v -> {
                setLocale("vn");
                currentLang = "vn";
                initializeViewAndViewModel(true);
                switchToCalculator();
            });
        }

        viewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        viewModel.getCurrentExpression().observe(this, expression -> {
            if (view != null) {
                view.updateTextView(expression);
            }
        });

        viewModel.isCalorieMode().observe(this, isCalorie -> {
            if (Boolean.TRUE.equals(isCalorie)) {
                activateCalorieMode();
            }
        });

        viewModel.isClassicMode().observe(this, isClassic -> {
            if (Boolean.TRUE.equals(isClassic)) {
                activateClassicMode();
            }
        });

        calorieButton.setOnClickListener(v -> {
            viewModel.setCalorieMode(true);
            viewModel.setClassicMode(false);
        });

        classicButton.setOnClickListener(v -> {
            viewModel.setCalorieMode(false);
            viewModel.setClassicMode(true);
        });
    }


    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.setLocale(locale);
        resources.updateConfiguration(config, dm);
    }

    private void activateCalorieMode() {
        classicLayout.setVisibility(View.GONE);
        calorieLayout.setVisibility(View.VISIBLE);

        CalculatorCalorieView calorieView = new CalculatorCalorieView(findViewById(R.id.main_layout));

        calorieView.setButtonClickListener(v -> {
            Button clickedButton = (Button) v;
            String itemName = clickedButton.getTag().toString();
            viewModel.fetchCaloriesForItem(itemName, this);
        });

        viewModel.getCalorieDataForItem().observe(this, calorieData -> {
            if (calorieData != null) {
                calorieView.updateTextView(calorieData);
            }
        });

        view = calorieView;
        animateIndicatorBar(calorieButton);
    }

    private void activateClassicMode() {
        calorieLayout.setVisibility(View.GONE);
        classicLayout.setVisibility(View.VISIBLE);
        view = new CalculatorClassicView(findViewById(R.id.main_layout));
        view.setButtonClickListener(this::onButtonClick);
        animateIndicatorBar(classicButton);
    }

    private void onButtonClick(View v) {
        int buttonId = v.getId();
        viewModel.processUserInput(buttonId);
    }


    private void animateIndicatorBar(Button targetButton) {
        AnimationUtils.animateIndicator(indicatorBar, targetButton, getResources());
    }

    private void enableFullScreenMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private void setButtonsEnabled(boolean enabled) {
        classicButton.setEnabled(enabled);
        calorieButton.setEnabled(enabled);
    }

    private void switchToCalculator() {
        languageSelectionLayout.setVisibility(View.GONE);
        setButtonsEnabled(true);
    }
}
