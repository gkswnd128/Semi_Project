package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import DTO.MemberDTO;


public class MemberDAO {

	private static MemberDAO instance = null;

	public synchronized static MemberDAO getInstance() {
		if(instance == null) { 
			instance = new MemberDAO(); 
		}
		return instance; 
	}
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds=(DataSource)ctx.lookup("java:comp/env/jdbc/orcl"); 
		return ds.getConnection();
	}
	
	//회원가입 정보 입력
	public int insert(MemberDTO dto) throws Exception {
		String sql = "insert into member values(?,?,?,?,?,?,?,?,?)";
			try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
					pstat.setString(1, dto.getId());
					pstat.setString(2, dto.getPw());
					pstat.setString(3, dto.getName());
					pstat.setString(4, dto.getBirthday());
					pstat.setString(5, dto.getPhone());
					pstat.setString(6, dto.getEmail());
					pstat.setString(7, dto.getZipcode());
					pstat.setString(8, dto.getAddress1());
					pstat.setString(9, dto.getAddress2());
					
					int result = pstat.executeUpdate();
					con.commit();
					return result;
				}
			}
	
	
	//아이디 중복확인
	public boolean isIdExist(String id) throws Exception{
		String sql = "select * from member where id = ?";
			try (Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
				pstat.setString(1, id);
				try(ResultSet rs = pstat.executeQuery();){
					return rs.next();
				}
			}
	}
	
	//로그인 성공여부
	public boolean login(String id,String pw) throws Exception {
		String sql = "select *from member where id=? and pw=?";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				) {
			pstat.setString(1, id);
			pstat.setString(2, pw);
			try(ResultSet rs = pstat.executeQuery();){
				return rs.next();
			}
		}

	}


	
	//아이디 찾기
	public String searchId(MemberDTO dto) throws Exception{
		String sql = "SELECT id FROM member WHERE name=? and phone=?";
	      try(Connection con = this.getConnection();
    		  PreparedStatement pstat = con.prepareStatement(sql);){
    		  pstat.setString(1, dto.getName());
    		  pstat.setString(2, dto.getPhone());
    		  try(ResultSet rs = pstat.executeQuery();){
    			  String id;
			      if(rs.next()) {
			        id = rs.getString("id");//rs.getString(1)
			      } else {
			        id = null;
			      }
			      return id;
    		  }
		  }
	}		
	
	//비번 확인하기
	public String searchPw(MemberDTO dto) throws Exception{
		String sql = "SELECT * FROM member WHERE id=? and name=? and phone=?";
	      try(Connection con = this.getConnection();
  		  PreparedStatement pstat = con.prepareStatement(sql);){
    	  pstat.setString(1, dto.getId());
  		  pstat.setString(2, dto.getName());
  		  pstat.setString(3, dto.getPhone());
  		  try(ResultSet rs = pstat.executeQuery();){
  			  String pw;
			      if(rs.next()) {
			        pw = rs.getString("pw");
			      } else {
			        pw = null;
			      }
			      return pw;
  		  		}
	      }
	}
	//비번 재설정
	public int updatePw(MemberDTO dto) throws Exception{
		String sql = "update member set pw=? where id=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, dto.getPw());
			pstat.setString(2, dto.getId());
			
			int result = pstat.executeUpdate();
			String pw = dto.getPw();
			con.commit();
			return result;
		}
	}
	
	//카카오 로그인 위한 이메일 확인
	public boolean kakaoLogin(String email) throws Exception {
		String sql = "select * from member where email=?";
		try(Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				) {
			pstat.setString(1, email);
			try(ResultSet rs = pstat.executeQuery();){
				return rs.next();
			}
		}
	}
	//카카오 로그인 후 이메일 false라면 회원가입
	public int kakaoInsert(MemberDTO dto) throws Exception {
		String sql = "insert into kakaomember values(?,?,?,?)";
			try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);){
					pstat.setString(1, dto.getId());
					pstat.setString(2, dto.getName());
					pstat.setString(3, dto.getBirthday());
					pstat.setString(4, dto.getEmail());
					int result = pstat.executeUpdate();
					con.commit();
					return result;
				}
			}
	
	
}
