$(document).ready(function(){
    $('.tooltipped').tooltip();
});

function joKenPo(id){
    //ImageButton imageButton = (ImageButton) view;
    let btnPlayAgain = $("#btn_jogar_novamente");
    // Button button = (Button) findViewById(R.id.buttonJogarNovamente);
    if(btnPlayAgain.css('display') == 'none'){
        alert(id);
    }
    //     //highlightButton(view);
    //     int playerChoice = view.getId();
    //     String computerChoice = getComputerChoice();
    //     Log.d("debug", "escolha do computador: " + computerChoice);
    //     int winner = getWinner(playerChoice, computerChoice);
    //     if(winner >= 0){
    //         updateScore(winner);
    //         if(winner == 0){
    //             if(mp != null){
    //                 mp.stop();
    //                 mp.release();
    //             }
    //             mp = MediaPlayer.create(getApplicationContext(), R.raw.acerto);
    //             mp.start();
    //         }
    //         else{
    //             if(mp != null){
    //                 mp.stop();
    //                 mp.release();
    //             }
    //             mp = MediaPlayer.create(getApplicationContext(), R.raw.erro);
    //             mp.start();
    //         }
    //     }
    //     updateMessage(winner);
    //     button.setVisibility(View.VISIBLE);
    // }
}