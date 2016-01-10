package mprz.cl.cle.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import mprz.cl.cle.buscadorEvaluadores;


/**
 * Created by elias on 02-01-16.
 */
public class EvaluadoresDialog extends DialogFragment {
    String nombre;
    String rut;
    int relacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle mArgs = getArguments();
        nombre = mArgs.getString("nombre");
        rut = mArgs.getString("rut");
        relacion = -1;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String[] categorias ={"Superior","Par", "Subalterno"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirmar su relación");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setSingleChoiceItems(categorias, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                relacion = which+1;
            }
        });
        //builder.setMessage("¿Desea elejir a " + nombre + " como su evaluador?");

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((buscadorEvaluadores) getActivity()).doPositiveClick(rut, nombre, relacion);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((buscadorEvaluadores) getActivity()).doNegativeClick();

            }
        });

        return builder.create();
    }
}
