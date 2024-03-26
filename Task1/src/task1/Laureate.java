package task1;

import java.util.List;

public class Laureate {

    String name;
    String birthYear;
    String deathYear; // null on defualt unless they are dead
    String country;
    String language;
    String citation;
    List<String> genres;

    public Laureate(String name, String birthYear, String deathYear, String country, String language, String citation, List<String> genres) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.country = country;
        this.language = language;
        this.citation = citation;
        this.genres = genres;
    }
    
    public String getName() {
        return this.name;
    }

    // Getter for the country
    public String getCountry() {
        return this.country;
    }
@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    String line = "|-------------------------------------------------------------|";
    String shortLine = "|-------------------------------------------------------------|";
    sb.append(shortLine).append('\n');
    sb.append(String.format("| %-13s | %-4s | %-4s | %-10s | %-14s |%n", "Winner(s)", "Born", "Died", "Language(s)", "Genre(s)"));
    sb.append(line).append('\n');
    sb.append(String.format("| %-13s | %-4s | %-4s | %-10s | %-14s |%n",
            name, birthYear, deathYear != null ? deathYear : "", language, genresToString()));
    sb.append(line).append('\n');
    sb.append("| Citation:\n");
    sb.append(wrapText(citation, 59)); // Assuming a width of 59 characters fits the citation
    sb.append(shortLine).append('\n');
    return sb.toString();
}

private String wrapText(String text, int width) {
    StringBuilder sb = new StringBuilder(text);

    int i = 0;
    while (i + width < sb.length() && (i = sb.lastIndexOf(" ", i + width)) != -1) {
        sb.replace(i, i + 1, "\n| ");
        i += width;
    }

    // Add padding spaces to align with the table
    String[] lines = sb.toString().split("\n");
    for (int j = 0; j < lines.length; j++) {
        lines[j] = String.format("| %-61s |", lines[j]);
    }

    return String.join("\n", lines) + "\n";
}

private String genresToString() {
    return String.join(", ", genres);
}
    
}
