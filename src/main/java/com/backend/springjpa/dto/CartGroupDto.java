package com.backend.springjpa.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartGroupDto {

    private Long id;
    private String name;
    @JsonProperty("cartItems")
    private List<CartItemDto> itemDto = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CartItemDto> getItemDto() {
        return itemDto;
    }

    public void setItemDto(List<CartItemDto> itemDto) {
        this.itemDto = itemDto;
    }
}
