package com.github.FulingBing.pojo;

import org.bukkit.entity.Player;

//玩家状态
public class ParasiteState {
	public Player pl;
	public int maxHp=100;
	public boolean isBedFool=false;
	public int[] state={0,0,0,0,0,0}; // 六个值，建议重构为可以清晰表示的 —— 754503921
	public ParasiteEffect effect;
}
