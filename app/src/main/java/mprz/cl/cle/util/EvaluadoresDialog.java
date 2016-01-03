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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle mArgs = getArguments();
        nombre = mArgs.getString("nombre");
        rut = mArgs.getString("rut");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Confirmar");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("¿Desea elejir a " + nombre + " como su evaluador?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((buscadorEvaluadores)getActivity()).doPositiveClick(rut, nombre);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((buscadorEvaluadores)getActivity()).doNegativeClick();

            }
        });

        return builder.create();
    }
}
