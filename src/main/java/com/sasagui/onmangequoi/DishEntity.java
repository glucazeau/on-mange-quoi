package com.sasagui.onmangequoi;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@Table(name = "dish")
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "label")
    private String label;

    @Column(name = "is_slow")
    private boolean slow;

    @Column(name = "is_quick")
    private boolean quick;

    @Column(name = "is_from_restaurant")
    private boolean fromRestaurant;

    @Column(name = "is_vegan")
    private boolean vegan;
}
