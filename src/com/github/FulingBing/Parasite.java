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

// δʹ�ð�������
// �ļ�����Ϊ GBK ���� UTF-8 ���� 754503921
public class Parasite extends JavaPlugin{
	/*
	
	���棬��ͨ������Ⱦ��ˮ��ˮ�����߲˽������塣֢״����������ʹ�����ġ���к��ƶѪ
	�׳棬ͨ����ʳ������֢״�����ա�ƣ�͡�������Ƥ�Ż�¡���к�������⡢��Ϣ�Ϳ���
	Ѫ���棬�Ӵ�������Ⱦ��ˮ��Ѫ����ͻ�������ǵ�Ƥ����֢״�����ȡ���ʹ�����ԡ���к�����͡�ͷ��
	�г棬ͨ����ȾʳƷ������֢״�����ġ�Ż�¡����෢�ס���к�����ؼ��ᡢͷ���ۻ������Ρ�Ӫ������
	���γ棬ͨ���Բ��������߱�����Ⱦ�ĳ����Ⱦ���ּ����档֢״������֢״�����ա���ս��������ͷ��
	űԭ�棬Ϊ���ô��������ӳ棬��ű���Ĳ�ԭ�塣֢״�����ȡ�ƶѪ
	
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
				getLogger().info("�޷��������ݣ����ͣ��");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		/*try{
			ParasiteData.data=YamlConfiguration.loadConfiguration(ParasiteData.file);
		}catch(Exception e){
			ParasiteData.data=null;
			getLogger().info("���س������ͣ��");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}*/
		readsize();
		ParasiteCore.readUserData();
		ParasiteData.plugin=this;
		Bukkit.getPluginManager().registerEvents(new ParasitePlayerListener(),this);
		Bukkit.getPluginManager().registerEvents(new ParasiteItemListener(),this);
		//����ϳɱ�
		List<Recipe> lr=(new ParasiteItem()).getRecipe();
		for(Recipe r:lr) {
			getServer().addRecipe(r);
		}
		//����ѭ������
		Timer timer=new Timer();
		// ʹ�� timer ������ֵ���Ƽ��ģ�������ִ��ʱ�׳��쳣����ֲ���Ԥ�ϵ����
		// Ϊʲô���� Bukkit �� Scheduler �� ���� 754503921
		ParasiteData.runtime_1=0;
		ParasiteData.runtime_5=0;
		ParasiteData.runtime_300=0;
		timer.schedule(new ParasiteRun(),500,500);
		getLogger().info("�������");
	}

	public void onDisable() {
		//if(ParasiteData.data==null) {
		//	return;
		//}
		getLogger().info("���ڱ�������");
		ParasiteCore.saveUserData();
		getLogger().info("���ͣ��");
	}
	
	public void readsize(){
		//��ȡ�������
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
		//��ȡ�������
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
						sender.sendMessage("��e��û��Ȩ��ִ�����ָ��");
						return false;
					}
				}
				reloadConfig();
				readsize();
				sender.sendMessage("��a���سɹ�");
				return true;
			}
			if(args[0].equalsIgnoreCase("show")) {
				if(sender instanceof Player) {
					if(!((Player)sender).hasPermission("Parasite.show")) {
						sender.sendMessage("��e��û��Ȩ��ִ�����ָ��");
						return false;
					}
				}
				if(args.length>1) {
					for(ParasitePlayer tpp:ParasiteData.userinfo) {
						if(tpp.name.equals(args[1])) {
							sender.sendMessage("��b��� ��c"+args[1]+" ��b�ĸ�Ⱦ�����");
							for(int i=0;i<6;i++) {
								String s="��a"+ParasiteData.ParasiteName[i]+"��b: ��d";
								if(tpp.time[i]==0) {
									s=s+"δ��Ⱦ";
								}else{
									if(tpp.time[i]<5000) {
										s=s+"Ǳ����";
									}else if(tpp.time[i]>=5000 && tpp.time[i]<18000) {
										s=s+"һ�׶�";
									}else if(tpp.time[i]>=18000 && tpp.time[i]<20000) {
										s=s+"���׶�";
									}else if(tpp.time[i]>=20000 && tpp.time[i]<22000) {
										s=s+"���׶�";
									}else {
										s=s+"�Ľ׶�";
									}
								}
								s=s+"��a, ���ƺ���֢��b: ��d";
								if(tpp.sequela[i]==0) {
									s=s+"��";
								}else {
									s=s+"��";
								}
								sender.sendMessage(s);
							}
							return true;
						}
					}
					sender.sendMessage("��a���û�δ��Ⱦ�κμ�����");
				}else {
					sender.sendMessage("��e�÷�: ��a/para show <���ID> ��e��ѯ��ҵĸ�Ⱦ���");
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("set")) {
				if(sender instanceof Player) {
					if(!((Player)sender).hasPermission("Parasite.set")) {
						sender.sendMessage("��e��û��Ȩ��ִ�����ָ��");
						return false;
					}
				}
				if(args.length==5) {
					Player pl=Bukkit.getPlayer(args[1]);
					if(pl==null) {
						sender.sendMessage("��e����Ҳ�����");
						return false;
					}
					if(!pl.getName().equals(args[1])) {
						sender.sendMessage("��e����Ҳ�����");
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
					sender.sendMessage("��a�����ɹ�");
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
				sender.sendMessage("��e��û��Ȩ��ִ�����ָ��");
				return true;
			}
		}
		sender.sendMessage("��b�������:");
		if(can[0]) sender.sendMessage("��a/para set <���ID> <��������> <��Ⱦ����> <��Ⱦ�ȼ�> ��e���ø�Ⱦ");
		if(can[0]) sender.sendMessage("��d��ע: ��eʹ�á�a/para set ��e�鿴����ϸ��ʹ��˵��");
		if(can[1]) sender.sendMessage("��a/para show <���ID> ��e��ѯ��ҵĸ�Ⱦ���");
		if(can[2]) sender.sendMessage("��a/para reload ��e���¼��������ļ�");
		return false;
	}
	
	public void showcmdsethelp(CommandSender sender) {
		sender.sendMessage("��e�÷�: ��a/para set <���ID> <��������> <��Ⱦ����> <��Ⱦ�ȼ�> ��e���ø�Ⱦ");
		sender.sendMessage("��b��������: ��d0.���棬1.�׳棬2.Ѫ���棬3.�г棬4.���γ棬5.űԭ��");
		sender.sendMessage("��b��Ⱦ����: ��d0.��ͨ��Ⱦ��1.���ƺ�ĺ���֢");
		sender.sendMessage("��b��Ⱦ�ȼ�: ��d0.�ޣ�1.Ǳ���ڣ�2.һ�׶Σ�3.���׶Σ�4.���׶Σ�5.�Ľ׶�");
	}
}
