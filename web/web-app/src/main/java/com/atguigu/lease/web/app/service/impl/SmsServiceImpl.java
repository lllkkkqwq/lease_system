package com.atguigu.lease.web.app.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.atguigu.lease.web.app.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

@Autowired
private Client client;

    @Override
    public void sendCode(String phone, String code) {

        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setPhoneNumbers(phone);
        smsRequest.setSignName("阿里云短信测试");
      //  smsRequest.setTemplateCode("SMS_154950909");
        smsRequest.setTemplateCode("SMS_305017537");

        smsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");
        try {
            SendSmsResponse sendSmsResponse = client.sendSms(smsRequest);
            System.out.println(sendSmsResponse.body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
