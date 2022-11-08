import java.util.Locale;

public class PlayerFactory {

    public Player buildPlayer(String playerName) {
        switch(playerName.toLowerCase(Locale.ENGLISH)) {
            case "human":
                return new HumanPlayer();
            case "whatever":
                return new WhateverPlayer();
            case "clever":
                return new CleverPlayer();
            case "genius":
                return new GeniusPlayer();
            default:
                return null;
        }
    }
}