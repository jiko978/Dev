package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	@Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	//log 출력
    public String xlog(String s)
    {
    	Exception e = new Exception();
    	String str = "";
    	str += String.format("[%-50s]", e.getStackTrace()[1].getFileName() + "::" + e.getStackTrace()[1].getMethodName());
    	str += String.format("line:[%-5d]", e.getStackTrace()[1].getLineNumber());
    	str += s;
    	System.out.println(str);
    	return s;
    }
	
    //쿠폰 조회
    public void dbCouponMst_displayList()
    {
    	List<MetaVo> list = dbCouponMst_getList();
    	 
        for(int i=0;i<list.size();i++)
        {
        	MetaVo vo = list.get(i);
        	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}",
        						i,vo.getCouponId(),vo.getUserId(),vo.getUseYn(),vo.getExpireDt()));
        }    
    }

    //쿠폰 조회
    public List<MetaVo> dbCouponMst_getList()
    {
    	List<MetaVo> list = new ArrayList<MetaVo>();
    	
    	xlog("# TB_COUPON_MST 정보");
    	list = jdbcTemplate.query("SELECT * FROM TB_COUPON_MST",
    			new RowMapper<MetaVo>()
				{
					public MetaVo mapRow(ResultSet rs,int rowNum) throws SQLException
					{
						MetaVo vo = new MetaVo();
						vo.setCouponId(rs.getString("coupon_id"));
						vo.setUserId(rs.getString("user_id"));
						vo.setUseYn(rs.getString("use_yn"));
						vo.setExpireDt(rs.getString("expire_dt"));
						return vo;
					}
				}        										
        );
        
        return list;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home()
    {
    	String ret = xlog("##### [home] #####");
    	String s = "";
    	List<MetaVo> list = new ArrayList<MetaVo>();
    	
    	try
    	{
	    	xlog("# USER 정보");
	        list = jdbcTemplate.query("SELECT * FROM USER",
	        		new RowMapper<MetaVo>()
					{
						public MetaVo mapRow(ResultSet rs,int rowNum) throws SQLException
						{
							MetaVo vo = new MetaVo();
							vo.setId(rs.getLong("id"));
							vo.setName(rs.getString("name"));
							return vo;
						}
					}        										
	        );	
	        
	        for(int i=0;i<list.size();i++)
	        {
	        	MetaVo vo = list.get(i);
	        	xlog(String.format("i={%d} id={%d} name={%s}",i,vo.getId(),vo.getName()));
	        }

	        dbCouponMst_displayList();
	        
	        List list1 = new ArrayList();
	        list1.add("a");
	        list1.add("b");
	        list1.add("c");
	        for(int i=0;i<list1.size();i++)
	        {
	        	s = (String)list1.get(i);
	        	xlog(String.format("i={%d} s={%s}",i,s));
	        }
	        
	        List<String> list2 = new ArrayList<String>();
	        list2.add("a");
	        list2.add("b");
	        list2.add("c");
	        for(int i=0;i<list2.size();i++)
	        {
	        	s = list2.get(i);
	        	xlog(String.format("i={%d} s={%s}",i,s));
	        }

    	}
    	catch(Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[finally]");
    		xlog(ret);
    	}
    	
    	return ret;
    }
    
    //1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요.
    @GetMapping("/ex01")
    public String ex01(@RequestParam(value="cnt",defaultValue="0") String paramCnt)
    {
    	String ret = xlog("#################### [ex01] ####################");
    	long maxCnt = 0;
    	String expireDt = "";

    	try
    	{
    		xlog(String.format("입력값 [I] cnt={%s}",paramCnt));
    		
    		maxCnt = Long.valueOf(paramCnt);

    		xlog(String.format("생성할 토큰 개수 maxCnt={%d}",maxCnt));
    		
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    		Calendar cal = Calendar.getInstance();
    		String curDtms = sdf.format(cal.getTime());
    		String curDt = curDtms.substring(0,8);
    		
    		xlog(String.format("curDt={%s}",curDt));//쿠폰 생성일
    		
    		int j = 0;
    		
    		for(int i=1;i<=maxCnt;i++)
    		{
    			//쿠폰번호 생성. 추후 오라클 시퀀스 사용해야 함.
    			String couponId = String.format("%s-%s-%07d","COUPON",curDt,i);
                
    			//4개까지는 만료일을 당일로 설정
    			if(i<=3)
    			{
    				expireDt = curDt;
    			}
    			//나머지는 하루씩 만료일 늘림
    			else
    			{
	    			cal = Calendar.getInstance();
	    			cal.add(Calendar.DATE,j++);
	    			expireDt = sdf.format(cal.getTime()).substring(0,8);
    			}
    			
    			String sql = "INSERT INTO TB_COUPON_MST(coupon_id, user_id, use_yn, expire_dt) VALUES(?, ?, ?, ?)";
    			jdbcTemplate.update(sql,new Object[] {couponId,"-","N",expireDt});
    		}
    		
    		//쿠폰 조회
	        dbCouponMst_displayList();
    	}
    	catch(Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[쿠폰 생성 finally]");
    		xlog(ret);
    	}

    	return ret;
    }

    //2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요.
    @GetMapping("/ex02")
    public String ex02(@RequestParam(value="userId",defaultValue="") String paramUserId)
    {
    	String ret = xlog("#################### [ex02] ####################");
    	String retCouponId = "";

    	try
    	{
    		xlog(String.format("입력값 [I] paramUserId={%s}",paramUserId));
    		
        	List<MetaVo> list = new ArrayList<MetaVo>();
        	
        	xlog("# USER_ID 미할당된 쿠폰 검색");

        	list = jdbcTemplate.query("SELECT * FROM TB_COUPON_MST WHERE user_id = '-' AND ROWNUM = 1 ORDER BY coupon_id",
        			new RowMapper<MetaVo>()
					{
						public MetaVo mapRow(ResultSet rs,int rowNum) throws SQLException
						{
							MetaVo vo = new MetaVo();
							vo.setCouponId(rs.getString("coupon_id"));
							vo.setUserId(rs.getString("user_id"));
							vo.setUseYn(rs.getString("use_yn"));
							vo.setExpireDt(rs.getString("expire_dt"));
							return vo;
						}
					}        										
            );
        	
            for(int i=0;i<list.size();i++)
            {
            	MetaVo vo = list.get(i);
            	
            	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}",
            						i,vo.getCouponId(),vo.getUserId(),vo.getUseYn(),vo.getExpireDt()));
            	
            	retCouponId = vo.getCouponId();
            }
            
        	xlog(String.format("신규 쿠폰 번호 retCouponId={%s}",retCouponId));
        	
			String sql = "UPDATE TB_COUPON_MST SET user_id = ? WHERE coupon_id = ?";
			jdbcTemplate.update(sql,new Object[] {paramUserId,retCouponId});

			//쿠폰 조회
			dbCouponMst_displayList();        	
    	}
    	catch(Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[쿠폰 발급 finally]");
    		xlog(retCouponId);
    	}

    	return retCouponId;
    }

    //3. 사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요.
    @GetMapping( "/ex03" )
    public List<Map<String,Object>> ex03(@RequestParam(value="userId",defaultValue="") String paramUserId)
    {
    	String ret = xlog("#################### [ex03] ####################");
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	
    	try
    	{
    		xlog(String.format("입력값 [I] paramUserId={%s}",paramUserId));
        	xlog("# USER_ID 할당된 쿠폰 검색");

        	String sql = "SELECT * FROM TB_COUPON_MST WHERE user_id = '" + paramUserId + "' ORDER BY coupon_id";
            list = jdbcTemplate.queryForList(sql);
            
            xlog(String.format("list.size={%d}",list.size()));
            
            for(int i=0;i<list.size();i++)
            {
            	map = list.get(i);
            	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}",
            						i,map.get("coupon_id"),map.get("user_id"),map.get("use_yn"),map.get("expire_dt")));
            }
    	}
    	catch(Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[사용자 지급 쿠폰 조회 finally]");
    	}

    	return list;
    }

    //4. 지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가)
    @GetMapping("/ex04")
    public String ex04(@RequestParam(value="couponId",defaultValue="") String paramCouponId)
    {
    	String ret = xlog("#################### [ex04] ####################");
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	String useYn = "";

    	try
    	{
    		xlog(String.format("입력값 [I] paramCouponId={%s}",paramCouponId));
    		
        	xlog("# 쿠폰 검색");
        	
        	String sql = "SELECT * FROM TB_COUPON_MST WHERE coupon_id = '" + paramCouponId + "' ORDER BY coupon_id";
            list = jdbcTemplate.queryForList(sql);

            xlog(String.format("list.size={%d}",list.size()));
            
            for(int i=0;i<list.size();i++)
            {
            	Map<String,Object> map = (Map<String,Object>)list.get(i);
            	
            	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}",
            						i,map.get("coupon_id"),map.get("user_id"),map.get("use_yn"),map.get("expire_dt")));
            	
            	if(paramCouponId.equals(map.get("coupon_id")))
            	{
            		useYn = (String)map.get("use_yn");
            		break;
            	}
            }
            
            if(useYn.getBytes().length<1)
            {
            	ret = String.format("[ERR] 미등록 쿠폰 coupon_id={%s}",paramCouponId);
            }
            else if("Y".equals(useYn))
            {
            	ret = String.format("[ERR] 쿠폰 사용 중 coupon_id={%s}",paramCouponId);            	
            }
            else if("N".equals(useYn))
            {
    			sql = "UPDATE TB_COUPON_MST SET use_yn = ? WHERE coupon_id = ?";
    			jdbcTemplate.update(sql, new Object[] {"Y",paramCouponId});

    			//쿠폰 조회
    			dbCouponMst_displayList();
    			
    			ret = "사용성공";    			
            }
            else
            {
            	ret = String.format("[ERR] 처리 도중 오류 발생 coupon_id={%s}",paramCouponId);            	
            }
    	}
    	catch(Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[쿠폰 사용 finally]");
    		xlog(ret);
    	}

    	return ret;
    }

    //지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능)
    @GetMapping("/ex05")
    public String ex05(@RequestParam(value="couponId",defaultValue="") String paramCouponId)
    {
    	String ret = xlog("#################### [ex05] ####################");
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	String useYn = "";

    	try
    	{
    		xlog(String.format("입력값 [I] paramCouponId={%s}",paramCouponId));
    		
        	xlog("# 쿠폰 검색");
        	
        	String sql = "SELECT * FROM TB_COUPON_MST WHERE coupon_id = '" + paramCouponId + "' order by coupon_id";
            list = jdbcTemplate.queryForList(sql);
            
            xlog(String.format("list.size={%d}",list.size()));
            
            for(int i=0;i<list.size();i++)
            {
            	Map<String,Object> map = (Map<String,Object>)list.get(i);

            	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}",
            						i,map.get("coupon_id"),map.get("user_id"),map.get("use_yn"),map.get("expire_dt")));
            	
            	if(paramCouponId.equals(map.get("coupon_id")))
            	{
            		useYn = (String)map.get("use_yn");
            		break;
            	}
            }
            
            if(useYn.getBytes().length<1)
            {
            	ret = String.format("[ERR] 미등록 쿠폰 coupon_id={%s}",paramCouponId);
            }
            else if("N".equals(useYn))
            {
            	ret = String.format("[ERR] 아직 미사용 쿠폰 coupon_id={%s}",paramCouponId);            	
            }
            else if("Y".equals(useYn))
            {
    			sql = "UPDATE TB_COUPON_MST SET use_yn = ? WHERE coupon_id = ?";
    			jdbcTemplate.update(sql,new Object[] {"N",paramCouponId});
    			
    			//쿠폰 조회
    			dbCouponMst_displayList();
    			ret = "취소성공";
            }
            else
            {
            	ret = String.format("[ERR] 처리 도중 오류 발생 coupon_id={%s}",paramCouponId);            	
            }
    	}
    	catch (Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[쿠폰 취소 finally]");
    		xlog(ret);
    	}

    	return ret;
    }
    
    //6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요.
    @GetMapping("/ex06")
    public List<Map<String,Object>> ex06(@RequestParam(value="baseDt",defaultValue="") String paramBaseDt)
    {
    	String ret = xlog("#################### [ex06] ####################");
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

    	try
    	{
    		xlog(String.format("입력값 [I] paramBaseDt={%s}",paramBaseDt));
    		
        	xlog("# USER_ID 미할당된 쿠폰 검색");
        	
        	String sql = "SELECT * FROM TB_COUPON_MST WHERE expire_dt = to_char('" + paramBaseDt + "','yyyymmdd') ORDER BY expire_dt";
            list = jdbcTemplate.queryForList(sql);
            
            xlog(String.format("list.size={%d}",list.size()));
            
            for(int i=0;i<list.size();i++)
            {
            	Map<String,Object> map = (Map<String,Object>)list.get(i);
            	
            	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}",
            						i,map.get("coupon_id"),map.get("user_id"),map.get("use_yn"),map.get("expire_dt")));
            }
    	}
    	catch (Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[당일 만료 쿠폰 조회 finally]");
    	}

    	return list;
    }

    //7. 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하 세요.
    @GetMapping("/ex07")
    public List<Map<String,Object>> ex07(@RequestParam(value="baseDt",defaultValue="") String paramBaseDt,
    										@RequestParam(value="userId",defaultValue="") String paramUserId)
    {
    	String ret = xlog("#################### [ex07] ####################");
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

    	try
    	{
    		xlog(String.format("입력값 [I] paramBaseDt={%s}",paramBaseDt));
    		xlog(String.format("입력값 [I] paramUserId={%s}",paramUserId));
    		
        	xlog("# USER_ID 미할당된 쿠폰 검색");
        	
        	String sql = "SELECT * FROM TB_COUPON_MST WHERE user_id = '" + paramUserId + "' AND expire_dt = to_char(to_date('" + paramBaseDt + "', 'yyyymmdd') + 3, 'yyyymmdd') ORDER BY expire_dt";
            list = jdbcTemplate.queryForList(sql);
            
            xlog(String.format("lst.size={%d}",list.size()));
            
            for(int i=0;i<list.size();i++)
            {
            	Map<String,Object> map = (Map<String,Object>)list.get(i);
            	
            	xlog(String.format("i={%d} couponId={%s} userId={%s} useYn={%s} expireDt={%s}", 
            						i,map.get("coupon_id"),map.get("user_id"),map.get("use_yn"),map.get("expire_dt")));
            	xlog(String.format("[%s] 쿠폰이 3일 후 만료됩니다.",map.get("coupon_id")));
            }
    	}
    	catch (Exception e)
    	{
    		ret += xlog(String.format("##### Exception ##### %s",e.getMessage()));
    		throw e;
    	}
    	finally
    	{
    		xlog("[3일후 만료 쿠폰 조회 finally]");
    	}

    	return list;
    }
    
    /*
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ArrayList<String> list() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("cafe");
        list.add("jjdev");
        return list;
    }
    */
    
}
