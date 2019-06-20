package com.github.FulingBing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.FulingBing.listener.ParasiteItemListener;
import com.github.FulingBing.listener.ParasitePlayerListener;
import com.github.FulingBing.pojo.ParasitePlayer;
import com.github.FulingBing.service.ParasiteCore;
import com.github.FulingBing.service.ParasiteItem;
import com.github.FulingBing.service.ParasiteRun;

public class Parasite extends JavaPlugin{
	/*
	
	钩虫，它通过受污染的水、水果和蔬菜进入人体。症状：虚弱、腹痛、恶心、腹泻、贫血
	蛔虫，通过摄食传播。症状：发烧、疲劳、过敏、皮疹、呕吐、腹泻、神经问题、喘息和咳嗽
	血吸虫，接触到受污染的水后，血吸虫就会刺破他们的皮肤。症状：发热、疼痛、咳嗽、腹泻、肿胀、头昏
	绦虫，通过污染食品传播，症状：恶心、呕吐、内脏发炎、腹泻、体重减轻、头晕眼花、痉挛、营养不良
	弓形虫，通过吃不熟的肉或者抱被感染的宠物感染这种寄生虫。症状：流感症状、发烧、寒战、虚弱、头疼
	疟原虫，为按蚊传播的孢子虫，是疟疾的病原体。症状：发热、贫血
	
	*/
	public void onEnable() {
		if (!getDataFolder().exists()){
			saveDefaultConfig();
		}
		ParasiteData.file=new File(this.getDataFolder(),"userdata.yml");
		if(!ParasiteData.file.exists()){
			//this.saveResource("userdata.yml",true);
			try {
				ParasiteData.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().info("无法创建数据，插件停用");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		/*try{
			ParasiteData.data=YamlConfiguration.loadConfiguration(ParasiteData.file); **Fuck Bukkit**
		}catch(Exception e){
			ParasiteData.data=null;
			getLogger().info("加载出错，插件停用");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}*/
		readsize();
		ParasiteCore.readUserData();
		ParasiteData.plugin=this;
		Bukkit.getPluginManager().registerEvents(new ParasitePlayerListener(),this);
		Bukkit.getPluginManager().registerEvents(new ParasiteItemListener(),this);
		//加入合成表
		List<Recipe> lr=(new ParasiteItem()).getRecipe();
		for(Recipe r:lr) {
			getServer().addRecipe(r);
		}
		//开启循环任务
		Timer timer=new Timer();
		ParasiteData.runtime_1=0;
		ParasiteData.runtime_5=0;
		ParasiteData.runtime_300=0;
		timer.schedule(new ParasiteRun(),500,500);
		getLogger().info("插件启用");
	}

	public void onDisable() {
		//if(ParasiteData.data==null) {
		//	return;
		//}
		getLogger().info("正在保存数据");
		ParasiteCore.saveUserData();
		getLogger().info("插件停用");
	}
	
	public void readsize(){
		//读取插件配置
		ParasiteData.ItemProbability=getConfig().getDouble("ItemProbability");
		ParasiteData.LocProbability=getConfig().getDouble("LocProbability");
		ParasiteData.Misdiagnosis=getConfig().getDouble("Misdiagnosis");
		ParasiteData.BotanyFool=getConfig().getStringList("BotanyFool");
		ParasiteData.DirtyFool=getConfig().getStringList("DirtyFool");
		ParasiteData.Entity=getConfig().getStringList("Entity");
		ParasiteData.RawFool=getConfig().getStringList("RawFool");
		ParasiteData.OceanBiomes=getConfig().getStringList("OceanBiomes");
		ParasiteData.DampBiomes=getConfig().getStringList("DampBiomes");
		ParasiteData.Water=getConfig().getStringList("Water");
		ParasiteData.DeathClear=getConfig().getBoolean("DeathClear");
		//读取玩家数据
		/*if(readPlayer) {
			if(!ParasiteData.userinfo.isEmpty()) {
				ParasiteData.userinfo.clear();
			}
			ParasiteCore pc=new ParasiteCore();
			List<String> list=ParasiteData.data.getStringList("data");
			for(String str:list) {
				ParasiteData.userinfo.add(pc.StrToData(str));
			}
		}*/
	}
	
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args){
		if(args.length>0) {
			if(args[0].equalsIgnoreCase("reload")) {
				if(sender instanceof Player) {
					if(!((Player)sender).hasPermission("Parasite.reload")) {
						sender.sendMessage("§e你没有权限执行这个指令");
						return false;
					}
				}
				reloadConfig();
				readsize();
				sender.sendMessage("§a重载成功");
				return true;
			}
			if(args[0].equalsIgnoreCase("show")) {
				if(sender instanceof Player) {
					if(!((Player)sender).hasPermission("Parasite.show")) {
						sender.sendMessage("§e你没有权限执行这个指令");
						return false;
					}
				}
				if(args.length>1) {
					for(ParasitePlayer tpp:ParasiteData.userinfo) {
						if(tpp.name.equals(args[1])) {
							sender.sendMessage("§b玩家 §c"+args[1]+" §b的感染情况：");
							for(int i=0;i<6;i++) {
								String s="§a"+ParasiteData.ParasiteName[i]+"§b: §d";
								if(tpp.time[i]==0) {
									s=s+"未感染";
								}else{
									if(tpp.time[i]<5000) {
										s=s+"潜伏期";
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
								s=s+"§a, 治疗后遗症§b: §d";
								if(tpp.sequela[i]==0) {
									s=s+"无";
								}else {
									s=s+"有";
								}
								sender.sendMessage(s);
							}
							return true;
						}
					}
					sender.sendMessage("§a该用户未感染任何寄生虫");
				}else {
					sender.sendMessage("§e用法: §a/para show <玩家ID> §e查询玩家的感染情况");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("set")) {
				if(sender instanceof Player) {
					if(!((Player)sender).hasPermission("Parasite.set")) {
						sender.sendMessage("§e你没有权限执行这个指令");
						return false;
					}
				}
				if(args.length==5) {
					Player pl=Bukkit.getPlayer(args[1]);
					if(pl==null) {
						sender.sendMessage("§e该玩家不在线");
						return false;
					}
					if(!pl.getName().equals(args[1])) {
						sender.sendMessage("§e该玩家不在线");
						return false;
					}
					int pid=0,lv=0;
					boolean type=args[3].equals("1");
					try {
						pid=Integer.parseInt(args[2]);
						lv=Integer.parseInt(args[4]);
					} catch (Exception e) {
						showcmdsethelp(sender);
						return false;
					}
					if(pid<0 || pid>5 || lv<0 || lv>5) {
						showcmdsethelp(sender);
						return false;
					}
					if(lv==1) {
						lv=1;
					}else if(lv==2) {
						lv=5000;
					}else if(lv==3) {
						lv=18000;
					}else if(lv==4) {
						lv=20000;
					}else if(lv==5){
						lv=22000;
					}
					ParasiteCore pc=new ParasiteCore();
					pc.setState(pl.getName(),pid,lv,type);
					sender.sendMessage("§a操作成功");
				}else {
					showcmdsethelp(sender);
				}
				return true;
			}
		}
		boolean[] can={true,true,true};
		if(sender instanceof Player) {
			Player pl=((Player)sender);
			can[0]=pl.hasPermission("Parasite.set");
			can[1]=pl.hasPermission("Parasite.show");
			can[2]=pl.hasPermission("Parasite.reload");
			if(!can[0] && !can[1] && !can[2]) {
				sender.sendMessage("§e你没有权限执行这个指令");
				return true;
			}
		}
		sender.sendMessage("§b插件帮助:");
		if(can[0]) sender.sendMessage("§a/para set <玩家ID> <寄生虫编号> <感染类型> <感染等级> §e设置感染");
		if(can[0]) sender.sendMessage("§d备注: §e使用§a/para set §e查看更详细的使用说明");
		if(can[1]) sender.sendMessage("§a/para show <玩家ID> §e查询玩家的感染情况");
		if(can[2]) sender.sendMessage("§a/para reload §e重新加载配置文件");
		return false;
	}
	
	public void showcmdsethelp(CommandSender sender) {
		sender.sendMessage("§e用法: §a/para set <玩家ID> <寄生虫编号> <感染类型> <感染等级> §e设置感染");
		sender.sendMessage("§b寄生虫编号: §d0.钩虫，1.蛔虫，2.血吸虫，3.绦虫，4.弓形虫，5.疟原虫");
		sender.sendMessage("§b感染类型: §d0.普通感染，1.治疗后的后遗症");
		sender.sendMessage("§b感染等级: §d0.无，1.潜伏期，2.一阶段，3.二阶段，4.三阶段，5.四阶段");
	}
}
