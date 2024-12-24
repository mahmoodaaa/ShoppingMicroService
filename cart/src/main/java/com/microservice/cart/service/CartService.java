package com.microservice.cart.service;

import com.microservice.cart.model.dto.requset.CartReqDTO;
import com.microservice.cart.model.dto.requset.UpdateCartReqDTO;
import com.microservice.cart.model.dto.response.CartResponseDTO;
import com.microservice.cart.model.entity.CartItem;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CartService {
    public CartResponseDTO createCart(String userId) ;
    public CartResponseDTO createCartToItem(String userId, List<CartItem>items);
    public CartResponseDTO addItemToCart(String userId, String cartItemId );
    public CartResponseDTO removeItemToCart(String userId,String cartItemId );
    public CartResponseDTO update(String cartId, List<UpdateCartReqDTO> updateDto);
    public CartResponseDTO getCartById(String cartId) ;
    public List<CartResponseDTO> getAllCarts() ;
    public void hardDeleteCart(String cartId);
    public Page<CartResponseDTO> getAllCartsPaginated(int page, int size);




}
