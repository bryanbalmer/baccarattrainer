package baccarattrainer;

import com.bryanbalmer.playingcards.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for FXML Document.  Main screen for game.
 * @author Bryan
 */
public class FXMLDocumentController implements Initializable {
    
        private Label label;
    @FXML
    private ImageView imgBankerCard1;
    @FXML
    private ImageView imgBankerCard2;
    @FXML
    private ImageView imgBankerCard3;
    @FXML
    private ImageView imgPlayerCard3;
    @FXML
    private ImageView imgPlayerCard2;
    @FXML
    private ImageView imgPlayerCard1;
    @FXML
    private Button btnStartGame;
    @FXML
    private Button btnPlayerDraw;
    @FXML
    private Button btnPlayerStand;
    @FXML
    private Button btnBankerDraw;
    @FXML
    private Button btnBankerStand;
    @FXML
    private Label labelStreak;
    @FXML
    private Label labelIncorrect;
    @FXML
    private Label labelCorrect;
    @FXML
    private TextField tfCorrect;
    @FXML
    private TextField tfIncorrect;
    @FXML
    private TextField tfStreak;
    @FXML
    private TextField tfFeedback;
    
        
    private Dealer dealer;
    
    private int correctScore;
    private int incorrectScore;
    private int streak;
    @FXML
    private Label labelBanker;
    @FXML
    private Label labelPlayer;
    @FXML
    private TextField tfPlayerScore;
    @FXML
    private TextField tfBankerScore;
    @FXML
    private Label labelPlayerWin;
    @FXML
    private Label labelBankerWin;
    @FXML
    private Label labelTie;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dealer = new Dealer();
        correctScore = 0;
        incorrectScore = 0;
        streak = 0;
    }    
    
    @FXML
    private void btnStartGame_Clicked(ActionEvent event) {
        
        newRound();

        // Set up images
        setInitialCardImages();

        // Disable StartGame Button
        // and Enable Player Buttons
        btnStartGame.setDisable(true);
        btnPlayerDraw.setDisable(false);
        btnPlayerStand.setDisable(false);
        
        updateGameScores();
    }
    // When a button is pressed, check the answer and set up
    // appropriate buttons for game
    @FXML
    private void btnPlayerDraw_Clicked(ActionEvent event) {
        checkPlayerAnswer(event);
        bankerTurn();
    }

    @FXML
    private void btnPlayerStand_Clicked(ActionEvent event) {
        checkPlayerAnswer(event);
        bankerTurn();
    }

    @FXML
    private void btnBankerDraw_Clicked(ActionEvent event) {
        checkBankerAnswer(event);
        endRound();
    }

    @FXML
    private void btnBankerStand_Clicked(ActionEvent event) {
        checkBankerAnswer(event);
        endRound();
    }
    
    // Gets the cards initially dealt to set their images
    private void setInitialCardImages() {
        imgPlayerCard1.setImage( cardImage( dealer.getPlayersFirstCard() ));
        imgPlayerCard2.setImage( cardImage( dealer.getPlayersSecondCard() ));
        imgBankerCard1.setImage( cardImage( dealer.getBankersFirstCard() ));
        imgBankerCard2.setImage( cardImage( dealer.getBankersSecondCard() ));
    }
    
    // Returns image for a given card
    private Image cardImage(Card card) {
        String image = "/baccarattrainer/cardimages/" +
                card.getRank().getName() + "_" +
                card.getSuit().getName() + ".png";
        Image cardImage = new Image(image);
        return cardImage;
    }
    
    // Methods for game scorekeeping and flow
    private void checkPlayerAnswer(ActionEvent event) {
        // Give Player a third card if they are to draw
        if (dealer.playerDraws()){
            imgPlayerCard3.setImage(cardImage(dealer.dealPlayerThirdCard()));
            updateGameScores();
        }
        
        // Update score is passed appropriate boolean based on button pressed
        // and correct action
        updateScore(((dealer.playerDraws() && (event.getSource() == btnPlayerDraw)) ||
                    (!dealer.playerDraws() && (event.getSource() == btnPlayerStand))));
    }
    
    private void checkBankerAnswer(ActionEvent event) {
        // Give Banker a third card if they are to draw
        if (dealer.bankerDraws()){
            imgBankerCard3.setImage(cardImage(dealer.dealBankerThirdCard()));
            updateGameScores();
        }
        
        // Update score is passed appropriate boolean based on button pressed
        // and correct action
        updateScore(((dealer.bankerDraws() && (event.getSource() == btnBankerDraw)) ||
                    (!dealer.bankerDraws() && (event.getSource() == btnBankerStand))));
    }
    
    // Player buttons disabled and Banker's enabled on Banker's turn
    private void bankerTurn() {
        btnPlayerDraw.setDisable(true);
        btnPlayerStand.setDisable(true);
        btnBankerDraw.setDisable(false);
        btnBankerStand.setDisable(false);
    }
    
    // New round started, reset images and winner labels.
    // Let dealer know new round started
    private void newRound() {
        imgPlayerCard1.setImage(null);
        imgPlayerCard2.setImage(null);
        imgPlayerCard3.setImage(null);
        
        imgBankerCard1.setImage(null);
        imgBankerCard2.setImage(null);
        imgBankerCard3.setImage(null);
        
        labelPlayerWin.setVisible(false);
        labelBankerWin.setVisible(false);
        labelTie.setVisible(false);
        
        
        dealer.newRoundStarted();
    }
    
    // Round is over.  Enable start game button and disable Banker's buttons
    // Determine winner
    private void endRound() {
        btnStartGame.setDisable(false);
        btnBankerDraw.setDisable(true);
        btnBankerStand.setDisable(true);
        determineWinner();
    }
    
    // Receives a boolean and updates the scoreboxes accordingly
    private void updateScore(boolean correct) {
        if (correct) {
            tfCorrect.setText(Integer.toString(++correctScore));
            tfStreak.setText(Integer.toString(++streak));
            tfFeedback.setText("Correct!");
        } else {
            tfIncorrect.setText(Integer.toString(++incorrectScore));
            streak = 0;
            tfStreak.setText(Integer.toString(streak));
            tfFeedback.setText("Wrong!");
        }
    }
    
    // Update Player and Banker scores
    private void updateGameScores() {
        tfPlayerScore.setText(Integer.toString(dealer.playerScore()));
        tfBankerScore.setText(Integer.toString(dealer.bankerScore()));
    }
    
    // Determines winner of game
    private void determineWinner() {
        int playerScore = dealer.playerScore();
        int bankerScore = dealer.bankerScore();
        
        if (playerScore > bankerScore)
            labelPlayerWin.setVisible(true);
        if (bankerScore > playerScore)
            labelBankerWin.setVisible(true);
        if (bankerScore == playerScore)
            labelTie.setVisible(true);
    }
}
