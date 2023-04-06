package coin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import coin.vo.Account;
import coin.vo.Coin;
import coin.vo.Customer;
import coin.vo.Wallet;


public interface DatabaseTemplates {
	Connection getConnect() throws SQLException;
	void closeAll(PreparedStatement ps, Connection conn)throws SQLException;
	void closeAll(ResultSet rs, PreparedStatement ps, Connection conn)throws SQLException;
	
	// 비즈니스 로직
	void insertCust(Customer cust);
	void updateCust(Customer cust);
	void withdrawCust(int custId);
	Customer getCustInfo(int custId);

	void openAccount(int custId);
	int getAccountStatus(int accountNo);
	int getAccountBalance();
	
	void createWallet(Wallet wl) throws SQLException;
	void deleteWallet(int walNo) throws SQLException, AttributeNotFoundException;
	
	
	
	ArrayList<Coin> getCoinList() throws SQLException;
	ArrayList<Coin> getCoinList(String coinName) throws SQLException;
	
	void buyCoin(int custId, String coinCode);
	void sellCoin(int custId, String coinCode);
	
	void deductBalance(String accountNo, int amount);
	void addBalance(String accountNo, int amount);
	


	
//	void insertWalletLog(WalletNo wl);
}
 
