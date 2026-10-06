package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.example.config.DatabaseConnection;

public class JdbcHelper {
    private static void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        if (params == null)
            return;
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
    
    // Fetch a list of records
    public static <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... params){
        List<T> list = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                setParameters(pstmt, params);

                try(ResultSet rs = pstmt.executeQuery()){
                    while(rs.next()){
                        list.add(mapper.map(rs));
                    }
                }
            }catch(SQLException e){
                throw new RuntimeException("Error executing query",e);
            }
            return list;
    }

    // Fetch a single record
    public static <T> T queryOne(String sql, RowMapper<T> mapper, Object... params){
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                setParameters(pstmt, params);
                try(ResultSet rs = pstmt.executeQuery()){
                    if(rs.next()){
                        return mapper.map(rs);
                    }
                }
                return null;
            }catch(SQLException e){
                throw new RuntimeException("Error executing query", e);
            }
    }

    // Execute INSERT, UPDATE, DELETE
    public static int update(String sql, Object... params){
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException("Error executing update", e);
        }
    }
}
