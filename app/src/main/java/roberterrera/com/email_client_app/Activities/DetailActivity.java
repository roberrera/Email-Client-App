package roberterrera.com.email_client_app.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartBody;

import java.io.IOException;

import roberterrera.com.email_client_app.Classes.EmailClass;
import roberterrera.com.email_client_app.R;

public class DetailActivity extends AppCompatActivity {
    public String messageBody;
    public TextView messageDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String intent = getIntent().getStringExtra("POSITION");
        messageDetailTextView = (TextView)findViewById(R.id.text);
        messageBody = null;

        MessageDetailTask messageDetailTask = new MessageDetailTask();
        messageDetailTask.execute();

    }

    private class MessageDetailTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        @Override
        protected String doInBackground(Void... params) {
            MessagePartBody messagePartBody= new MessagePartBody();
            String user = "me";

//            try {
//                messagePartBody = mService.users().messages().get(user, messageId).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            for (MessagePartBody body : message.getPayload().getBody()) {
//
//                // Get the contents of each individual message via the messages' messageId.
//                Message messages = null;
//                try {
//                    messages = EmailClass.getMessage(mService, user, messageId);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                messageBody = String.valueOf(messages.getPayload().getBody());
//
////               MessagePartBody messagePartBody = messages.getPayload().getBody();
//            }
////            String messageBody = String.valueOf(messagePartBody);
            return messageBody;
        }

        @Override
        protected void onPostExecute(String output) {
            super.onPostExecute(output);
            messageDetailTextView.setText(messageBody);
        }
    }
}
