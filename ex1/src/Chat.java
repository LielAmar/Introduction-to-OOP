import java.util.Scanner;

public class Chat {

    public static void main(String[] args) {
        // Creating all bots
        ChatterBot[] bots = {
                new ChatterBot(
                    "Ash",
                    new String[] {
                            "Uhm, " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + "? Alright: " +
                                ChatterBot.REQUESTED_PHRASE_PLACEHOLDER,
                            ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + " it is!",
                            "I like it! " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER
                    },
                    new String[] {
                            "What do you even want?",
                            "I don't want to say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "!",
                            "I have no idea what " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + " means...",
                            ChatterBot.REQUEST_PREFIX + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER
                    }
                ),
                new ChatterBot(
                    "Pikachu",
                    new String[] {
                            "Hey! " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER + "? Okay :)",
                            "Are you sure? Fine... " + ChatterBot.REQUESTED_PHRASE_PLACEHOLDER
                    },
                    new String[] {
                            "what is " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "?",
                            "I can't say " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + "!",
                            ChatterBot.REQUEST_PREFIX + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER
                    }
                )
        };

        // Generating a conversion between the created bots
        Scanner scanner = new Scanner(System.in);
        String statement = scanner.nextLine();

        while(true) {
            for(ChatterBot bot : bots) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement);
                scanner.nextLine();
            }
        }
    }
}
