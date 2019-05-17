import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;


public class A3 {



    char[] lowercase = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    char[] uppercase = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    ArrayList<ArrayList<ActorNode>> actorList = new ArrayList<ArrayList<ActorNode>>();
    ArrayList<ArrayList<MovieNode>> movieList = new ArrayList<ArrayList<MovieNode>>();

    public A3() {

        for (int i = 0; i < lowercase.length + 1; i++) {

            actorList.add(new ArrayList<ActorNode>());
            movieList.add(new ArrayList<MovieNode>());

        }
    }

    private void CSVParser(String filePath) {

        File file = new File(filePath);

        String parsedText = "";
        String movieTemp = "";
        String actorTemp = "";

        boolean commaFound = false;
        boolean bracketFound = false;

        boolean characterFound = false;
        boolean nameFound = false;

        long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();

        try {

            ActorNode currentActorNode = null;
            MovieNode currentMovieNode = null;

            Scanner scan = new Scanner(file);
            float howManyTokensHaveBeenParsed = 0;

            while (scan.hasNext()) {


                String currentToken = scan.next();

                for (int i = 0; i < currentToken.length(); i++) {

                    parsedText += currentToken.charAt(i);

                    if (currentToken.charAt(i) == '[') {
                        bracketFound = true;
                    }
                    if (currentToken.charAt(i) == ']') {
                        bracketFound = false;
                    }

                    if (bracketFound == false) {

                        if (commaFound == true && currentToken.charAt(i) == '"') {

                            commaFound = false;

                            if (parsedText.length() > 0 && parsedText.length() < 250) {

                                movieTemp = stringReformat(parsedText);

                                if (movieTemp.length() > 0) {

                                    currentMovieNode = createMovieNode(movieTemp);

                                }

                                parsedText = "";

                            }

                        } else if (commaFound == true) {


                            commaFound = false;
                            parsedText = "";
                            parsedText += currentToken.charAt(i);

                        }

                        if (currentToken.charAt(i) == ',') {

                            commaFound = true;

                        }
                    }
                }


                if (currentToken.length() > 1) {

                    if (currentToken.equals("\"\"character\"\":")) {

                        characterFound = true;

                    }
                    if (currentToken.equals("\"\"name\"\":") && characterFound) {

                        characterFound = false;
                        actorTemp = "";
                        nameFound = true;

                    } else if (nameFound == true
                            && currentToken.charAt(currentToken.length() - 1) != ','
                            && currentToken.charAt(currentToken.length() - 2) != ']') {

                        actorTemp += currentToken + " ";

                    } else if (nameFound == true) {

                        nameFound = false;

                        actorTemp += currentToken;
                        actorTemp = stringReformat(actorTemp);

                        currentActorNode = createActorNode(actorTemp);

                        if (movieTemp.length() > 0 && actorTemp.length() > 0) {

                            createVertex(currentMovieNode, currentActorNode);

                        }
                    }

                    howManyTokensHaveBeenParsed += 1f;
                    currentTime = System.currentTimeMillis();

                    if (currentTime - startTime > 1000) {

                        startTime = System.currentTimeMillis();
                        loadingBar(howManyTokensHaveBeenParsed);

                    }
                }
            }

            scan.close();
            System.out.print("\rParsing Complete (100%)                                            ");

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            System.out.println("\nPlease input the full path");
            System.exit(1);

        }
    }

    private ActorNode returnLastActor(String actor) {

        for (int i = 0; i < lowercase.length; i++) {

            if (actor.charAt(0) == lowercase[i]) {

                if (actorList.get(i).get(actorList.get(i).size() - 1).actorName.equals(actor)) {

                    return actorList.get(i).get(actorList.get(i).size() - 1);

                }

                for (int j = 0; j < actorList.get(i).size(); j++) {

                    if (actorList.get(i).get(j).actorName.equals(actor)) {

                        return actorList.get(i).get(j);

                    }
                }
            }
        }


        for (int j = 0; j < actorList.get(lowercase.length).size(); j++) {

            if (actorList.get(lowercase.length).get(j).actorName.equals(actor)) {

                return actorList.get(lowercase.length).get(j);

            }
        }

        return null;

    }

    private boolean doesActorExist(String actor) {

        actor = stringReformat(actor);

        for (int i = 0; i < lowercase.length; i++) {

            if (actor.charAt(0) == lowercase[i]) {

                for (int j = 0; j < actorList.get(i).size(); j++) {

                    if (actorList.get(i).get(j).actorName.equals(actor)) {

                        return true;

                    }
                }

                return false;

            }
        }

        for (int j = 0; j < actorList.get(lowercase.length).size(); j++) {

            if (actorList.get(lowercase.length).get(j).actorName.equals(actor)) {

                return true;

            }
        }

        return false;

    }

    private ActorNode createActorNode(String actor) {

        if (doesActorExist(actor)) {

            return returnLastActor(actor);

        } else {

            for (int i = 0; i < lowercase.length; i++) {

                if (actor.charAt(0) == lowercase[i]) {

                    ActorNode temp = new ActorNode(actor);
                    actorList.get(i).add(temp);
                    return temp;

                }
            }

            ActorNode temp = new ActorNode(actor);
            actorList.get(lowercase.length).add(temp);
            return temp;

        }
    }

    private MovieNode createMovieNode(String movie) {

        for (int i = 0; i < lowercase.length; i++) {

            if (movie.charAt(0) == lowercase[i]) {

                MovieNode temp = new MovieNode(movie);
                movieList.get(i).add(new MovieNode(movie));
                return temp;

            }
        }

        MovieNode temp = new MovieNode(movie);
        movieList.get(lowercase.length).add(new MovieNode(movie));
        return temp;

    }

