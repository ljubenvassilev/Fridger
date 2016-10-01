package ljuboandtedi.fridger.activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikepenz.materialdrawer.DrawerBuilder;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.User;

public class DrawerActivity extends AppCompatActivity {


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        new DrawerBuilder().withActivity(this).build();
        user = DatabaseHelper.getInstance(this).getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = DatabaseHelper.getInstance(this).getCurrentUser();
    }
}
