package com.w11k.lsql.tests;

import com.google.common.base.Optional;
import com.w11k.lsql.exceptions.InsertException;
import com.w11k.lsql.relational.Row;
import com.w11k.lsql.relational.Table;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static junit.framework.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class TableTest extends AbstractLSqlTest {

    @Test public void insertRow() throws SQLException {
        lSql.executeRawSql("CREATE TABLE table1 (name TEXT)");
        Table table1 = lSql.table("table1");

        Row row = new Row().addKeyVals("name", "cus1");
        table1.insert(row);

        Row insertedRow = lSql.executeRawQuery("select * from table1").getFirstRow();
        assertEquals(insertedRow.getString("name"), "cus1");
    }

    @Test public void insertShouldReturnGeneratedKey() {
        lSql.executeRawSql("CREATE TABLE table1 (id SERIAL, age INT)");
        Table table1 = lSql.table("table1");
        Object newId = table1.insert(new Row().addKeyVals("age", 1)).get();

        Row query = lSql.executeRawQuery("select * from table1 where id = " + newId).getFirstRow();
        assertEquals(query.getInt("age"), 1);
    }

    @Test public void insertShouldPutIdIntoRowObject() {
        lSql.executeRawSql("CREATE TABLE table1 (id serial primary key, age int)");
        Table table1 = lSql.table("table1");
        Row row = new Row().addKeyVals("age", 1);
        Optional<Object> optional = table1.insert(row);
        assertTrue(optional.isPresent());
        assertEquals(optional.get(), row.get("id"));
    }

    @Test(expectedExceptions = InsertException.class)
    public void insertShouldFailIfPrimaryKeyIsAlreadyPresent() {
        lSql.executeRawSql("CREATE TABLE table1 (id serial primary key, age int)");
        Table table1 = lSql.table("table1");
        Row row = new Row().addKeyVals("id", 1, "age", 1);
        table1.insert(row);
    }

    @Test public void getById() {
        lSql.executeRawSql("CREATE TABLE table1 (id serial primary key, age int)");
        Table table1 = lSql.table("table1");

        Object id1 = table1.insert(Row.fromKeyVals("age", 1)).get();
        Object id2 = table1.insert(Row.fromKeyVals("age", 2)).get();
        Object id3 = table1.insert(Row.fromKeyVals("age", 3)).get();

        assertEquals(table1.get(id1).getInt("age"), 1);
        assertEquals(table1.get(id2).getInt("age"), 2);
        assertEquals(table1.get(id3).getInt("age"), 3);
    }

}
