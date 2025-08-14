package Utilities.Database;

import java.util.Optional;
import javafx.collections.ObservableList;

/**
 * Interface for database operations
 */
public interface DataObject<T> {

    Optional<T> fetch(int id);
    ObservableList<T> get_every();

    /**
     * Inserts data into the tables<br/>
     * @param t
     * @return boolean flag
     */
    boolean insert(T t);

    /**
     * Updates the tables<br/>
     * @param t
     * @return boolean flag
     */
    boolean update(T t);

    /**
     * Deleted data from  the tables<br/>
     * @param id
     * @return boolean flag
     */
    boolean delete(int id);
}