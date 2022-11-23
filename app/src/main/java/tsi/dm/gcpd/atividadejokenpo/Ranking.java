package tsi.dm.gcpd.atividadejokenpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Ranking extends AppCompatActivity {

    //private static ArrayList<Winner> listWinner = new ArrayList<>();
    //private WinnerDatabase db;
    private List<Winner> winners = new ArrayList<>();
    private FirebaseFirestore db;
    private final String TAG = "DEBUG ----";
    private String lastWinnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        this.db = FirebaseFirestore.getInstance();
//        ArrayList<Winner> winners = getIntent().getParcelableArrayListExtra("ranking");
        db.collection("ranking")
                .orderBy("endTime", Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                winners.add(document.toObject(Winner.class));
                                if(winners.size() == 1){
                                    lastWinnerId = document.getId();
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        if(!winners.isEmpty()){
                            Log.d("winners", winners.toString());
                            //Collections.sort(winners, Collections.reverseOrder());
                            Winner winner = winners.get(0);
//            for(Winner win : winners){
//                if(win.getIsLast() == 1){
                            //this.listWinner.add(win);
                            if(winner.getIsPlayer() == 1){
                                TextView input = (TextView) findViewById(R.id.inputName);
                                Button button = (Button) findViewById(R.id.buttonEnviarNome);
                                input.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                            }
//                }
//            }
                            Collections.sort(winners, Collections.reverseOrder());
                        }
                        checkEmptyRanking();
                        buildRankingList();
                    }
                });
//        if(!winners.isEmpty()){
//            Log.d("winners", winners.toString());
//            //Collections.sort(winners, Collections.reverseOrder());
//            Winner winner = db.winnerDao().getLastWinner();
////            for(Winner win : winners){
////                if(win.getIsLast() == 1){
//                    //this.listWinner.add(win);
//                    if(winner.isPlayer() == 1){
//                        TextView input = (TextView) findViewById(R.id.inputName);
//                        Button button = (Button) findViewById(R.id.buttonEnviarNome);
//                        input.setVisibility(View.VISIBLE);
//                        button.setVisibility(View.VISIBLE);
//                    }
////                }
////            }
//            Collections.sort(winners, Collections.reverseOrder());
//        }
//        checkEmptyRanking();
//        buildRankingList(winners);
    }

    private void checkEmptyRanking() {
        //List<Winner> winners = db.winnerDao().getAll();
        TextView textView = (TextView) findViewById(R.id.textViewRankingVazio);
        Button button = (Button) findViewById(R.id.buttonLimparRanking);
        TextView input = (TextView) findViewById(R.id.inputName);
        Button buttonName = (Button) findViewById(R.id.buttonEnviarNome);
        if(!winners.isEmpty()){
            textView.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
        }
        else{
            input.setVisibility(View.GONE);
            buttonName.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }
    }

    private void buildRankingList() {
        winners = new ArrayList<>();
        db.collection("ranking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                winners.add(document.toObject(Winner.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Collections.sort(winners, Collections.reverseOrder());
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        ListView listView = (ListView) findViewById(R.id.ranking);

                        ArrayAdapter<Winner> adapter = new ArrayAdapter<Winner>(
                                getApplicationContext(), android.R.layout.simple_list_item_1, winners
                        );

                        listView.setAdapter(adapter);

                        listView.setClickable(true);

                        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "" + parent.getAdapter().getItem(position),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        };

                        listView.setOnItemClickListener(listener);


                        checkEmptyRanking();
                    }
                });
//        ListView listView = (ListView) findViewById(R.id.ranking);
//
//            ArrayAdapter<Winner> adapter = new ArrayAdapter<Winner>(
//                    this, android.R.layout.simple_list_item_1, winners
//            );
//
//            listView.setAdapter(adapter);
//
//            listView.setClickable(true);
//
//            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "" + parent.getAdapter().getItem(position),
//                            Toast.LENGTH_SHORT
//                    ).show();
//                }
//            };
//
//            listView.setOnItemClickListener(listener);
//
//
//            checkEmptyRanking();

    }

    public void setPlayerName(View view){
//        List<Winner> winners = db.winnerDao().getAll();
        TextView input = (TextView) findViewById(R.id.inputName);
        Button button = (Button) findViewById(R.id.buttonEnviarNome);
        DocumentReference playerRef = db.collection("ranking").document(lastWinnerId);
        playerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Winner winner = documentSnapshot.toObject(Winner.class);
                if(winner.getIsPlayer()==1){
                    //winner.setIsLast(0);
                    if(!input.getText().toString().isEmpty()) {
                        winner.setNome(input.getText().toString());
                    }
                    else{
                        winner.setNome("Player");
                    }
                }
                playerRef
                        .update("nome", winner.getNome())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                buildRankingList();
                                input.setVisibility(View.GONE);
                                button.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });
        //for(Winner winner : winners){

        //}
        //DocumentReference playerRef = db.collection("ranking").document(lastWinnerId);

        //db.winnerDao().updatePlayerName(winner);
        //winners = db.winnerDao().getAll();

    }

    public void clearRanking(){
        //listWinner = new ArrayList<>();
//        db.winnerDao().clearTable();
//        List<Winner> winners = db.winnerDao().getAll();
        View view = View.inflate(getApplicationContext(), R.layout.activity_main, null);
        db.collection("ranking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("ranking").document(document.getId()).delete();
                            }
                            PersonalNotification.criaNotificacao("Ranking Jo Ken Po", "O ranking foi resetado", view);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        checkEmptyRanking();
                        buildRankingList();
                    }
                });


    }

    public void alertClearRanking(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(R.string.certeza_apagar)
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearRanking();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .show();
    }
}