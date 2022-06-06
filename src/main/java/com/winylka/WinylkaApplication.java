package com.winylka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WinylkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinylkaApplication.class, args);
    }

}

/*

Fix: ----DONE----
1. Albums to add - artistId key
    1.1. The same probably for adding orders and orderitems
2. Fix to show clients/1/order GET
3. Review assemblers
4. Test orders/ order items/ order PK/ clients.

Plans:
0. Security

1. Warehouse items - to see how many there are
    1.1 decrement when something is bought
    1.2 can add new поставка
2. Warehouseitemstatus
    2.1 can't be ordered if something is out of stock
    2.2 can be ordered to preorder if something is preorder
    2.3 can order to in progress if soemthing is in stock
3. Releaser
    release items when there's certain date and release all the orderitems
 */
