/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Account;
import java.util.Optional;

/**
 *
 * @author GamerGruft
 */
public interface AccountRepository {
    void createAccount(Account account) throws Exception;
    Optional<Account> selectAccount(Account account) throws Exception;
    Optional<Account> loginAccount (Account account) throws Exception;

    public boolean checkAccount(String trim) throws Exception;
}
