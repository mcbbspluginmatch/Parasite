package com.github.FulingBing.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.FulingBing.ParasiteData;
import com.github.FulingBing.pojo.ParasitePlayer;
import com.github.FulingBing.service.ParasiteCore;

public class ParasitePlayerListener implements Listener{
	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
	public void PlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
		if(e.isCancelled()) {
			return;
		}
		ItemStack is=e.getItem();
		if(is==null) {
			return;
		}
		String type=is.getType().name();
		ParasiteCore pc=new ParasiteCore();
		String name=e.getPlayer().getName();
		//ºÈÑù±¾
		if(type.equals("POTION")) {
			ItemMeta im=is.getItemMeta();
			if(im.hasDisplayName() && im.hasLore()) {
				if(im.getDisplayName().equals(ParasiteData.getItemHpName()) || im.getDisplayName().equals(ParasiteData.getItemFoName())) {
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON,2400,1));
					return;
				}
			}
		}
		if(((int)(Math.random()*100))<pc.getNoParas(name)) {
			return;
		}
		//ÉúµÄÊ³Îï
		for(String tmp:ParasiteData.RawFool) {
			if(tmp.equals(type)) {
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,0);//¹³³æ
				}
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,1);//»×³æ
				}
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,4);//¹­ÐÎ³æ
				}
				break;
			}
		}
		//°¹Ôà/¸¯ÀÃµÄÊ³Îï
		for(String tmp:ParasiteData.DirtyFool) {
			if(tmp.equals(type)) {
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,3);//ÌÐ³æ
				}
				break;
			}
		}
		//Ë®¡¢Ë®¹û¡¢Êß²Ë
		for(String tmp:ParasiteData.BotanyFool) {
			if(tmp.equals(type)) {
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,0);//¹³³æ
				}
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,1);//»×³æ
				}
				if((Math.random()*100)<ParasiteData.ItemProbability) {
					pc.addState(name,3);//ÌÐ³æ
				}
				break;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
	public void PlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		if(e.isCancelled()) {
			return;
		}
		if((Math.random()*100)<ParasiteData.ItemProbability) {
			if(((int)(Math.random()*100))<(new ParasiteCore()).getNoParas(e.getPlayer().getName())) {
				return;
			}
			String type=e.getRightClicked().getType().name();
			for(String tmp:ParasiteData.Entity) {
				if(tmp.equals(type)) {
					(new ParasiteCore()).addState(e.getPlayer().getName(),4);//¹­ÐÎ³æ
					return;
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
	public void PlayerDeathEvent(PlayerDeathEvent e) {
		String name=e.getEntity().getName();
		for(int i=0;i<ParasiteData.userinfo.size();i++) {
			if(ParasiteData.userinfo.get(i).name.equals(name)) {
				if(ParasiteData.DeathClear) {
					ParasiteData.userinfo.remove(i);
				}else{
					ParasitePlayer pp=ParasiteData.userinfo.get(i);
					for(int k=0;k<6;k++) {
						pp.time[k]=ParasiteCore.removeLv(pp.time[k]);
						pp.sequela[k]=ParasiteCore.removeLv(pp.sequela[k]);
					}
					ParasiteData.userinfo.set(i,pp);
				}
				return;
			}
		}
	}
}
