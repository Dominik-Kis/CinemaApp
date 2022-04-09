/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.dal.sql.SqlAccountRepository;
import hr.algebra.dal.sql.SqlRepository;

/**
 *
 * @author GamerGruft
 */
public class RepositoryFactory {
    private RepositoryFactory() {
    }
    
    public static Repository getRepository() {
        return new SqlRepository();
    }
    
    public static AccountRepository getAccountRepository(){
        return new SqlAccountRepository();
    }
}
