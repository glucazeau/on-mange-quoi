package com.sasagui.onmangequoi.dish;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<Dish> listDishes(DishSearchCriteria criteria) {
        log.info("Loading dishes with criteria {}", criteria);
        criteria = criteria == null ? new DishSearchCriteria() : criteria;
        return dishRepository.findAll(criteria.toSpec(), Sort.by(Sort.Direction.ASC, "label")).stream()
                .map(Dish::from)
                .toList();
    }
}
