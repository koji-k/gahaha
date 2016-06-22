package gahaha

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import java.lang.reflect.Field

class Gahaha {
    Class clazz
    List<Field> declaredFields
    static String jdbcUrl
    static String jdbcDriverClassName
    static String tableName

    public static gahanize(Class clazz) {
        new Gahaha(clazz, GahahaPool.defaultJdbcUrl, GahahaPool.defaultJdbcDriverClassName)
    }

    public static gahanize(Class clazz, String jdbcUrl, String jdbcDriverClassName) {
        new Gahaha(clazz, jdbcUrl, jdbcDriverClassName)
    }

    private Gahaha(Class clazz, String jdbcUrl, String jdbcDriverClassName) {
        Gahaha.jdbcUrl = jdbcUrl
        Gahaha.jdbcDriverClassName = jdbcDriverClassName
        this.clazz = clazz
        tableName = clazz.simpleName
        this.clazz.metaClass.id = 0
        declaredFields = fields()
        createTable()
        addUtilMethods()
    }

    static Sql getConnection() {
        GahahaPool.connection(jdbcUrl, jdbcDriverClassName)
    }

    private List<Field> fields() {
        this.clazz.declaredFields.findAll {!it.synthetic}
    }

    private createTable(){

        String columns = declaredFields.collect {
            "${it.name} ${SqlTypeMapper.mappings[it.type]}"
        }.join(', ')
        String ddl = "CREATE TABLE IF NOT EXISTS ${tableName} (id bigint auto_increment, ${columns})".toString()
        connection.execute(ddl)
    }

    protected Object convertToClazz(GroovyRowResult row) {
        Map map = [:]
        List<String>props = ['id'] + declaredFields.collect {it.name}
        props.each {String propertyName->
            map[propertyName] = row[propertyName]
        }
        this.clazz.newInstance(map)
    }

    private addUtilMethods() {

        this.clazz.metaClass.static.list = {->
            connection.rows("SELECT * FROM ${tableName}".toString()).collect{GroovyRowResult row ->
                convertToClazz(row)
            }
        }

        this.clazz.metaClass.static.get = { Long id->
            GroovyRowResult row = connection.firstRow("SELECT * FROM ${tableName} WHERE id = ?".toString(), [id])
            convertToClazz(row)
        }

        this.clazz.metaClass.static.count = {->
            connection.firstRow("SELECT COUNT(*) cnt FROM ${tableName}".toString())["cnt"]
        }

        this.clazz.metaClass.static.deleteAll = {
            connection.execute("DELETE FROM ${tableName}".toString())
        }

        this.clazz.metaClass.save = {
            List<String>fieldNames = this.declaredFields.collect{it.name}
            List<Object>fieldValues = fieldNames.collect { delegate.getProperty(it)}
            String placeHolders = fieldNames.collect{"?"}.join(', ')
            def inserted = connection.executeInsert("INSERT INTO ${tableName} (${fieldNames.join(',')}) VALUES(${placeHolders})".toString(), fieldValues)
            Long insertedId = inserted[0][0]
            def p = get(insertedId)
            delegate.id = p.id
            p
        }

        this.clazz.metaClass.delete = {
            Long id = delegate.id
            connection.execute("DELETE FROM ${tableName} WHERE id = ?", [id])
        }
    }
}