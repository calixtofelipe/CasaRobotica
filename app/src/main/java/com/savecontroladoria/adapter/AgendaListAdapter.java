package com.savecontroladoria.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.savecontroladoria.bancoDados.dao.AgendaDAO;
import com.savecontroladoria.objetos.Agendamento;
import com.savecontroladoria.util.SpinnerUtils;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

import com.savecontroladoria.Interface.RVHackConsultaAgenda;
import com.savecontroladoria.R;

/**
 * Created by ADMIN on 17/04/2017.
 */

public class AgendaListAdapter extends RecyclerView.Adapter<AgendaListAdapter.AgendaViewHolder> {
    private RVHackConsultaAgenda mRVHackConsultaAgenda;
    private LinkedList<Agendamento> mDataset;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AgendaListAdapter(Context c, LinkedList<Agendamento> myDataset) {
        mContext = c;
        mDataset = myDataset;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = mLayoutInflater.inflate(R.layout.itens_adapter_agenda, parent, false);
        AgendaViewHolder mvh = new AgendaViewHolder(v);
        return mvh;
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addListItem(Agendamento c, int position) {
        mDataset.add(c);
        notifyItemInserted(position);
    }

    public void setRVHackConsultaAgenda(RVHackConsultaAgenda r) {
        this.mRVHackConsultaAgenda = r;
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Log.d("AgendaAdapter", "ACAO: " + (mDataset.get(position).getAcao()==null ? "0" : mDataset.get(position).getAcao()));
        holder.tvHora.setText(mDataset.get(position).getDescrHora());
        holder.ImgOnOff.setChecked(mDataset.get(position).getOnoff().equals("S")? true : false);
        holder.checkBoxMiniDom.setChecked(mDataset.get(position).getDomingo().equals("S")? true : false);
        holder.checkBoxMiniSeg.setChecked(mDataset.get(position).getSegunda().equals("S")? true : false);
        holder.checkBoxMiniTer.setChecked(mDataset.get(position).getTerca().equals("S")? true : false);
        holder.checkBoxMiniQua.setChecked(mDataset.get(position).getQuarta().equals("S")? true : false);
        holder.checkBoxMiniQui.setChecked(mDataset.get(position).getQuinta().equals("S")? true : false);
        holder.checkBoxMiniSex.setChecked(mDataset.get(position).getSexta().equals("S")? true : false);
        holder.checkBoxMiniSab.setChecked(mDataset.get(position).getSabado().equals("S")? true : false);
        holder.spinnerAcaoAdp.setSelection(new Integer(mDataset.get(position).getAcao()==null ? "0" : mDataset.get(position).getAcao()));

    }


    public class AgendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvHora;
        public ImageView tvImagem;
        public CheckBox checkBoxMiniDom;
        public CheckBox checkBoxMiniSeg;
        public CheckBox checkBoxMiniTer;
        public CheckBox checkBoxMiniQua;
        public CheckBox checkBoxMiniQui;
        public CheckBox checkBoxMiniSex;
        public CheckBox checkBoxMiniSab;
        public Spinner spinnerAcaoAdp;


        public CheckBox ImgOnOff;

        public AgendaViewHolder(View itemView) {
            super(itemView);

            tvHora = (TextView) itemView.findViewById(R.id.tvHora);
            tvImagem = (ImageView) itemView.findViewById(R.id.imgRelogio);
            ImgOnOff = (CheckBox) itemView.findViewById(R.id.checkBoxLamp);
            checkBoxMiniDom = (CheckBox) itemView.findViewById(R.id.checkBoxMiniDom);
            checkBoxMiniSeg = (CheckBox) itemView.findViewById(R.id.checkBoxMiniSeg);
            checkBoxMiniTer = (CheckBox) itemView.findViewById(R.id.checkBoxMiniTer);
            checkBoxMiniQua = (CheckBox) itemView.findViewById(R.id.checkBoxMiniQua);
            checkBoxMiniQui = (CheckBox) itemView.findViewById(R.id.checkBoxMiniQui);
            checkBoxMiniSex = (CheckBox) itemView.findViewById(R.id.checkBoxMiniSex);
            checkBoxMiniSab = (CheckBox) itemView.findViewById(R.id.checkBoxMiniSab);
            spinnerAcaoAdp = (Spinner) itemView.findViewById(R.id.spinnerAcaoAdp);
            checkBoxMiniDom.setEnabled(false);
            checkBoxMiniSeg.setEnabled(false);
            checkBoxMiniTer.setEnabled(false);
            checkBoxMiniQua.setEnabled(false);
            checkBoxMiniQui.setEnabled(false);
            checkBoxMiniSex.setEnabled(false);
            checkBoxMiniSab.setEnabled(false);
            spinnerAcaoAdp.setEnabled(false);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (mContext, android.R.layout.simple_spinner_item, SpinnerUtils.getSpinnerAcao());
            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            spinnerAcaoAdp.setAdapter(dataAdapter);

            ImgOnOff.setEnabled(false);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.i("ConsultPedAdapter","LongClick"+getAdapterPosition());
                    mRVHackConsultaAgenda.onLongPressClickListener(view,getAdapterPosition());
                    AgendaDAO agendaDAO = new AgendaDAO(mContext);
                    agendaDAO.deleteItem(getAdapterPosition());
                    mDataset.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {

            if (mRVHackConsultaAgenda != null) {
                mRVHackConsultaAgenda.onClickListener(v, getAdapterPosition());
                //Log.i("ConsultPedAdapter","onClick"+getAdapterPosition());
            }
        }


    }
}