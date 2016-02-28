/**
 * Created by Rob on 2/26/16.
 */
package roberterrera.com.email_client_app.Activities;

/**
 * Created by Rob on 2/26/16.
 */
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.gmail.GmailScopes;

import com.google.api.services.gmail.model.*;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import roberterrera.com.email_client_app.Classes.MessagesListClass;
import roberterrera.com.email_client_app.R;

public class MainActivity extends AppCompatActivity {
    GoogleAccountCredential mCredential;
//    private TextView mOutputText;
    ProgressDialog mProgress;
    private boolean mTwoPane;
    private ListView mEmailListView;
    private ArrayList<String> mEmailMessageList;
    private ArrayAdapter<String> mEmailListAdapter;
    private int getMessageListURL = R.string.get_list;
    private int getMessageDetailURL;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY, GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_INSERT,GmailScopes.MAIL_GOOGLE_COM, GmailScopes.GMAIL_MODIFY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        // Check if the device is a landscape tablet
        if (findViewById(R.id.detail_fragment) != null) {
            mTwoPane = true;
        }

//        mOutputText = new TextView(this);
//        mOutputText.setVerticalScrollBarEnabled(true);
//        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");
        mEmailListView = (ListView) findViewById(R.id.listview_email_list);
        mEmailMessageList = new ArrayList<>();
        mEmailListAdapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, mEmailMessageList);
        mEmailListView.setAdapter(mEmailListAdapter);

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mEmailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You tapped " + id, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.putExtra("Position", position);
//                startActivity(intent);
            }
        });


    }



     //Called whenever this activity is pushed to the foreground, such as after
     // a call to onCreate().

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
//            mOutputText.setText("Google Play Services required: " +
//                    "after installing, close and relaunch this app.");
            Toast.makeText(MainActivity.this, "Google Play Services required: " +
                    "after installing, close and relaunch this app.", Toast.LENGTH_SHORT).show();
        }
    }


      // Called when an activity launched here (specifically, AccountPicker
      // and authorization) exits, giving you the requestCode you started it with,
      // the resultCode it returned, and any additional data from it.

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
//                    mOutputText.setText("Account unspecified.");
                    Toast.makeText(MainActivity.this, "Account unspecified", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


      // Attempt to get a set of data from the Gmail API to display. If the
      // email address isn't known yet, then call chooseAccount() method so the
      // user can pick an account.

    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new MakeRequestTask(mCredential).execute();
            } else {
//                mOutputText.setText("No network connection available.");
                Toast.makeText(MainActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
            }
        }
    }


      // Starts an activity in Google Play Services so the user can pick an
      //account.

    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


      // Checks whether the device currently has a network connection.

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


      // Check that Google Play services APK is installed and up to date. Will
      // launch an error dialog for the user to update Google Play Services if
      // possible.

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

      // Display an error dialog showing that Google Play Services is missing
      //or out of date.

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                MainActivity.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

      // An asynchronous task that handles the Gmail API call.

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("RobMail")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi() throws IOException, MessagingException {

            // Get the labels in the user's account.
  /*          String user = "me";
            ListLabelsResponse listResponse = mService.users().labels().list(user).execute();
            for (Label label : listResponse.getLabels()) {
                mEmailMessageList.add(label.getName());
            }
            return mEmailMessageList;
        }
        */
            // Declare an array list of type MessagePartHeader to contain a list of headers.
//            List<String> messagePartHeaders = new ArrayList<>();
            ListMessagesResponse messageResponse = mService.users().messages().list("me").execute();
            // Call the getMessages() method and loop through the list of messages in the user account.
            for (Message message : messageResponse.getMessages()) {
                // Get the id of the individual messages.
                String messageId = message.getId();
                 Log.d("FOR_EACH_MESSAGE", "Message ID = " + messageId);
                // Get the contents of each individual message via the messages' messageId.
                Message messages = MessagesListClass.getMessage(mService, "me", messageId);

                // For each header in a message, find the subject line of the message.
                for (MessagePartHeader header : messages.getPayload().getHeaders()){
                   // If the header name is 'Subject' get the value of the subject line.
                    if (header.getName().equals("Subject")){
                        String subject = header.getValue();
                        // Add the subject line value to the mssage part headers list.
                        mEmailMessageList.add("Subject: "+subject);
                        Log.d("MESSAGES", "Subject line = " + subject);
                        Log.d("MESSAGES", "mEmailMessageList size = " + mEmailMessageList.size());
                    }
                }
//                mEmailMessageList.add(messagePartHeaders);
            }
            return mEmailMessageList;
        }

        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
//                mOutputText.setText("No results returned.");
            } else {
//                output.add(0, "Data retrieved using the Gmail API:");
//                mOutputText.setText(TextUtils.join("\n", output));
            }
            mEmailListAdapter.notifyDataSetChanged();
        }


        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                    System.out.println( "The following error occurred:\n"
                           + mLastError.getMessage() );
                }
            } else {
//                mOutputText.setText("Request cancelled.");
                System.out.println("Request cancelled.");
            }
        }
    }
/*
    public class GetMessagesTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            String contentAsString = "";
            InputStream inputStream = null;

            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                inputStream = conn.getInputStream();

                // Converts the InputStream into a string
                contentAsString = getInputStream(inputStream);

            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                mEmailMessageList.clear();

                // Get the data inside the JSON object, and the data inside the object's array.
                JSONObject dataObject = new JSONObject(contentAsString); // Could take no params, or could take the string you want to use.
                JSONArray messagesJsonArray = dataObject.getJSONArray("messages"); // Getting the array gets the stuff inside the object.

                // For every object in the item array, add the name to the ArrayList.
                for (int i = 0; i < messagesJsonArray.length(); i++) {
                    JSONObject object = messagesJsonArray.optJSONObject(i);
                    String messages = object.optString("messages");
                    mEmailMessageList.add(messages);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(Void output) {
            super.onPostExecute(output);
            mProgress.hide();
            mEmailListAdapter.notifyDataSetChanged();
        }
    }
*/
    public String getInputStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String read;

        while((read = br.readLine()) != null) {
            sb.append(read);
        }

        br.close();
        return sb.toString();
    }

//    @Override
//    public void onMessageSelected(String selectedMessage) {
//
//        if (mTwoPane) {
//            // Getting the text up from the first fragment
//            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
//            // Passing the text down to the second fragment
//            detailFragment.setMessageText(selectedMessage);
//        } else {
//            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//            intent.putExtra(selectedMessage, "Selected message");
//            startActivity(intent);
//        }
//
//    }
}