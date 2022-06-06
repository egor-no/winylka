package com.winylka.business.domainobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winylka.data.entities.*;

import javax.persistence.*;
import java.util.Objects;

public class OrderItemDTO {

    private OrderItemPK pk;

    private int quantity;

    private OrderItemStatus status;

    private String artistName;

    private String albumName;

    public OrderItemDTO(OrderItemPK pk, int quantity, OrderItemStatus status, String artistName, String albumName) {
        this.pk = pk;
        this.quantity = quantity;
        this.status = status;
        this.artistName = artistName;
        this.albumName = albumName;
    }

    public OrderItemPK getPk() {
        return pk;
    }

    public void setPk(OrderItemPK pk) {
        this.pk = pk;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getOrderId() {
        return pk.getOrder().getOrderId();
    }

    public Order getOrder() {
        return pk.getOrder();
    }

    public long getReleaseId() {
        return pk.getRelease().getReleaseId();
    }

}
