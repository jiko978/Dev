package com.example.demo;

public class MetaVo {

	public long id;
	public 	String 	name = "";

	public 	String 	couponId = "";
	public	String	userId = "";
	public	String	useYn = "";
	public 	String 	expireDt = "";
	
	public long getId() {
		return this.id;
		}
	public void setId(long id) {
		this.id = id;
		}
	
	public String getName() { 
		return this.name; 
		}
	public void setName(String name) { 
		this.name = name; 
		}
	
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	public String getExpireDt() {
		return expireDt;
	}
	public void setExpireDt(String expireDt) {
		this.expireDt = expireDt;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
}
