package com.adweb.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adweb.app.entity.Collect;
import com.adweb.app.entity.Footstep;
import com.adweb.app.entity.Item;
import com.adweb.app.entity.Location;
import com.adweb.app.entity.Share;
import com.adweb.app.entity.User;
import com.adweb.app.entity.Wanted;
import com.adweb.app.model.GetItemListForm;
import com.adweb.app.model.SendItemForm;
import com.adweb.app.model.SendItemListForm;
import com.adweb.app.model.ToForm;
import com.adweb.app.service.CollectService;
import com.adweb.app.service.FootstepService;
import com.adweb.app.service.ItemService;
import com.adweb.app.service.LocationService;
import com.adweb.app.service.RatingService;
import com.adweb.app.service.ShareService;
import com.adweb.app.service.UserService;
import com.adweb.app.service.WantedService;

@Controller
public class ItemController {

	@Autowired ItemService itemService;
	
	@Autowired LocationService locationService;
	
	@Autowired UserService userService;
	
	@Autowired CollectService collectService;
	
	@Autowired ShareService shareService;
	
	@Autowired FootstepService footstepService;
	
	@Autowired WantedService wantedService;
	
	@Autowired RatingService ratingService;
	
	//用户将一个item收藏
	@RequestMapping(value = "/toCollect")
	public @ResponseBody void toCollect(@RequestBody ToForm toForm){
		Item item = this.itemService.findById(toForm.getItemId());
		User user = this.userService.findByUsername(toForm.getUsername());
		
		item.setCollect(item.getCollect() + 1);
		this.itemService.create(item);
		
		Collect collect = new Collect();
		collect.setItem(item);
		collect.setUser(user);
		
		this.collectService.create(collect);
	}

	//用户将一个item分享
	@RequestMapping(value = "/toShare")
	public @ResponseBody void toShare(@RequestBody ToForm toForm){
		Item item = this.itemService.findById(toForm.getItemId());
		User user = this.userService.findByUsername(toForm.getUsername());
		
		item.setShare(item.getShare() + 1);
		this.itemService.create(item);
		
		Share share = new Share();
		share.setItem(item);
		share.setUser(user);
		
		this.shareService.create(share);
	}
	
	//用户将一个item标记为足迹
	@RequestMapping(value = "/toStep")
	public @ResponseBody void toStep(@RequestBody ToForm toForm){
		Item item = this.itemService.findById(toForm.getItemId());
		User user = this.userService.findByUsername(toForm.getUsername());
		
		item.setFootstep(item.getFootstep() + 1);
		this.itemService.create(item);
		
		Footstep footstep = new Footstep();
		footstep.setItem(item);
		footstep.setUser(user);
		this.footstepService.create(footstep);
	}
	
	//用户将一个item加入心愿
	@RequestMapping(value = "/toWanted")
	public @ResponseBody void toWanted(@RequestBody ToForm toForm){
		Item item = this.itemService.findById(toForm.getItemId());
		User user = this.userService.findByUsername(toForm.getUsername());
		
		item.setWanted(item.getWanted() + 1);
		this.itemService.create(item);
		
		Wanted wanted = new Wanted();
		wanted.setItem(item);
		wanted.setUser(user);
		this.wantedService.create(wanted);
	}
	
	//前端传来itemId,返回所属的Item
	
	@RequestMapping(value = "/getItemById/{itemId}")
	public @ResponseBody SendItemForm getItemById(@PathVariable int itemId){
		Item item = this.itemService.findById(itemId);
		SendItemForm form = new SendItemForm();
		form.setBaseContent(item.getBasecontent());
		form.setName(item.getName());
		form.setFiveStar(this.ratingService.calculateFiveStarCountForItem(item));
		form.setFourStar(this.ratingService.calculateFourStarCountForItem(item));
		form.setThreeStar(this.ratingService.calculateThreeStarCountForItem(item));
		form.setTwoStar(this.ratingService.calculateTwoStarCountForItem(item));
		form.setOneStar(this.ratingService.calculateOneStarCountForItem(item));
		return form;
	}
	
	//前端传来locationName,返回所有属于该location的item
	@RequestMapping(value = "/getItemList/normal")
	public @ResponseBody List<Item> getItemListNormal(@RequestBody GetItemListForm getItemListForm){
		Location location = this.locationService.findByName(getItemListForm.getLocationName());
		List<Item> itemList = this.itemService.findByLocation(location);
		return itemList;
	}
	//前端传来locationName,按照平均打分返回属于该location的item
	@RequestMapping(value = "/getItemList/averageRating")
	public @ResponseBody List<SendItemListForm> getItemListWithRating(@RequestBody GetItemListForm getItemListForm){
		Location location = this.locationService.findByName(getItemListForm.getLocationName());
		List<Item> itemList = this.itemService.findByLocation(location);
		return sort(itemList,0);
	}
	//前端传来locationName,按照收藏数返回属于该location的item
	@RequestMapping(value = "/getItemList/collect")
	public @ResponseBody List<SendItemListForm> getItemListWithCollect(@RequestBody GetItemListForm getItemListForm){
		Location location = this.locationService.findByName(getItemListForm.getLocationName());
		List<Item> itemList = this.itemService.findByLocation(location);
		return sort(itemList,1);
	}
	//前端传来locationName,按照足迹数返回属于该location的item
	@RequestMapping(value = "/getItemList/footstep")
	public @ResponseBody List<SendItemListForm> getItemListWithFootstep(@RequestBody GetItemListForm getItemListForm){
		Location location = this.locationService.findByName(getItemListForm.getLocationName());
		List<Item> itemList = this.itemService.findByLocation(location);
		return sort(itemList,2);
	}
	//前端传来locationName,按照心愿数返回属于该location的item
	@RequestMapping(value = "/getItemList/wanted")
	public @ResponseBody List<SendItemListForm> getItemListWithWanted(@RequestBody GetItemListForm getItemListForm){
		Location location = this.locationService.findByName(getItemListForm.getLocationName());
		List<Item> itemList = this.itemService.findByLocation(location);
		return sort(itemList,3);
	}
	
