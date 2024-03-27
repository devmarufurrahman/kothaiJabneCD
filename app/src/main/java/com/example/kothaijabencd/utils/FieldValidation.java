package com.example.kothaijabencd.utils;

import android.widget.Button;
import android.widget.EditText;

public class FieldValidation {

    public FieldValidation() {
    }

    // field validation
    public void buttonFocus(Button btn, String error) {
        btn.setError(error);
        btn.setFocusable(true);
        btn.setFocusableInTouchMode(true);
        btn.requestFocus();
    }
    public void fieldFocus(EditText field, String error) {
        field.setError(error);
        field.requestFocus();
    }

}
