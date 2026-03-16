INSERT INTO dish (label, is_slow, is_quick, is_from_restaurant, is_vegan, is_fish, is_kid_lunch) VALUES
('Dish 1', false, false, false, false, false, false),
('Dish 2', false, false, false, true, false, false),
('Dish 3', false, false, true, false, false, false),
('Dish 4', false, false, true, true, false, false),
('Dish 5', false, true, false, false, false, false),
('Dish 6', false, true, false, true, false, false),
('Dish 7', false, true, true, false, false, false),
('Dish 8', false, true, true, true, false, false),
('Dish 9', true, false, false, false, false, false),
('Dish 10', true, false, false, true, false, false),
('Dish 11', true, false, true, false, false, false),
('Dish 12', true, false, true, true, false, false),
('Dish 13', true, true, false, false, false, false),
('Dish 14', true, true, false, true, false, false),
('Dish 15', true, true, true, false, false, false),
('Dish 16', true, true, true, true, false, false);

INSERT INTO dish_month (dish_id, month_number) VALUES
(4, 1), (4, 2), (4, 3), (4, 5), (4, 6), (4, 7), (4, 8), (4, 9), (4, 10), (4, 11), (4, 12),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5);
