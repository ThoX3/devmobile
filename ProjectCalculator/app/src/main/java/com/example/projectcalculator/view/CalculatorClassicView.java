package com.example.projectcalculator.view;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import com.example.projectcalculator.R;

import java.util.ArrayList;
import java.util.List;

public class CalculatorClassicView implements CalculatorViewInterface {
    private final View _rootView;
    private final HorizontalScrollView _resultScrollView;
    private final TextView _resultClassicTextView;
    private List<Button> _buttons;

    public CalculatorClassicView(View rootView) {
        _rootView = rootView;
        _resultClassicTextView = _rootView.findViewById(R.id.result);
        _resultScrollView = _rootView.findViewById(R.id.horizontal_scroll_view);
        initializeButtons();
    }

    public void initializeButtons() {
        int[] buttonIds = {
                R.id.buttonClear, R.id.buttonOpenParenthesis, R.id.buttonCloseParenthesis, R.id.buttonDivide,
                R.id.button7, R.id.button8, R.id.button9, R.id.buttonMultiply,
                R.id.button4, R.id.button5, R.id.button6, R.id.buttonMinus,
                R.id.button1, R.id.button2, R.id.button3, R.id.buttonPlus,
                R.id.button0, R.id.buttonDot, R.id.buttonDelete, R.id.buttonEqual
        };

        List<Button> buttonList = new ArrayList<>();
        for (int id : buttonIds) {
            Button button = _rootView.findViewById(id);
            buttonList.add(button);
        }
        _buttons = buttonList;
    }

    public void setButtonClickListener(View.OnClickListener listener) {
        for (Button button : _buttons) {
            button.setOnClickListener(listener);
        }
    }

    public void updateTextView(String text) {
        _resultClassicTextView.setText(text);
        _resultScrollView.post(() -> _resultScrollView.fullScroll(View.FOCUS_RIGHT));
    }
}
