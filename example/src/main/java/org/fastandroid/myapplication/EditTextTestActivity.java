package org.fastandroid.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class EditTextTestActivity extends Activity {

    private int MAX_LENGTH = 200;

    @ViewById
    EditText et_leave_input;

    @ViewById
    TextView tv_leave_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_test);
    }

    @AfterViews
    public void afterViews() {

        et_leave_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = et_leave_input.getText();
                int len = editable.length();

                if (len > MAX_LENGTH) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串  
                    String newStr = str.substring(0, MAX_LENGTH);
                    et_leave_input.setText(newStr);
                    editable = et_leave_input.getText();

                    //新字符串的长度  
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度  
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置  
                    Selection.setSelection(editable, selEndIndex);
                    Animation shake = AnimationUtils.loadAnimation(EditTextTestActivity.this, R.anim
                            .shake);
                    tv_leave_count.startAnimation(shake);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = et_leave_input.getText().toString();
                tv_leave_count.setText(content.length() + "/" + MAX_LENGTH);
            }
        });

    }
}
