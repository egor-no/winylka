package com.winylka.business.domainobjects;

import com.winylka.data.entities.OrderItem;
import com.winylka.data.entities.OrderItemPK;
import com.winylka.data.entities.OrderItemStatus;
import org.springframework.stereotype.Component;

//@Component
public class Mapper {

    public OrderItemDTO toDTO(OrderItem orderItem) {
        OrderItemPK pk = orderItem.getPk();
        int quantity = orderItem.getQuantity();
        OrderItemStatus status = orderItem.getStatus();
        String artistName = orderItem.getRelease().getArtistName();
        String albumTitle = orderItem.getRelease().getAlbumTitle();

        OrderItemDTO dto = new OrderItemDTO(pk, quantity, status, artistName, albumTitle);
        return dto;
    }

    public OrderItem toOrderItem(OrderItemDTO dto) {
        OrderItemPK pk = dto.getPk();
        int quantity = dto.getQuantity();
        OrderItemStatus status = dto.getStatus();

        return new OrderItem(pk, quantity, status);
    }
}
