package com.example.backend.services.orders;

import com.example.backend.dtos.orders.*;
import com.example.backend.dtos.saleItems.PageDto;
import com.example.backend.entities.*;
import com.example.backend.exceptions.ConflictException;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.OrderItemRepository;
import com.example.backend.repositories.OrderRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.saleitems.SaleItemService_v1;
import com.example.backend.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public PageDto<GetAllSellerOrderDto> getOrderBySellerId(Integer id, Integer page, Integer size, String sortField, AuthUserDetail principal) {
        if (!principal.getId().equals(id)) throw new AccessDeniedException("Not allowed to access other seller's resources");
        Page<Order> pageOrder = orderRepository.findOrdersBySeller_Id(id, PageRequest.of(page, size));
        PageDto<GetAllSellerOrderDto> getAllSellerOrderDtoPageDto = listMapper.toPageDTO(pageOrder, GetAllSellerOrderDto.class, modelMapper, sortField);
        getAllSellerOrderDtoPageDto.getContent().forEach((order) -> {
            User buyer = userRepository.findById(order.getBuyer().getId()).orElseThrow(() -> new ItemNotFoundException("User (Buyer) not found"));
            User seller = userRepository.findById(order.getSellerId()).orElseThrow(() -> new ItemNotFoundException("User (Seller) not found"));
            order.getBuyer().setUsername(buyer.getNickName());
            List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrders_Id(order.getId());
            List<OrderItemDto> orderItemDtoList = orderItems.stream().map((item) -> modelMapper.map(item, OrderItemDto.class)).toList();
            order.setOrderItems(orderItemDtoList);
        });
        return getAllSellerOrderDtoPageDto;
    }

    @Transactional
    public List<PlaceOrderResponseDto> createOrder(List<PlaceOrderRequestDto> orders) {
        if (orders.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing request parameters");
        List<PlaceOrderResponseDto> placeOrderList = new ArrayList<>();
        orders.forEach((order) -> {
           User buyer = userRepository.findById(order.getBuyerId()).orElseThrow(() -> new ItemNotFoundException("Buyer not found"));
           User seller = userRepository.findById(order.getSellerId()).orElseThrow(() -> new ItemNotFoundException("Seller not found"));
           SellerForPlaceOrderDto sellerForPlaceOrderDto = modelMapper.map(seller, SellerForPlaceOrderDto.class);
           sellerForPlaceOrderDto.setUsername(seller.getNickName());
           Order newOrder = new Order();
           newOrder.setBuyer(buyer);
           newOrder.setSeller(seller);
           newOrder.setOrderDate(order.getOrderDate());
           newOrder.setPaymentDate(order.getOrderDate());
           newOrder.setShippingAddress(order.getShippingAddress());
           newOrder.setOrderNote(order.getOrderNote());
           newOrder.setOrderStatus(order.getOrderStatus());
           orderRepository.save(newOrder);

           PlaceOrderResponseDto placeOrder = new PlaceOrderResponseDto();
           placeOrder.setId(newOrder.getId());
           placeOrder.setBuyerId(buyer.getId());
           placeOrder.setSeller(sellerForPlaceOrderDto);
           placeOrder.setOrderDate(newOrder.getOrderDate());
           placeOrder.setPaymentDate(newOrder.getPaymentDate());
           placeOrder.setShippingAddress(newOrder.getShippingAddress());
           placeOrder.setOrderNote(newOrder.getOrderNote());
           placeOrder.setOrderStatus(newOrder.getOrderStatus());
           order.getOrderItems().forEach((item) -> {
               SaleItem saleItem = saleItemRepository.findById(item.getSaleItemId()).orElseThrow(() -> new ItemNotFoundException("Sale item not found"));
               if (saleItem.getQuantity() < item.getQuantity()) throw new ConflictException("Quantity sale id: " + saleItem.getId() + " not enough");
               OrderItem orderItem = new OrderItem();
               orderItem.setOrders(newOrder);
               orderItem.setSaleItems(saleItem);
               orderItem.setPrice(item.getPrice());
               orderItem.setQuantity(item.getQuantity());
               orderItem.setDescription(item.getDescription());
               orderItemRepository.save(orderItem);

               saleItem.setQuantity(saleItem.getQuantity() - item.getQuantity());
               saleItemRepository.save(saleItem);
           });
           placeOrder.setOrderItems(order.getOrderItems());
           placeOrderList.add(placeOrder);
        });
        return placeOrderList;
    }
}
