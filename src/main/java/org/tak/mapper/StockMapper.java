package org.tak.mapper;

import org.tak.entity.Stock;

public interface StockMapper {

    Stock selectStockById(Integer id);

    int updateStock(Stock stock);

    void updateStockB(Stock stock);
}
