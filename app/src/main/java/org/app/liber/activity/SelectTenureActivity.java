package org.app.liber.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import org.app.liber.R;
import org.app.liber.SelectPaymentActivity;
import org.app.liber.model.LibraryDataModel;

public class SelectTenureActivity extends AppCompatActivity {

    RadioButton weeks2, month1, months2;
    Button submit;
    LibraryDataModel l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_tenure_card_layout);
        weeks2 = (RadioButton)findViewById(R.id.two_weeks_id);
        month1 = (RadioButton)findViewById(R.id.one_month_id);
        months2 = (RadioButton)findViewById(R.id.two_months_id);
        submit = (Button)findViewById(R.id.submit_tenure_id);
        final String[] selectedTenure = {""};


        Intent i = getIntent();
        l = (LibraryDataModel)i.getSerializableExtra("order_book");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weeks2.isChecked()) {
                    selectedTenure[0] = "1";
                } else if (month1.isChecked()) {
                    selectedTenure[0] = "2";
                } else if (months2.isChecked()) {
                    selectedTenure[0] = "3";
                }
                Intent i2 = new Intent(getApplicationContext(), SelectPaymentActivity.class);
                i2.putExtra("order_book2",l);
                i2.putExtra("tenure_selected",selectedTenure[0]);
                startActivity(i2);
                finish();
            }
        });


    }
}
