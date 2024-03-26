/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package task1;

import java.util.Scanner;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.ArrayList;

public class Display {

    List<LiteraturePrize> literaturePrizes = new ArrayList<>();

    public Display(List<LiteraturePrize> literaturePrizes) {
        this.literaturePrizes = literaturePrizes;
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("----------------------");
            System.out.println("Literature prize menu");
            System.out.println("----------------------");
            System.out.println("List ................1");
            System.out.println("Select ..............2");
            System.out.println("Search ..............3");
            System.out.println("Exit.................0");
            System.out.println("----------------------");
            System.out.print("Enter choice > ");

            // Check inf an integer
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a valid number. Please enter a number between 0 and 3.");
                System.out.print("Enter choice > ");
                scanner.next(); // get invalid inputs
            }

            int choice = scanner.nextInt();

            // Chech if withing the range
            if (choice < 0 || choice > 3) {
                System.out.println("Invalid choice. Please enter a number between 0 and 3.");
                continue; // Skip loop and ask again
            }

            switch (choice) {
                case 1:
                    System.out.println("List option selected.");
                    System.out.print("Enter start year: ");
                    int startYear = scanner.nextInt();
                    System.out.print("Enter end year: ");
                    int endYear = scanner.nextInt();
                    String winnersList = listPrizeWinners(startYear, endYear, literaturePrizes);
                    System.out.println(winnersList);
                    break;
                case 2:
                    System.out.println("Select option selected.");
                    displayWinnerForYear();
                    break;
                case 3:
                    System.out.println("Search option selected.");
                    searchByGenre();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    // Will never be reached but just in case
                    System.out.println("Unexpected error. Please try again.");
            }
        }
    }
    // This method prints all the winners between two dates given by user

    // Assuming this method is part of a class that has access to a list of LiteraturePrize objects
    public String listPrizeWinners(int startYear, int endYear, List<LiteraturePrize> literaturePrizes) {
        StringBuilder output = new StringBuilder();
        output.append("-------------------------------------------------------\n");
        output.append("| Year | Prize winners (and associated nations)        |\n");
        output.append("-------------------------------------------------------\n");

        List<LiteraturePrize> filteredPrizes = literaturePrizes.stream()
                .filter(prize -> prize.getYear() >= startYear && prize.getYear() <= endYear)
                .collect(Collectors.toList());

        for (LiteraturePrize prize : filteredPrizes) {
            output.append(prize.toString());
        }

        output.append("-------------------------------------------------------\n");
        return output.toString();
    }

    public void displayWinnerForYear() {
        int year = getValidYear(); // usage of a helper method to get valid year
        // Find the LiteraturePrize object for the given year
        LiteraturePrize prizeForYear = literaturePrizes.stream()
                .filter(prize -> prize.getYear() == year)
                .findFirst()
                .orElse(null);

        // does the literaturePrize exist?
        if (prizeForYear == null) {
            System.out.println("No Literature Prize was awarded in the year " + year + ".");
        } else {
            System.out.println("Winner(s) for the year " + year + ":");
            for (Laureate laureate : prizeForYear.winners) {
                // Use the toString from Laureate
                System.out.println(laureate.toString());
            }
        }
    }

    // This method searches for all genres of given type and highlights them
    public void searchByGenre() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter search term for writing genre > ");
        String searchTerm = scanner.nextLine().toLowerCase();
        // Find the Laureate object for the genre
        List<Laureate> matchingLaureates = literaturePrizes.stream()
                .flatMap(prize -> prize.winners.stream())
                .filter(laureate -> laureate.genres.stream().anyMatch(genre -> genre.toLowerCase().contains(searchTerm)))
                .sorted(Comparator.comparing(laureate -> laureate.name))
                .collect(Collectors.toList());

        if (matchingLaureates.isEmpty()) {
            System.out.println("No laureates found for the genre '" + searchTerm + "'.");
            return;
        }

        // Calculate column widths
        int nameWidth = matchingLaureates.stream().mapToInt(laureate -> laureate.name.length()).max().orElse(10);
        int genreWidth = matchingLaureates.stream()
                .map(laureate -> String.join(", ", laureate.genres))
                .mapToInt(String::length)
                .max()
                .orElse(10);

        // Adjust for the uppercased search term and padding
        nameWidth += 2;
        genreWidth = Math.max(genreWidth, searchTerm.length() + 4) + 2;

        // Create header and row format strings using dynamic widths
        String headerFormat = "| %-" + nameWidth + "s | %-" + genreWidth + "s | Year |%n";
        String lineFormat = "| %-" + nameWidth + "s | %-" + genreWidth + "s | %4d |%n";
        String dashLine = createDashLine(nameWidth + genreWidth + 9); // Adjusted total width for the dash line
        
        //Print top line       
        System.out.println(dashLine);
        System.out.printf(headerFormat, "Name", "Genres");
        System.out.println(dashLine);
        
        //Print the contents with genre in CAPS
        for (Laureate laureate : matchingLaureates) {
            String highlightedGenres = laureate.genres.stream()
                    .map(genre -> genre.toLowerCase().contains(searchTerm) ? genre.toUpperCase() : genre)
                    .collect(Collectors.joining(", "));

            int correspondingYear = literaturePrizes.stream()
                    .filter(prize -> prize.winners.contains(laureate))
                    .findFirst()
                    .map(prize -> prize.year)
                    .orElse(0);

            System.out.printf(lineFormat, laureate.name, highlightedGenres, correspondingYear);
            System.out.println(dashLine);
        }
    }
    //Helper metho for ----
    private static String createDashLine(int width) {
        return String.join("", Collections.nCopies(width + 5, "-"));
    }

    //A helper method for getting a valid year
    private int getValidYear() {
        Scanner scanner = new Scanner(System.in);
        int year = 0; // Initialize year

        while (year < 1901 || year > 2022) {
            System.out.println("Enter year of prize (1901 - 2022) > ");
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a valid year! Please enter a number.");
                scanner.next(); // Consume the non-integer input
                System.out.println("Enter year of prize (1901 - 2022) > ");
            }
            year = scanner.nextInt();

            if (year < 1901 || year > 2022) {
                System.out.println("Invalid year. Please enter a year between 1901 and 2022.");
            }
        }
        return year;
    }
}
