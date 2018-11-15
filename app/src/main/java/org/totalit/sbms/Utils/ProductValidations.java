package org.totalit.sbms.Utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.totalit.sbms.R;

public class ProductValidations {




    public static boolean validate(String field, TextInputLayout textInputLayout, Window window) {
        if (field.isEmpty()) {
            textInputLayout.setError("Field Required.");
            requestFocus(textInputLayout, window);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }


    public static boolean setSpinnerError(SearchableSpinner spinner, Context context){

        int selectedItemOfMySpinner = spinner.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) spinner.getItemAtPosition(selectedItemOfMySpinner);

        if (actualPositionOfMySpinner==null) {

            View selectedView = spinner.getSelectedView();
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                spinner.requestFocus();
                selectedTextView.setError("error");
                selectedTextView.setTextColor(context.getResources().getColor(R.color.colorDanger));
                selectedTextView.setText("Field Required");
                spinner.performClick();
                return false;
            }
        }
        return true;
    }

    public static void requestFocus(View view, Window window) {
        if (view.requestFocus()) {

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
