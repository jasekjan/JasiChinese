package com.development.honza.jasichinese.db;

/**
 * Created by Honza on 7. 3. 2018.
 */

public class Words implements WordsInterface {
    private long id;
    private String myLang;
    private String myReading;
    private String myForeign;
    private String category;

    public Words(long id, String myLang, String myReading, String myForeign, String category) {
        this.id = id;
        this.myLang = myLang;
        this.myReading = myReading;
        this.myForeign = myForeign;
        this.category = category;
    }

    public Words(String myLang, String myReading, String myForeign, String category) {
        this.myLang = myLang;
        this.myReading = myReading;
        this.myForeign = myForeign;
        this.category = category;
    }

    public Words() {
    }

    public String getmyLang() {
        return myLang;
    }

    public void setmyLang(String myLang) {
        this.myLang = myLang;
    }

    public String getmyReading() {
        return myReading;
    }

    public void setmyReading(String myReading) {
        this.myReading = myReading;
    }

    public String getmyForeign() {
        return myForeign;
    }

    public void setmyForeign(String myForeign) {
        this.myForeign = myForeign;
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
        return myLang+"-"+myReading+"-"+myForeign + "-" + category;
    }

    @Override
    public boolean equals(Words words) {
        boolean ret = false;
        if (words == null) {
            ret = false;
        }
        if (!Words.class.isAssignableFrom(words.getClass())) {
            ret = false;
        }
        final Words other = (Words) words;
        if ((this.myForeign == null) ? (other.myForeign != null) : !this.myForeign.equals(other.myForeign)) {
            ret = false;
        }

        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + myForeign.hashCode();
        hash = hash * 13 + myLang.hashCode();
        hash = hash * 29 + myReading.hashCode();
        hash = hash * 13 + category.hashCode();
        return hash;
    }
}
