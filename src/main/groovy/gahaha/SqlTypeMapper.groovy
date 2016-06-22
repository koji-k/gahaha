package gahaha

class SqlTypeMapper {
    static final mappings = [
            (java.lang.Integer) : "NUMERIC",
            (java.lang.Long)    : "NUMERIC",
            (java.util.Date)    : "TIMESTAMP",
            (java.sql.Timestamp): "TIMESTAMP",
            (java.sql.Date)     : "TIMESTAMP",
            (java.sql.Time)     : "TIMESTAMP",
            (java.lang.String)  : "LONGVARCHAR(${Integer.MAX_VALUE})"
    ]
}
