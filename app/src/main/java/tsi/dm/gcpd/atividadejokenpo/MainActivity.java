package tsi.dm.gcpd.atividadejokenpo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Winner> winnerList = new ArrayList<>();
    private Winner winner;
    //private WinnerDatabase db;
    private FirebaseFirestore db;
    private final String TAG = "DEBUG ----";
    private Vibrator vibrator;
    private MediaPlayer mp = null;
    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.winner = new Winner();
        this.db = FirebaseFirestore.getInstance();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setContentView(R.layout.activity_main);
        Log.d("winner create", winner.toString());
        //createChannel();
        View view = View.inflate(getApplicationContext(), R.layout.activity_main, null);
//        db.collection("ranking")
//                .addSnapshotListener(
//                        new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(
//                                    @Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
//                                if (e != null) {
//                                    System.err.println("Listen failed: " + e);
//                                    return;
//                                }
//                                for (DocumentChange dc : snapshots.getDocumentChanges()) {
//                                    switch (dc.getType()) {
//                                        case ADDED:
////                                            System.out.println("New city: " + dc.getDocument().getData());
//                                            PersonalNotification.criaNotificacao("Ranking Jo Ken Po", "Um novo jogador foi adicionado ao ranking", view);
//                                            break;
//                                        case MODIFIED:
//                                            Log.d("Modified: ", dc.getDocument().getData().toString());
//                                            break;
//                                        case REMOVED:
//                                            //limpou = true;
//                                            Log.d("Removed: ", dc.getDocument().getData().toString());
//                                            break;
//                                        default:
//                                            break;
//                                    }
//                                }
//                            }
//                        });
        // Configura a WebView
        webView = (WebView) findViewById(R.id.webView);
        webView.setVisibility(View.INVISIBLE);
        webView.setWebChromeClient(new WebChromeClient());
        // Habilita o JS
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Garante que usará a WebView e não o navegador padrão
        webView.setWebViewClient(new WebViewClient(){
            // Callback que determina quando terminou de ser carregada a
        // WebView, para trocarmos a imagem de carregamento por ela
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ImageView imageView = (ImageView)
                        findViewById(R.id.imageView);
                imageView.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.VISIBLE);
            }
        });

    // Associa a interface (a ser definida abaixo) e carrega o HTML
        webView.addJavascriptInterface(new WebAppInterface(this),"Android");
        webView.loadUrl("file:///android_asset/index.html");
    }
    // Interface para binding Javascript -> Java
    public class WebAppInterface {
        MainActivity mainActivity;
        public WebAppInterface(MainActivity activity) {
            this.mainActivity = activity;
        }
        @JavascriptInterface
        public void androidToast(String msg) {
            Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
        // Chama uma função do JavaScript
            //runJavaScript("oculta_botao();");
        }
    }

    // Possibilita o uso do botão voltar
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    // Executa um comando JavaScript
    public void runJavaScript(final String jsCode){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(jsCode, null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.winner = new Winner();
//        setContentView(R.layout.activity_main);
//        Log.d("winner resume", winner.toString());
    }
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void redirectToRanking(MenuItem menuItem){
        //Intent intent = new Intent(this, Ranking.class);
//        intent.putParcelableArrayListExtra("ranking", winnerList);
        //startActivity(intent);
        webView.loadUrl("file:///android_asset/ranking.html");
//        runJavaScript("redirectToRanking();");
    }
//
    public void clearScore(View view){
        TextView placarHumano = (TextView) findViewById(R.id.placarHumano);
        TextView placarComputador = (TextView) findViewById(R.id.placarComputador);
        TextView textView = (TextView) findViewById(R.id.textViewSelecione);
        //Button button = (Button) findViewById(R.id.buttonJogarNovamente);
        Log.d("debug", "placarHumano"+placarHumano.getText());
        Log.d("debug", "placarComputador"+placarComputador.getText());
        placarHumano.setText(R.string.placar_inicio_humano);
        placarComputador.setText(R.string.placar_inicio_humano);
        Log.d("debug", "placarHumano"+placarHumano.getText());
        Log.d("debug", "placarComputador"+placarComputador.getText());
        //clearComputerChoice();
        //clearHighlight();
        textView.setText(getString(R.string.selecione));
        //button.setVisibility(View.INVISIBLE);
    }
//
    public void joKenPo(/*View view*/){
        //ImageButton imageButton = (ImageButton) view;
        //Button button = (Button) findViewById(R.id.buttonJogarNovamente);

//        if(button.getVisibility() == View.INVISIBLE){
//            highlightButton(view);
//            int playerChoice = view.getId();
//            String computerChoice = getComputerChoice();
//            Log.d("debug", "escolha do computador: " + computerChoice);
//            int winner = getWinner(playerChoice, computerChoice);
//            if(winner >= 0){
//                updateScore(winner);
//                if(winner == 0){
//                    if(mp != null){
//                        mp.stop();
//                        mp.release();
//                    }
//                    mp = MediaPlayer.create(getApplicationContext(), R.raw.acerto);
//                    mp.start();
//                }
//                else{
//                    if(mp != null){
//                        mp.stop();
//                        mp.release();
//                    }
//                    mp = MediaPlayer.create(getApplicationContext(), R.raw.erro);
//                    mp.start();
//                }
//            }
//            updateMessage(winner);
//            button.setVisibility(View.VISIBLE);
//        }
    }
//
    public void playAgain(View view){
        TextView textView = (TextView) findViewById(R.id.textViewSelecione);
        textView.setText(getString(R.string.selecione));
        //clearHighlight();
        view.setVisibility(View.INVISIBLE);
    }
//
    private void highlightButton(View view) {
        //clearHighlight();
        ArrayList<ImageButton> imageButtons = getImageButtons();
        for(ImageButton imageButton : imageButtons){
            if(imageButton == view) {
                imageButton.setBackgroundColor(getColor(R.color.dark_gray));
                imageButton.setColorFilter(getColor(R.color.white));
            }
        }
    }
//
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
//
    private void clearHighlight(){
        ArrayList<ImageButton> imageButtons = getImageButtons();
        for(ImageButton imageButton : imageButtons){
            imageButton.setBackgroundColor(getColor(R.color.white));
            imageButton.setColorFilter(getColor(R.color.black));
        }
    }
//
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
//
//    private void updateScore(int winner) {
//        final int player = 0;
//        final int computer = 1;
//        int score = 0;
//
//        TextView placarHumano = (TextView) findViewById(R.id.placarHumano);
//        TextView placarComputador = (TextView) findViewById(R.id.placarComputador);
//        Log.d("debug", "placarHumano"+placarHumano.getText());
//        Log.d("debug", "placarComputador"+placarComputador.getText());
//
//        switch (winner){
//            case player:
//                score = Integer.valueOf(placarHumano.getText().toString());
//                score++;
//                placarHumano.setText(String.valueOf(score));
//                break;
//            case computer:
//                score = Integer.valueOf(placarComputador.getText().toString());
//                score++;
//                placarComputador.setText(String.valueOf(score));
//                break;
//        }
//
//        if(score == 5){
//            buildRanking(winner, placarHumano, placarComputador);
//        }
//
//    }
//
    private void buildRanking(int winnerGame, TextView placarHumano, TextView placarComputador) {
        final int player = 0;
        final int computer = 1;
        int score = 0;
        Integer isPlayer = 0;
        final VibrationEffect vibrationEffect;
        long[] pattern;
        switch (winnerGame){
            case player:
                score = Integer.valueOf(placarHumano.getText().toString()) - Integer.valueOf(placarComputador.getText().toString());
                isPlayer = 1;
                winner.setNome("Player");
                pattern = new long[]{0,100, 60, 100, 60, 100};
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    vibrationEffect = VibrationEffect.createWaveform(pattern, -1);

                    // it is safe to cancel other vibrations currently taking place
                    vibrator.cancel();

                    vibrator.vibrate(vibrationEffect);
                }
                else{
                    vibrator.cancel();
                    vibrator.vibrate(pattern, -1);
                }
                break;
            case computer:
                score = Integer.valueOf(placarComputador.getText().toString()) - Integer.valueOf(placarHumano.getText().toString());
                pattern = new long[]{0,300};
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    vibrationEffect = VibrationEffect.createWaveform(pattern, -1);

                    // it is safe to cancel other vibrations currently taking place
                    vibrator.cancel();

                    vibrator.vibrate(vibrationEffect);
                }
                else{
                    vibrator.cancel();
                    vibrator.vibrate(pattern, -1);
                }
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
        //db.winnerDao().insert(winner);

        db.collection("ranking")
                .add(winner)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        //winnerList.add(winner);
        //Log.d("winners main", winnerList.toString());
        Intent intent = new Intent(this, Ranking.class);
        //intent.putParcelableArrayListExtra("ranking", winnerList);
        startActivity(intent);
    }
