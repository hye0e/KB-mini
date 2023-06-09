package coin.wallet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import coin.vo.Coin;
import coin.vo.Wallet;
import config.ServerInfo;

public class WalletDAOImpl implements WalletDAO {
	private static WalletDAOImpl dao = new WalletDAOImpl();
	
	private WalletDAOImpl() {}
	
	public static WalletDAOImpl getInstance() {
		return dao;
	}
	
	@Override
	public Connection getConnect() throws SQLException {
		Connection conn =DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASSWORD);
//        System.out.println("Database Connection......");
        return conn;
	}


	@Override
	public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
		if(ps!=null) ps.close();
	    if(conn!=null) conn.close();    
		
	}

	@Override
	public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
		if(rs!=null) rs.close();
	    closeAll(ps, conn);
	}
        
	public boolean isExist(int walNo, Connection conn) throws SQLException{
	    String sql ="SELECT wallet_no FROM wallet WHERE wallet_no=?";
	    PreparedStatement ps = conn.prepareStatement(sql);
	    
	    ps.setInt(1,walNo);
	    ResultSet wal_seq_no = ps.executeQuery();
		return wal_seq_no.next();
	}
	
	/* 전자지갑 생성 */
	@Override
	public void createWallet(Wallet wl) throws SQLException, DuplicateFormatFlagsException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn=getConnect();
			if(!isExist(wl.getWalNo(),conn)) {
				String query = "INSERT INTO wallet(wallet_no,c_quantity, c_dealprice, c_dealtype,c_dealdate,c_code,account_no) values(wal_seq_no.NEXTVAL,?,?,?,sysdate,?,?)";
						// c_code, account_no => 처음 생성 때 어떻게 값 주입?
				ps = conn.prepareStatement(query);
				ps.setDouble(1, wl.getcQuantity());
				ps.setDouble(2, wl.getcDealPrice());
				ps.setString(3, wl.getcDealType());
				ps.setString(4, wl.getcCode());
				ps.setString(5, wl.getAccountNo());
				
				
				System.out.println(ps.executeUpdate() + "개의 전자지갑이 생성되었습니다.");
			}else {
				throw new  DuplicateFormatFlagsException("이미 존재하는 전자지갑이 있습니다.");
			}
		} finally {
			closeAll(ps, conn);
		}
	}

	@Override
	public void deleteWallet(String Id) {
		// TODO Auto-generated method stub
		
	}

	/* 전체 코인 검색 */
	@Override
	public ArrayList<Coin> getCoinList() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Coin> list = new ArrayList<>();
		
		try {
			conn = getConnect();
			String query = "SELECT c_code, c_name, c_nowprice FROM COIN";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Coin(rs.getString("c_code"), 
						 rs.getString("c_name"), 
						 rs.getInt("c_nowprice")));
			}
		}finally {
			closeAll(rs,ps,conn);
		}return list;
	}

	/*코인 명으로 검색*/
	@Override
	public ArrayList<Coin> getCoinList(String cName) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Coin> list = new ArrayList<>();
		
		try {
			conn = getConnect();
			String query = "SELECT c_code, c_name, c_nowprice FROM coin WHERE c_name=?";
			ps = conn.prepareStatement(query);
			ps.setString(1,cName);
			rs = ps.executeQuery();
			while(rs.next()) {
				list.add(new Coin(cName, rs.getString("c_code"), rs.getInt("c_nowprice")));
			}
		}finally {
			closeAll(rs,ps,conn);
		}
		
		return list;
		
	}
}
