package ljuboandtedi.fridger.activties;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class WelcomeActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    ProfileTracker profileTracker;
    DatabaseHelper db = DatabaseHelper.getInstance(WelcomeActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if(currentProfile==null)return;
                String userID = currentProfile.getId();
                if(db.userExists(userID)){
                    new LoginTask().execute(userID);
                }

            }

        };
        setContentView(R.layout.activity_welcome);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                               "ljuboandtedi.fridger",
                                PackageManager.GET_SIGNATURES);
                        for (Signature signature : info.signatures) {
                            MessageDigest md = MessageDigest.getInstance("SHA");
                                md.update(signature.toByteArray());
                                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {}
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                String userID=loginResult.getAccessToken().getUserId();
                new LoginTask().execute(userID);
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("FB",String.valueOf(resultCode));
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        profileTracker.stopTracking();
        super.onDestroy();
    }

    class LoginTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {
            db.initUsers(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            finish();
        }
    }
}