//
    private int getWinner(int playerChoice, String computerChoice) {
        final int player = 0;
        final int computer = 1;
        final int draw = -1;
        int winner = draw;

//        switch (playerChoice){
//            case R.id.imagePedra:
//                if(computerChoice == getString(R.string.pedra)){
//                    winner = draw;
//                }
//                else if(computerChoice == getString(R.string.papel)){
//                    winner = computer;
//                }
//                else {
//                    winner = player;
//                }
//                break;
//            case R.id.imagePapel:
//                if(computerChoice == getString(R.string.pedra)){
//                    winner =  player;
//                }
//                else if(computerChoice == getString(R.string.papel)){
//                    winner =  draw;
//                }
//                else {
//                    winner =  computer;
//                }
//                break;
//            case R.id.imageTesoura:
//                if(computerChoice == getString(R.string.pedra)){
//                    winner =  computer;
//                }
//                else if(computerChoice == getString(R.string.papel)){
//                    winner = player;
//                }
//                else {
//                    winner = draw;
//                }
//                break;
//        }
        return winner;
    }
//
//
//    private String getComputerChoice(){
//        Switch switchMode = new Switch(getApplicationContext());//(Switch) findViewById(R.id.isConstantMode);
//        Random random = new Random();
//        int res = switchMode.isChecked() ? 0 : random.nextInt(3);
//        Log.d("debug", "random number: " + res);
//        ImageView imageView = (ImageView) findViewById(R.id.jogadaComp);
//        switch (res){
//            case 0:
//                imageView.setImageResource(R.drawable.fist);
//                return getString(R.string.pedra);
//            case 1:
//                imageView.setImageResource(R.drawable.hand);
//                return getString(R.string.papel);
//            default:
//                imageView.setImageResource(R.drawable.scissor);
//                return getString(R.string.tesoura);
//        }
//
//    }
//
    private void clearComputerChoice(){
        ImageView imageView = (ImageView) findViewById(R.id.jogadaComp);
        imageView.setImageResource(0);
    }
//
    public void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "notific";
            String description = "test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}