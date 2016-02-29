package roberterrera.com.email_client_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import roberterrera.com.email_client_app.Classes.Email;
import roberterrera.com.email_client_app.R;

/**
 * Created by Rob on 2/28/16.
 */
public class EmailListAdapter extends ArrayAdapter<Email> {

    ArrayList<Email> mArrayList;

    public EmailListAdapter(Context context, ArrayList<Email> newEmailArrayList) {
        super(context, -1);

        mArrayList = new ArrayList<Email>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Email inboxList = mArrayList.get(position);

        Context context = getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View inboxLayoutView = inflater.inflate(R.layout.list_item_layout, parent, false);

        TextView fromText = (TextView) inboxLayoutView.findViewById(R.id.textview_from);
        TextView subjectText = (TextView) inboxLayoutView.findViewById(R.id.textview_subject);
//
        fromText.setText(inboxList.getmFromUser());
        subjectText.setText(inboxList.getmSubjectLine());

        return inboxLayoutView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}