    private void createVertex(MovieNode movie, ActorNode actor) {

        actor.addMovie(movie);
        movie.addActor(actor);

    }

    private void pathBetween(String actor1, String actor2) {

        ArrayList<ActorNode> path = pathfinder(returnLastActor(actor1), returnLastActor(actor2));

        if (path == null) {

            System.out.println("No path has been found.");

        } else {

            for (int i = 0; i < path.size(); i++) {

                System.out.print(path.get(i));

                if (i + 1 != path.size()) {
                    System.out.print(" --> ");
                }
            }
        }
    }

    private ArrayList<ActorNode> pathfinder(ActorNode actor1, ActorNode actor2) {


        ArrayList<ActorNode> actorsToSort = new ArrayList<ActorNode>();
        ArrayList<ActorNode> tempActors = new ArrayList<ActorNode>();

        ArrayList<ArrayList<ActorNode>> allPaths = new ArrayList<ArrayList<ActorNode>>();
        ArrayList<ArrayList<ActorNode>> tempAllPaths = new ArrayList<ArrayList<ActorNode>>();

        boolean unreachablePath = false;

        actorsToSort.add(actor1);

        ArrayList temp = new ArrayList();
        temp.add(actor1);
        allPaths.add(temp);

        while (!unreachablePath) {

            if (allPaths.size() > 0) {

                for (int k = 0; k < actorsToSort.size(); k++) {

                    for (int i = 0; i < actorsToSort.get(k).movieList.size(); i++) {

                        for (int j = 0; j < actorsToSort.get(k).movieList.get(i).actorList.size(); j++) {

                            if (actorsToSort.get(k).movieList.get(i).actorList.get(j).actorName.equals(actor2.actorName)) {

                                temp = (ArrayList) allPaths.get(k).clone();
                                temp.add(actorsToSort.get(k).movieList.get(i).actorList.get(j));
                                tempAllPaths.add(temp);

                                return tempAllPaths.get(tempAllPaths.size() - 1);

                            } else {

                                tempActors.add(actorsToSort.get(k).movieList.get(i).actorList.get(j));

                                temp = (ArrayList) allPaths.get(k).clone();
                                temp.add(actorsToSort.get(k).movieList.get(i).actorList.get(j));
                                tempAllPaths.add(temp);

                            }
                        }
                    }
                }

                actorsToSort = tempActors;
                tempActors = new ArrayList<ActorNode>();

                allPaths = tempAllPaths;
                tempAllPaths = new ArrayList<ArrayList<ActorNode>>();
            } else {

                unreachablePath = true;

            }
        }

        return null;

    }

    private String stringReformat(String input) {

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {

            if (input.charAt(i) == ' ') {

                output.append(' ');

            } else if (input.charAt(i) != '"' && input.charAt(i) != ':' && input.charAt(i) != ','
                    && input.charAt(i) != '{' && input.charAt(i) != '}' && input.charAt(i) != '.'
                    && input.charAt(i) != '\'' && input.charAt(i) != '!' && input.charAt(i) != '&'
                    && input.charAt(i) != '?' && input.charAt(i) != '[' && input.charAt(i) != ']') {

                for (int j = 0; j < uppercase.length; j++) {

                    if (input.charAt(i) == uppercase[j] || input.charAt(i) == lowercase[j]) {

                        output.append(lowercase[j]);
                        break;

                    }
                }

                for (char number : numbers) {

                    if (input.charAt(i) == number) {

                        output.append(number);
                        break;

                    }
                }
            }
        }

        return output.toString();

    }

    private void loadingBar(float current) {

        String percent = Float.toString(current / 3543753f);

        if (percent.length() == 3) {

            percent = percent.substring(2, 3);

        } else {

            percent = percent.substring(2, 4);

        }

        System.out.print("\rParsing " + current + " of 3543753.0 (" + percent + "%)");
    }


    public static void main(String[] args) {

        String filePath = "";

        if (args.length == 0) {

            filePath = "tmdb_5000_credits.csv";

        } else {

            filePath = args[0];

        }

        A3 handler = new A3();
        String input = "";
        String actor1 = "";
        String actor2 = "";
        boolean breaker = false;

        handler.CSVParser(filePath);

        System.out.println("\nInput the names of two actors: ");

        while (breaker == false) {

            Scanner scan = new Scanner(System.in);

            while (breaker == false) {

                System.out.print("\n Actor 1 name: ");

                input = scan.nextLine();
                if (input.equals("exit")) {
                    breaker = true;
                    break;
                }
                if (input.length() > 0 && handler.doesActorExist(input)) {

                    actor1 = input;
                    break;
                } else {

                    System.out.print("\n Actor not found. ");
                }
            }
            while (breaker == false) {
                System.out.print("\n Actor 2 name: ");
                input = scan.nextLine();
                if (input.equals("exit")) {
                    breaker = true;
                    break;
                }
                if (input.length() > 0 && handler.doesActorExist(input)) {

                    actor2 = input;
                    break;
                } else {
                    System.out.print("\n Actor not found. ");

                }
            }
            if (breaker == false) {

                System.out.print("\n Path between " + actor1 + " and " + actor2 + ": ");

                handler.pathBetween(handler.stringReformat(actor1), handler.stringReformat(actor2));

            }
        }
    }
}