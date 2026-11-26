package com.fritosverda.titanapi.model;

import com.google.gson.annotations.SerializedName;

public class Info {

    private int count;
    private int pages;

    @SerializedName("next_page")
    private String nextPage;

    @SerializedName("prev_page")
    private String prevPage;

    public int getCount() { return count; }
    public int getPages() { return pages; }
    public String getNextPage() { return nextPage; }
    public String getPrevPage() { return prevPage; }
}
