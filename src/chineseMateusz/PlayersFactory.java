package chineseMateusz;

public class PlayersFactory {

    private static PlayersFactory playersFactory;

    private PlayersFactory() {
    }

    public Player createPlayer(boolean isHuman, String name, Pawn.PlayerColor playerColor) {
        if(isHuman) {
            return new Human(name, playerColor);
        } else {
            return new Bot(name, playerColor);
        }
    }

    public static PlayersFactory getInstance() {

        if(playersFactory == null) {
            playersFactory = new PlayersFactory();
        }

        return playersFactory;
    }
}
