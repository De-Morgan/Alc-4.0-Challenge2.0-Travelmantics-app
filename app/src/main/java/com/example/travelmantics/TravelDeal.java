package com.example.travelmantics;

import java.io.Serializable;

public class TravelDeal  implements Serializable {
    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;

    public TravelDeal() {
    }

    TravelDeal(String title, String description, String price) {

        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }

    @Override
    public String toString() {
        return "TravelDeal{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
