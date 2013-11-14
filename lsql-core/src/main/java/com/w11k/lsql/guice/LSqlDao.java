package com.w11k.lsql.guice;

import com.google.inject.Inject;
import com.w11k.lsql.LSql;
import com.w11k.lsql.Table;
import com.w11k.lsql.sqlfile.LSqlFile;
import com.w11k.lsql.sqlfile.LSqlFileStatement;

public class LSqlDao {

    @Inject
    private LSql lSql;

    private LSqlFile lSqlFile;

    public LSql getlSql() {
        return lSql;
    }

    public void setlSql(LSql lSql) {
        this.lSql = lSql;
    }

    public LSqlFile getlSqlFile() {
        if (lSqlFile != null) {
            return lSqlFile;
        } else {
            return lSql.readSqlFile(getClass());
        }
    }

    public void setlSqlFile(LSqlFile lSqlFile) {
        this.lSqlFile = lSqlFile;
    }

    public LSqlFileStatement statement(String name) {
        return getlSqlFile().statement(name);
    }

    private Table table(String tableName) {
        return getlSql().table(tableName);
    }

}