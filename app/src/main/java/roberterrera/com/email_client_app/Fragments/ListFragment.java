package roberterrera.com.email_client_app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.services.gmail.Gmail;

import roberterrera.com.email_client_app.Activities.MainActivity;
import roberterrera.com.email_client_app.Classes.MessageDetailClass;
import roberterrera.com.email_client_app.Classes.MessagesListClass;
import roberterrera.com.email_client_app.R;

/**
 * Created by Rob on 2/26/16.
 */
public class ListFragment extends android.support.v4.app.ListFragment{

        OnMessageSelectedListener mListener;

        public interface OnMessageSelectedListener {
            public void onMessageSelected(String messagePlanet);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                mListener = (OnMessageSelectedListener) getActivity();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//            ArrayAdapter<String> myFragmentAdapter = new ArrayAdapter<String>(
//                    getContext(),
//                    android.R.layout.simple_list_item_1,
//                    MainActivity.mEmailList);
//            setListAdapter(myFragmentAdapter);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            mListener.onMessageSelected(l.getAdapter().getItem(position).toString());
        }
}

