package com.github.FulingBing.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.FulingBing.ParasiteData;
import com.github.FulingBing.pojo.ParasiteEffect;
import com.github.FulingBing.pojo.ParasitePlayer;
import com.github.FulingBing.pojo.ParasiteState;

public class ParasiteCore {
	//玩家数据转换为字符串，用于文件存储
	public static String DataToStr(ParasitePlayer pp) {
		return pp.name+","+pp.time[0]+","+pp.time[1]+","+pp.time[2]+","+pp.time[3]+","+pp.time[4]+","+pp.time[5]+","
				+pp.sequela[0]+","+pp.sequela[1]+","+pp.sequela[2]+","+pp.sequela[3]+","+pp.sequela[4]+","+pp.sequela[5];
	}
	
	//字符串转换为玩家数据，用于读取存储的数据
	public static ParasitePlayer StrToData(String str) {
		ParasitePlayer re=new ParasitePlayer();
		String[] i=str.split(",");
		re.name=i[0];
		re.time[0]=Integer.parseInt(i[1]);
		re.time[1]=Integer.parseInt(i[2]);
		re.time[2]=Integer.parseInt(i[3]);
		re.time[3]=Integer.parseInt(i[4]);
		re.time[4]=Integer.parseInt(i[5]);
		re.time[5]=Integer.parseInt(i[6]);
		re.sequela[0]=Integer.parseInt(i[7]);
		re.sequela[1]=Integer.parseInt(i[8]);
		re.sequela[2]=Integer.parseInt(i[9]);
		re.sequela[3]=Integer.parseInt(i[10]);
		re.sequela[4]=Integer.parseInt(i[11]);
		re.sequela[5]=Integer.parseInt(i[12]);
		return re;
	}
	
	//获取玩家状态
	public ParasiteState getState(ParasitePlayer pp,Player pl) {
		ParasiteState re=new ParasiteState();
		re.pl=pl;
		re.maxHp=100;
		for(int i=0;i<6;i++) {
			if(pp.time[i]==0 && pp.sequela[i]==0) {
				re.state[i]=0;
			}else{
				int maxi=pp.time[i]>pp.sequela[i]?pp.time[i]:pp.sequela[i];
				re.state[i]=(int)((Math.atan(0.0007*(maxi-20145))+1.5)*40);
			}
			int hp=getmaxHp(i,re.state[i]);
			if(re.maxHp>hp) {
				re.maxHp=hp;
			}
			if(i<4) {
				re.isBedFool=true;
			}
		}
		return re;
	}
	
	//玩家被感染
	public void addState(String name,int type) {
		for(int i=0;i<ParasiteData.userinfo.size();i++) {
			ParasitePlayer pp=ParasiteData.userinfo.get(i);
			if(pp.name.equals(name)) {
				if(pp.time[type]>19900) {
					//发展到中期时，感染是否加重已经无所谓了
					return;
				}else{
					pp.time[type]++;
					ParasiteData.userinfo.set(i, pp);
					return;
				}
			}
		}
		ParasitePlayer pp=new ParasitePlayer();
		pp.name=name;
		pp.time[type]=1;
		ParasiteData.userinfo.add(pp);
	}
	
	//设置玩家的感染状况
	public void setState(String name,int type,int time,boolean hyz) {
		if(time<0) {
			time=0;
		}
		if(time>30000) {
			time=30000;
		}
		for(int i=0;i<ParasiteData.userinfo.size();i++) {
			ParasitePlayer pp=ParasiteData.userinfo.get(i);
			if(pp.name.equals(name)) {
				if(hyz) {
					pp.sequela[type]=time;
				}else {
					pp.time[type]=time;
				}
				ParasiteData.userinfo.set(i, pp);
				return;
			}
		}
		ParasitePlayer pp=new ParasitePlayer();
		pp.name=name;
		if(hyz) {
			pp.sequela[type]=time;
		}else {
			pp.time[type]=time;
		}
		ParasiteData.userinfo.add(pp);
	}
	
