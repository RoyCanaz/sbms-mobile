package org.totalit.sbms.CallListerner;

import org.totalit.sbms.domain.Product;

import java.util.List;

public interface ProductListListener {
    public void productList(List<Product> productList);
    public void product(Product product);
}
