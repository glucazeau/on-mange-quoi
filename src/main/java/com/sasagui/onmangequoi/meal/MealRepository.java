package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, MealId>, JpaSpecificationExecutor<MealEntity> {

    static Specification<MealEntity> fromWeek(Week week) {
        return (root, query, criteriaBuilder) -> {
            Predicate yearPredicate = criteriaBuilder.equal(root.get("id").get("year"), week.getYear());
            Predicate weekNumberPredicate = criteriaBuilder.equal(root.get("id").get("weekNumber"), week.getNumber());
            return criteriaBuilder.and(yearPredicate, weekNumberPredicate);
        };
    }
}
