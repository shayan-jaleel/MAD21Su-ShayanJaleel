package com.example.numad21su_mohammadsjaleel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ItemCard implements ItemClickListener {

    private final int imageSource;
    private final String urlName;
    private final String urlText;
    private boolean isChecked;

    //Constructor
    public ItemCard(int imageSource, String urlName, String urlText, boolean isChecked) {
        this.imageSource = imageSource;
        this.urlName = urlName;
        this.urlText = urlText;
        this.isChecked = isChecked;
    }

    //Getters for the imageSource, itemName and itemDesc
    public int getImageSource() {
        return imageSource;
    }

    public String getUrlText() {
        return urlText;
    }

    public String getUrlName() {
        return urlName + (isChecked ? "(checked)" : "");
    }

    public boolean getStatus() {
        return isChecked;
    }


    @Override
    public void onItemClick(int position) {
        isChecked = !isChecked;
        openWebPage(App.context, urlText.toString());
    }

    @Override
    public void onCheckBoxClick(int position) {
        isChecked = !isChecked;
    }

    /**
     * Open a web page of a specified URL
     *
     * @param url URL to open
     */
    public void openWebPage(Context context, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}