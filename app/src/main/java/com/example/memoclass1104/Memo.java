package com.example.memoclass1104;

class Memo {
    private int id;
    private String memo;
    private String date;
    private String bgcolor;
    private int remind;

    public Memo(int id, String memo, String date, String bgcolor, int remind) {
        this.id = id;
        this.memo = memo;
        this.date = date;
        this.bgcolor = bgcolor;
        this.remind = remind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }
}
