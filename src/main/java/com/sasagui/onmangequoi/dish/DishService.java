package com.sasagui.onmangequoi.dish;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    @Transactional(readOnly = true)
    public List<Dish> listDishes(DishSearchCriteria criteria) {
        log.info("Loading dishes with criteria {}", criteria);
        criteria = criteria == null ? new DishSearchCriteria() : criteria;
        return dishRepository.findAll(criteria.toSpec(), Sort.by(Sort.Direction.ASC, "label")).stream()
                .map(Dish::from)
                .toList();
    }

    public Dish getDish(long dishId) {
        log.info("Loading dish with ID {}", dishId);
        DishEntity entity = dishRepository.getReferenceById(dishId);
        return Dish.from(entity);
    }

    public Dish addDish(NewDish newDish) {
        try {
            DishEntity entity = DishEntity.from(newDish);
            DishEntity saved = dishRepository.save(entity);
            return Dish.from(saved);
        } catch (DataIntegrityViolationException e) {
            log.error("Error while saving new dish {}: {}", newDish, e.getMessage(), e);
            throw new DishAlreadyExistsException(newDish.getLabel());
        }
    }

    public Dish updateDish(long id, NewDish dish) {
        log.info("Updating dish with ID {}", id);
        DishEntity entity = dishRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Dish with ID %d not found", id)));
        entity.update(dish);
        DishEntity saved = dishRepository.save(entity);
        return Dish.from(saved);
    }
}
