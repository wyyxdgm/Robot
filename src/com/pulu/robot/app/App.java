package com.pulu.robot.app;

import java.util.List;

import com.pulu.robot.om.ChanelService;
import com.pulu.robot.util.ReadFileToObjects;

public class App {

	public static void main(String[] args) {
		ReadFileToObjects chanelsInfo = new ReadFileToObjects(
				"data\\chanel.txt", true, false);
		List<String> chanels = chanelsInfo.StringsFromFile();
		ChanelService.doChanels(chanels);
	}

}
