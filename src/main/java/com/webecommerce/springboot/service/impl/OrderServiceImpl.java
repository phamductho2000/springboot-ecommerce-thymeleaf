package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.*;
import com.webecommerce.springboot.entity.DetailOrderEntity;
import com.webecommerce.springboot.entity.OrderEntity;
import com.webecommerce.springboot.entity.ProductEntity;
import com.webecommerce.springboot.repository.DetailOrderRepository;
import com.webecommerce.springboot.repository.OrderRepository;
import com.webecommerce.springboot.repository.ProductRepository;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.OrderService;
import com.webecommerce.springboot.service.ProductService;
import com.webecommerce.springboot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    final int DA_HUY = 0;

    final int CHO_XU_LY = 1;

    final int DA_TIEP_NHAN = 2;

    final int DANG_GIAO = 3;

    final int DA_GIAO = 4;

    final int HOAN_TAT = 5;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    DetailOrderRepository detailOrderRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(o -> mapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(Long id) {
        Optional<OrderEntity> foundOrder = orderRepository.findById(id);
        if (!foundOrder.isPresent()) {
            throw new RuntimeException("");
        }
        return mapper.map(foundOrder.get(), OrderDTO.class);
    }

    @Override
    public OrderDTO save(String userEmail, Long addressId, HashMap<Long, CartDTO> cart, Double totalOrderPrice, Double feeShip) {
        OrderEntity newOrder = new OrderEntity();
        newOrder.setDeliveryAddress(
                deliveryAddressService.findEntityById(addressId)
        );
        newOrder.setUser(userService.findByEmail(userEmail));
        newOrder.setTotal(totalOrderPrice);
        newOrder.setFeeShip(feeShip);
        newOrder.setStatus(CHO_XU_LY);
        newOrder.setPaymentStatus(0);
        newOrder = orderRepository.save(newOrder);
        newOrder.setDetailOrders(saveDetail(newOrder, cart));
        return mapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO adminSave(FormAddOrderDTO formAddOrderDTO) {
        OrderEntity newOrder = new OrderEntity();
        if (formAddOrderDTO.getOrderId() != null) {
            newOrder.setId(formAddOrderDTO.getOrderId());
        }
        newOrder.setUser(userService.findEntityById(formAddOrderDTO.getCusId()));
        newOrder.setDeliveryAddress(deliveryAddressService.findEntityById(formAddOrderDTO.getDeliveryAddressId()));
        newOrder.setStatus(CHO_XU_LY);
        newOrder.setPaymentStatus(0);
        newOrder.setFeeShip(formAddOrderDTO.getFeeShip());
        newOrder.setTotal(formAddOrderDTO.getTotal());
        newOrder = orderRepository.save(newOrder);
        newOrder.setDetailOrders(adminSaveDetail(newOrder, formAddOrderDTO.getDetailOrders()));
        return mapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public OrderDTO adminUpdate(FormAddOrderDTO formAddOrderDTO) {
        OrderEntity newOrder = findEntityById(formAddOrderDTO.getOrderId());
        newOrder.setUser(userService.findEntityById(formAddOrderDTO.getCusId()));
        newOrder.setDeliveryAddress(deliveryAddressService.findEntityById(formAddOrderDTO.getDeliveryAddressId()));
        newOrder.setStatus(formAddOrderDTO.getStatus());
        newOrder.setFeeShip(formAddOrderDTO.getFeeShip());
        newOrder.setTotal(formAddOrderDTO.getTotal());
        newOrder.getDetailOrders().stream()
                .forEach(detail -> {
                    detailOrderRepository.deleteById(detail.getId());
                });
        newOrder.setDetailOrders(adminSaveDetail(newOrder, formAddOrderDTO.getDetailOrders()));
        newOrder = orderRepository.save(newOrder);
        return mapper.map(newOrder, OrderDTO.class);
    }

    @Override
    public void updateStatus(Long id, int status) {
        OrderEntity orderEntity = findEntityById(id);
        orderEntity.setStatus(status);
        if (status == HOAN_TAT) {
            orderEntity.getDetailOrders().stream()
                    .forEach(detail -> {
                        ProductEntity p = productService.findEntityById(detail.getProduct().getId());
                        p.setQuantity(p.getQuantity() - detail.getQuantity());
                        p.setSellCount(detail.getQuantity());
                        if(p.getQuantity() == 0) {
                            p.setStatus(0);
                        }
                        productRepository.save(p);
                    });
        }
        orderRepository.save(orderEntity);
    }

    @Override
    public List<OrderDTO> findAllByUserEmail(String email) {
        return orderRepository.findAllByUser_Email(email).stream().map(o ->
            mapper.map(o, OrderDTO.class)
        ).collect(Collectors.toList());
    }

    public OrderEntity findEntityById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<DetailOrderEntity> adminSaveDetail(OrderEntity order, List<DetailOrderDTO> detailOrders) {
        return detailOrders.stream()
                .map(d -> {
                    DetailOrderEntity detailOrder = new DetailOrderEntity();
                    detailOrder.setPrice(d.getPrice());
                    detailOrder.setQuantity(d.getQuantity());
                    detailOrder.setProduct(productService.findEntityById(d.getProduct().getId()));
                    detailOrder.setTotal(d.getTotal());
                    detailOrder.setOrder(order);
                    return detailOrderRepository.save(detailOrder);
                })
                .collect(Collectors.toList());
    }

    public List<DetailOrderEntity> saveDetail(OrderEntity order, HashMap<Long, CartDTO> cart) {
        return cart.entrySet().stream()
                .map(c -> {
                    DetailOrderEntity detailOrder = new DetailOrderEntity();
                    detailOrder.setPrice(c.getValue().getProduct().getPrice());
                    detailOrder.setQuantity(c.getValue().getQuantity());
                    detailOrder.setProduct(productService.findEntityById(c.getKey()));
                    detailOrder.setTotal(c.getValue().getTotalPrice());
                    detailOrder.setOrder(order);
                    return detailOrderRepository.save(detailOrder);
                })
                .collect(Collectors.toList());
    }
}
