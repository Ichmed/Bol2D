package com.ichmed.bol2d.entity;

public enum EntityType
{
	NPC, PROJECTILE, PICKUP, PLAYER, PARTICLE, MISC;

	public static final EntityType[] ALL = { NPC, PROJECTILE, PICKUP, PLAYER, PARTICLE, MISC};
	
	public static final EntityType[] PLAYER_ONLY = { PLAYER };
	public static final EntityType[] NPC_ONLY = { NPC };
	public static final EntityType[] PROJECTILE_ONLY = { PROJECTILE };
	public static final EntityType[] PICKUP_ONLY = { PICKUP };
	public static final EntityType[] PARTICLES_ONLY = { PARTICLE };
	
	public static final EntityType[] CHARCTERS_ONLY = { PLAYER, NPC };
	
	public static final EntityType[] NO_PARTICLES = { NPC, PROJECTILE, PICKUP, PLAYER, MISC};
}
