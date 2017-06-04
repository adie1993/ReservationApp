package app.adie.reservation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import app.adie.reservation.R;
import app.adie.reservation.activity.ChatActivity;
import app.adie.reservation.activity.MainActivity;

/**
 * Created by Adie on 27/05/2016.
 */
public class DialogInProgressFragment extends Fragment implements View.OnClickListener {
    private TextView txtkode;
    private static String kod;
    private Button btnpesan;
    public static DialogInProgressFragment newInstance(String kode) {
        kod = kode;
        return new DialogInProgressFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_inprogress,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.txtkode=(TextView)getView().findViewById(R.id.txtkod);
        this.btnpesan=(Button)getView().findViewById(R.id.btn_pesan);
        btnpesan.setOnClickListener(this);

        txtkode.setText(kod);



    }

    @Override
    public void onClick(View v) {
        Intent a = new Intent(getActivity(), ChatActivity.class);

        startActivityForResult(a, 20);
        startActivity(a);
        getActivity().finish();
    }
}
