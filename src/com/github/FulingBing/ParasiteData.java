package com.github.FulingBing;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.github.FulingBing.pojo.ParasitePlayer;
import com.github.FulingBing.pojo.ParasiteState;

public class ParasiteData {
	public static String[] ParasiteName={"钩虫","蛔虫","血吸虫","绦虫","弓形虫","疟原虫"};//寄生虫的名字

	//public static FileConfiguration data;//玩家的数据文件
	public static File file;//玩家的数据文件
	public static List<ParasitePlayer> userinfo=new ArrayList<ParasitePlayer>();//玩家的感染信息
	public static List<ParasiteState> userstate=new ArrayList<ParasiteState>();//玩家的状态信息
	public static List<String> noParasite=new ArrayList<String>();//预防
	public static int runtime_1;//循环周期
	public static int runtime_5;//循环周期
	public static int runtime_300;//循环周期
	
	public static Plugin plugin;
	
	public static double ItemProbability;//感染几率（物品）
	public static double LocProbability;//感染几率（地方）
	public static double Misdiagnosis;//误诊几率
	
	public static boolean DeathClear;//死亡后是否清除感染
	public static List<String> RawFool=new ArrayList<String>();//生的食物
	public static List<String> DirtyFool=new ArrayList<String>();//肮脏/腐烂的食物
	public static List<String> BotanyFool=new ArrayList<String>();//水、水果、蔬菜
	public static List<String> Entity=new ArrayList<String>();//实体
	public static List<String> OceanBiomes=new ArrayList<String>();//海洋
	public static List<String> DampBiomes=new ArrayList<String>();//温暖潮湿的地方
	public static List<String> Water=new ArrayList<String>();//水源
	
	private static String typeGLASS_PANE=null;
	private static String typeCOMPARATOR=null;
	private static String typeSNOWBALL=null;
	private static String typeWHEAT_SEEDS=null;
	public static String fuckBukkit(int fuck) {
		if(typeGLASS_PANE==null) {
			Material[] lm=Material.values();
			for(Material m:lm) {
				if(m.name().equals("COMPARATOR")) {
					typeGLASS_PANE="GLASS_PANE";
					typeCOMPARATOR="COMPARATOR";
					typeSNOWBALL="SNOWBALL";
					typeWHEAT_SEEDS="WHEAT_SEEDS";
					break;
				}
			}
		}
		if(typeGLASS_PANE==null) {
			typeGLASS_PANE="STAINED_GLASS_PANE";
			typeCOMPARATOR="REDSTONE_COMPARATOR";
			typeSNOWBALL="SNOW_BALL";
			typeWHEAT_SEEDS="SEEDS";
		}
		switch (fuck) {
		case 0:
			return typeGLASS_PANE;
		case 1:
			return typeCOMPARATOR;
		case 2:
			return typeSNOWBALL;
		default:
			return typeWHEAT_SEEDS;
		}
	}
	
	//物品
	public static String getItemCureName() {
		return "§b寄生虫治疗药物 §c";
	}
	public static String getItemHpName() {
		return "§c试管 §d[§b血液样本§d]";
	}
	public static String getItemFoName() {
		return "§c试管 §d[§b粪便样本§d]";
	}
	public static String getItemLoName() {
		return "§a样本检测仪";
	}
	public static String getItemNoName() {
		return "§b寄生虫预防药物";
	}
	public static String getItemBtName() {
		return "§e宝塔糖";
	}
	public static List<String> getItemCureLore() {
		List<String> re=new ArrayList<String>();
		re.add("§a可一定程度上减少体内寄生虫的数量");
		re.add("§e若未感染对应的寄生虫，则服用此药会中毒");
		re.add("§b点击服用");
		return re;
	}
	public static List<String> getItemHpLore() {
		List<String> re=new ArrayList<String>();
		re.add("§a空的");
		re.add("§e消耗1点生命值,以便往里面装入血液");
		re.add("§b点击使用");
		return re;
	}
	public static List<String> getItemFoLore() {
		List<String> re=new ArrayList<String>();
		re.add("§a空的");
		re.add("§e消耗1点饥饿度,以便往里面装入粪便");
		re.add("§b点击使用");
		return re;
	}
	public static List<String> getItemLoreUse(String name) {
		List<String> re=new ArrayList<String>();
		re.add("§a"+name);
		re.add("§e编号: §d"+(new Date()).getTime());
		re.add("§b千万别把这东西喝了");
		return re;
	}
	public static List<String> getItemLoLore() {
		List<String> re=new ArrayList<String>();
		re.add("§d检测寄生虫的仪器");
		re.add("§b点击打开检测界面");
		return re;
	}
	public static List<String> getItemNoLore() {
		List<String> re=new ArrayList<String>();
		re.add("§a可短时间内减少感染寄生虫的几率");
		re.add("§e随着时间的流逝，药效会逐渐减弱");
		re.add("§b点击服用");
		return re;
	}
	public static List<String> getItemBtLore() {
		List<String> re=new ArrayList<String>();
		re.add("§a可小幅度减少肠道内的寄生虫的数量");
		re.add("§e若感染程度发展到二阶段，则药效微乎其微");
		re.add("§b点击服用");
		return re;
	}
	
	//GUI
	public static String getGUITitle() {
		return "§9样本检测仪";
	}
}
