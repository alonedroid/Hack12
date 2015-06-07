package mikamiyusen.com.hack12.utility;

import android.content.Context;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.sns.samples.tools.AmazonSNSClientWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

import java.io.IOException;

import mikamiyusen.com.hack12.R;

@EBean
public class SNSMobilePush {

    @RootContext
    Context context;

    @StringRes
    String endpoint;

    private AmazonSNSClientWrapper snsClientWrapper;

    @AfterViews
    void initViews() {
        try {
            AmazonSNS sns = new AmazonSNSClient(
                    new PropertiesCredentials(this.context.getResources().openRawResource(R.raw.credentials)));
            sns.setEndpoint(this.endpoint);
            this.snsClientWrapper = new AmazonSNSClientWrapper(sns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void push(String message) {
        this.snsClientWrapper.demoNotification(message);
    }
}
