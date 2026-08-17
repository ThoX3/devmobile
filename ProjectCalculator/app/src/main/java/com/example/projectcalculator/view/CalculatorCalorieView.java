package com.example.projectcalculator.view;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.example.projectcalculator.R;

import java.util.ArrayList;
import java.util.List;

public class CalculatorCalorieView implements CalculatorViewInterface {
    private final View _rootView;
    private final HorizontalScrollView _resultScrollView;
    private List<Button> _buttons;
    private final TextView _resultCalorieTextView;

    public CalculatorCalorieView(View rootView) {
        _rootView = rootView;
        _resultCalorieTextView = _rootView.findViewById(R.id.result);
        _resultScrollView = _rootView.findViewById(R.id.horizontal_scroll_view);
        initializeButtons();
    }

    public void initializeButtons() {
        int[] buttonFoodIds = {
                R.id.poulet, R.id.boeuf, R.id.porc,
                R.id.oeuf, R.id.oeufplat,
                R.id.salade, R.id.tomate, R.id.carotte, R.id.brocoli,
                R.id.banane, R.id.pomme, R.id.kiwi, R.id.fraise,
                R.id.riz, R.id.pate, R.id.pain, R.id.mais,
        };

        String[] itemNames = {
                "chicken", "beef", "pork",
                "egg", "fried egg",
                "salad", "tomato", "carrot", "broccoli",
                "banana", "apple", "kiwi", "strawberry",
                "rice", "pasta", "bread", "corn"
        };

        _buttons = new ArrayList<>();
        for (int i = 0; i < buttonFoodIds.length; i++) {
            Button button = _rootView.findViewById(buttonFoodIds[i]);
            button.setTag(itemNames[i]);
            _buttons.add(button);
        }
    }


    @Override
    public void setButtonClickListener(View.OnClickListener listener) {
        for (Button button : _buttons) {
            button.setOnClickListener(listener);
        }
    }

    public void updateTextView(String text) {
        _resultCalorieTextView.setText(text);
        _resultScrollView.post(() -> _resultScrollView.fullScroll(View.FOCUS_RIGHT));
    }
}
