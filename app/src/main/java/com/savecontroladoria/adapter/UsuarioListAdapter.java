package com.savecontroladoria.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.savecontroladoria.R;
import com.savecontroladoria.bancoDados.dao.AgendaDAO;
import com.savecontroladoria.bancoDados.dao.UsuarioDAO;
import com.savecontroladoria.objetos.Usuario;

import java.util.LinkedList;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by ADMIN on 17/04/2017.
 */

public class UsuarioListAdapter extends RecyclerView.Adapter<UsuarioListAdapter.UsuarioViewHolder> {

    private LinkedList<Usuario> mDataset;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public UsuarioListAdapter(Context c, LinkedList<Usuario> myDataset) {
        mContext = c;
        mDataset = myDataset;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = mLayoutInflater.inflate(R.layout.adapter_usuarios, parent, false);
        UsuarioViewHolder mvh = new UsuarioViewHolder(v);
        return mvh;
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public LinkedList<Usuario> getmDataset() {
        return mDataset;
    }

    public void addListItem(Usuario c, int position) {
        mDataset.add(c);
        notifyItemInserted(position);
    }

    public void refreshItens(LinkedList<Usuario> dataset) {
        mDataset.clear();
        mDataset.addAll(dataset);
        super.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        holder.tvNomeUsu.setText(mDataset.get(position).getNomeusu());
        holder.cbAdmin.setChecked(mDataset.get(position).getAdm().equals("S") ? true : false);
        holder.cbAcessoComand.setChecked(mDataset.get(position).getAcessoAcao().equals("S") ? true : false);
        holder.cbAcessoJob.setChecked(mDataset.get(position).getAcessoJob().equals("S") ? true : false);



    }


    public class UsuarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNomeUsu;
        public CheckBox cbAdmin;
        public CheckBox cbAcessoComand;
        public CheckBox cbAcessoJob;

        public UsuarioViewHolder(View itemView) {
            super(itemView);

            tvNomeUsu = (TextView) itemView.findViewById(R.id.tvNomeUsu);
            cbAdmin = (CheckBox) itemView.findViewById(R.id.cbAdmin);
            cbAcessoComand = (CheckBox) itemView.findViewById(R.id.cbAcessoComand);
            cbAcessoJob = (CheckBox) itemView.findViewById(R.id.cbAcessoJob);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UsuarioDAO usuDAO = new UsuarioDAO(mContext);
            Usuario userLogado = usuDAO.getUsuario(user.getUid());
            cbAdmin.setEnabled(false);
            if(userLogado.getAdm().equals("N")) {
                cbAcessoComand.setEnabled(false);
                cbAcessoJob.setEnabled(false);
            }

            cbAcessoComand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataset.get(getAdapterPosition()).setAcessoAcao(cbAcessoComand.isChecked() ? "S" : "N");
                }
            });
            cbAcessoJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataset.get(getAdapterPosition()).setAcessoJob(cbAcessoJob.isChecked() ? "S" : "N");
                }
            });


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.i(TAG, "click" + getAdapterPosition());
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick" + getAdapterPosition());

        }
    }


}
