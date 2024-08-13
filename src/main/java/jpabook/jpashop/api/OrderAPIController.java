package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.dto.OrderDTO;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderAPIController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDTO> ordersV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<OrderDTO> result = orders.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDTO> result = orders.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDTO> result = orders.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
        return result;
    }
}