	@RequestMapping(value = "/getItemList/recommend")
	public @ResponseBody List<SendItemListForm> getItemListWithRecommend(@RequestBody GetItemListForm getItemListForm){
		Location location = this.locationService.findByName(getItemListForm.getLocationName());
		List<Item> itemList = this.itemService.findByLocation(location);
		// need to do 
		return null;
	}
	
	//返回所有数据库内item
	@RequestMapping(value = "/getAllItemList")
	public @ResponseBody List<SendItemListForm> getAllItemList(){
		List<Item> itemList = this.itemService.findAll();
		return sort(itemList,4);
	}
	
	//按照平均打分的多少返回所有item
	@RequestMapping(value = "/getAllItemList/averageRating")
	public @ResponseBody List<SendItemListForm> getAllItemListByRating(){
		List<Item> itemList = this.itemService.findAll();
		return sort(itemList,0);
	}
	
	//按照收藏数的多少返回所有item
	@RequestMapping(value = "/getAllItemList/collect")
	public @ResponseBody List<SendItemListForm> getAllItemListByCollect(){
		List<Item> itemList = this.itemService.findAll();
		return sort(itemList,1);
	}
	
	//按照足迹数的多少返回所有item
	@RequestMapping(value = "/getAllItemList/footstep")
	public @ResponseBody List<SendItemListForm> getAllItemListByFootstep(){
		List<Item> itemList = this.itemService.findAll();
		return sort(itemList,2);
	}
	
	//按照心愿数的多少返回所有item
	@RequestMapping(value = "/getAllItemList/wanted")
	public @ResponseBody List<SendItemListForm> getAllItemListByWanted(){
		List<Item> itemList = this.itemService.findAll();
		return sort(itemList,3);
	}
	
	public List<SendItemListForm> sort(List<Item> itemList,int mode){
		List<SendItemListForm> sendItemListForm = new ArrayList<SendItemListForm>();
		
		for(Item item : itemList){
			SendItemListForm itemForm = new SendItemListForm();
			itemForm.setId(item.getId());
			itemForm.setName(item.getName());
			itemForm.setCollect(item.getCollect());
			itemForm.setFootstep(item.getFootstep());
			itemForm.setWanted(item.getWanted());
			itemForm.setAverageRating(this.itemService.getAverageRating(item));
			sendItemListForm.add(itemForm);
		}
			
		// 按照AverageRating大小降序排序
		if (mode == 0){
			 for(int i=0;i<sendItemListForm.size();i++){  
		         for(int j=i+1;j<sendItemListForm.size();j++){  
			         if(sendItemListForm.get(i).getAverageRating() < sendItemListForm.get(j).getAverageRating()){  
			             swap(sendItemListForm,i,j);  
			         }  
			     }
			 }
		}
		
		// 按照Collect次数大小降序排列
		if(mode == 1){
			 for(int i=0;i<sendItemListForm.size();i++){  
		         for(int j=i+1;j<sendItemListForm.size();j++){  
			         if(sendItemListForm.get(i).getCollect() < sendItemListForm.get(j).getCollect()){  
			             swap(sendItemListForm,i,j);  
			         }  
			     }
			 }
		}
		
		// 按照footstep次数大小降序排列
		if(mode == 2){
			 for(int i=0;i<sendItemListForm.size();i++){  
		         for(int j=i+1;j<sendItemListForm.size();j++){  
			         if(sendItemListForm.get(i).getFootstep() < sendItemListForm.get(j).getFootstep()){  
			             swap(sendItemListForm,i,j);  
			         }  
			     }
			 }
		}
		
		// 按照wanted次数大小降序排列
		if(mode == 3){
			for(int i=0;i<sendItemListForm.size();i++){  
				for(int j=i+1;j<sendItemListForm.size();j++){  
					if(sendItemListForm.get(i).getWanted() < sendItemListForm.get(j).getWanted()){  
						swap(sendItemListForm,i,j);  
					}  
				}
			}
		}
		
		return sendItemListForm;
	}
	
	public static void swap(List<SendItemListForm> list,int a, int b){
		SendItemListForm objectA = list.get(a);
		list.set(a, list.get(b));
		list.set(b, objectA);
	}
	
}
