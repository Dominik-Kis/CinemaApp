/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.AccountRepository;
import hr.algebra.model.Account;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;
import javax.sql.DataSource;

/**
 *
 * @author GamerGruft
 */
public class SqlAccountRepository implements AccountRepository{

    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String ADMIN = "Admin";
    private static final String CHECK = "Check";

    private static final String CREATE_ACCOUNT = "{ CALL createAccount (?,?,?) }";
    private static final String SELECT_ACCOUNT = "{ CALL selectAccount (?) }";
    private static final String LOGIN_ACCOUNT = "{ CALL loginAccount (?,?) }";
    private static final String CHECK_ACCOUNT = "{ CALL checkAccount (?) }";
    
    @Override
    public void createAccount(Account account) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACCOUNT)) {

            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassword());
            stmt.setBoolean(3, false);
            stmt.executeUpdate();
            }
        }

    @Override
    public Optional<Account> selectAccount(Account account) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(SELECT_ACCOUNT)) {

            stmt.setString(1, account.getUserName());
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Account(
                        rs.getString(USERNAME),
                        rs.getString(PASSWORD),
                        rs.getBoolean(ADMIN)));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean checkAccount(String username) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CHECK_ACCOUNT)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(CHECK);
                }
            }
        }
        return false;
    }

    @Override
    public Optional<Account> loginAccount(Account account) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(LOGIN_ACCOUNT)) {

            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassword());
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new Account(
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getBoolean(ADMIN)));
                }
            }
        }
        return Optional.empty();
    }


    
}
