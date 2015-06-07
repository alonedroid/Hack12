package com.amazonaws.sns.samples.tools;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class AmazonSNSClientWrapper {
    private final AmazonSNS snsClient;

    public AmazonSNSClientWrapper(AmazonSNS client) {
        this.snsClient = client;
    }

    private PublishResult publish(String endpointArn, String sendMessage) {
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessageStructure("json");
        publishRequest.setTargetArn(endpointArn);
        publishRequest.setMessage("{\"APNS_SANDBOX\":\"{\\\"aps\\\":{\\\"sound\\\":\\\"default\\\",\\\"alert\\\":\\\"" + sendMessage + "\\\"}}\"}");
        return snsClient.publish(publishRequest);
    }

    public void demoNotification(String message) {
        publish("arn:aws:sns:us-west-2:406563354822:endpoint/APNS_SANDBOX/Bee/8d731564-518f-39e1-8e02-2a3e582431d4", message);
    }
}
