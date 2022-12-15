$(document).ready(function(){
    $('.tooltipped').tooltip();
    //$('.modal').modal();
});

var onModalClose = function() {
    clearScore();
    $('#first_name')[0].value="";
};

var modal = document.querySelector('.modal');
M.Modal.init(modal,{
    onCloseEnd: onModalClose // Callback für Modal schließen.
});

const pedra = "pedra";
const papel = "papel";
const tesoura = "tesoura";
const venceu = "Você venceu!";
const perdeu = "Você perdeu!";
const empate = "Empate!";
const selecioneJogada = "Selecione sua jogada:";

function joKenPo(playerChoice){
    const player = 0;
    const computer = 1;
    const draw = -1;
    //ImageButton imageButton = (ImageButton) view;
    let btnPlayAgain = $("#btn_jogar_novamente");
    // Button button = (Button) findViewById(R.id.buttonJogarNovamente);
    if(btnPlayAgain.css('display') === 'none'){
        //alert(playerChoice);
    
    //     //highlightButton(view);
        let computerChoice = getComputerChoice();
        //console.log(computerChoice);
        let winner = getWinner(playerChoice, computerChoice);
        if(winner >= 0){
            var result = updateScore(winner);
            // if(winner == 0){
            //     if(mp != null){
            //         mp.stop();
            //         mp.release();
            //     }
            //     mp = MediaPlayer.create(getApplicationContext(), R.raw.acerto);
            //     mp.start();
            // }
            // else{
            //     if(mp != null){
            //         mp.stop();
            //         mp.release();
            //     }
            //     mp = MediaPlayer.create(getApplicationContext(), R.raw.erro);
            //     mp.start();
            // }
        }
        updateMessage(winner);
        btnPlayAgain.css("display", "block");
        if(result){
            if(winner === player){
                var instance = M.Modal.getInstance(modal);
                instance.open();
            }
            else{
                alert("Que pena, você perdeu!");
                clearScore();
            }
        }
    }
    
}

$("#btnEnviarNome").click(function() {
    let name = $('#first_name')[0].value;
    if (name){
        //buildRanking
    }
});

function playAgain(){
    let selecione = $("#selecioneJogada")[0];
    let btnPlayAgain = $("#btn_jogar_novamente");
    selecione.innerText = selecioneJogada;
    btnPlayAgain.css("display", "none");
}

function getComputerChoice(){
    let isConstantMode = $("#switch")[0].checked;
    let result = isConstantMode ? 0 : Math.floor(Math.random() * 3);
    // int res = switchMode.isChecked() ? 0 : random.nextInt(3);
    // Log.d("debug", "random number: " + res);
    // ImageView imageView = (ImageView) findViewById(R.id.jogadaComp);
    let image = $("#jogadaComp")[0];
    // console.log(image);
    switch (result){
        case 0:
            image.src = "img/fist.png";
            return "pedra";
        case 1:
            image.src = "img/hand.png";
            return "papel";
        default:
            image.src = "img/scissor.png";
            return "tesoura";
    }

}

function getWinner(playerChoice, computerChoice) {
    const player = 0;
    const computer = 1;
    const draw = -1;
    let winner = draw;

    switch (playerChoice){
        case pedra:
            if(computerChoice === pedra){
                winner = draw;
            }
            else if(computerChoice === papel){
                winner = computer;
            }
            else {
                winner = player;
            }
            break;
        case papel:
            if(computerChoice === pedra){
                winner =  player;
            }
            else if(computerChoice === papel){
                winner =  draw;
            }
            else {
                winner =  computer;
            }
            break;
        case tesoura:
            if(computerChoice === pedra){
                winner =  computer;
            }
            else if(computerChoice === papel){
                winner = player;
            }
            else {
                winner = draw;
            }
            break;
    }
    return winner;
}

function updateScore(winner) {
    const player = 0;
    const computer = 1;
    let score = 0;

    let placarHumano = $("#placarHumano")[0];
    console.log(placarHumano);
    let placarComputador = $("#placarComputador")[0];

    switch (winner){
        case player:
            score = parseInt(placarHumano.innerText);
            score++;
            placarHumano.innerText = score;
            break;
        case computer:
            score = parseInt(placarComputador.innerText);
            score++;
            placarComputador.innerText = score;
            break;
    }
    console.log(score);
    if(score === 5){
        return true;
    }
    return false;
}

function updateMessage(winner) {
    const player = 0;
    const computer = 1;

    let selecione = $("#selecioneJogada")[0];

    switch (winner){
        case player:
            selecione.innerText=venceu;
            break;
        case computer:
            selecione.innerText=perdeu;    
            
            break;
        default:
            selecione.innerText=empate;
            break;
    }
}

function clearScore(){
    let placarHumano = $("#placarHumano")[0];
    let placarComputador = $("#placarComputador")[0];
    let selecione = $("#selecioneJogada")[0];
    placarHumano.innerText = 0;
    placarComputador.innerText = 0;
    selecione.innerText = selecioneJogada;
    //clearComputerChoice();
    let btnPlayAgain = $("#btn_jogar_novamente");
    btnPlayAgain.css("display", "none");
}



