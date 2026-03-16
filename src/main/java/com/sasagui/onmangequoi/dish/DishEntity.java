package com.sasagui.onmangequoi.dish;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@Table(name = "dish")
public class DishEntity {

    private static final Set<Integer> allMonths = java.util.Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dish_month", joinColumns = @JoinColumn(name = "dish_id"))
    @Column(name = "month_number")
    private Set<Integer> months = new HashSet<>();

    public static DishEntity from(NewDish newDish) {
        DishEntity entity = new DishEntity();
        entity.label = newDish.getLabel();
        entity.slow = newDish.isSlow();
        entity.quick = newDish.isQuick();
        entity.fromRestaurant = newDish.isFromRestaurant();
        entity.vegan = newDish.isVegan();
        entity.fish = newDish.isFish();
        entity.kidLunch = newDish.isKidLunch();
        entity.months = computeMonths(newDish);
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
        this.months = computeMonths(updatedDish);
    }

    private static Set<Integer> computeMonths(NewDish dish) {
        if (dish.getMonths() == null || dish.getMonths().isEmpty()) {
            log.info("Months list is empty, all 12 months will be saved");
            return allMonths;
        } else {
            return dish.getMonths();
        }
    }
}
