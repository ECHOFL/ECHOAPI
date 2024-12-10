package me.fliqq.echoprisonrank.enom;

public enum RankEnum {

    DIRECTEUR,
	RESPONSABLE,
	SUPERVISEUR,
	GARDIEN,
	MECENE,
	BOSS,
	SENIOR,
	VIRTUOSE,
	MAITRE,
	ASPIRANT,
	INITIE,
	STAGIAIRE,
	DEFAULT;

    public String getLowerCaseName() {
        return this.name().toLowerCase();
    }
}