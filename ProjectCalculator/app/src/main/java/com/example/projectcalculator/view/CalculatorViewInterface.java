package com.example.projectcalculator.view;

import android.view.View;

public interface CalculatorViewInterface {
    void initializeButtons();
    void setButtonClickListener(View.OnClickListener listener);
    void updateTextView(String text);
}
