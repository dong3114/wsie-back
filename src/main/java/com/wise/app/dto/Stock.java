package com.wise.app.dto;

import lombok.Data;

@Data
public class Stock {
    private String stock_name;
    private int stock_quantity;
    private int stock_unit_price;
    private String stock_expiration_date;
    private String storage_type;
}
