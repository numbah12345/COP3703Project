package unf.g1.project.models;

import java.util.Map;

/**
 * Base class for all data models in the hospital system. Provides common
 * interface for database operations.
 */
public abstract class Model {

    /**
     * Returns the database table name for this model
     */
    public abstract String getTableName();

    /**
     * Returns the primary key column name(s) For composite keys, return
     * comma-separated string
     */
    public abstract String getPrimaryKeyColumn();

    /**
     * Returns the primary key value(s) For composite keys, could return a Map
     * or array
     */
    public abstract Object getPrimaryKeyValue();

    /**
     * Converts the model to a Map of column names to values Used by
     * QueryBuilder to generate INSERT and UPDATE statements
     */
    public abstract Map<String, Object> toMap();

    /**
     * Validates the model data Returns true if valid, false otherwise
     */
    public boolean validate() {
        // Override in subclasses for specific validation
        return true;
    }
}
