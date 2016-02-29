package roberterrera.com.email_client_app.Classes;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rob on 2/28/16.
 */
public class Email_ThreadsClass {

    /**
     * List all Threads of the user's mailbox matching the query.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param query String used to filter the Threads listed.
     * @throws IOException
     */
    public static void listThreadsMatchingQuery (Gmail service, String userId,
                                                 String query) throws IOException {
        ListThreadsResponse response = service.users().threads().list(userId).setQ(query).execute();
        List<Thread> threads = new ArrayList<Thread>();
        while(response.getThreads() != null) {
            threads.addAll(response.getThreads());
            if(response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().threads().list(userId).setQ(query).setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for(Thread thread : threads) {
            System.out.println(thread.toPrettyString());
        }
    }

    /**
     * List all Threads of the user's mailbox with labelIds applied.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param labelIds String used to filter the Threads listed.
     * @throws IOException
     */
    public static void listThreadsWithLabels (Gmail service, String userId,
                                              List<String> labelIds) throws IOException {
        ListThreadsResponse response = service.users().threads().list(userId).setLabelIds(labelIds).execute();
        List<Thread> threads = new ArrayList<Thread>();
        while(response.getThreads() != null) {
            threads.addAll(response.getThreads());
            if(response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().threads().list(userId).setLabelIds(labelIds)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for(Thread thread : threads) {
            System.out.println(thread.toPrettyString());
        }
    }
}
