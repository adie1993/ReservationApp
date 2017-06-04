package app.adie.reservation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import app.adie.reservation.activity.MainActivity;
import app.adie.reservation.R;

/**
 * Created by Adie on 27/05/2016.
 */
public class DialogHabisFragment extends Fragment implements OnClickListener {
    private Button btnpesan;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_habis,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.btnpesan=(Button)getView().findViewById(R.id.btn_pesan);
        btnpesan.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        Intent a = new Intent(getActivity(), MainActivity.class);

        startActivityForResult(a, 20);
        startActivity(a);
        getActivity().finish();
    }
}
