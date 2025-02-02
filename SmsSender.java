import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    private static final String ACCOUNT_SID = "ACa80c6a51ce91d0f07beee3936add6652";
    private static final String AUTH_TOKEN = "cab3f51a7f33349ed22d3093b1045115";
    private static final String TWILIO_PHONE_NUMBER = "+15737702737";

    public static void sendSms(String recipientPhone, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(recipientPhone),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                messageBody
        ).create();

        System.out.println("SMS sent successfully to " + recipientPhone);
    }
}
