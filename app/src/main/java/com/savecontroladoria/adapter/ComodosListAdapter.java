package com.savecontroladoria.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savecontroladoria.R;
import com.savecontroladoria.objetos.Acao;
import com.savecontroladoria.viewholders.AcaoViewHolder;
import com.savecontroladoria.viewholders.ComodosViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandCollapseController;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ADMIN on 29/04/2017.
 */

public class ComodosListAdapter extends ExpandableRecyclerViewAdapter<ComodosViewHolder, AcaoViewHolder> {
    private Context mContext;
    private List<Acao> salaList;
    List<? extends ExpandableGroup> comodos;
    public static final String TAG = "ComandosListAdapter";
    private Acao acao;
    private LayoutInflater mLayoutInflater;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference tab = database.getReference("tabelas");
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ComodosViewHolder comodosViewHolder;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public ComodosListAdapter(List<? extends ExpandableGroup> groups, Context context) {
        super(groups);
        mContext = context;
        comodos = groups;

        expandirGrupo();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ComodosViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comodos, parent, false);
        comodosViewHolder = new ComodosViewHolder(view);

        return comodosViewHolder;
    }

    @Override
    public AcaoViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_acoes, parent, false);

        return new AcaoViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(ComodosViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setComodoTitle(group.getTitle());
        //toggleGroup(group);
    }

    @Override
    public void onBindChildViewHolder(AcaoViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

            acao = (Acao) group.getItems().get(childIndex);

        //Log.d(TAG,group.getTitle()+"entrou aqui"+isGroupExpanded(2)+"grupo1"+isGroupExpanded(1));
        salaList = group.getItems();
        holder.setAcaoTitle(acao.getTipoacao());
        holder.setStatusSwitch(acao.isStatus());

    }



    public void expandirGrupo(){
        toggleGroup(comodos.get(0));
        toggleGroup(comodos.get(1));
    }



    public void changeStatus(Integer position, boolean status) {
        salaList.get(position).setStatus(status);
    }

    public void trocaStatusBotoes(String comando, String retorno) {
            if (retorno.equals("ok")) {
                if (comando.equals("LIGARRELE01")) {
                    salaList.get(1).setStatus(true);
                    super.notifyItemChanged(2);
                    tab.child("status").child("1").setValue(new Acao("TV/PLAYSTATION/DUOSAT", true, user.getUid(), 2, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("LIGARRELE02")) {
                    salaList.get(2).setStatus(true);
                    super.notifyItemChanged(3);
                    tab.child("status").child("2").setValue(new Acao("LUZ SALA TV", true, user.getUid(), 3, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("LIGARRELE03")) {
                    salaList.get(3).setStatus(true);
                    super.notifyItemChanged(4);
                    tab.child("status").child("3").setValue(new Acao("LUZ SALA", true, user.getUid(), 4, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("LIGARRELE04")) {
                    salaList.get(4).setStatus(true);
                    super.notifyItemChanged(5);
                    tab.child("status").child("4").setValue(new Acao("LUZ HALL", true, user.getUid(), 5, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("DESLIGARRELE01")) {
                    salaList.get(1).setStatus(false);
                    super.notifyItemChanged(2);
                    tab.child("status").child("1").setValue(new Acao("TV/PLAYSTATION/DUOSAT", false, user.getUid(), 2, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("DESLIGARRELE02")) {
                    salaList.get(2).setStatus(false);
                    super.notifyItemChanged(3);
                    tab.child("status").child("2").setValue(new Acao("LUZ SALA TV", false, user.getUid(), 3, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("DESLIGARRELE03")) {
                    salaList.get(3).setStatus(false);
                    super.notifyItemChanged(4);
                    tab.child("status").child("3").setValue(new Acao("LUZ SALA", false, user.getUid(), 4, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
                if (comando.equals("DESLIGARRELE04")) {
                    salaList.get(4).setStatus(false);
                    super.notifyItemChanged(5);
                    tab.child("status").child("4").setValue(new Acao("LUZ HALL", false, user.getUid(), 5, dateFormat.format(Calendar.getInstance().getTime()), "Sala"));
                }
            }

        if (retorno.equals("ok-cozinha")) {
            //CENA2
            salaList = getGroups().get(1).getItems();
            //Log.d(TAG,"TESTE AGENDAMENTO MUDANÇA STATUS"+comando);
            if (comando.equals("LIGARRELE01")) {

                salaList.get(2).setStatus(true);
                notifyDataSetChanged();
                tab.child("status").child("7").setValue(new Acao("FILTRO",true,user.getUid(),8,dateFormat.format(Calendar.getInstance().getTime()),"Cozinha"));
            }
            if (comando.equals("DESLIGARRELE01")) {
                salaList.get(2).setStatus(false);
                notifyDataSetChanged();
                tab.child("status").child("7").setValue(new Acao("FILTRO",false,user.getUid(),8,dateFormat.format(Calendar.getInstance().getTime()),"Cozinha"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

}
