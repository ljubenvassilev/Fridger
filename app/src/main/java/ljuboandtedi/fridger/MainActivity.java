package ljuboandtedi.fridger;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ljuboandtedi.fridger.model.User;

public class MainActivity extends BasicActivity {

    User user = User.getInstance();
    DatabaseHelper db;
    TextView likeMeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userID=getIntent().getStringExtra("userID");
        initUser(userID);
        likeMeat = (TextView) findViewById(R.id.likeMeat);
        likeMeat.setText("Likes meat: ".concat(user.isLikeMeaty()?"Yes":"No"));
    }

    void initUser (String userID){
        db = new DatabaseHelper(MainActivity.this);
        if(db.userExists(userID)) {
            Cursor rs = db.getUser(userID);
            rs.moveToFirst();
            user.setFacebookID(rs.getString(0));
            user.setLikeSalty(rs.getString(1).equalsIgnoreCase("YES"));
            user.setLikeMeaty(rs.getString(2).equalsIgnoreCase("YES"));
            user.setLikePiquant(rs.getString(3).equalsIgnoreCase("YES"));
            user.setLikeBitter(rs.getString(4).equalsIgnoreCase("YES"));
            user.setLikeSour(rs.getString(5).equalsIgnoreCase("YES"));
            user.setLikeSweet(rs.getString(6).equalsIgnoreCase("YES"));
            if (!rs.isClosed()) {
                rs.close();
            }
        }
    }
}
