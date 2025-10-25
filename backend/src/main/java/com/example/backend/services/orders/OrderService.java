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
import jakarta.persistence.EntityManager;
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
    @Autowired
    private EntityManager entityManager;

    public PageDto<GetAllSellerOrderDto> getOrderBySellerId(Integer id, String orderStatus, Integer page, Integer size, String sortField, AuthUserDetail principal) {
        if (!principal.getId().equals(id)) throw new AccessDeniedException("Not allowed to access other seller's resources");
        Page<Order> pageOrder = orderRepository.findOrdersBySeller_IdAndOrderStatusOrderByIdDesc(id, orderStatus, PageRequest.of(page, size));
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
           newOrder.setShippingAddress(order.getShippingAddress());
           newOrder.setOrderNote(order.getOrderNote());
           newOrder.setOrderStatus(order.getOrderStatus());
           newOrder.setIsNewOrder(order.getIsNewOrder());
           orderRepository.save(newOrder);
           entityManager.refresh(newOrder);

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

    public GetBuyerOrderDto getBuyerOrderById(Integer orderId, AuthUserDetail principal) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Order not found"));

        if (!order.getBuyer().getId().equals(principal.getId())
                && !order.getSeller().getId().equals(principal.getId())) {
            throw new AccessDeniedException("Not allowed to access this order");
        }

        GetBuyerOrderDto buyerOrderDto = new GetBuyerOrderDto();
        buyerOrderDto.setId(order.getId());
        buyerOrderDto.setBuyerId(order.getBuyer().getId());
        buyerOrderDto.setOrderDate(order.getOrderDate());
        buyerOrderDto.setPaymentDate(order.getPaymentDate());
        buyerOrderDto.setShippingAddress(order.getShippingAddress());
        buyerOrderDto.setOrderNote(order.getOrderNote());
        buyerOrderDto.setOrderStatus(order.getOrderStatus());

        User seller = order.getSeller();
        SellerDtoForGetBuyerOrderById sellerDto = modelMapper.map(seller, SellerDtoForGetBuyerOrderById.class);
        buyerOrderDto.setSeller(sellerDto);

        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrders_Id(order.getId());
        List<OrderItemDto> orderItemDto = orderItems.stream()
                .map(item -> modelMapper.map(item, OrderItemDto.class))
                .toList();
        buyerOrderDto.setOrderItems(orderItemDto);

        return buyerOrderDto;
    }
    public PageDto<GetAllBuyerOrderDto> getAllBuyerOrdersById(Integer id, Integer page, Integer size, String sortField, AuthUserDetail principal) {
        User user = userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("User not found"));

        Page<Order> pageOrder = orderRepository.findOrdersByBuyer_IdOrderByIdDesc(id, PageRequest.of(page, size));
        PageDto<GetAllBuyerOrderDto> getAllBuyerOrderDtoPageDto = listMapper.toPageDTO(pageOrder, GetAllBuyerOrderDto.class, modelMapper, sortField);

        getAllBuyerOrderDtoPageDto.getContent().forEach((order) -> {
            User seller = userRepository.findById(order.getSeller().getId()).orElseThrow(() -> new ItemNotFoundException("User (Seller) not found"));
            order.getSeller().setUsername(seller.getNickName());

            List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrders_Id(order.getId());
            List<OrderItemDto> orderItemDtoList = orderItems.stream().map((item) -> modelMapper.map(item, OrderItemDto.class)).toList();
            order.setOrderItems(orderItemDtoList);
        });

        return getAllBuyerOrderDtoPageDto;
    }

    public GetAllSellerOrderDto getOrderSellerByOrderId(Integer sid, Integer oId, AuthUserDetail principal) {
        Order order = orderRepository.findById(oId).orElseThrow(() -> new ItemNotFoundException("order not found!!"));
        if (!sid.equals(principal.getId())) throw new AccessDeniedException("Not allowed to access other seller's resources");
        GetAllSellerOrderDto sellerOrderDto = modelMapper.map(order, GetAllSellerOrderDto.class);

        User buyer = userRepository.findById(order.getBuyer().getId()).orElseThrow(() -> new ItemNotFoundException("Buyer not found!!"));
        BuyerForGetAllSellerOrderDto buyerDto = modelMapper.map(buyer, BuyerForGetAllSellerOrderDto.class);
        buyerDto.setUsername(buyer.getNickName());
        sellerOrderDto.setBuyer(buyerDto);

        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrders_Id(order.getId());
        List<OrderItemDto> orderItemDtoList = orderItems.stream().map((item) -> modelMapper.map(item, OrderItemDto.class)).toList();
        sellerOrderDto.setOrderItems(orderItemDtoList);
        sellerOrderDto.setOrderNote(order.getOrderNote());

        return sellerOrderDto;
    }

    public Integer getCountCompletedAndNewOrder(String orderStatus, Boolean isNewOrder, Integer sellerId) {
        List<Order> orders = orderRepository.findOrdersByOrderStatusAndIsNewOrderAndSeller_Id(orderStatus, isNewOrder, sellerId);
        return orders.toArray().length;
    }

    public PlaceOrderResponseDto changeIsNewOrderStatus(Integer oid, Boolean isNewOrder) {
        Order order = orderRepository.findById(oid).orElseThrow(() -> new ItemNotFoundException("order not found"));
        User seller = userRepository.findById(order.getSeller().getId()).orElseThrow(() -> new ItemNotFoundException("seller not found"));
        order.setIsNewOrder(isNewOrder);
        Order updateOrder = orderRepository.save(order);
        PlaceOrderResponseDto placeOrderResponseDto = modelMapper.map(updateOrder, PlaceOrderResponseDto.class);
        placeOrderResponseDto.getSeller().setUsername(seller.getNickName());
        return placeOrderResponseDto;
    }

}
