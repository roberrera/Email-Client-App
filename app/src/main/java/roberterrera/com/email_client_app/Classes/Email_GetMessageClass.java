package roberterrera.com.email_client_app.Classes;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * Created by Rob on 2/28/16.
 */
public class Email_GetMessageClass {

    /**
     * Get Message with given ID.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    public static Message getMessage(Gmail service, String userId, String messageId)
            throws IOException {
        Message message = service.users().messages().get(userId, messageId).execute();

        System.out.println("Message snippet: " + message.getSnippet());

        return message;
    }

//    public static List<String> getFromUserFromApi() throws IOException, MessagingException {
//        com.google.api.services.gmail.Gmail mService = null;
//        Exception mLastError = null;
//        String user = "me";
//
//        ArrayList<String> labelsList = new ArrayList<>();
//        labelsList.add("INBOX");
//        labelsList.add("CATEGORY_PERSONAL");
//
//        ArrayList<String> fromUserList = new ArrayList<>();
//        ListMessagesResponse messageResponse = mService.users().messages().list(user).
//                setLabelIds(labelsList).setIncludeSpamTrash(false).setMaxResults(20L).execute();
//
//        for (Message message : messageResponse.getMessages()) {
//            String messageId = message.getId();
//            Message messages = Email_GetMessageClass.getMessage(mService, user, messageId);
//
//            for (MessagePartHeader header : messages.getPayload().getHeaders()) {
//
//                if (header.getName().equals("From")) {
//                    String from = header.getValue();
//                    fromUserList.add("From: " + from);
//                }
//            }
//        }
//        return fromUserList;
//    }

    /**
     * Get a Message and use it to create a MimeMessage.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return MimeMessage MimeMessage populated from retrieved Message.
     * @throws IOException
     * @throws MessagingException
     */
    public static MimeMessage getMimeMessage(Gmail service, String userId, String messageId)
            throws IOException, MessagingException {
        Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();

        byte[] emailBytes = Base64.decodeBase64(message.getRaw());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

        return email;
    }
}
