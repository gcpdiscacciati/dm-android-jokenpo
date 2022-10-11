package tsi.dm.gcpd.atividadejokenpo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Winner> winnerList = new ArrayList<>();
    private Winner winner;
    private WinnerDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.winner = new Winner();
        this.db = WinnerDatabase.getDatabase(this);
        setContentView(R.layout.activity_main);
        Log.d("winner create", winner.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.winner = new Winner();
        setContentView(R.layout.activity_main);
        Log.d("winner resume", winner.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void redirectToRanking(MenuItem menuItem){
        Intent intent = new Intent(this, Ranking.class);
//        intent.putParcelableArrayListExtra("ranking", winnerList);
        startActivity(intent);
    }

    public void clearScore(View view){
        TextView placarHumano = (TextView) findViewById(R.id.placarHumano);
        TextView placarComputador = (TextView) findViewById(R.id.placarComputador);
        TextView textView = (TextView) findViewById(R.id.textViewSelecione);
        Button button = (Button) findViewById(R.id.buttonJogarNovamente);
        Log.d("debug", "placarHumano"+placarHumano.getText());
        Log.d("debug", "placarComputador"+placarComputador.getText());
        placarHumano.setText(R.string.placar_inicio_humano);
        placarComputador.setText(R.string.placar_inicio_humano);
        Log.d("debug", "placarHumano"+placarHumano.getText());
        Log.d("debug", "placarComputador"+placarComputador.getText());
        clearComputerChoice();
        clearHighlight();
        textView.setText(getString(R.string.selecione));
        button.setVisibility(View.INVISIBLE);
    }

    public void joKenPo(View view){
        ImageButton imageButton = (ImageButton) view;
        Button button = (Button) findViewById(R.id.buttonJogarNovamente);
        if(button.getVisibility() == View.INVISIBLE){
            highlightButton(view);
            int playerChoice = view.getId();
            String computerChoice = getComputerChoice();
            Log.d("debug", "escolha do computador: " + computerChoice);
            int winner = getWinner(playerChoice, computerChoice);
            if(winner >= 0){
                updateScore(winner);
            }
            updateMessage(winner);
            button.setVisibility(View.VISIBLE);
        }
    }

    public void playAgain(View view){
        TextView textView = (TextView) findViewById(R.id.textViewSelecione);
        textView.setText(getString(R.string.selecione));
        clearHighlight();
        view.setVisibility(View.INVISIBLE);
    }

    private void highlightButton(View view) {
        clearHighlight();
        ArrayList<ImageButton> imageButtons = getImageButtons();
        for(ImageButton imageButton : imageButtons){
            if(imageButton == view) {
                imageButton.setBackgroundColor(getColor(R.color.dark_gray));
                imageButton.setColorFilter(getColor(R.color.white));
            }
        }
    }

    private ArrayList<ImageButton> getImageButtons() {
        ArrayList<ImageButton> imageButtons = new ArrayList<>();
        ImageButton imagePedra = (ImageButton) findViewById(R.id.imagePedra);
        ImageButton imagePapel = (ImageButton) findViewById(R.id.imagePapel);
        ImageButton imageTesoura = (ImageButton) findViewById(R.id.imageTesoura);
        imageButtons.add(imagePedra);
        imageButtons.add(imagePapel);
        imageButtons.add(imageTesoura);
        return imageButtons;
    }

    private void clearHighlight(){
        ArrayList<ImageButton> imageButtons = getImageButtons();
        for(ImageButton imageButton : imageButtons){
            imageButton.setBackgroundColor(getColor(R.color.white));
            imageButton.setColorFilter(getColor(R.color.black));
        }
    }

    private void updateMessage(int winner) {
        final int player = 0;
        final int computer = 1;

        TextView textView = (TextView) findViewById(R.id.textViewSelecione);

        switch (winner){
            case player:
                textView.setText(getString(R.string.venceu));
                break;
            case computer:
                textView.setText(getString(R.string.perdeu));
                break;
            default:
                textView.setText(getString(R.string.empate));
                break;
        }
    }

    private void updateScore(int winner) {
        final int player = 0;
        final int computer = 1;
        int score = 0;

        TextView placarHumano = (TextView) findViewById(R.id.placarHumano);
        TextView placarComputador = (TextView) findViewById(R.id.placarComputador);
        Log.d("debug", "placarHumano"+placarHumano.getText());
        Log.d("debug", "placarComputador"+placarComputador.getText());

        switch (winner){
            case player:
                score = Integer.valueOf(placarHumano.getText().toString());
                score++;
                placarHumano.setText(String.valueOf(score));
                break;
            case computer:
                score = Integer.valueOf(placarComputador.getText().toString());
                score++;
                placarComputador.setText(String.valueOf(score));
                break;
        }

        if(score == 5){
            buildRanking(winner, placarHumano, placarComputador);
        }

    }

    private void buildRanking(int winnerGame, TextView placarHumano, TextView placarComputador) {
        final int player = 0;
        final int computer = 1;
        int score = 0;
        Integer isPlayer = 0;

        switch (winnerGame){
            case player:
                score = Integer.valueOf(placarHumano.getText().toString()) - Integer.valueOf(placarComputador.getText().toString());
                isPlayer = 1;
                winner.setNome("Player");
                break;
            case computer:
                score = Integer.valueOf(placarComputador.getText().toString()) - Integer.valueOf(placarHumano.getText().toString());
                break;
        }
        for(Winner win : winnerList){
            win.setIsLast(0);
        }
        winner.setEndTime(System.currentTimeMillis());
        winner.setScore(score);
        winner.setIsPlayer(isPlayer);
        winner.setIsLast(1);
        Log.d("winner", winner.toString());
        db.winnerDao().insert(winner);
        //winnerList.add(winner);
        //Log.d("winners main", winnerList.toString());
        Intent intent = new Intent(this, Ranking.class);
        //intent.putParcelableArrayListExtra("ranking", winnerList);
        startActivity(intent);
    }

    private int getWinner(int playerChoice, String computerChoice) {
        final int player = 0;
        final int computer = 1;
        final int draw = -1;
        int winner = draw;

        switch (playerChoice){
            case R.id.imagePedra:
                if(computerChoice == getString(R.string.pedra)){
                    winner = draw;
                }
                else if(computerChoice == getString(R.string.papel)){
                    winner = computer;
                }
                else {
                    winner = player;
                }
                break;
            case R.id.imagePapel:
                if(computerChoice == getString(R.string.pedra)){
                    winner =  player;
                }
                else if(computerChoice == getString(R.string.papel)){
                    winner =  draw;
                }
                else {
                    winner =  computer;
                }
                break;
            case R.id.imageTesoura:
                if(computerChoice == getString(R.string.pedra)){
                    winner =  computer;
                }
                else if(computerChoice == getString(R.string.papel)){
                    winner = player;
                }
                else {
                    winner = draw;
                }
                break;
        }
        return winner;
    }


    private String getComputerChoice(){
        Switch switchMode = (Switch) findViewById(R.id.isConstantMode);
        Random random = new Random();
        int res = switchMode.isChecked() ? 0 : random.nextInt(3);
        Log.d("debug", "random number: " + res);
        ImageView imageView = (ImageView) findViewById(R.id.jogadaComp);
        switch (res){
            case 0:
                imageView.setImageResource(R.drawable.fist);
                return getString(R.string.pedra);
            case 1:
                imageView.setImageResource(R.drawable.hand);
                return getString(R.string.papel);
            default:
                imageView.setImageResource(R.drawable.scissor);
                return getString(R.string.tesoura);
        }
    }

    private void clearComputerChoice(){
        ImageView imageView = (ImageView) findViewById(R.id.jogadaComp);
        imageView.setImageResource(0);
    }
}