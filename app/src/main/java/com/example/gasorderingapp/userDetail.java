package com.example.gasorderingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class userDetail extends AppCompatActivity {

    EditText half_cylinder_1200,full_cylinder_3400,delivery_15;
    TextView total_1200,total_3400,total_15,grand_total;
    Button cancel,confirm;
    int cylinders=0,cylinders_half=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initialize();

        half_cylinder_1200.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!half_cylinder_1200.getText().toString().isEmpty()){
                    if(Integer.parseInt(half_cylinder_1200.getText().toString())>cylinders_half)
                        half_cylinder_1200.setText(String.valueOf(cylinders_half));
                    if(Integer.parseInt(half_cylinder_1200.getText().toString())<0)
                        half_cylinder_1200.setText("0");
                    total_1200.setText(String.valueOf(Integer.parseInt(half_cylinder_1200.getText().toString()) * 1200));
                }else
                    total_1200.setText("");
                totalCalculate();
            }
        });
        full_cylinder_3400.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!full_cylinder_3400.getText().toString().isEmpty()){
                    if(Integer.parseInt(full_cylinder_3400.getText().toString())>cylinders)
                        full_cylinder_3400.setText(String.valueOf(cylinders));
                    if(Integer.parseInt(full_cylinder_3400.getText().toString())<0)
                        full_cylinder_3400.setText("0");
                    total_3400.setText(String.valueOf(Integer.parseInt(full_cylinder_3400.getText().toString()) * 3400));
                }else
                    total_3400.setText("");
                totalCalculate();

            }
        });
        delivery_15.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!delivery_15.getText().toString().isEmpty()){
                    total_15.setText(String.valueOf(Integer.parseInt(delivery_15.getText().toString()) * 15));
                }else
                    total_15.setText("");
                totalCalculate();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(userDetail.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(userDetail.this,MainActivity.class);
                startActivity(intent);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(userDetail.this, "Order Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(userDetail.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void totalCalculate() {
        int total = 0;
        if(!total_1200.getText().toString().isEmpty())
            total += Integer.parseInt(total_1200.getText().toString());
        if(!total_3400.getText().toString().isEmpty())
            total += Integer.parseInt(total_3400.getText().toString());
        if(!total_15.getText().toString().isEmpty())
            total += Integer.parseInt(total_15.getText().toString());

        grand_total.setText(String.valueOf(total));
        if(total == 0)
            grand_total.setText("");
    }

    private void initialize() {
        half_cylinder_1200 = findViewById(R.id.half_cylinder_1200);
        full_cylinder_3400 = findViewById(R.id.full_cylinder_3400);
        delivery_15 = findViewById(R.id.delivery_15);
        total_1200 = findViewById(R.id.total_1200);
        total_3400 = findViewById(R.id.total_3400);
        total_15 = findViewById(R.id.total_15);
        grand_total = findViewById(R.id.grand_total);
        cancel = findViewById(R.id.cancel);
        confirm = findViewById(R.id.confirm);
        cylinders = getIntent().getExtras().getInt("cylinders");
        cylinders_half = getIntent().getExtras().getInt("half_cylinders");

        half_cylinder_1200.setHint("Max ("+cylinders_half+")");
        full_cylinder_3400.setHint("Max (" + cylinders + ")");
    }
}
