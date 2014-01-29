/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package baccarattrainer;

import com.bryanbalmer.playingcards.*;

/**
 * A dealer to help handle Deck and flow of game.
 * @author Bryan
 */
public class Dealer {
    Deck deck;
    BaccPlayer player;
    BaccPlayer banker;
    
    public Dealer() {
        deck = new Deck();
        deck.shuffleDeck();
        player = new BaccPlayer();
        banker = new BaccPlayer();
    }
    
    // Each new round, hands are reset, the deck count is checked
    // and new cards are distributed
    public void newRoundStarted() {
        resetHands();
        checkRemainingCards();
        dealFirstCards();
    }
    
    // ----Methods for deck of cards----
    private Card dealCard() {
        return deck.drawNextCard();
    }
    
    private void checkRemainingCards() {
        if (deck.cardsLeft() < 6) {
            deck = new Deck();
            deck.shuffleDeck();
        }
    }
    
    // ----Methods for Baccarat game----
    private void dealFirstCards() {
        player.setInitialCards(dealCard(), dealCard());
        banker.setInitialCards(dealCard(), dealCard());
    }
    
    // Returns cards to set up images
    public Card getPlayersFirstCard() { return player.getCard(1); }
    public Card getPlayersSecondCard() { return player.getCard(2); }
    public Card getBankersFirstCard() { return banker.getCard(1); }
    public Card getBankersSecondCard() { return banker.getCard(2); }
    
    public Card dealPlayerThirdCard() {
        player.setThirdCard(dealCard());
        return player.getCard(3);
    }
    
    public Card dealBankerThirdCard() {
        banker.setThirdCard(dealCard());
        return banker.getCard(3);
    }
    
    // Returns scores
    public int playerScore() { return player.finalScore(); }
    public int bankerScore() { return banker.finalScore(); }
    
    // Clear cards in players hands
    private void resetHands() {
        player.resetCards();
        banker.resetCards();
    }
    
    // ----Drawing rule methods----
    // Determine if player or banker has a natural
    // A natural is an initial score of 8 or 9
    // Neither player nor banker draws if there is a natural
    private boolean natural() {
        if ((player.initialScore() == 8) ||
            (player.initialScore() == 9) ||
            (banker.initialScore() == 8) ||
            (banker.initialScore() == 9)) {
            
            return true;
        }
        else {
            return false;
        }
    }
    
    // Determine if player draws a third card
    // Player draws if initial score is 0-5
    // and banker does not have a natural
    public boolean playerDraws() {
        if (natural()) {
            return false;
        }
        else if (player.initialScore() > 5) {
            return false;
        }
        else {
            return true;
        }
    }
    
    /*
    Banker rules are more complex than player's, they are as follows
    If there is a natural, banker does not draw.
    If player's score is 6 or 7, banker draws on a score of 0-5, otherwise stands.
    If the player draws, banker will follow the "Third Card Rule" and draw
    based on the player's third card and banker's initial score:
        Player card is 2 or 3: Banker draws on total of 0-4
        Player card is 4 or 5: Banker draws on total of 0-5
        Player card is 6 or 7: Banker draws on total of 0-6
        Player card is 8: Banker draws on total of 0-2
        Player card is 1, 9 or 0: Banker draws on total of 0-3
        Otherwise Banker stands
    
    Third Card Rule can be simplified into the equation:
        result = floor ( playerThirdCard / 2 ) + 3
        where 8 is given a value of -2 and 9 a value of -1.
        If Banker's initial total is less than or equal to the result
        then Banker draws, otherwise stands.
    */
    public boolean bankerDraws() {
        if (natural()) { return false; } 
        
        else if ((player.finalScore() == 6) ||
                 (player.finalScore() == 7)) {
            
            if (banker.initialScore() <= 5) { return true; }
            else { return false; } }
        
        else return thirdCardRule();
    }
    
    private boolean thirdCardRule() {
        int result;
        
        switch (player.getCardValue(3)){
            // Assigning 9 and 8 to their results directly
            case 9: 
                result = 3;
                break;
            case 8: 
                result = 2;
                break;
            // All other values will be computed
            default:
                result = ((int) Math.floor(player.getCardValue(3) / 2)) + 3;
        }
        
        if (banker.initialScore() <= result)
            return true;
        else
            return false;
    }
}
