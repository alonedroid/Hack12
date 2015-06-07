package mikamiyusen.com.hack12.utility;

import android.content.Context;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.sns.samples.tools.AmazonSNSClientWrapper;
import com.amazonaws.sns.samples.tools.SampleMessageGenerator.Platform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mikamiyusen.com.hack12.R;

public class SNSMobilePush {

    private AmazonSNSClientWrapper snsClientWrapper;

    public SNSMobilePush(AmazonSNS snsClient) {
        this.snsClientWrapper = new AmazonSNSClientWrapper(snsClient);
    }

    public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap = new HashMap<Platform, Map<String, MessageAttributeValue>>();

    static {
        attributesMap.put(Platform.ADM, null);
        attributesMap.put(Platform.GCM, null);
        attributesMap.put(Platform.APNS, null);
        attributesMap.put(Platform.APNS_SANDBOX, null);
        attributesMap.put(Platform.BAIDU, addBaiduNotificationAttributes());
        attributesMap.put(Platform.WNS, addWNSNotificationAttributes());
        attributesMap.put(Platform.MPNS, addMPNSNotificationAttributes());
    }

    public static void push(Context context) throws IOException {
        AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(
                context.getResources().openRawResource(R.raw.credentials)));

        sns.setEndpoint("https://sns.us-west-2.amazonaws.com/");
        System.out.println("===========================================\n");
        System.out.println("Getting Started with Amazon SNS");
        System.out.println("===========================================\n");
        try {
            SNSMobilePush sample = new SNSMobilePush(sns);
            sample.demoAppleSandboxAppNotification();
        } catch (AmazonServiceException ase) {
            System.out
                    .println("Caught an AmazonServiceException, which means your request made it "
                            + "to Amazon SNS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out
                    .println("Caught an AmazonClientException, which means the client encountered "
                            + "a serious internal problem while trying to communicate with SNS, such as not "
                            + "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    public void demoAppleSandboxAppNotification() {
        // TODO: Please fill in following values for your application. You can
        // also change the notification payload as per your preferences using
        // the method
        // com.amazonaws.sns.samples.tools.SampleMessageGenerator.getSampleAppleMessage()
        String privateKey = "";
        String certificate = "";
        // end of each line.
        String applicationName = "APNS_SANDBOX";
        String deviceToken = "b267ee46225f45734306921101135e1bd26b0811f8d44dd777dbad8e58f66b37";
        snsClientWrapper.demoNotification(Platform.APNS_SANDBOX, certificate,
                privateKey, deviceToken, applicationName, attributesMap);
    }

    private static Map<String, MessageAttributeValue> addBaiduNotificationAttributes() {
        Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
        notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.DeployStatus",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("1"));
        notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageKey",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("default-channel-msg-key"));
        notificationAttributes.put("AWS.SNS.MOBILE.BAIDU.MessageType",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("0"));
        return notificationAttributes;
    }

    private static Map<String, MessageAttributeValue> addWNSNotificationAttributes() {
        Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
        notificationAttributes.put("AWS.SNS.MOBILE.WNS.CachePolicy",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("cache"));
        notificationAttributes.put("AWS.SNS.MOBILE.WNS.Type",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("wns/badge"));
        return notificationAttributes;
    }

    private static Map<String, MessageAttributeValue> addMPNSNotificationAttributes() {
        Map<String, MessageAttributeValue> notificationAttributes = new HashMap<String, MessageAttributeValue>();
        notificationAttributes.put("AWS.SNS.MOBILE.MPNS.Type",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("token")); // This attribute is required.
        notificationAttributes.put("AWS.SNS.MOBILE.MPNS.NotificationClass",
                new MessageAttributeValue().withDataType("String")
                        .withStringValue("realtime")); // This attribute is required.

        return notificationAttributes;
    }
}
