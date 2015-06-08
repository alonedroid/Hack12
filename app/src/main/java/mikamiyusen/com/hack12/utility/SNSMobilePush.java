package mikamiyusen.com.hack12.utility;

import android.content.Context;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

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
    String server;

    @StringRes
    String endpoint;

    private AmazonSNS sns;

    @AfterViews
    void initViews() {
        try {
            this.sns = new AmazonSNSClient(
                    new PropertiesCredentials(this.context.getResources().openRawResource(R.raw.credentials)));
            this.sns.setEndpoint(this.server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessageStructure("json");
        publishRequest.setTargetArn(this.endpoint);
        publishRequest.setMessage("{\"APNS_SANDBOX\":\"{\\\"aps\\\":{\\\"sound\\\":\\\"default\\\",\\\"alert\\\":\\\"" + message + "\\\"}}\"}");
        this.sns.publish(publishRequest);
    }
}
