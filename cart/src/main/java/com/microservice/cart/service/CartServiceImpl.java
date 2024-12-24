package com.microservice.cart.service;

import com.microservice.cart.enums.CartStatus;
import com.microservice.cart.error.RecordNotFoundExciption;
import com.microservice.cart.model.dto.requset.CartItemReq;
import com.microservice.cart.model.dto.requset.CartReqDTO;
import com.microservice.cart.model.dto.requset.UpdateCartReqDTO;
import com.microservice.cart.model.dto.response.CartResponseDTO;
import com.microservice.cart.model.dto.response.CartItemResponseDTO;
import com.microservice.cart.model.entity.Cart;
import com.microservice.cart.model.entity.CartItem;
import com.microservice.cart.model.mapper.CartMapper;
import com.microservice.cart.proxy.ItemProxy;
import com.microservice.cart.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;



@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemProxy itemProxy;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartRepository cartRepository;

    public CartResponseDTO createCart(String userId) {
        try {
            Cart newCart = Cart.builder()
                   // .id(UUID.randomUUID().toString())
                    .userId(userId)
                    .itemList(new ArrayList<>())
                    .cartStatus(CartStatus.ACTIVE)
                    .build();
            Cart savedCart = cartRepository.save(newCart);
            return cartMapper.toResponse(savedCart);
        } catch (NoSuchElementException ex) {
            throw new RecordNotFoundExciption(String.format("no found with cart was found in database"));
        }
    }
    public CartResponseDTO createCartToItem(String userId,List<CartItem>items){

        // Ensure only one active cart for the user
        List<Cart> activeCarts = cartRepository.findByUserIdAndCartStatus(userId, CartStatus.ACTIVE);

        Cart cart;
        if (activeCarts.isEmpty()) {
            // If no active cart exists, create a new one
            cart = Cart.builder()
                    .userId(userId)
                    .itemList(new ArrayList<>(items)) // Add provided items
                    .cartStatus(CartStatus.ACTIVE)
                    .build();
        } else if (activeCarts.size() == 1) {
            // If exactly one active cart exists, add items to it
            cart = activeCarts.get(0);
            cart.getItemList().addAll(items); // Add items to the existing cart
        } else {
            throw new RecordNotFoundExciption("Multiple active carts found for the user. Please resolve duplicates.");
        }

        // Save the cart
        Cart savedCart = cartRepository.save(cart);

        // Fetch item details for the response
        List<String> itemIds = items.stream().map(CartItem::getId).collect(Collectors.toList());
        ResponseEntity<List<CartItemResponseDTO>> response = itemProxy.getItemsByIds(itemIds);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RecordNotFoundExciption("Failed to fetch item details from Item Service");
        }

        List<CartItemResponseDTO> itemDetails = response.getBody();

        // Map the updated cart and item details to CartResponseDTO
        return cartMapper.toResponse(savedCart, itemDetails);
    }
    public CartResponseDTO addItemToCart(String userId, String cartItemId ) {

        List<Cart> activeCarts = cartRepository.findByUserIdAndCartStatus(userId, CartStatus.ACTIVE);

        if (activeCarts.isEmpty()) {
            throw new RecordNotFoundExciption("No active cart found for the user.");
        }

        if (activeCarts.size() > 1) {
            throw new RecordNotFoundExciption("Multiple active carts found for the user.");
        }

        // Use the single active cart
        Cart cart = activeCarts.get(0);

        // Add or update the item in the cart
        CartItem existingItem = cart.getItemList().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            // Fetch item details
            ResponseEntity<CartItemResponseDTO> response = itemProxy.getItemById(cartItemId);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RecordNotFoundExciption("Item not found in Item Service");
            }
            CartItemResponseDTO itemDetails = response.getBody();
            CartItem newItem = new CartItem(cartItemId, 1);
            cart.getItemList().add(newItem);
        }

        // Save the updated cart
        Cart updatedCart = cartRepository.save(cart);

        // Fetch all item details for the cart
        List<String> itemIds = cart.getItemList().stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());
        ResponseEntity<List<CartItemResponseDTO>> itemDetailsResponse = itemProxy.getItemsByIds(itemIds);
        List<CartItemResponseDTO> itemDetails = itemDetailsResponse.getBody();

        return cartMapper.toResponse(updatedCart, itemDetails);

    }
    public CartResponseDTO removeItemToCart(String userId,String cartItemId ){
        List<Cart>activeCarts = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE);

        if (activeCarts.isEmpty()) {
            throw new RecordNotFoundExciption("No active cart found for the user." +userId);
        }

        Cart cart = activeCarts.get(0);

        CartItem cartItem = cart.getItemList().stream()
                .filter(item->item.getId().equals(cartItemId))
                .findFirst()
                 .orElseThrow(() -> new RecordNotFoundExciption("Cart item not found: " + cartItemId));

          Integer updatedQuantity  = cartItem.getQuantity()-1;
          if(updatedQuantity>0){

              cartItem.setQuantity(updatedQuantity);
          }else{
              cart.getItemList().remove(cartItem);
          }

        Cart savedCart = cartRepository.save(cart);

        // Optionally fetch updated item details if necessary
        List<String> itemIds = cart.getItemList().stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        ResponseEntity<List<CartItemResponseDTO>> response = itemProxy.getItemsByIds(itemIds);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RecordNotFoundExciption("Failed to fetch item details from Item Service");
        }
        List<CartItemResponseDTO> itemDetails = response.getBody();
        return cartMapper.toResponse(savedCart, itemDetails);
    }
    public CartResponseDTO update(String cartId, List<UpdateCartReqDTO> updateDto) {
        Cart cart = cartRepository.findByIdAndCartStatus(cartId,CartStatus.ACTIVE)
                .orElseThrow(() -> new RecordNotFoundExciption("Cart not found"));

        // Iterate over the provided update DTOs and update the cart items
        for (UpdateCartReqDTO cartReqDTO : updateDto) {
            CartItem cartItem = cart.getItemList().stream()
                    .filter(item -> item.getId().equals(cartReqDTO.getCartItemId()))
                    .findFirst()
                    .orElseThrow(() -> new RecordNotFoundExciption("Item not found in the cart: " + cartReqDTO.getCartItemId()));

            if (cartReqDTO.getQuantity() <= 0) {
                throw new RecordNotFoundExciption("Quantity must be greater than zero for item: " + cartReqDTO.getCartItemId());
            }

            // Update the quantity of the item in the cart
            cartItem.setQuantity(cartReqDTO.getQuantity());
        }
        Cart savedCart = cartRepository.save(cart);

        // Fetch item details (if needed) for the response
        List<String> itemIds = cart.getItemList().stream()
                .map(CartItem::getId).collect(Collectors.toList());
        ResponseEntity<List<CartItemResponseDTO>> response = itemProxy.getItemsByIds(itemIds);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RecordNotFoundExciption("Failed to fetch item details from Item Service");
        }

        List<CartItemResponseDTO> itemDetails = response.getBody();
        return cartMapper.toResponse(savedCart, itemDetails);
    }
    public CartResponseDTO getCartById(String cartId) {
        Cart cart = cartRepository.findByIdAndCartStatus(cartId,CartStatus.ACTIVE)
                .orElseThrow(() -> new RecordNotFoundExciption("Cart not found"));

        // Fetch item details for each cart item
        List<String> itemIds = cart.getItemList().stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        ResponseEntity<List<CartItemResponseDTO>> response = itemProxy.getItemsByIds(itemIds);
        List<CartItemResponseDTO> itemDetails = response.getBody().stream()
                .map(detail -> CartItemResponseDTO.builder()
                        .id(detail.getId())
                        .name(detail.getName())
                        .price(detail.getPrice())
                        .quantity(detail.getQuantity())
                        .build())
                .collect(Collectors.toList());
        return cartMapper.toResponse(cart, itemDetails);
    }

    public List<CartResponseDTO> getAllCarts() {

        List<Cart>carts = cartRepository.findAllByCartStatus(CartStatus.ACTIVE);
        return  carts.stream()
                .map(cart -> cartMapper.toResponse(cart,fetchItemDetails(cart.getItemList())))
                .collect(Collectors.toList());
    }
    private List<CartItemResponseDTO> fetchItemDetails(List<CartItem> items) {
        List<String> itemIds = items.stream().map(CartItem::getId).collect(Collectors.toList());

        ResponseEntity<List<CartItemResponseDTO>> response = itemProxy.getItemsByIds(itemIds);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RecordNotFoundExciption("Failed to fetch item details from Item Service");
        }
        return response.getBody();
    }
    public void hardDeleteCart(String cartId) {
        cartRepository.deleteById(cartId);
    }
    public CartResponseDTO softDeleteCart(String cartId) {
        Cart cart = cartRepository.findByIdAndCartStatus(cartId,CartStatus.ACTIVE)
                .orElseThrow(() -> new RecordNotFoundExciption("cart not found"));
        cart.setCartStatus(CartStatus.DELETED);
        Cart save = cartRepository.save(cart);
        return this.cartMapper.toResponse(save);
    }

    public Page<CartResponseDTO> getAllCartsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Cart>itemPage = cartRepository.findAllByCartStatus(CartStatus.ACTIVE,pageable);
        return itemPage.map(cartMapper::toResponse);
    }
}