	//获取玩家贫血等级
	private int getmaxHp(int type,int lv) {
		if(type==0 || type==5) {
			if(lv>90) {
				lv=90;
			}
			return 100-lv;
		}else{
			return 100;
		}
	}
	
	//根据寄生虫类型，给与药水状态
	public ParasiteEffect getEffect(ParasiteState ps) {
		if(!ps.pl.isOnline()) {
			return null;
		}
		List<PotionEffect> le=new ArrayList<PotionEffect>();
		boolean isFire=false;//是否着火（发热/发烧症状）
		for(int i=0;i<6;i++) {
			if(((int)(Math.random()*100+1))<ps.state[i]) {
				int l=getEffLv(ps.state[i]);
				boolean[] isok;
				switch (i) {
				case 0:
					isok=getIsOk(4,ps.state[i]);
					if(isok[0]) le.add(new PotionEffect(PotionEffectType.WEAKNESS,120,l));//虚弱
					if(isok[1]) le.add(new PotionEffect(PotionEffectType.CONFUSION,120,l));//反胃
					if(isok[2]) le.add(new PotionEffect(PotionEffectType.HUNGER,120,l));//饥饿
					if(isok[3]) le.add(new PotionEffect(PotionEffectType.HARM,1,0));//瞬间伤害
					break;
				case 1:
					isok=getIsOk(5,ps.state[i]);
					if(isok[0]) le.add(new PotionEffect(PotionEffectType.CONFUSION,120,l));//反胃
					if(isok[1]) le.add(new PotionEffect(PotionEffectType.HUNGER,120,l));//饥饿
					if(isok[2]) le.add(new PotionEffect(PotionEffectType.SLOW_DIGGING,120,l));//挖掘疲劳
					if(isok[3]) le.add(new PotionEffect(PotionEffectType.BLINDNESS,120,l));//失明
					if(isok[4]) le.add(new PotionEffect(PotionEffectType.HARM,1,0));//瞬间伤害
					break;
				case 2:
					isok=getIsOk(3,ps.state[i]);
					if(isok[0]) le.add(new PotionEffect(PotionEffectType.CONFUSION,120,l));//反胃
					if(isok[1]) le.add(new PotionEffect(PotionEffectType.HUNGER,120,l));//饥饿
					if(isok[2]) le.add(new PotionEffect(PotionEffectType.HARM,1,0));//瞬间伤害
					break;
				case 3:
					isok=getIsOk(4,ps.state[i]);
					if(isok[0]) le.add(new PotionEffect(PotionEffectType.CONFUSION,120,l));//反胃
					if(isok[1]) le.add(new PotionEffect(PotionEffectType.HUNGER,120,l));//饥饿
					if(isok[2]) le.add(new PotionEffect(PotionEffectType.WEAKNESS,120,l));//虚弱
					if(isok[3]) le.add(new PotionEffect(PotionEffectType.BLINDNESS,120,l));//失明
					break;
				case 4:
					isok=getIsOk(2,ps.state[i]);
					if(isok[0]) le.add(new PotionEffect(PotionEffectType.WEAKNESS,120,l));//虚弱
					if(isok[1]) le.add(new PotionEffect(PotionEffectType.HARM,1,0));//瞬间伤害
					break;
				default:
					if(ps.state[i]>99) {
						le.add(new PotionEffect(PotionEffectType.WITHER,120,l));//凋零（已发展为疟疾）
					}
					break;
				}
				if(i==0 || ps.state[i]<50) {
					isFire=false;
				}else{
					isFire=true;
				}
			}
		}
		//合并重复的状态
		List<PotionEffect> over=new ArrayList<PotionEffect>();
		for(PotionEffect pe:le) {
			boolean have=false;
			for(int i=0;i<over.size();i++) {
				PotionEffect t=over.get(i);
				if(t.getType().equals(pe.getType())) {
					t=new PotionEffect(t.getType(),120,t.getAmplifier()+pe.getAmplifier());
					over.set(i,t);
					have=true;
					break;
				}
			}
			if(!have) {
				over.add(pe);
			}
		}
		ParasiteEffect re=new ParasiteEffect();
		re.effect=over;
		re.isFire=isFire;
		return re;
	}
	
