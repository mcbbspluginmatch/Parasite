package com.github.FulingBing.service;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.FulingBing.ParasiteData;

public class ParasiteGUI {
	//检测仪器的界面
	public Inventory openLook(Player pl) {
		Inventory re=Bukkit.createInventory(pl,27,ParasiteData.getGUITitle());
		ItemStack is=new ItemStack(Material.getMaterial(ParasiteData.valueBukkit(0)));
		//用个for吧 —— mimimis
		re.setItem(3, is);
		re.setItem(4, is);
		re.setItem(5, is);
		re.setItem(12, is);
		re.setItem(14, is);
		re.setItem(21, is);
		re.setItem(22, is);
		re.setItem(23, is);
		re.setItem(6, is);
		re.setItem(7, is);
		re.setItem(8, is);
		re.setItem(15, is);
		re.setItem(17, is);
		re.setItem(24, is);
		re.setItem(25, is);
		re.setItem(26, is);
		ItemMeta im=is.getItemMeta();
		im.setDisplayName("§b在此处放入样本");
		is.setItemMeta(im);
		re.setItem(0, is);
		re.setItem(1, is);
		re.setItem(2, is);
		re.setItem(9, is);
		re.setItem(11, is);
		re.setItem(18, is);
		re.setItem(19, is);
		re.setItem(20, is);
		is.setType(Material.REDSTONE_BLOCK);
		im.setDisplayName("§a点击检测");
		is.setItemMeta(im);
		re.setItem(13, is);
		return re;
	}
}
