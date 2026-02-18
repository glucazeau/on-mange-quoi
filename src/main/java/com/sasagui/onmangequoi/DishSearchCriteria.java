package com.sasagui.onmangequoi;

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

@Setter
@ToString(onlyExplicitlyIncluded = true)
public class DishSearchCriteria {

    @ToString.Include
    private String label;

    @ToString.Include
    private Boolean slow;

    @ToString.Include
    private Boolean quick;

    @ToString.Include
    private Boolean fromRestaurant;

    @ToString.Include
    private Boolean vegan;

    private final List<Specification<DishEntity>> specs = new ArrayList<>();

    public Specification<DishEntity> toSpec() {
        if (label != null) {
            specs.add(hasLabelLike(label));
        }

        if (slow != null) {
            specs.add(slow(slow));
        }

        if (quick != null) {
            specs.add(quick(quick));
        }

        if (fromRestaurant != null) {
            specs.add(fromRestaurant(fromRestaurant));
        }

        if (vegan != null) {
            specs.add(vegan(vegan));
        }

        return Specification.allOf(specs);
    }

    private Specification<DishEntity> hasLabelLike(final String label) {
        return (root, query, builder) -> {
            String expression = "%" + label + "%";
            return builder.like(root.get("label"), expression);
        };
    }

    private Specification<DishEntity> slow(final Boolean slow) {
        return (root, query, builder) -> builder.equal(root.get("slow"), slow);
    }

    private Specification<DishEntity> quick(final Boolean quick) {
        return (root, query, builder) -> builder.equal(root.get("quick"), quick);
    }

    private Specification<DishEntity> fromRestaurant(final Boolean fromRestaurant) {
        return (root, query, builder) -> builder.equal(root.get("fromRestaurant"), fromRestaurant);
    }

    private Specification<DishEntity> vegan(final Boolean vegan) {
        return (root, query, builder) -> builder.equal(root.get("vegan"), vegan);
    }
}