	private boolean[] getIsOk(int n,int max) {
		boolean[] re={false,false,false,false,false,false};
		boolean have=false;
		for(int i=0;i<n;i++) {
			re[i]=((int)(Math.random()*100+1))<max;
			if(re[i]) {
				have=true;
			}
		}
		if(!have) {
			re[(int)(Math.random()*n)]=true;
		}
		return re;
	}
	
	private int getEffLv(int lv) {
		if(lv<50) {
			return 0;
		}else if(lv>=50 && lv<100) {
			return 1;
		}else {
			return 2;
		}
	}
	
	//设置玩家状态
	public void setEffect(ParasiteState ps) {
		if(!ps.pl.isOnline()) {
			return;
		}
		ParasiteEffect pe=ps.effect;
		//着火
		if(pe.isFire) {
			int i=ps.pl.getFireTicks();
			if(i<120) {
				ps.pl.setFireTicks(120);
			}
		}
		//状态
		List<PotionEffect> e=pe.effect;
		Collection<PotionEffect> cpe=ps.pl.getActivePotionEffects();
		for(PotionEffect eff:e) {
			//兼容旧版本的mc
			PotionEffect tmp=getPotionEffectByList(cpe,eff.getType());
			if(tmp!=null) {
				if(tmp.getAmplifier()>eff.getAmplifier()) {
					eff=new PotionEffect(eff.getType(),eff.getDuration(),tmp.getAmplifier());
				}
				if(tmp.getDuration()>eff.getDuration()) {
					eff=new PotionEffect(eff.getType(),tmp.getDuration(),eff.getAmplifier());
				}
				ps.pl.removePotionEffect(eff.getType());
			}
			ps.pl.addPotionEffect(eff);
		}
	}
	
	private PotionEffect getPotionEffectByList(Collection<PotionEffect> cpe,PotionEffectType t) {
		for(PotionEffect p:cpe) {
			if(p.getType().equals(t)) {
				return p;
			}
		}
		return null;
	}
	
	//设置所有玩家的血量和饱食度
	@SuppressWarnings("deprecation")
	public void setAllHpFool() {
		for(ParasiteState ps:ParasiteData.userstate) {
			if(ps.pl.isOnline()) {
				//设置生命值（贫血的玩家）
				if(ps.maxHp<100) {
					Double hp=ps.pl.getMaxHealth();
					hp=hp/100*ps.maxHp;
					if(hp<1D) {
						hp=1D;
					}
					if(hp<ps.pl.getHealth()) {
						ps.pl.setHealth(hp);
					}
				}
				//设置饱食度和饥饿度（腹泻等的玩家）
				if(ps.isBedFool) {
					ps.pl.setSaturation(0);
					if(ps.maxHp<100) {
						int f=20*ps.maxHp/100;
						if(f<ps.pl.getFoodLevel()) {
							ps.pl.setFoodLevel(f);
						}
					}
				}
			}
		}
	}
	
	//感染疟原虫和血吸虫（根据生物群系和地理位置）
	public void getParasiteByLoc(Player pl) {
		if((Math.random()*100)<ParasiteData.LocProbability) {
			Location loc=pl.getLocation();
			String Biomes=loc.getWorld().getBiome(loc.getBlockX(),loc.getBlockZ()).name();
			//生物群系
			for(String tmp:ParasiteData.DampBiomes) {
				if(tmp.equals(Biomes)) {
					addState(pl.getName(),5);//疟原虫
					break;
				}
			}
			//水源
			String m1=loc.getBlock().getType().name();
			loc=loc.add(0,1D,0);
			String m2=loc.getBlock().getType().name();
			for(String tmp:ParasiteData.Water) {
				if(tmp.equals(m1) || tmp.equals(m2)) {
					for(String str:ParasiteData.OceanBiomes) {
						if(str.equals(Biomes)) {
							return;
						}
					}
					addState(pl.getName(),2);//血吸虫
					if((Math.random()*100)<ParasiteData.ItemProbability) {
						addState(pl.getName(),0);//钩虫
					}
					return;
				}
			}
		}
	}
	
