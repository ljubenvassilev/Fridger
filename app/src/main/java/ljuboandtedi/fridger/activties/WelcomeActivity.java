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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ljuboandtedi.fridger.R;
import ljuboandtedi.fridger.model.DatabaseHelper;

public class WelcomeActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
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
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {}
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.logo);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        final LoginResult result = loginResult;
                        DatabaseHelper db = DatabaseHelper.getInstance(WelcomeActivity.this);
                        db.initUsers();
                        db.setCurrentUser(result.getAccessToken().getUserId());
                        return null;
                    }
                }.execute();
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
