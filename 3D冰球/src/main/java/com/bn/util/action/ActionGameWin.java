package com.bn.util.action;

public class ActionGameWin extends MyAction
{
	public ActionGameWin(boolean computer_win,int index)//当游戏胜利时
	{
		this.computer_win=computer_win;
		this.index=index;
	}
}