	//保存玩家数据
	public static synchronized void saveUserData() {
		/*List<String> save=new ArrayList<String>();
		ParasiteCore pc=new ParasiteCore();
		for(ParasitePlayer pp:ParasiteData.userinfo) {
			save.add(pc.DataToStr(pp));
		}
		ParasiteData.data.set("data",save);
		try {
			ParasiteData.data.save(ParasiteData.file);
		} catch (IOException e) {
			System.out.println("[Parasite] 配置保存失败");
			return;
		}*/
		FileOutputStream fos=null;
		OutputStreamWriter osw=null;
		BufferedWriter bw=null;
		try {
			fos=new FileOutputStream(ParasiteData.file);
			osw=new OutputStreamWriter(fos, "UTF-8");
			bw=new BufferedWriter(osw);
			String line=System.getProperty("line.separator");
			for(ParasitePlayer pp:ParasiteData.userinfo){
				bw.write(DataToStr(pp)+line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Parasite] 读取数据失败");
		} finally {
			if(bw!=null) {
				try {
					bw.close();
				} catch (IOException e1) {}
            }
			if(osw!=null) {
				try {
					osw.close();
				} catch (IOException e1) {}
            }
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e1) {}
            }
		}
	}
	
	//读取玩家数据
	public static synchronized void readUserData() {
		if(!ParasiteData.userinfo.isEmpty()) {
			ParasiteData.userinfo.clear();
		}
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(ParasiteData.file));
            String tmp=null;
            while ((tmp=reader.readLine())!=null) {
            	if(!tmp.isEmpty()) {
            		ParasiteData.userinfo.add(StrToData(tmp));
            	}
            }
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[Parasite] 读取数据失败");
		} finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e1) {}
            }
		}
    }
	
	//感染级别减少一个等级
	public static int removeLv(int lv) {
		if(lv<5000) {
			return 0;
		}
		if(lv<18000) {
			return 1;
		}
		if(lv<20000) {
			return 5000;
		}
		if(lv<22000) {
			return 18000;
		}
		return 20000;
	}
	
	//获取寄生虫ID
	public int getParasIdBuName(String name) {
		for(int i=0;i<6;i++) {
			if(ParasiteData.ParasiteName[i].contains(name)) {
				return i;
			}
		}
		return -1;
	}
	
	//治疗玩家（药物治疗）
	public void CureUser(Player pl,int type) {
		String name=pl.getName();
		for(int i=0;i<ParasiteData.userinfo.size();i++) {
			ParasitePlayer pp=ParasiteData.userinfo.get(i);
			if(pp.name.equals(name)) {
				if(pp.time[type]==0) {
					pl.addPotionEffect(new PotionEffect(PotionEffectType.POISON,1200,1));
				}else{
					if(pp.time[type]>=22000) {
						return;
					}
					int hyz=0;
					if(pp.time[type]>18000) {
						hyz=pp.time[type];
					}
					if(hyz>pp.sequela[type]) {
						pp.sequela[type]=hyz;
					}
					pp.time[type]=removeLv(pp.time[type]);
					ParasiteData.userinfo.set(i, pp);
				}
				return;
			}
		}
		pl.addPotionEffect(new PotionEffect(PotionEffectType.POISON,1200,1));
	}
	
	//获取防止感染的几率
	public int getNoParas(String name) {
		for(int i=0;i<ParasiteData.noParasite.size();i++) {
			String str=ParasiteData.noParasite.get(i);
			if(str.startsWith(name+",")) {
				String[] tmp=str.split(",");
				return Integer.parseInt(tmp[1]);
			}
		}
		return 0;
	}
}
