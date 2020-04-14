import java.util.*;

/**
 * Static class with helper functions needed for commands execution.
 */
public final class Utils {

    /** Not to be used. */
    private Utils() {
        throw new UnsupportedOperationException("This constructor should never be used.");
    }

    /**
     * Gets list of books in library if this list is not null and all entries are not null.
     * @param data a given library of books.
     * @return list of books of a library
     * @throws NullPointerException if library is empty, list of books is empty, or any book is empty.
     */
    public static List<BookEntry> getNonNullBookData(LibraryData data) {
        Objects.requireNonNull(data, "Library data must not be null.");
        List<BookEntry> books = data.getBookData();

        Objects.requireNonNull(books, "List of books must not be null.");

        for (BookEntry book : books) {
            Objects.requireNonNull(book, "Book in a list must not be null.");
        }

        return books;
    }

    /**
     * Add key -> value map into a given dictionary. Values of a given key are stored
     * in ArrayList, if a given key already exists, its value is appended to the current values.
     * @param key key to be added to HashMap.
     * @param value value to be added to HashMap.
     * @param dictionary HashMap we add key -> value pair into.
     */
    public static <T> void packToDictionary(T key, T value, HashMap<T, ArrayList<T>> dictionary) {
        if (dictionary.containsKey(key)) {
            dictionary.get(key).add(value);
        } else {
            ArrayList<T> values = new ArrayList<>();
            values.add(value);
            dictionary.put(key, values);
        }
    }

    /**
     * Gets string representation of an array without square brackets.
     * @param array array of any non-primitive type, e.g. new Integer[] {1, 2, 3, 4, 5}.
     * @return array string without square brackets, e.g. "1, 2, 3, 4, 5".
     */
    static <T>String arrayWithoutBrackets(T[] array) {
        return Arrays.toString(array).replace("[", "").replace("]", "");
    }
}