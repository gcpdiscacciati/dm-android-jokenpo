package tsi.dm.gcpd.atividadejokenpo;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Ranking extends AppCompatActivity {

    private static ArrayList<Winner> listWinner = new ArrayList<>();
    private WinnerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        this.db = WinnerDatabase.getDatabase(getApplicationContext());
//        ArrayList<Winner> winners = getIntent().getParcelableArrayListExtra("ranking");
        List<Winner> winners = db.winnerDao().getAll();
        if(!winners.isEmpty()){
            Log.d("winners", winners.toString());
            //Collections.sort(winners, Collections.reverseOrder());
            Winner winner = db.winnerDao().getLastWinner();
//            for(Winner win : winners){
//                if(win.getIsLast() == 1){
                    //this.listWinner.add(win);
                    if(winner.isPlayer() == 1){
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
        buildRankingList(winners);
    }

    private void checkEmptyRanking() {
        List<Winner> winners = db.winnerDao().getAll();
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

    private void buildRankingList(List<Winner> winners) {
        ListView listView = (ListView) findViewById(R.id.ranking);

            ArrayAdapter<Winner> adapter = new ArrayAdapter<Winner>(
                    this, android.R.layout.simple_list_item_1, winners
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

    public void setPlayerName(View view){
        List<Winner> winners = db.winnerDao().getAll();
        TextView input = (TextView) findViewById(R.id.inputName);
        Button button = (Button) findViewById(R.id.buttonEnviarNome);
        Winner winner = db.winnerDao().getLastWinner();
        //for(Winner winner : winners){
            if(winner.isPlayer()==1){
                //winner.setIsLast(0);
                if(!input.getText().toString().isEmpty()) {
                    winner.setNome(input.getText().toString());
                }
                else{
                    winner.setNome("Player");
                }
            }
        //}
        db.winnerDao().updatePlayerName(winner);
        winners = db.winnerDao().getAll();
        buildRankingList(winners);
        input.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
    }

    public void clearRanking(){
        //listWinner = new ArrayList<>();
        db.winnerDao().clearTable();
        List<Winner> winners = db.winnerDao().getAll();
        checkEmptyRanking();
        buildRankingList(winners);

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