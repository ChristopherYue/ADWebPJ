package com.adweb.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
/*
 * Item 类表示一个具体项目（景点）
 */
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//景点名字
	@Column
	private String name;
	
	//经度
	@Column
	private float longitude;
	
	//纬度
	@Column
	private float latitude;
	
	//表示item所属于的location
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;
	
	//被收藏数
	@Column
	private int collect;
	
	//足迹(被去过的次数)
	@Column
	private int footstep;
	
	//心愿(被加入心愿单的次数)
	@Column
	private int wanted;
	
	@Column
	private String basecontent;
	
	public String getBasecontent() {
		return basecontent;
	}

	public void setBasecontent(String basecontent) {
		this.basecontent = basecontent;
	}

	//被分享的次数
	@Column
	private int share;
	
	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getWanted() {
		return wanted;
	}

	public void setWanted(int wanted) {
		this.wanted = wanted;
	}

	public int getFootstep() {
		return footstep;
	}

	public void setFootstep(int footstep) {
		this.footstep = footstep;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
