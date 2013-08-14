package com.w11k.lsql.sqlfile;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.w11k.lsql.LSql;
import com.w11k.lsql.Query;
import com.w11k.lsql.exceptions.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.ImmutableMap.copyOf;

public class SqlFile {

    private static final Pattern STMT_BLOCK_BEGINN = Pattern.compile(
            "^--\\s*(\\w*)\\s*$",
            Pattern.MULTILINE);

    private static final Pattern STMT_BLOCK_END = Pattern.compile(
            ";\\s*$",
            Pattern.MULTILINE);

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LSql lSql;

    private final String fileName; // without .sql extension

    private final InputStream inputStream;

    private final Map<String, LazyPreparedStatement> statements = Maps.newHashMap();

    public SqlFile(LSql lSql, String fileName, InputStream is) {
        this.lSql = lSql;
        this.fileName = fileName;
        inputStream = is;
        parseSqlStatements();
    }

    // ----- public -----

    public ImmutableMap<String, LazyPreparedStatement> getStatements() {
        return copyOf(statements);
    }

    public void execute(String sqlStatementName) {
        PreparedStatement ps = statements.get(sqlStatementName).getPreparedStatement();
        logger.debug("Executing SQL statement {}", sqlStatementName);
        try {
            ps.execute();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    public Query executeQuery(String sqlStatementName) {
        PreparedStatement ps = statements.get(sqlStatementName).getPreparedStatement();
        logger.debug("Executing SQL query {}", sqlStatementName);
        return new Query(lSql, ps);
    }

    // ----- private -----

    private void parseSqlStatements() {
        logger.info("Reading SQL file '" + fileName + "'");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String content = CharStreams.toString(reader);
            Matcher startMatcher = STMT_BLOCK_BEGINN.matcher(content);
            while (startMatcher.find()) {
                String name = startMatcher.group(1);
                String sub = content.substring(startMatcher.end());
                Matcher endMatcher = STMT_BLOCK_END.matcher(sub);
                if (!endMatcher.find()) {
                    throw new IllegalStateException("Could not find the end of the SQL expression '" +
                            name + "'. Did you add ';' at the end?");
                }
                sub = sub.substring(0, endMatcher.end()).trim();
                // TODO handle parameters
                logger.debug("Found SQL statement '" + name + "'");
                statements.put(name, new LazyPreparedStatement(lSql, sub));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
