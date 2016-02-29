package roberterrera.com.email_client_app.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import roberterrera.com.email_client_app.R;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getIntent();

    }
}
