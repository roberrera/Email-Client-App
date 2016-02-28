package roberterrera.com.email_client_app.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import roberterrera.com.email_client_app.Activities.MainActivity;
import roberterrera.com.email_client_app.Classes.MessagesListClass;
import roberterrera.com.email_client_app.R;

/**
 * Created by Rob on 2/25/16.
 */
public class DetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

//    public void setMessageText(String messageText){
//
//
//    }
//
//    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
//        private com.google.api.services.gmail.Gmail mService = null;
//        private Exception mLastError = null;
//
//        @Override
//        protected List<String> doInBackground(Void... params) {
//            try {
//                textView.setText(messageText);
//                return getDataFromApi();
//            } catch (Exception e) {
//                mLastError = e;
//                cancel(true);
//                return null;
//            }
//        }
//
//        private List<String> getDataFromApi() throws IOException, MessagingException {
//
//            String user = "me";
//
//            ArrayList<String> labelsList = new ArrayList<>();
//            labelsList.add("INBOX");
//            labelsList.add("CATEGORY_PERSONAL");
//
//            // Get the labels in the user's account.
//            ListMessagesResponse messageResponse = mService.users().messages().list(user).
//                    setLabelIds(labelsList).setIncludeSpamTrash(false).setMaxResults(20L).execute();
//            // Call the getMessages() method and loop through the list of messages in the user account.
//            for (Message message : messageResponse.getMessages()) {
//                // Get the id of the individual messages.
//                String messageId = message.getId();
//                // Get the contents of each individual message via the messages' messageId.
//                Message messages = MessagesListClass.getMessage(mService, user, messageId);
//
//                // For each MessagePartHeader in a message, find the subject line of the message.
//                for (MessagePartHeader header : messages.getPayload().getHeaders()){
//                    // If the header name is 'Subject' get the value of the subject line.
//                    if (header.getName().equals("Subject")){
//                        String subject = header.getValue();
//                        // Add the subject line value to the mssage part headers list.
//                        mEmailMessageList.add("Subject: "+subject);
//                    }
//                }
//            }
//            return mEmailMessageList;
//        }
//
//        @Override
//        protected void onPreExecute() {
////            mOutputText.setText("");
//            mProgress.show();
//        }
//
//        @Override
//        protected void onPostExecute(List<String> output) {
//            mProgress.hide();
//            if (output == null || output.size() == 0) {
////                mOutputText.setText("No results returned.");
//            } else {
////                output.add(0, "Data retrieved using the Gmail API:");
////                mOutputText.setText(TextUtils.join("\n", output));
//            }
//
//        }
//
//    }
}
