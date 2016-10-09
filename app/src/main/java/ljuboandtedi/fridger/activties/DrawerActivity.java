package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;
import ljuboandtedi.fridger.model.User;

public class DrawerActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    Drawer result;
    public final int CONTENT_LAYOUT_ID = R.id.frame_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        DatabaseHelper db = DatabaseHelper.getInstance(DrawerActivity.this);
        user = db.getCurrentUser();
        String userID = user.getFacebookID();
        Log.e("USER",userID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Ljuben Vassilev")
                                .withEmail(user.getFacebookID())
                                .withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerWidthDp(240)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)
                .withSliderBackgroundColorRes(R.color.md_black_1000)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.
                                drawer_item_home).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        return false;

                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.
                                drawer_item_search).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),SearchMealsActivity.class));
                                        return false;
                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000),
                        new PrimaryDrawerItem().withIdentifier(3).withName(R.string.
                                drawer_item_fridge).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),YourFridgeActivity.class));
                                        return false;

                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000),
                        new PrimaryDrawerItem().withIdentifier(4).withName(R.string.
                                drawer_item_list).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                                        return false;
                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000),

                        new PrimaryDrawerItem().withIdentifier(5).withName(R.string.
                                drawer_item_favorite).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),FavouriteMealsActivity.class));
                                        return false;
                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000),
                        new PrimaryDrawerItem().withIdentifier(6).withName(R.string.
                                drawer_item_profile).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                        return false;
                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000),
                        new PrimaryDrawerItem().withIdentifier(7).withName(R.string.
                                drawer_item_logout).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
                                        return false;
                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000)
                        )
                .build();
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
