package roberterrera.com.email_client_app.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public void setPlanetText(String planetText){
        TextView textView = (TextView)getView().findViewById(R.id.text);
        textView.setText(planetText);
    }
}
