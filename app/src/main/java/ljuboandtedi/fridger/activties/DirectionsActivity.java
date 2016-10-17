package ljuboandtedi.fridger.activties;

import android.os.Bundle;
import android.webkit.WebView;

import ljuboandtedi.fridger.R;

public class DirectionsActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.replaceContentLayout(R.layout.activity_directions,
                super.CONTENT_LAYOUT_ID);
        WebView webView = (WebView) findViewById(R.id.wvDirections);
        webView.loadUrl(getIntent().getStringExtra("directions"));
    }

}
