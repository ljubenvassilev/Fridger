package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.User;

public class DrawerActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    public final int CONTENT_LAYOUT_ID = R.id.frame_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        user = DatabaseHelper.getInstance(this).getCurrentUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerWidthDp(300)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.
                                drawer_item_home).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();
                                        return false;

                                    }
                                }),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.
                                drawer_item_profile).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                        finish();
                                        return false;
                                    }
                                })
                )
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = DatabaseHelper.getInstance(this).getCurrentUser();
    }

    protected void replaceContentLayout(int sourceId, int destinationId) {
        View contentLayout = findViewById(destinationId);

        ViewGroup parent = (ViewGroup) contentLayout.getParent();
        int index = parent.indexOfChild(contentLayout);

        parent.removeView(contentLayout);
        contentLayout = getLayoutInflater().inflate(sourceId, parent, false);
        parent.addView(contentLayout, index);
    }
}
