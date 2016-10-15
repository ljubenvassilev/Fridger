package ljuboandtedi.fridger.activties;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class WelcomeActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private DatabaseHelper db = DatabaseHelper.getInstance(WelcomeActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_welcome);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... params) {
                        db.initUsers(loginResult.getAccessToken().getUserId());
                        return null;
                    }
                }.execute();
            }
            @Override
            public void onCancel() {
                Toast.makeText(WelcomeActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException e) {
                Toast.makeText(WelcomeActivity.this, "Login error! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, final Profile currentProfile) {
                if (currentProfile != null) {
                    initiateLogin(currentProfile);
                }
            }
        };
        profileTracker.startTracking();

        if(isLoggedIn()){
            loginButton.setVisibility(View.INVISIBLE);
            db.initUsers(AccessToken.getCurrentAccessToken().getUserId());
            initiateLogin(Profile.getCurrentProfile());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        profileTracker.stopTracking();
        super.onDestroy();
    }

    public boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void initiateLogin(final Profile currentProfile){
        final SharedPreferences.Editor editor = WelcomeActivity.this.
                getSharedPreferences("Fridger", Context.MODE_PRIVATE).edit();
        editor.putString("name", currentProfile.getName());
        editor.putString("pic",currentProfile.getProfilePictureUri(150,150).toString());
        Bundle params = new Bundle();
        params.putString("fields","email");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me",params,
                HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if(response!=null){
                    try {
                        editor.putString("email",response.getJSONObject().
                                getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                editor.putString("current", currentProfile.getId());
                editor.apply();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }).executeAsync();
    }
}
