package com.pulu.robot.om;

import java.io.Serializable;

public class Experience implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;

	public Experience() {
		name = "hello";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
