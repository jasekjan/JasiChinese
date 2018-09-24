package com.development.honza.jasichinese.db;

/**
 * Created by Honza on 7. 3. 2018.
 */

public class Characters implements CharactersInterface {
    private long id;
    private String inCzech;
    private String inPinyin;
    private String inChinese;
    private String category;

    public Characters(long id, String inCzech, String inPinyin, String inChinese, String category) {
        this.id = id;
        this.inCzech = inCzech;
        this.inPinyin = inPinyin;
        this.inChinese = inChinese;
        this.category = category;
    }

    public Characters(String inCzech, String inPinyin, String inChinese, String category) {
        this.inCzech = inCzech;
        this.inPinyin = inPinyin;
        this.inChinese = inChinese;
        this.category = category;
    }

    public Characters() {
    }

    public String getInCzech() {
        return inCzech;
    }

    public void setInCzech(String inCzech) {
        this.inCzech = inCzech;
    }

    public String getInPinyin() {
        return inPinyin;
    }

    public void setInPinyin(String inPinyin) {
        this.inPinyin = inPinyin;
    }

    public String getInChinese() {
        return inChinese;
    }

    public void setInChinese(String inChinese) {
        this.inChinese = inChinese;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) { this.category = category; }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return inCzech+"-"+inPinyin+"-"+inChinese + "-" + category;
    }

    @Override
    public boolean equals(Characters chars) {
        if (chars == null) {
            return false;
        }
        if (!Characters.class.isAssignableFrom(chars.getClass())) {
            return false;
        }
        final Characters other = (Characters) chars;
        if ((this.inChinese == null) ? (other.inChinese != null) : !this.inChinese.equals(other.inChinese)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + inChinese.hashCode();
        hash = hash * 13 + inCzech.hashCode();
        hash = hash * 29 + inPinyin.hashCode();
        hash = hash * 13 + category.hashCode();
        return hash;
    }
}
