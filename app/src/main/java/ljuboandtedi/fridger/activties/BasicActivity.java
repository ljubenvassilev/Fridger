package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.User;

public class BasicActivity extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user= DatabaseHelper.getInstance(BasicActivity.this).getCurrentUser();
    }
}
