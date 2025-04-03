package com.exam.payments;

import com.exam.cartAnalysis.entity.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "payments")
public class Payments {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "orders_id", referencedColumnName = "ordersId")
    private Orders orders;

    @Column(name = "member_no")
    private Long memberNo;

    @Column(name = "final_price")
    private Long finalPrice;

    @Column(name = "payment_amount")
    private Long paymentAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "payment_approved")
    private LocalDateTime paymentApproved;
}
