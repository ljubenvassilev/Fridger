package ljuboandtedi.fridger.activties;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_drawer);
        DatabaseHelper db = DatabaseHelper.getInstance(DrawerActivity.this);
        user = db.getCurrentUser();
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name, email, pic;
        SharedPreferences prefs = getSharedPreferences("Fridger", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = DrawerActivity.this.
                getSharedPreferences("Fridger", Context.MODE_PRIVATE).edit();
        name = prefs.getString("name", "");
        email = prefs.getString("email", "");
        pic = prefs.getString("pic", "");
        int selectedItem = 0;
        switch (prefs.getString("page", "")){
            case "":
                selectedItem=1;
                break;
            case "Home":
                selectedItem=1;
                break;
            case "Search":
                selectedItem=2;
                break;
            case "MyFridge":
                selectedItem=3;
                break;
            case "Shopping List":
                selectedItem=4;
                break;
            case "Favorite":
                selectedItem=5;
                break;
            case "Profile":
                selectedItem=6;
        }
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(name)
                                .withEmail(email)
                                .withIcon(Uri.parse(pic))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerWidthDp(200)
                .withTranslucentStatusBar(true)
                .withDisplayBelowStatusBar(true)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)
                .withSliderBackgroundColorRes(R.color.md_black_1000)
                .withFooterDivider(true)
                .withStickyFooterDivider(true)
                .withSelectedItemByPosition(selectedItem)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.
                                drawer_item_home).withOnDrawerItemClickListener
                                (new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position,
                                                               IDrawerItem drawerItem) {
                                        editor.putString("page", "Home");
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
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
                                        editor.putString("page", "Search");
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), SearchMealsActivity.class));
                                        finish();
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
                                        editor.putString("page", "MyFridge");
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), YourFridgeActivity.class));
                                        finish();
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
                                        editor.putString("page", "Shopping List");
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                                        finish();
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
                                        editor.putString("page", "Favorite");
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), FavouriteMealsActivity.class));
                                        finish();
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
                                        editor.putString("page", "Profile");
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                        finish();
                                        return false;
                                    }
                                })
                                .withTextColorRes(R.color.md_white_1000)

                )
                .build();
        result.setToolbar(this,toolbar);
        result.addItem(new DividerDrawerItem());
        result.addStickyFooterItem(new PrimaryDrawerItem().withIdentifier(7).withName(R.string.
                drawer_item_logout).withOnDrawerItemClickListener
                (new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position,
                                               IDrawerItem drawerItem) {
                        LoginManager.getInstance().logOut();
                        editor.putString("page", " ");
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        finish();
                        return false;
                    }
                })
                .withTextColorRes(R.color.md_black_1000));
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



