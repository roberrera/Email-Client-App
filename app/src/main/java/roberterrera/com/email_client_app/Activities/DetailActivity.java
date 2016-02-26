package roberterrera.com.email_client_app.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import roberterrera.com.email_client_app.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String intent = getIntent().getStringExtra("selected planet");
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText(intent);

    }
}
