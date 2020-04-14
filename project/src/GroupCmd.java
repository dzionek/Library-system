import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Group command used to group and display books by {@link BookField} value.
 */
public class GroupCmd extends LibraryCommand {

    /** Message displayed before printing groups. Followed by {@link BookField} value. */
    private static final String GROUPED_MESSAGE = "Grouped data by ";
    /** Message displayed if a library is empty. */
    private static final String EMPTY_LIBRARY_MESSAGE = "The library has no book entries.";
    /** String denoting titles starting with a digit, when grouping by title. */
    private static final String DIGITS_GROUP = "[0-9]";
    /** String printed before a group, followed by a group's name. */
    private static final String GROUP_HEADER = "## ";

    /** Mode of a group command - one of {@link BookField} values. */
    private BookField mode;

    /**
     * Creates a group method.
     * @param argumentInput argument input is expected to be {@link BookField} value.
     * @throws IllegalArgumentException if given argument is invalid.
     * @throws NullPointerException if given argument is null.
     * @see LibraryCommand#LibraryCommand for errors handling.
     * @see GroupCmd#parseArguments for {@link GroupCmd#mode} initialisation.
     */
    public GroupCmd(String argumentInput) {
        super(CommandType.GROUP, argumentInput);
    }

    /**
     * Checks if a given argument is valid (is one of {@link BookField} value).
     * @param argumentInput argument input for this command.
     * @return {@code true} if the argument is valid, otherwise {@code false}.
     * @throws NullPointerException if the given argument is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        Objects.requireNonNull(argumentInput, "Given input argument must not be null.");

        for (BookField legalMode : BookField.values()) {
            String legalModeStr = legalMode.name();
            if (argumentInput.equals(legalModeStr)) {
                mode = legalMode;
                return true;
            }
        }
        return false;
    }

    /**
     * Executes group command, displays books grouped according to {@link GroupCmd#mode}.
     * If there are no books in a library, prints a special message instead.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException if library, list of books, any book, or {@link GroupCmd#mode} is null.
     * @throws IllegalArgumentException if an instance's mode is invalid.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, "Library data must not be null.");

        List<BookEntry> books = Utils.getNonNullBookData(data);

        if (books.isEmpty()) {
            System.out.println(EMPTY_LIBRARY_MESSAGE);
        } else {
            Objects.requireNonNull(mode, "Mode must not be null.");
            System.out.println(GROUPED_MESSAGE + mode.name());

            switch (mode) {
                case TITLE:
                    groupByTitle(books);
                    break;
                case AUTHOR:
                    groupByAuthor(books);
                    break;
                default:
                    throw new IllegalArgumentException("The given mode is invalid.");
            }
        }
    }

    /**
     * Groups by title and print all groups.
     * @param books not null and not empty list of books which will be grouped.
     */
    private static void groupByTitle(List<BookEntry> books) {
        List<String> listOfTitles = getListOfTitles(books);
        TreeMap<String, List<String>> mapOfTitles = groupByFirstLetter(listOfTitles);
        printGrouped(mapOfTitles);
    }

    /**
     * Get list of titles of a given list of books.
     * Each book is mapped to its title.
     * @param books not null and not empty list of books.
     * @return list of titles in a library.
     */
    private static List<String> getListOfTitles(List<BookEntry> books) {
        List<String> listOfTitles = new ArrayList<>();
        for (BookEntry book : books) {
            listOfTitles.add(book.getTitle());
        }
        return listOfTitles;
    }

    /**
     * Groups all entries in a list into a TreeMap where a key is the first letter
     * which maps to all entries starting with that letter.
     *
     * If a digit is a first letter, the key is {@value DIGITS_GROUP}.
     *
     * @param values list of strings.
     * @return Map of ordered keys where first letter of each list entry maps to a list
     *         of all entries starting with that letter.
     */
    private static TreeMap<String, List<String>> groupByFirstLetter(List<String> values) {
        TreeMap<String, List<String>> map = new TreeMap<>();
        for (String value : values) {
            char firstLetter = value.charAt(0);
            String key;
            if (Character.isDigit(firstLetter)) {
                key = DIGITS_GROUP;
            } else {
                key = Character.toString(Character.toUpperCase(firstLetter));
            }
            Utils.packToMap(key, value, map);
        }
        return map;
    }

    /**
     * Prints {@value GROUP_HEADER} followed by TreeMap key
     * and all elements in a list corresponding to that key.
     *
     * @param treeMap a given not null, and not empty TreeMap to be printed.
     */
    private static void printGrouped(TreeMap<String, List<String>> treeMap) {
        for (Map.Entry<String, List<String>> entry : treeMap.entrySet()) {
            System.out.println(GROUP_HEADER + entry.getKey());
            for (String value : entry.getValue()) {
                System.out.println(value);
            }
        }
    }

    /**
     * Groups by author, and prints all groups.
     * @param books list of books to be grouped and printed.
     */
    private static void groupByAuthor(List<BookEntry> books) {
        TreeMap<String, List<String>> authorsTitles = getAuthorsTitles(books);
        printGrouped(authorsTitles);
    }

    /**
     * Gets a TreeMap where authors are keys, and values are books they have written,
     * packed into a list.
     *
     * @param books list of books to be transformed into a HashMap.
     * @return TreeMap specified above, e.g. {author1 -> [book1, book2], author2 -> [book1]}.
     */
    private static TreeMap<String, List<String>> getAuthorsTitles(List<BookEntry> books) {
        TreeMap<String, List<String>> authorsTitles = new TreeMap<>();
        for (BookEntry book : books) {
            String title = book.getTitle();
            for (String author : book.getAuthors()) {
                Utils.packToMap(author, title, authorsTitles);
            }
        }
        return authorsTitles;
    }
}
