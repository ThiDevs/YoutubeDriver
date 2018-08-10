package com.ThiagoAlves;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardModel {
    private String imageId;
    private String titleId;
    private String subtitleId;

    public CardModel(String imageId, String titleId, String subtitleId) {
        this.imageId = imageId;
        this.titleId = titleId;
        this.subtitleId = subtitleId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getTitle() {
        return titleId;
    }

    public String getSubtitle() {
        return subtitleId;
    }
}
