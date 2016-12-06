package com.pulu.robot.om;

import java.io.Serializable;

public class EM implements Serializable {

	private static final long serialVersionUID = 1L;
	private Experience experience = null;

	public Experience getExperience() {
		return experience;
	}

	public EM() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EM(Experience experience) {
		super();
		this.experience = experience;
	}

	public void setExperience(Experience experience) {
		this.experience = experience;
	}

}
