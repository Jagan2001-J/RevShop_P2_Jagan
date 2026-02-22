package com.rev.app.service;

import com.rev.app.entity.Cart;
import com.rev.app.entity.CartItem;
import com.rev.app.entity.Product;
import com.rev.app.entity.User;
import com.rev.app.repository.ICartItemRepository;
import com.rev.app.repository.ICartRepository;
import com.rev.app.repository.IProductRepository;
import com.rev.app.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired private ICartRepository cartRepo;
    @Autowired private ICartItemRepository itemRepo;
    @Autowired private IProductRepository productRepo;
    @Autowired private IUserRepository userRepo;

    public void addToCart(int UserId,int productId,int qty){
        User u=UserRepo.findById(userId).get();
        Cart cart=cartRepo.findByUser(u).orElseGet(()->cartRepo.save(new Cart(null,u)));
        Product p=productRepo.findById(productId).get();

        CartItem item=new CartItem();
        item.setCart(cart);
        item.setProduct(p);
        item.setQuantity(qty);

        itemRepo.save(item);
    }
}
