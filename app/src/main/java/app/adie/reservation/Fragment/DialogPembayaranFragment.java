package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.adie.reservation.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class DialogPembayaranFragment extends Fragment {
    private TextView waktu;
    private static String tanggal,batas;
    public static DialogPembayaranFragment newInstance(String tgl,String wak) {
        tanggal = tgl;
        batas = wak;
        return new DialogPembayaranFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_pembayaran,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.waktu=(TextView)getView().findViewById(R.id.jampem);


        waktu.setText(batas+" WIB "+tanggal);



    }
}
