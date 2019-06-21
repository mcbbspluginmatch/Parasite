package com.github.FulingBing.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.FulingBing.ParasiteData;
import com.github.FulingBing.pojo.ParasitePlayer;
import com.github.FulingBing.service.ParasiteCore;
import com.github.FulingBing.service.ParasiteGUI;
import com.github.FulingBing.service.ParasiteItem;

public class ParasiteItemListener implements Listener{
	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerInteractEvent(PlayerInteractEvent e) {
		if((e.getAction()==Action.RIGHT_CLICK_AIR) || (e.getAction()==Action.LEFT_CLICK_AIR) || (e.getAction()==Action.RIGHT_CLICK_BLOCK) || (e.getAction()==Action.LEFT_CLICK_BLOCK) || (e.getAction()==Action.PHYSICAL)){
			ItemStack is=e.getItem();
			if(is==null){
				return;
			}
			//获取样本
			if(is.getType().equals(Material.GLASS_BOTTLE)) {
				ItemMeta im=is.getItemMeta();
				if(im.hasDisplayName() && im.hasLore()) {
					if(im.getDisplayName().endsWith(ParasiteData.getItemHpName()) && im.getLore().equals(ParasiteData.getItemHpLore())) {
						e.setCancelled(true);
						Player pl=e.getPlayer();
						double hp=pl.getHealth();
						if(hp>1) {
							hp--;
						}else {
							pl.sendMessage("§e你剩余的生命值太少，不足以制作样本");
							return;
						}
						pl.setHealth(hp);
						im.setLore(ParasiteData.getItemLoreUse(pl.getName()));
						if(is.getAmount()>1) {
							is.setAmount(is.getAmount()-1);
						}else {
							//is=null;
							try {
								//有双持的mc
								if(e.getHand().equals(EquipmentSlot.HAND)) {
									pl.getInventory().setItemInMainHand(null);
								}else {
									pl.getInventory().setItemInOffHand(null);
								}
							} catch (NoSuchMethodError e2) {
								//没有双持的mc
								pl.getInventory().setItemInHand(null);
							}
						}
						ItemStack fh=new ItemStack(Material.POTION);
						fh.setItemMeta(im);
						fh.setAmount(1);
						pl.getInventory().addItem(new ItemStack[] {fh});
						pl.updateInventory();
						return;
					}
					if(im.getDisplayName().endsWith(ParasiteData.getItemFoName()) && im.getLore().equals(ParasiteData.getItemFoLore())) {
						e.setCancelled(true);
						Player pl=e.getPlayer();
						int fo=pl.getFoodLevel();
						if(fo>1) {
							fo--;
						}else {
							pl.sendMessage("§e你剩余的饥饿值太少，不足以制作样本");
							return;
						}
						pl.setFoodLevel(fo);
						im.setLore(ParasiteData.getItemLoreUse(pl.getName()));
						if(is.getAmount()>1) {
							is.setAmount(is.getAmount()-1);
						}else {
							//is=null;
							try {
								if(e.getHand().equals(EquipmentSlot.HAND)) {
									pl.getInventory().setItemInMainHand(null);
								}else {
									pl.getInventory().setItemInOffHand(null);
								}
							} catch (NoSuchMethodError e2) {
								pl.getInventory().setItemInHand(null);
							}
						}
						ItemStack fh=new ItemStack(Material.POTION);
						fh.setItemMeta(im);
						fh.setAmount(1);
						pl.getInventory().addItem(new ItemStack[] {fh});
						pl.updateInventory();
						return;
					}
				}
				return;
			}
			//检测样本
			if(is.getType().name().startsWith(ParasiteData.fuckBukkit(1))) {
				ItemMeta im=is.getItemMeta();
				if(im.hasDisplayName() && im.hasLore()) {
					if(im.getDisplayName().endsWith(ParasiteData.getItemLoName()) && im.getLore().equals(ParasiteData.getItemLoLore())) {
						e.setCancelled(true);
						Player pl=e.getPlayer();
						pl.closeInventory();
						ParasiteGUI pg=new ParasiteGUI();
						pl.openInventory(pg.openLook(pl));
						return;
					}
				}
				return;
			}
			//药物
			if(is.getType().equals(Material.getMaterial(ParasiteData.fuckBukkit(2)))) {
				ItemMeta im=is.getItemMeta();
				if(im.hasDisplayName() && im.hasLore()) {
					//治疗
					if(im.getDisplayName().contains(ParasiteData.getItemCureName()) && im.getLore().equals(ParasiteData.getItemCureLore())) {
						ParasiteCore pc=new ParasiteCore();
						int id=pc.getParasIdBuName(im.getDisplayName().replaceAll(ParasiteData.getItemCureName(),""));
						if(id!=-1) {
							e.setCancelled(true);
							if(is.getAmount()>1) {
								is.setAmount(is.getAmount()-1);
							}else {
								//is=null;
								try {
									if(e.getHand().equals(EquipmentSlot.HAND)) {
										e.getPlayer().getInventory().setItemInMainHand(null);
									}else {
										e.getPlayer().getInventory().setItemInOffHand(null);
									}
								} catch (NoSuchMethodError e2) {
									e.getPlayer().getInventory().setItemInHand(null);
								}
							}
							pc.CureUser(e.getPlayer(), id);
						}
						return;
					}
					//预防
					if(im.getDisplayName().endsWith(ParasiteData.getItemNoName()) && im.getLore().equals(ParasiteData.getItemNoLore())) {
						boolean have=false;
						for(int i=0;i<ParasiteData.noParasite.size();i++) {
							String str=ParasiteData.noParasite.get(i);
							if(str.startsWith(e.getPlayer().getName()+",")) {
								ParasiteData.noParasite.set(i, e.getPlayer().getName()+",100");
								have=true;
								break;
							}
						}
						if(!have) {
							ParasiteData.noParasite.add(e.getPlayer().getName()+",100");
						}
						e.setCancelled(true);
						if(is.getAmount()>1) {
							is.setAmount(is.getAmount()-1);
						}else {
							//is=null;
							try {
								if(e.getHand().equals(EquipmentSlot.HAND)) {
									e.getPlayer().getInventory().setItemInMainHand(null);
								}else {
									e.getPlayer().getInventory().setItemInOffHand(null);
								}
							} catch (NoSuchMethodError e2) {
								e.getPlayer().getInventory().setItemInHand(null);
							}
						}
					}
				}
				return;
			}
			//治疗（宝塔糖）
			if(is.getType().equals(Material.GLOWSTONE_DUST)) {
				ItemMeta im=is.getItemMeta();
				if(im.hasDisplayName() && im.hasLore()) {
					if(im.getDisplayName().endsWith(ParasiteData.getItemBtName()) && im.getLore().equals(ParasiteData.getItemBtLore())) {
						e.setCancelled(true);
						if(is.getAmount()>1) {
							is.setAmount(is.getAmount()-1);
						}else {
							//is=null;
							try {
								if(e.getHand().equals(EquipmentSlot.HAND)) {
									e.getPlayer().getInventory().setItemInMainHand(null);
								}else {
									e.getPlayer().getInventory().setItemInOffHand(null);
								}
							} catch (NoSuchMethodError e2) {
								e.getPlayer().getInventory().setItemInHand(null);
							}
						}
						String name=e.getPlayer().getName();
						for(int i=0;i<ParasiteData.userinfo.size();i++) {
							ParasitePlayer pp=ParasiteData.userinfo.get(i);
							if(pp.name.equals(name)) {
								if(pp.time[0]<18000) {
									pp.time[0]-=100;
									if(pp.time[0]<0) {
										pp.time[0]=0;
									}
								}
								if(pp.time[1]<18000) {
									pp.time[1]-=100;
									if(pp.time[1]<0) {
										pp.time[1]=0;
									}
								}
								if(pp.time[3]<18000) {
									pp.time[3]-=100;
									if(pp.time[3]<0) {
										pp.time[3]=0;
									}
								}
								ParasiteData.userinfo.set(i,pp);
								return;
							}
						}
					}
				}
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH,ignoreCancelled=true)
	public void InventoryClickEvent(InventoryClickEvent e) {
		if(e.isCancelled()) {
			return;
		}
		String title=e.getInventory().getTitle();
		if(title==null) {
			return;
		}
		if(title.endsWith(ParasiteData.getGUITitle()) && e.getInventory().getSize()==27) {
			//检测样本
			int i=e.getRawSlot();
			if(i<27) {
				if(i==10 || i==13 || i==16) {
					Player pl=(Player)e.getWhoClicked();
					if(i==13) {
						e.setCancelled(true);
						ItemStack yb=e.getInventory().getItem(10);
						if(yb==null) {
							return;
						}
						if(e.getInventory().getItem(16)!=null) {
							return;
						}
						ItemStack bg=(new ParasiteItem().look(yb));
						if(bg==null) {
							return;
						}
						e.getInventory().setItem(10, null);
						e.getInventory().setItem(16, bg);
						pl.updateInventory();
					}else if(i==10) {
						/*ItemStack is=pl.getItemOnCursor();
						if(is==null) {
							return;
						}
						if(is.getType().equals(Material.POTION)) {
							ItemMeta im=is.getItemMeta();
							if(im.hasDisplayName() && im.hasLore()) {
								if(im.getDisplayName().equals(ParasiteData.getItemFoName()) || im.getDisplayName().equals(ParasiteData.getItemHpName())) {
									if(!im.getLore().get(0).equals(ParasiteData.getItemHpLore().get(0))) {
										return;
									}
								}
							}
						}
						e.setCancelled(true);*/
					}else {
						//ItemStack is=pl.getItemOnCursor();
						//if(is!=null) {
						//	e.setCancelled(true);
						//}
					}
				}else{
					e.setCancelled(true);
				}
			}
		}
	}
}
