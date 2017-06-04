package app.adie.reservation.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.adie.reservation.Fragment.AlertDialogFragment;
import app.adie.reservation.activity.PesanTiket;
import app.adie.reservation.R;
import app.adie.reservation.entity.Penumpang;

public class PenumpangAdapter extends Adapter<PenumpangAdapter.ViewHolder> {
    private Context context;
    private PesanTiket mPesanTiket;
    private Fragment mFragment;
    private ArrayList<Penumpang> mPenumpangs;



    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener, OnLongClickListener {
        public PesanTiket mPesanTiket;
        public Fragment mFragment;
        public TextView mName;
        public Penumpang mPenumpang;
        private ArrayList<Penumpang> mPenumpangs;
        public CheckBox mSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mName = (TextView) itemLayoutView.findViewById(R.id.name);
            this.mSelected = (CheckBox) itemLayoutView.findViewById(R.id.selected);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        public void onClick(View v) {
            if (this.mSelected.isChecked()) {
                this.mSelected.setChecked(false);
            } else {
                this.mSelected.setChecked(true);
            }
            Penumpang passenger = (Penumpang) this.mSelected.getTag();
            passenger.setIsSelected(this.mSelected.isChecked());
            this.mPenumpang.setIsSelected(this.mSelected.isChecked());
            List<String> listSelect = new ArrayList();
            ArrayList<Penumpang> passengerList = this.mPenumpangs;
            for (int i = 0; i < passengerList.size(); i++) {
                Penumpang singlePassenger = (Penumpang) passengerList.get(i);
                if (singlePassenger.isSelected()) {
                    listSelect.add(singlePassenger.getName().toString());
                }
            }
            if (listSelect.size() > 3) {
                this.mSelected.setChecked(false);
                passenger.setIsSelected(false);
                ((Penumpang) passengerList.get(getLayoutPosition())).setIsSelected(false);
            } else {
                setPassengerSelected(v, listSelect, passengerList);
            }
        }

        private void setPassengerSelected(View view, List<String> listSelect, ArrayList<Penumpang> passengerList) {
            String selected = view.getContext().getResources().getText(R.string.message_no_passenger_selected).toString();
            switch (listSelect.size()) {
                case 1:
                    selected = ((String) listSelect.get(0)).toString();
                    break;
                case 2:
                    selected = ((String) listSelect.get(0)).toString() + ", " + ((String) listSelect.get(1)).toString();
                    break;
                case 3:
                    selected = ((String) listSelect.get(0)).toString() + ", " + ((String) listSelect.get(1)).toString() + ", " + ((String) listSelect.get(2)).toString();
                    break;
            }
            PesanTiket.getInstance().getPassengerFromFragmentContainer(selected, listSelect.size(), passengerList);
        }

        public boolean onLongClick(View view) {
            if (this.mPenumpang.getId()>=2) {
                String message = this.mFragment.getResources().getString(R.string.message_delete_passenger) + " " + this.mPenumpang.getName() + "?";
                AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance();
                dialogFragment.init(this.mFragment.getContext(), AlertDialogFragment.DIALOG_ALERT).setMessage(message).setTitle((int) R.string.title_attention).setPositiveButton((int) R.string.action_delete).setNegativeButton((int) R.string.dismiss_button_text).dialogKey(this.mPenumpang.id).dialogType("DELETE_PENUMPANG");
                dialogFragment.show(this.mFragment.getFragmentManager(), "dialog");
            }
            return false;
        }
    }

    public PenumpangAdapter(Context context, Fragment fragment, ArrayList<Penumpang> passenger) {
        this.context = context;
        this.mFragment = fragment;
        this.mPenumpangs = passenger;

    }

    public ArrayList<Penumpang> getPassengerList() {
        return this.mPenumpangs;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_penumpang, null));
    }

    public void addItem(int position, Penumpang data) {
        this.mPenumpangs.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(String idd) {
        int id = Integer.parseInt(idd);
        for (int i = 0; i < this.mPenumpangs.size(); i++) {
            if (((Penumpang) this.mPenumpangs.get(i)).getId()==(id)) {
                this.mPenumpangs.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final int pos = position;
        viewHolder.mPesanTiket = this.mPesanTiket;
        viewHolder.mPenumpangs = getPassengerList();
        viewHolder.mPenumpang = (Penumpang) this.mPenumpangs.get(pos);
        viewHolder.mFragment = this.mFragment;
        viewHolder.mName.setText(((Penumpang) this.mPenumpangs.get(position)).getName());
        viewHolder.mSelected.setChecked(((Penumpang) this.mPenumpangs.get(position)).isSelected());
        viewHolder.mSelected.setTag(this.mPenumpangs.get(position));
        viewHolder.mSelected.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Penumpang passenger = (Penumpang) checkBox.getTag();
                passenger.setIsSelected(checkBox.isChecked());
                ((Penumpang) PenumpangAdapter.this.mPenumpangs.get(pos)).setIsSelected(checkBox.isChecked());
                List<String> listSelect = new ArrayList();
                ArrayList<Penumpang> passengerList = PenumpangAdapter.this.getPassengerList();
                for (int i = 0; i < passengerList.size(); i++) {
                    Penumpang singlePassenger = (Penumpang) passengerList.get(i);
                    if (singlePassenger.isSelected()) {
                        listSelect.add(singlePassenger.getName().toString());
                    }
                }

                    if (listSelect.size() > 3) {
                        viewHolder.mSelected.setChecked(false);
                        passenger.setIsSelected(false);
                        ((Penumpang) passengerList.get(pos)).setIsSelected(false);
                        return;
                    }

               else {
                    PenumpangAdapter.this.setPassengerSelected(v, listSelect, passengerList);
                }
            }
        });
    }

    public int getItemCount() {
        return this.mPenumpangs.size();
    }

    private void setPassengerSelected(View view, List<String> listSelect, ArrayList<Penumpang> passengerList) {
        String selected = view.getContext().getResources().getText(R.string.message_no_passenger_selected).toString();
        switch (listSelect.size()) {
            case 1:
                selected = ((String) listSelect.get(0)).toString();
                break;
            case 2:
                selected = ((String) listSelect.get(0)).toString() + ", " + ((String) listSelect.get(1)).toString();
                break;
            case 3:
                selected = ((String) listSelect.get(0)).toString() + ", " + ((String) listSelect.get(1)).toString() + ", " + ((String) listSelect.get(2)).toString();
                break;
        }
        PesanTiket.getInstance().getPassengerFromFragmentContainer(selected, listSelect.size(), passengerList);
    }
}
