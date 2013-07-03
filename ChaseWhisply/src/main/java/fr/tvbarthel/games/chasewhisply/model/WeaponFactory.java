package fr.tvbarthel.games.chasewhisply.model;

public class WeaponFactory {
	private static final int BASIC_WEAPON_DAMAGE = 1;
	private static final int BASIC_WEAPON_AMMUNITION_LIMIT = 8;

	public static Weapon createBasicWeapon(){
		Weapon basicWeapon = new Weapon();
		basicWeapon.setDamage(BASIC_WEAPON_DAMAGE);
		basicWeapon.setAmmunitionLimit(BASIC_WEAPON_AMMUNITION_LIMIT);
		return  basicWeapon;
	}
}
