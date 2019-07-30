package com.github.FulingBing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.FulingBing.ParasiteData;
import com.github.FulingBing.pojo.ParasitePlayer;

public class ParasiteItem {
	//合成表
	@SuppressWarnings("deprecation")
	public List<Recipe> getRecipe(){
		List<Recipe> re=new ArrayList<Recipe>();
		ItemStack is=new ItemStack(Material.GLASS_BOTTLE);
		ItemMeta im=is.getItemMeta();
		//容器
		im.setDisplayName(ParasiteData.getItemHpName());
		im.setLore(ParasiteData.getItemHpLore());
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { "XYX", "X X", "XXX" }).setIngredient('X',Material.GLASS).setIngredient('Y',Material.ICE));
		im.setDisplayName(ParasiteData.getItemFoName());
		im.setLore(ParasiteData.getItemFoLore());
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { "X X", "X X", "XXX" }).setIngredient('X',Material.GLASS));
		//仪器
		is.setType(Material.getMaterial(ParasiteData.valueBukkit(1)));
		im.setDisplayName(ParasiteData.getItemLoName());
		im.setLore(ParasiteData.getItemLoLore());
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " X ", " Y ", " Z " }).setIngredient('X',Material.DIAMOND).setIngredient('Y',Material.getMaterial(ParasiteData.valueBukkit(1))).setIngredient('Z',Material.COMPASS));
		//药物
		is.setType(Material.getMaterial(ParasiteData.valueBukkit(2)));
		im.setLore(ParasiteData.getItemCureLore());
		im.setDisplayName(ParasiteData.getItemCureName()+ParasiteData.ParasiteName[0]);
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", "   " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.MAGMA_CREAM));//岩浆膏
		im.setDisplayName(ParasiteData.getItemCureName()+ParasiteData.ParasiteName[1]);
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", "   " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.GHAST_TEAR));//恶魂之泪
		im.setDisplayName(ParasiteData.getItemCureName()+ParasiteData.ParasiteName[2]);
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", "   " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.FERMENTED_SPIDER_EYE));//发酵蜘蛛眼
		im.setDisplayName(ParasiteData.getItemCureName()+ParasiteData.ParasiteName[3]);
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", " Z " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.SUGAR).setIngredient('Z',Material.EGG));//糖,鸡蛋
		im.setDisplayName(ParasiteData.getItemCureName()+ParasiteData.ParasiteName[4]);
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", " Z " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.SUGAR).setIngredient('Z',Material.MELON));//糖,西瓜
		im.setDisplayName(ParasiteData.getItemCureName()+ParasiteData.ParasiteName[5]);
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", "   " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.GOLDEN_CARROT));//金胡萝卜
		im.setLore(ParasiteData.getItemNoLore());
		im.setDisplayName(ParasiteData.getItemNoName());
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " X ", "   " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(2))).setIngredient('Y',Material.ICE));//冰
		is.setType(Material.GLOWSTONE_DUST);
		im.setDisplayName(ParasiteData.getItemBtName());
		im.setLore(ParasiteData.getItemBtLore());
		is.setItemMeta(im);
		re.add(new ShapedRecipe(is).shape(new String[] { " Y ", " Z ", " X " }).setIngredient('X',Material.getMaterial(ParasiteData.valueBukkit(3))).setIngredient('Y',Material.SUGAR).setIngredient('Z',Material.GLOWSTONE_DUST));//小麦种子，萤石粉，糖
		return re;
	}
	
	//检测样品
	public ItemStack look(ItemStack is) {
		if(is.getType().equals(Material.POTION)) {
			ItemMeta im=is.getItemMeta();
			if(im.hasDisplayName() && im.hasLore()) {
				if(im.getLore().size()!=3) {
					return null;
				}
				if(!im.getLore().get(2).equals(ParasiteData.getItemLoreUse("").get(2))) {
					return null;
				}
				int type=0;
				if(im.getDisplayName().endsWith(ParasiteData.getItemFoName()) || im.getDisplayName().endsWith(ParasiteData.getItemHpName())) {
					if(!im.getLore().get(0).equals(ParasiteData.getItemHpLore().get(0))) {
						if(im.getDisplayName().endsWith(ParasiteData.getItemFoName())) {
							type=1;
						}
						String name=im.getLore().get(0).replaceAll("§a","");
						long time=(new Date()).getTime()-Long.parseLong(im.getLore().get(1).replaceAll("§e编号: §d",""));
						ItemStack re=new ItemStack(Material.PAPER);
						ItemMeta rim=re.getItemMeta();
						rim.setDisplayName("§b化验报告单");
						List<String> lore=new ArrayList<String>();
						if(time>120000) {
							lore.add("§e这份样本在空气中暴露了太长的时间，已");
							lore.add("§e经完全被污染了，无法用于检测");
						}else {
							for(ParasitePlayer tpp:ParasiteData.userinfo) {
								if(tpp.name.equals(name)) {
									for(int i=0;i<6;i++) {
										String s="§a"+ParasiteData.ParasiteName[i]+"§b: §d";
										boolean jc=true;
										if(type==0) {
											if(i==2 || i==4 || i==5) {
												jc=true;
											}else {
												jc=false;
											}
										}else {
											if(i==2 || i==4 || i==5) {
												jc=false;
											}else {
												jc=true;
											}
										}
										if(jc) {
											if(tpp.time[i]==0) {
												s=s+"未感染";
											}else{
												if(tpp.time[i]<5000) {
													if((Math.random()*100)<ParasiteData.Misdiagnosis) {
														s=s+"未感染";
													}else{
														s=s+"潜伏期";
													}
												}else if(tpp.time[i]>=5000 && tpp.time[i]<18000) {
													s=s+"一阶段";
												}else if(tpp.time[i]>=18000 && tpp.time[i]<20000) {
													s=s+"二阶段";
												}else if(tpp.time[i]>=20000 && tpp.time[i]<22000) {
													s=s+"三阶段";
												}else {
													s=s+"四阶段";
												}
											}
											if(tpp.sequela[i]!=0) {
												s=s+"§a, 似乎曾经尝试过治疗，但留下了后遗症";
											}
										}else {
											s=s+"未检测此项";
										}
										lore.add(s);
									}
									break;
								}
							}
							if(lore.size()==0) {
								for(int i=0;i<6;i++) {
									boolean jc=true;
									if(type==0) {
										if(i==2 || i==5) {
											jc=true;
										}else {
											jc=false;
										}
									}else {
										if(i==2 || i==5) {
											jc=false;
										}else {
											jc=true;
										}
									}
									if(jc) {
										lore.add("§a"+ParasiteData.ParasiteName[i]+"§b: §d未感染");
									}else {
										lore.add("§a"+ParasiteData.ParasiteName[i]+"§b: §d未检测此项");
									}
								}
							}
						}
						rim.setLore(lore);
						re.setItemMeta(rim);
						return re;
					}
				}
			}
		}
		return null;
	}
}
