package com.amazonaws.sns.samples.mobilepush;

/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

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

    public static void main(String[] args) throws IOException {
        /*
         * TODO: Be sure to fill in your AWS access credentials in the
		 * AwsCredentials.properties file before you try to run this sample.
		 * http://aws.amazon.com/security-credentials
		 */
        AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(
                SNSMobilePush.class
                        .getResourceAsStream("AwsCredentials.properties")));

        sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
        try {
            SNSMobilePush sample = new SNSMobilePush(sns);
        } catch (AmazonServiceException ase) {
        } catch (AmazonClientException ace) {
        }
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
