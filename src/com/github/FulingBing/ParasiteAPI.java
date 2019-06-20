package com.github.FulingBing;

import com.github.FulingBing.pojo.ParasitePlayer;
import com.github.FulingBing.service.ParasiteCore;

public class ParasiteAPI {
	//设置钩虫等级
	public static void setPlayerParasiteHookworm(String name,int num) {
		(new ParasiteCore()).setState(name, 0, num, false);
	}
	//设置蛔虫等级
	public static void setPlayerParasiteRoundworm(String name,int num) {
		(new ParasiteCore()).setState(name, 1, num, false);
	}
	//设置血吸虫等级
	public static void setPlayerParasiteSchistosome(String name,int num) {
		(new ParasiteCore()).setState(name, 2, num, false);
	}
	//设置绦虫等级
	public static void setPlayerParasiteTapeworm(String name,int num) {
		(new ParasiteCore()).setState(name, 3, num, false);
	}
	//设置弓形虫等级
	public static void setPlayerParasiteToxoplasma(String name,int num) {
		(new ParasiteCore()).setState(name, 4, num, false);
	}
	//设置疟原虫等级
	public static void setPlayerParasitePlasmodium(String name,int num) {
		(new ParasiteCore()).setState(name, 5, num, false);
	}
	//查询钩虫等级
	public static int getPlayerParasiteHookworm(String name) {
		return getParasiteTime(name,0,false);
	}
	//查询蛔虫等级
	public static int getPlayerParasiteRoundworm(String name) {
		return getParasiteTime(name,1,false);
	}
	//查询血吸虫等级
	public static int getPlayerParasiteSchistosome(String name) {
		return getParasiteTime(name,2,false);
	}
	//查询绦虫等级
	public static int getPlayerParasiteTapeworm(String name) {
		return getParasiteTime(name,3,false);
	}
	//查询弓形虫等级
	public static int getPlayerParasiteToxoplasma(String name) {
		return getParasiteTime(name,4,false);
	}
	//查询疟原虫等级
	public static int getPlayerParasitePlasmodium(String name) {
		return getParasiteTime(name,5,false);
	}
	//后遗症
	public static void setPlayerParasiteHookwormSequela(String name,int num) {
		(new ParasiteCore()).setState(name, 0, num, true);
	}
	public static void setPlayerParasiteRoundwormSequela(String name,int num) {
		(new ParasiteCore()).setState(name, 1, num, true);
	}
	public static void setPlayerParasiteSchistosomeSequela(String name,int num) {
		(new ParasiteCore()).setState(name, 2, num, true);
	}
	public static void setPlayerParasiteTapewormSequela(String name,int num) {
		(new ParasiteCore()).setState(name, 3, num, true);
	}
	public static void setPlayerParasiteToxoplasmaSequela(String name,int num) {
		(new ParasiteCore()).setState(name, 4, num, true);
	}
	public static void setPlayerParasitePlasmodiumSequela(String name,int num) {
		(new ParasiteCore()).setState(name, 5, num, true);
	}
	public static int getPlayerParasiteHookwormSequela(String name) {
		return getParasiteTime(name,0,true);
	}
	public static int getPlayerParasiteRoundwormSequela(String name) {
		return getParasiteTime(name,1,true);
	}
	public static int getPlayerParasiteSchistosomeSequela(String name) {
		return getParasiteTime(name,2,true);
	}
	public static int getPlayerParasiteTapewormSequela(String name) {
		return getParasiteTime(name,3,true);
	}
	public static int getPlayerParasiteToxoplasmaSequela(String name) {
		return getParasiteTime(name,4,true);
	}
	public static int getPlayerParasitePlasmodiumSequela(String name) {
		return getParasiteTime(name,5,true);
	}
	
	private static int getParasiteTime(String name,int type,boolean hyz) {
		for(ParasitePlayer tpp:ParasiteData.userinfo) {
			if(tpp.name.equals(name)) {
				if(hyz) {
					return tpp.sequela[type];
				}else {
					return tpp.time[type];
				}
			}
		}
		return 0;
	}
}
