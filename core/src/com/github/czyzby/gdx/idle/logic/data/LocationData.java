package com.github.czyzby.gdx.idle.logic.data;

import com.badlogic.gdx.utils.Array;

public class LocationData { // Using public, non-final for LibGDX JSON utilitites.
	public String name;
	public Array<MonsterData> monsters;

	public MonsterData getRandomMonster() {
		return monsters.random();
	}
}
