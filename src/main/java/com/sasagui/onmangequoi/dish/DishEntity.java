package com.sasagui.onmangequoi.dish;

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
    private long id;

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

    @Column(name = "is_fish")
    private boolean fish;

    @Column(name = "is_kid_lunch")
    private boolean kidLunch;

    public static DishEntity from(NewDish newDish) {
        DishEntity entity = new DishEntity();
        entity.label = newDish.getLabel();
        entity.slow = newDish.isSlow();
        entity.quick = newDish.isQuick();
        entity.fromRestaurant = newDish.isFromRestaurant();
        entity.vegan = newDish.isVegan();
        entity.fish = newDish.isFish();
        entity.kidLunch = newDish.isKidLunch();
        return entity;
    }

    public void update(NewDish updatedDish) {
        this.label = updatedDish.getLabel();
        this.slow = updatedDish.isSlow();
        this.quick = updatedDish.isQuick();
        this.fromRestaurant = updatedDish.isFromRestaurant();
        this.vegan = updatedDish.isVegan();
        this.fish = updatedDish.isFish();
        this.kidLunch = updatedDish.isKidLunch();
    }
}
